/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.util.CraftingHelper;
import therogue.storehouse.util.LOG;

public class MachineCraftingHandler {
	
	private static final Map<Class<? extends ICrafter>, MachineCraftingHandler> CRAFTING_HANDLERS = new HashMap<Class<? extends ICrafter>, MachineCraftingHandler>();
	
	public static MachineCraftingHandler getHandler (Class<? extends ICrafter> crafterClass) {
		if (!CRAFTING_HANDLERS.containsKey(crafterClass))
		{
			CRAFTING_HANDLERS.put(crafterClass, new MachineCraftingHandler());
		}
		return CRAFTING_HANDLERS.get(crafterClass);
	}
	
	public static void register (Class<? extends ICrafter> crafterClass, MachineRecipe recipe) {
		getHandler(crafterClass).register(recipe);
	}
	
	private List<MachineRecipe> RECIPES = Lists.newArrayList();
	
	private MachineCraftingHandler () {
	}
	
	public void register (MachineRecipe recipe) {
		if (!RECIPES.contains(recipe))
		{
			RECIPES.add(recipe);
		}
		else
		{
			LOG.warn("Tried to register a duplicate recipe: " + recipe);
		}
	}
	
	public CraftingManager newCrafter (ICrafter attachedTile) {
		return new CraftingManager(attachedTile);
	}
	
	private boolean checkItemValidForSlot (ICrafter tile, int index, IRecipeWrapper stack) {
		IRecipeInventory craftingInventory = tile.getCraftingInventory();
		if (stack.mergable(craftingInventory.getComponent(index), craftingInventory.getComponentSlotLimit(index))) return true;
		recipeloop: for (MachineRecipe recipe : RECIPES)
		{
			Set<Integer> matchedIngredients = new HashSet<Integer>();
			int emptySlots = 0;
			for (int i = 0; i < craftingInventory.getSize(); i++)
			{
				if (craftingInventory.getComponent(i).isUnUsed())
				{
					++emptySlots;
					continue;
				}
				int ingredientIndex = recipe.matchesRecipeIngredient(tile, i, craftingInventory.getComponent(i), matchedIngredients);
				if (ingredientIndex == -1)
				{
					continue recipeloop;
				}
				else
				{
					matchedIngredients.add(ingredientIndex);
				}
			}
			if (matchedIngredients.size() + emptySlots > recipe.craftingInputs.size() && recipe.matchesRecipeIngredient(tile, index, stack, null) != -1) return true;
			if (recipe.matchesRecipeIngredient(tile, index, stack, matchedIngredients) != -1) return true;
		}
		return false;
	}
	
	public class CraftingManager {
		
		private MachineRecipe currentCrafting = null;
		private final ICrafter attachedTile;
		private int craftingTime = 0;
		private boolean craftingLock = false;
		
		private CraftingManager (ICrafter attachedTile) {
			this.attachedTile = attachedTile;
		}
		
		public boolean checkItemValidForSlot (int index, IRecipeWrapper stack) {
			return MachineCraftingHandler.this.checkItemValidForSlot(attachedTile, index, stack);
		}
		
		public void checkRecipes () {
			if ((currentCrafting != null && currentCrafting.matches(attachedTile)) || craftingLock) return;
			currentCrafting = null;
			IRecipeInventory outputInventory = attachedTile.getOutputInventory();
			for (MachineRecipe recipe : RECIPES)
			{
				if (recipe.getResults().size() < outputInventory.getSize()) continue;
				Map<Integer, Integer> ouptutMap = CraftingHelper.getCorrespondingInventorySlots(recipe.getResults(), outputInventory, CraftingHelper.getSlotLimitsList(outputInventory));
				if (recipe.matches(attachedTile) && CraftingHelper.checkMatchingSlots(ouptutMap))
				{
					currentCrafting = recipe;
					craftingTime = recipe.timeTaken;
					break;
				}
			}
		}
		
		public void updateCraftingStatus () {
			if (currentCrafting != null)
			{
				if (currentCrafting.matches(attachedTile))
				{
					if (attachedTile.isRunning())
					{
						attachedTile.doRunTick();
						if (craftingTime <= 1)
						{
							IRecipeInventory outputInventory = attachedTile.getOutputInventory();
							Map<Integer, Integer> outputMap = CraftingHelper.getCorrespondingInventorySlots(currentCrafting.getResults(), outputInventory, CraftingHelper.getSlotLimitsList(outputInventory));
							if (CraftingHelper.checkMatchingSlots(outputMap))
							{
								craftingLock = true;
								if (currentCrafting.craft(attachedTile))
								{
									List<IRecipeWrapper> results = currentCrafting.getWrappedResults();
									for (int i = 0; i < results.size(); i++)
									{
										outputInventory.insertComponent(outputMap.get(i), results.get(i));
									}
								}
								currentCrafting = null;
								craftingTime = 0;
								craftingLock = false;
								checkRecipes();
							}
						}
						else
						{
							--craftingTime;
						}
					}
				}
				else
				{
					currentCrafting = null;
					craftingTime = 0;
				}
			}
		}
	}
}
