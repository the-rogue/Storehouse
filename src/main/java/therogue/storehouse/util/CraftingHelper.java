/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import therogue.storehouse.crafting.RecipeInput;

public class CraftingHelper {
	
	public static int getMachedIndex (@Nullable List<Integer> limits, List<ItemStack> list, ItemStack stack) {
		if (list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if ((stack.isEmpty() && list.get(i).isEmpty()) || ItemUtils.areItemStacksMergableWithLimit(limits != null ? limits.get(i) != null ? limits.get(i) : 64 : 64, stack, list.get(i))) { return i; }
			}
		}
		return -1;
	}
	
	public static int getMachedCraftingIndex (List<RecipeInput> list, ItemStack stack) {
		if (list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).matches(stack)) { return i; }
			}
		}
		return -1;
	}
	
	public static int getMachedCraftingIndex (@Nonnull NonNullList<ItemStack> selection, RecipeInput comparison) {
		for (int i = 0; i < selection.size(); i++)
		{
			if (comparison.matches(selection.get(i))) { return i; }
		}
		return -1;
	}
	
	public static Map<Integer, Integer> getCorrespondingInventorySlots (List<ItemStack> inputs, List<ItemStack> inventory, @Nullable List<Integer> slotLimits) {
		Map<Integer, Integer> slots = new HashMap<Integer, Integer>();
		NonNullList<ItemStack> availableIngredients = NonNullList.create();
		for (int i = 0; i < inventory.size(); i++)
		{
			availableIngredients.add(inventory.get(i).copy());
		}
		for (int i = 0; i < inputs.size(); i++)
		{
			if (inputs.get(i).isEmpty()) continue;
			int index = getMachedIndex(slotLimits, availableIngredients, inputs.get(i));
			if (index != -1)
			{
				availableIngredients.get(index).grow(inputs.get(i).getCount());
			}
			slots.put(i, index);
		}
		return slots;
	}
	
	public static boolean checkMatchingSlots (Map<Integer, Integer> slotMap) {
		for (Integer i : slotMap.keySet())
		{
			if (slotMap.get(i) < 0) return false;
		}
		return true;
	}
	
	public static List<ItemStack> getInventoryList (IItemHandler inventory) {
		List<ItemStack> listInventory = new ArrayList<ItemStack>();
		for (int i = 0; i < inventory.getSlots(); i++)
		{
			listInventory.add(inventory.getStackInSlot(i));
		}
		return listInventory;
	}
	
	public static List<Integer> getSlotLimitsList (IItemHandler inventory) {
		List<Integer> listInventory = new ArrayList<Integer>();
		for (int i = 0; i < inventory.getSlots(); i++)
		{
			listInventory.add(inventory.getSlotLimit(i));
		}
		return listInventory;
	}
	
	public static void insertStack (IItemHandlerModifiable inventory, int slot, ItemStack stack) {
		inventory.setStackInSlot(slot, ItemUtils.mergeStacks(inventory.getSlotLimit(slot), true, inventory.getStackInSlot(slot), stack));
	}
}
