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

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.util.NonNullList;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.wrapper.IRecipeComponent;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.util.CraftingHelper;

public class MachineRecipe {
	
	public static final Predicate<ICrafter> ALWAYSMODE = new Predicate<ICrafter>() {
		
		@Override
		public boolean test (ICrafter crafter) {
			return true;
		}
	};
	public final Predicate<ICrafter> correctMode;
	public final int timeTaken;
	public final List<IRecipeComponent> output;
	public final List<IRecipeComponent> craftingInputs;
	
	public MachineRecipe (Predicate<ICrafter> correctMode, int timeTaken, IRecipeComponent output, IRecipeComponent... craftingInputs) {
		this(correctMode, timeTaken, Lists.newArrayList(output), craftingInputs);
	}
	
	public MachineRecipe (Predicate<ICrafter> correctMode, int timeTaken, List<IRecipeComponent> output, IRecipeComponent... craftingInputs) {
		this.correctMode = correctMode;
		this.timeTaken = timeTaken;
		this.output = output;
		this.craftingInputs = Arrays.asList(craftingInputs);
	}
	
	public List<IRecipeComponent> getResults () {
		List<IRecipeComponent> copy = Lists.newArrayList();
		for (IRecipeComponent stack : output)
		{
			copy.add(stack.copy());
		}
		return copy;
	}
	
	public List<IRecipeWrapper> getWrappedResults () {
		List<IRecipeWrapper> copy = Lists.newArrayList();
		for (IRecipeComponent stack : output)
		{
			copy.add(stack.getWrapper());
		}
		return copy;
	}
	
	public int matchesRecipeIngredient (ICrafter machine, int index, IRecipeWrapper test, @Nullable Set<Integer> exclusions) {
		int orderedTest = matchesOrderedRecipeIngredient(machine, index, test);
		if (orderedTest == -2) return matchesUnOrderedRecipeIngredient(machine, index, test, exclusions);
		return orderedTest;
	}
	
	public int matchesOrderedRecipeIngredient (ICrafter machine, int index, IRecipeWrapper test) {
		if (!correctMode.test(machine)) return -1;
		Set<Integer> orderedSlots = machine.getOrderMattersSlots();
		if (orderedSlots.contains(index))
		{
			if (craftingInputs.get(index).matches(test)) return index;
			return -1;
		}
		return -2;
	}
	
	public int matchesUnOrderedRecipeIngredient (ICrafter machine, int index, IRecipeWrapper test, @Nullable Set<Integer> exclusions) {
		if (!correctMode.test(machine)) return -1;
		Set<Integer> orderedSlots = machine.getOrderMattersSlots();
		for (int i = 0; i < craftingInputs.size(); i++)
		{
			if (exclusions != null && exclusions.contains(i)) continue;
			if (orderedSlots.contains(i)) continue;
			if (craftingInputs.get(i).isUnUsed()) continue;
			if (craftingInputs.get(i).matches(test)) return i;
		}
		return -1;
	}
	
	public boolean matches (ICrafter machine) {
		if (!correctMode.test(machine)) return false;
		IRecipeInventory craftInventory = machine.getCraftingInventory();
		Set<Integer> orderedSlots = machine.getOrderMattersSlots();
		for (Integer i : orderedSlots)
		{
			if (craftingInputs.get(i).isUnUsed()) continue;
			if (!craftingInputs.get(i).matches(craftInventory.getComponent(i))) return false;
		}
		NonNullList<IRecipeWrapper> availableIngredients = NonNullList.create();
		for (int i = 0; i < craftInventory.getSize(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			availableIngredients.add(craftInventory.getComponent(i).copy());
		}
		for (int i = 0; i < craftingInputs.size(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			if (craftingInputs.get(i).isUnUsed()) continue;
			int index = CraftingHelper.getMachedCraftingIndex(availableIngredients, craftingInputs.get(i));
			if (index != -1)
			{
				availableIngredients.get(index).increaseSize(-craftingInputs.get(index).getSize());
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	private Map<Integer, Integer> ingredientSlots (ICrafter machine) {
		IRecipeInventory craftInventory = machine.getCraftingInventory();
		Map<Integer, Integer> slots = new HashMap<Integer, Integer>();
		Set<Integer> orderedSlots = machine.getOrderMattersSlots();
		for (Integer i : orderedSlots)
		{
			if (!craftingInputs.get(i).isUnUsed()) slots.put(i, i);
		}
		NonNullList<IRecipeWrapper> availableIngredients = NonNullList.create();
		for (int i = 0; i < craftInventory.getSize(); i++)
		{
			if (orderedSlots.contains(i))
			{
				availableIngredients.add(IRecipeWrapper.NOTHING);
			}
			else
			{
				availableIngredients.add(craftInventory.getComponent(i).copy());
			}
		}
		for (int i = 0; i < craftInventory.getSize(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			if (craftingInputs.get(i).isUnUsed()) continue;
			int index = CraftingHelper.getMachedCraftingIndex(availableIngredients, craftingInputs.get(i));
			if (index != -1)
			{
				availableIngredients.get(index).increaseSize(-craftingInputs.get(index).getSize());
				slots.put(i, index);
			}
		}
		return slots;
	}
	
	public boolean craft (ICrafter machine) {
		if (!matches(machine)) return false;
		Map<Integer, Integer> ingredientSlots = ingredientSlots(machine);
		IRecipeInventory inventoryCraft = machine.getCraftingInventory();
		Map<Integer, IRecipeWrapper> containerItems = new HashMap<Integer, IRecipeWrapper>();
		for (Integer ingredient : ingredientSlots.keySet())
		{
			IRecipeComponent component = craftingInputs.get(ingredient);
			IRecipeWrapper stack = inventoryCraft.getComponent(ingredientSlots.get(ingredient));
			IRecipeWrapper container = component.getResidue();
			if (!container.isUnUsed())
			{
				if (stack.getSize() > component.getSize() && !stack.mergable(container, inventoryCraft.getComponentSlotLimit(ingredient))) return false;
				containerItems.put(ingredient, container);
			}
		}
		for (Integer ingredient : ingredientSlots.keySet())
		{
			IRecipeWrapper stack = inventoryCraft.getComponent(ingredientSlots.get(ingredient));
			stack.increaseSize(-craftingInputs.get(ingredient).getSize());
			if (containerItems.containsKey(ingredient))
			{
				inventoryCraft.insertComponent(ingredientSlots.get(ingredient), containerItems.get(ingredient));
			}
		}
		return true;
	}
	
	@Override
	public String toString () {
		return "Inputs: " + craftingInputs.toString() + "\nOutputs: " + output.toString();
	}
}
