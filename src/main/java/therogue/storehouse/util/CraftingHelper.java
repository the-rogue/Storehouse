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

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.wrapper.IRecipeComponent;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;

public class CraftingHelper {
	
	public static int getMachedIndex (@Nullable List<Integer> limits, List<ItemStack> list, ItemStack stack) {
		if (list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if ((stack.isEmpty() && list.get(i).isEmpty()) || ItemUtils.areStacksMergableWithLimit(limits != null ? limits.get(i) != null ? limits.get(i) : 64 : 64, stack, list.get(i))) { return i; }
			}
		}
		return -1;
	}
	
	public static int getMachedCraftingIndex (List<IRecipeWrapper> list, IRecipeComponent stack) {
		if (list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (stack.matches(list.get(i))) { return i; }
			}
		}
		return -1;
	}
	
	public static Map<Integer, Integer> getCorrespondingInventorySlots (List<IRecipeComponent> inputs, IRecipeInventory inventory, @Nullable List<Integer> slotLimits) {
		Map<Integer, Integer> slots = new HashMap<Integer, Integer>();
		NonNullList<IRecipeWrapper> availableIngredients = NonNullList.create();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			availableIngredients.add(inventory.getComponent(i).copy());
		}
		for (int i = 0; i < inputs.size(); i++)
		{
			IRecipeComponent thisInput = inputs.get(i);
			if (thisInput.isUnUsed()) continue;
			int index = -1;
			for (int j = 0; j < availableIngredients.size(); j++)
			{
				if (availableIngredients.get(j).canAddComponent(thisInput, slotLimits != null ? slotLimits.get(j) : -1))
				{
					index = i;
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
	
	public static boolean checkMatchingSlots (Map<Integer, Integer> slotMap) {
		for (Integer i : slotMap.keySet())
		{
			if (slotMap.get(i) < 0) return false;
		}
		return true;
	}
	
	public static List<Integer> getSlotLimitsList (IRecipeInventory inventory) {
		List<Integer> listInventory = new ArrayList<Integer>();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			listInventory.add(inventory.getComponentSlotLimit(i));
		}
		return listInventory;
	}
}
