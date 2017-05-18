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

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import therogue.storehouse.crafting.RecipeInput;

public class CraftingHelper {
	
	public static int getMachedIndex (ItemStack[] array, ItemStack stack, boolean useMeta) {
		return getMachedIndex(Arrays.asList(array), stack, useMeta);
	}
	
	public static int getMachedIndex (List<ItemStack> list, ItemStack stack, boolean useMeta) {
		if (list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if ((stack.isEmpty() && list.get(i).isEmpty()) || ItemUtils.areItemStacksEqual(stack, list.get(i), useMeta)) { return i; }
			}
		}
		return -1;
	}
	
	public static int getMachedCraftingIndex (RecipeInput[] array, ItemStack stack, boolean useMeta) {
		return getMachedCraftingIndex(Arrays.asList(array), stack, useMeta);
	}
	
	public static int getMachedCraftingIndex (List<RecipeInput> list, ItemStack stack, boolean useMeta) {
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
}
