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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.wrapper.IRecipeComponent;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
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
	
	private final List<CraftingManager> CRAFTERS = new ArrayList<CraftingManager>();
	private List<MachineRecipe> RECIPES = Lists.newArrayList();
	
	private MachineCraftingHandler () {
	}
	
	public CraftingManager newCrafter (ICrafter attachedTile) {
		CraftingManager cm = new CraftingManager(attachedTile);
		CRAFTERS.add(cm);
		return cm;
	}
	
	public CraftingManager newNonTickingCrafter (ICrafter attachedTile) {
		CraftingManager cm = new CraftingManager(attachedTile);
		return cm;
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
	
	private boolean checkItemValidForSlot (ICrafter tile, int index, IRecipeWrapper stack) {
		IRecipeInventory craftingInventory = tile.getCraftingInventory();
		if (stack.mergable(craftingInventory.getComponent(index), craftingInventory.getComponentSlotLimit(index))) return true;
		for (MachineRecipe recipe : RECIPES)
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
				int ingredientIndex = matchesRecipeIngredient(recipe, tile, i, craftingInventory.getComponent(i), matchedIngredients);
				if (ingredientIndex != -1)
				{
					matchedIngredients.add(ingredientIndex);
				}
			}
			if (matchedIngredients.size() + emptySlots >= recipe.getAmountOfInputs() && matchesRecipeIngredient(recipe, tile, index, stack, null) != -1) return true;
			if (matchesRecipeIngredient(recipe, tile, index, stack, matchedIngredients) != -1) return true;
		}
		return false;
	}
	
	private int matchesRecipeIngredient (MachineRecipe recipe, ICrafter machine, int index, IRecipeWrapper test, @Nullable Set<Integer> exclusions) {
		if (!recipe.correctMode.test(machine)) return -1;
		Set<Integer> orderedSlots = machine.getOrderMattersSlots();
		if (orderedSlots.contains(index))
		{
			if (recipe.getInputComponent(index).matches(test)) return index;
			return -1;
		}
		for (int i = 0; i < recipe.getAmountOfInputs(); i++)
		{
			if (exclusions != null && exclusions.contains(i)) continue;
			if (orderedSlots.contains(i)) continue;
			if (recipe.getInputComponent(i).isUnUsed()) continue;
			if (recipe.getInputComponent(i).matches(test)) return i;
		}
		return -1;
	}
	
	private static Map<Integer, Integer> getCorrespondingInventorySlots (MachineRecipe recipe, IRecipeInventory inventory, @Nullable List<Integer> slotLimits) {
		Map<Integer, Integer> slots = new HashMap<Integer, Integer>();
		NonNullList<IRecipeWrapper> availableIngredients = NonNullList.create();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			availableIngredients.add(inventory.getComponent(i).copy());
		}
		for (int i = 0; i < recipe.getAmountOfOutputs(); i++)
		{
			IRecipeComponent thisInput = recipe.getOutputComponent(i);
			if (thisInput.isUnUsed()) continue;
			int index = -1;
			for (int j = 0; j < availableIngredients.size(); j++)
			{
				if (availableIngredients.get(j).canAddComponent(thisInput, slotLimits != null ? slotLimits.get(j) : -1))
				{
					index = j;
					break;
				}
			}
			if (index != -1)
			{
				availableIngredients.get(index).increaseSize(thisInput.getSize());
			}
			slots.put(i, index);
		}
		return slots;
	}
	
	private static List<Integer> getSlotLimitsList (IRecipeInventory inventory) {
		List<Integer> listInventory = new ArrayList<Integer>();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			listInventory.add(inventory.getComponentSlotLimit(i));
		}
		return listInventory;
	}
	
	public static void tickCrafters (TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) return;
		for (Entry<Class<? extends ICrafter>, MachineCraftingHandler> craftinghandler : CRAFTING_HANDLERS.entrySet())
		{
			for (CraftingManager manager : craftinghandler.getValue().CRAFTERS)
			{
				manager.updateCraftingStatus();
			}
		}
	}
	
	public class CraftingManager {
		
		private final ICrafter attachedTile;
		private MachineRecipe currentCrafting = null;
		public int totalCraftingTime = 0;
		public int craftingTime = 0;
		private boolean craftingLock = false;
		
		private CraftingManager (ICrafter attachedTile) {
			this.attachedTile = attachedTile;
		}
		
		private void resetCraftingStatus () {
			currentCrafting = null;
			totalCraftingTime = 0;
			craftingTime = 0;
			craftingLock = false;
		}
		
		public boolean checkItemValidForSlot (int index, IRecipeWrapper stack) {
			return MachineCraftingHandler.this.checkItemValidForSlot(attachedTile, index, stack);
		}
		
		public void checkRecipes () {
			if ((currentCrafting != null && matches(currentCrafting)) || craftingLock) return;
			resetCraftingStatus();
			for (MachineRecipe recipe : RECIPES)
			{
				if (matches(recipe))
				{
					currentCrafting = recipe;
					totalCraftingTime = recipe.timeTaken;
					craftingTime = recipe.timeTaken;
					break;
				}
			}
		}
		//TODO : Make sure this recognises that there may be multiple items in an ingredient stack
		private boolean matches (MachineRecipe recipe) {
			if (!recipe.correctMode.test(attachedTile)) return false;
			IRecipeInventory craftInventory = attachedTile.getCraftingInventory();
			IRecipeInventory outputInventory = attachedTile.getOutputInventory();
			Map<Integer, Integer> ingredientSlots = new HashMap<Integer, Integer>();
			Set<Integer> orderedSlots = attachedTile.getOrderMattersSlots();
			if (recipe.getAmountOfOutputs() < outputInventory.getSize()) return false;
			Map<Integer, Integer> outputMap = getCorrespondingInventorySlots(recipe, outputInventory, getSlotLimitsList(outputInventory));
			if (!checkMatchingSlots(outputMap)) return false;
			for (Integer i : orderedSlots)
			{
				if (recipe.getInputComponent(i).isUnUsed()) continue;
				if (!recipe.getInputComponent(i).matches(craftInventory.getComponent(i))) return false;
				ingredientSlots.put(i, i);
			}
			NonNullList<IRecipeWrapper> availableIngredients = NonNullList.create();
			for (int i = 0; i < craftInventory.getSize(); i++)
			{
				if (orderedSlots.contains(i)) continue;
				availableIngredients.add(craftInventory.getComponent(i).copy());
			}
			for (int i = 0; i < recipe.getAmountOfInputs(); i++)
			{
				if (orderedSlots.contains(i)) continue;
				if (recipe.getInputComponent(i).isUnUsed()) continue;
				int index = getMachedCraftingIndex(availableIngredients, recipe.getInputComponent(i));
				if (index != -1)
				{
					availableIngredients.get(index).increaseSize(-recipe.getInputComponent(i).getSize());
					ingredientSlots.put(i, index);
				}
				else
				{
					return false;
				}
			}
			for (Integer ingredient : ingredientSlots.keySet())
			{
				IRecipeComponent component = recipe.getInputComponent(ingredient);
				IRecipeWrapper stack = craftInventory.getComponent(ingredientSlots.get(ingredient) + orderedSlots.size());
				IRecipeWrapper testStack = stack.copy();
				testStack.increaseSize(-component.getSize());
				IRecipeWrapper container = component.getResidue();
				if (!container.isUnUsed())
				{
					if (stack.getSize() > component.getSize() && !testStack.mergable(container, craftInventory.getComponentSlotLimit(ingredient))) return false;
				}
			}
			return true;
		}
		
		private void updateCraftingStatus () {
			if (currentCrafting != null)
			{
				if (matches(currentCrafting))
				{
					if (attachedTile.isRunning())
					{
						attachedTile.doRunTick();
						if (craftingTime <= 1) craft();
						else--craftingTime;
					}
				}
				else
				{
					resetCraftingStatus();
				}
			}
		}
		
		public boolean craft () {
			craftingLock = true;
			boolean crafted = tryCraft();
			resetCraftingStatus();
			checkRecipes();
			return crafted;
		}
		
		private boolean tryCraft () {
			if (!matches(currentCrafting)) return false;
			IRecipeInventory craftingInventory = attachedTile.getCraftingInventory();
			IRecipeInventory outputInventory = attachedTile.getOutputInventory();
			Map<Integer, Integer> ingredientSlots = new HashMap<Integer, Integer>();
			Set<Integer> orderedSlots = attachedTile.getOrderMattersSlots();
			// Map Ordered slots to the machine slot
			for (Integer i : orderedSlots)
			{
				if (!currentCrafting.getInputComponent(i).isUnUsed()) ingredientSlots.put(i, i);
			}
			// Create a list of all the ingredients in unordered slots
			NonNullList<IRecipeWrapper> availableIngredients = NonNullList.create();
			for (int i = 0; i < craftingInventory.getSize(); i++)
			{
				if (orderedSlots.contains(i))
				{
					availableIngredients.add(IRecipeWrapper.NOTHING);
				}
				else
				{
					availableIngredients.add(craftingInventory.getComponent(i).copy());
				}
			}
			// Map Unordered Slots to the machine slot with the ingredient
			for (int i = 0; i < currentCrafting.getAmountOfInputs(); i++)
			{
				if (orderedSlots.contains(i)) continue;
				if (currentCrafting.getInputComponent(i).isUnUsed()) continue;
				int index = getMachedCraftingIndex(availableIngredients, currentCrafting.getInputComponent(i));
				if (index != -1)
				{
					availableIngredients.get(index).increaseSize(-currentCrafting.getInputComponent(i).getSize());
					ingredientSlots.put(i, index);
				}
			}
			// Remove all the items that are used up in the process and deposit any left-overs
			for (Integer ingredient : ingredientSlots.keySet())
			{
				IRecipeWrapper stack = craftingInventory.getComponent(ingredientSlots.get(ingredient));
				stack.increaseSize(-currentCrafting.getInputComponent(ingredient).getSize());
				IRecipeWrapper container = currentCrafting.getInputComponent(ingredient).getResidue();
				if (!container.isUnUsed())
				{
					craftingInventory.insertComponent(ingredientSlots.get(ingredient), container);
				}
			}
			// Put all output items in the output slots
			Map<Integer, Integer> outputMap = getCorrespondingInventorySlots(currentCrafting, outputInventory, getSlotLimitsList(outputInventory));
			for (int i = 0; i < currentCrafting.getAmountOfOutputs(); i++)
			{
				outputInventory.insertComponent(outputMap.get(i), currentCrafting.getOutputComponent(i).getWrapper());
			}
			return true;
		}
		
		private int getMachedCraftingIndex (List<IRecipeWrapper> list, IRecipeComponent stack) {
			if (list != null && list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					if (stack.matches(list.get(i))) { return i; }
				}
			}
			return -1;
		}
		
		private boolean checkMatchingSlots (Map<Integer, Integer> slotMap) {
			for (Integer i : slotMap.keySet())
			{
				if (slotMap.get(i) < 0) return false;
			}
			return true;
		}
	}
}
