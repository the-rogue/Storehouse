/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.fluid;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;

public class FluidUtils {
	
	public static boolean areStacksMergable (@Nullable FluidStack stackA, @Nullable FluidStack stackB) {
		return stackA == null || stackA.isFluidEqual(stackB);
	}
	
	public static boolean areStacksMergableWithLimit (int limit, @Nullable FluidStack stackA, @Nullable FluidStack stackB) {
		return stackA == null || stackA.isFluidEqual(stackB) && stackA.amount + stackB.amount <= limit;
	}
	
	public static FluidStack mergeStacks (int limit, boolean modifyStacks, FluidStack... stacks) {
		if (stacks == null || stacks.length <= 0)
		{
			return null;
		}
		else if (stacks.length == 1) return stacks[0];
		FluidStack result = stacks[0].copy();
		if (modifyStacks)
		{
			stacks[0].amount = 0;
		}
		for (int i = 1; i < stacks.length; i++)
		{
			if (result.amount == 0)
			{
				result = stacks[i].copy();
				if (result.amount > limit)
				{
					result.amount = limit;
					if (modifyStacks)
					{
						stacks[i].amount -= limit;
					}
				}
				else
				{
					stacks[i].amount = 0;
				}
				continue;
			}
			if (result.isFluidEqual(stacks[i]))
			{
				if (result.amount + stacks[i].amount > limit)
				{
					if (modifyStacks)
					{
						stacks[i].amount = result.amount + stacks[i].amount - limit;
					}
					result.amount = limit;
				}
				else
				{
					result.amount = result.amount + stacks[i].amount;
					if (modifyStacks)
					{
						stacks[i].amount = 0;
					}
				}
			}
		}
		return result;
	}
}
