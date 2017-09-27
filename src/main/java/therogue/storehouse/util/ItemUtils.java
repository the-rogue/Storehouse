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

import net.minecraft.item.ItemStack;

public class ItemUtils {
	
	/**
	 * @param stackA The first stack to be compared, shouldn't be empty (i.e. the source stack)
	 * @param stackB The second stack to be compared, this can be empty (i.e. the destination stack)
	 * 
	 * @return Whether stackA can be merged with stackB
	 */
	public static boolean areStacksMergable (ItemStack stackA, ItemStack stackB) {
		return !stackA.isEmpty() && (stackB.isEmpty() || (stackB.getItem() == stackA.getItem() && stackA.isStackable() && stackB.isStackable() && (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) && ItemStack.areItemStackTagsEqual(stackA, stackB)));
	}
	
	public static boolean areStacksMergableWithLimit (int limit, ItemStack stackA, ItemStack stackB) {
		return stackA.getCount() + stackB.getCount() <= Math.min(limit, stackA.getMaxStackSize()) && areStacksMergable(stackA, stackB);
	}
	
	public static boolean areStacksEqual (ItemStack stack1, ItemStack stack2, boolean useMeta) {
		if (!stack1.isEmpty() && stack1.getItem() == stack2.getItem() && !((stack1.getHasSubtypes() || useMeta) && stack1.getMetadata() != stack2.getMetadata())) return true;
		return false;
	}
	
	public static ItemStack mergeStacks (int limit, boolean modifyStacks, ItemStack... stacks) {
		if (stacks == null || stacks.length <= 0)
		{
			return null;
		}
		else if (stacks.length == 1) return stacks[0];
		ItemStack result = stacks[0].copy();
		if (modifyStacks)
		{
			stacks[0].setCount(0);
		}
		for (int i = 1; i < stacks.length; i++)
		{
			int maxSize = Math.min(limit, result.getMaxStackSize());
			if (result.isEmpty())
			{
				result = stacks[i].copy();
				if (result.getCount() > maxSize)
				{
					result.setCount(maxSize);
					if (modifyStacks)
					{
						stacks[i].setCount(stacks[i].getCount() - maxSize);
					}
				}
				else
				{
					if (modifyStacks){
						stacks[i].setCount(0);
					}
				}
				continue;
			}
			if (areStacksMergable(stacks[i], result))
			{
				if (result.getCount() + stacks[i].getCount() > maxSize)
				{
					if (modifyStacks)
					{
						stacks[i].setCount(result.getCount() + stacks[i].getCount() - maxSize);
					}
					result.setCount(maxSize);
				}
				else
				{
					result.setCount(result.getCount() + stacks[i].getCount());
					if (modifyStacks)
					{
						stacks[i].setCount(0);
					}
				}
			}
		}
		return result;
	}
}
