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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandlerModifiable;
import therogue.storehouse.util.CraftingHelper;
import therogue.storehouse.util.ItemUtils;
import therogue.storehouse.util.LOG;

public class MachineRecipe {
	
	public final Predicate<ICrafter> correctMode;
	public final int timeTaken;
	public final List<ItemStack> output;
	public final List<RecipeInput> craftingInputs;
	
	public MachineRecipe (Predicate<ICrafter> correctMode, int timeTaken, ItemStack output, RecipeInput... craftingInputs) {
		this(correctMode, timeTaken, Lists.newArrayList(output), craftingInputs);
	}
	
	public MachineRecipe (Predicate<ICrafter> correctMode, int timeTaken, List<ItemStack> output, RecipeInput... craftingInputs) {
		this.correctMode = correctMode;
		this.timeTaken = timeTaken;
		this.output = output;
		this.craftingInputs = Arrays.asList(craftingInputs);
	}
	
	public List<ItemStack> getResults () {
		List<ItemStack> copy = Lists.newArrayList();
		for (ItemStack stack : output)
		{
			copy.add(stack.copy());
		}
		return copy;
	}
	
	public boolean matches (ICrafter machine) {
		if (!correctMode.test(machine)) return false;
		IItemHandlerModifiable craftInventory = machine.getCraftingInventory();
		Set<Integer> orderedSlots = machine.getOrderMattersSlots();
		for (Integer i : orderedSlots)
		{
			if (craftingInputs.get(i).isEmpty()) continue;
			if (!craftingInputs.get(i).matches(craftInventory.getStackInSlot(i))) return false;
		}
		NonNullList<ItemStack> availableIngredients = NonNullList.create();
		for (int i = 0; i < craftInventory.getSlots(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			availableIngredients.add(craftInventory.getStackInSlot(i).copy());
		}
		for (int i = 0; i < craftInventory.getSlots(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			if (craftingInputs.get(i).isEmpty()) continue;
			int index = CraftingHelper.getMachedCraftingIndex(availableIngredients, craftingInputs.get(i));
			if (index != -1)
			{
				availableIngredients.get(index).shrink(craftingInputs.get(index).getInput().getCount());
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	private Map<Integer, Integer> ingredientSlots (ICrafter machine) {
		IItemHandlerModifiable craftInventory = machine.getCraftingInventory();
		Map<Integer, Integer> slots = new HashMap<Integer, Integer>();
		Set<Integer> orderedSlots = machine.getOrderMattersSlots();
		for (Integer i : orderedSlots)
		{
			if (!craftingInputs.get(i).isEmpty()) slots.put(i, craftingInputs.get(i).getInput().getCount());
		}
		NonNullList<ItemStack> availableIngredients = NonNullList.create();
		for (int i = 0; i < craftInventory.getSlots(); i++)
		{
			if (orderedSlots.contains(i))
			{
				availableIngredients.add(ItemStack.EMPTY);
			}
			else
			{
				availableIngredients.add(craftInventory.getStackInSlot(i).copy());
			}
		}
		for (int i = 0; i < craftInventory.getSlots(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			if (craftingInputs.get(i).isEmpty()) continue;
			int index = CraftingHelper.getMachedCraftingIndex(availableIngredients, craftingInputs.get(i));
			if (index != -1)
			{
				availableIngredients.get(index).shrink(craftingInputs.get(index).getInput().getCount());
				if (slots.get(index) != null)
				{
					slots.put(index, slots.get(index) + craftingInputs.get(i).getInput().getCount());
				}
				else
				{
					slots.put(index, craftingInputs.get(i).getInput().getCount());
				}
			}
		}
		return slots;
	}
	
	public List<ItemStack> craft (ICrafter machine) {
		if (!matches(machine)) return Lists.newArrayList();
		Map<Integer, Integer> ingredientSlots = ingredientSlots(machine);
		LOG.log("info", ingredientSlots);
		IItemHandlerModifiable inventoryCraft = machine.getCraftingInventory();
		Map<Integer, ItemStack> containerItems = new HashMap<Integer, ItemStack>();
		for (Integer slot : ingredientSlots.keySet())
		{
			ItemStack stack = inventoryCraft.getStackInSlot(slot);
			ItemStack copy = stack.copy();
			copy.setCount(ingredientSlots.get(slot));
			ItemStack container = ForgeHooks.getContainerItem(copy);
			if (!container.isEmpty())
			{
				if (stack.getCount() > 1 && !ItemUtils.areItemStacksMergableWithLimit(Math.min(stack.getMaxStackSize(), inventoryCraft.getSlotLimit(slot)), container, stack)) return Lists.newArrayList();
				containerItems.put(slot, container);
			}
		}
		for (Integer slot : ingredientSlots.keySet())
		{
			ItemStack stack = inventoryCraft.getStackInSlot(slot);
			stack.setCount(stack.getCount() - ingredientSlots.get(slot));
			if (containerItems.containsKey(slot))
			{
				ItemStack container = containerItems.get(slot);
				inventoryCraft.setStackInSlot(slot, ItemUtils.mergeStacks(Math.min(stack.getMaxStackSize(), inventoryCraft.getSlotLimit(slot)), true, stack, container));
			}
		}
		return getResults();
	}
	
	@Override
	public String toString () {
		return "Inputs: " + craftingInputs.toString() + "\nOutputs: " + output.toString();
	}
}
