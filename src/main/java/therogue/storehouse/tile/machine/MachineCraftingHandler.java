/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineRecipe;
import therogue.storehouse.util.ItemUtils;
import therogue.storehouse.util.LOG;

public class MachineCraftingHandler {
	
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
	
	private class TileCrafter {
		private MachineRecipe currentCrafting = null;
		private final ICrafter attachedTile;
		private int craftingTime = 0;
		private boolean craftingLock;
		
		public TileCrafter(ICrafter attachedTile) {
			this.attachedTile = attachedTile;
		}
	public void checkRecipes(ICrafter machine) {
		if ((currentCrafting != null && currentCrafting.matches(attachedTile)) || craftingLock) return;
		IItemHandlerModifiable outputInventory = machine.getOutputInventory();
		IItemHandlerModifiable craftingInventory = machine.getCraftingInventory();
		for (MachineRecipe recipe : RECIPES)
		{
			if (recipe.getResults().size() < outputInventory.getSlots()) continue;
			
			for (int i = 0; i < outputInventory.getSlots(); i++) {
				if (!ItemUtils.areItemStacksMergableWithLimit(inventory.getSlotLimit(0), recipe.getResults().get(0), inventory.getStackInSlot(0))) continue;
			}
			boolean isValidRecipe = recipe.matches(attachedTile);
			if (isValidRecipe)
			{
				currentCrafting = recipe;
				craftingTime = recipe.timeTaken;
			}
		}
	}
	
	public void doCraftingLoop(ICrafter machine) {
		if (currentCrafting != null)
		{
			if (currentCrafting.matches(attachedTile))
			{
				if (craftingTime <= 1)
				{
					if (ItemUtils.areItemStacksMergableWithLimit(Math.min(inventory.getStackInSlot(0).getMaxStackSize(), inventory.getSlotLimit(0)), currentCrafting.getResults().get(0), inventory.getStackInSlot(0)))
					{
						craftingLock = true;
						ItemStack craftingResult = currentCrafting.craft(this).get(0);
						if (craftingResult != null && !craftingResult.isEmpty())
						{
							inventory.setStackInSlot(0, ItemUtils.mergeStacks(Math.min(inventory.getStackInSlot(0).getMaxStackSize(), inventory.getSlotLimit(0)), true, inventory.getStackInSlot(0), craftingResult));
						}
						currentCrafting = null;
						craftingTime = 0;
						craftingLock = false;
						onInventoryChange();
					}
				}
				else
				{
					if (energyStorage.getEnergyStored() >= RFPerTick)
					{
						energyStorage.modifyEnergyStored(-RFPerTick);
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
