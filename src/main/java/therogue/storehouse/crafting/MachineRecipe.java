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
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import therogue.storehouse.util.CraftingHelper;
import therogue.storehouse.util.ItemUtils;

public class MachineRecipe {
	
	public final Predicate<IRecipeUser> correctMode;
	public final int energyRequired;
	public final ItemStack output[];
	public final List<RecipeInput> craftingInputs;
	
	public MachineRecipe (Predicate<IRecipeUser> correctMode, int energyRequired, ItemStack output, RecipeInput... craftingInputs) {
		this(correctMode, energyRequired, new ItemStack[] { output }, craftingInputs);
	}
	
	public MachineRecipe (Predicate<IRecipeUser> correctMode, int energyRequired, ItemStack output[], RecipeInput... craftingInputs) {
		this.correctMode = correctMode;
		this.energyRequired = energyRequired;
		this.output = output;
		this.craftingInputs = Arrays.asList(craftingInputs);
	}
	
	public ItemStack[] getResult () {
		ItemStack[] copy = new ItemStack[output.length];
		for (int i = 0; i < output.length; i++)
		{
			copy[i] = output[i].copy();
		}
		return copy;
	}
	
	public MatchReturn matches (IRecipeUser machine) {
		if (!correctMode.test(machine)) return new MatchReturn();
		NonNullList<ItemStack> available = machine.getCraftingStacks().inventory;
		NonNullList<ItemStack> inventoryIngredients = NonNullList.create();
		for (int i = 0; i < machine.getNumberOrderMattersSlots(); i++)
		{
			if (craftingInputs.get(i).isEmpty())
			{
				inventoryIngredients.add(ItemStack.EMPTY);
				continue;
			}
			if (craftingInputs.get(i).matches(available.get(i)))
			{
				inventoryIngredients.add(available.remove(i));
			}
			else
			{
				return new MatchReturn();
			}
		}
		for (int i = machine.getNumberOrderMattersSlots(); i < machine.getNumberCraftingSlots(); i++)
		{
			if (craftingInputs.get(i).isEmpty())
			{
				inventoryIngredients.add(ItemStack.EMPTY);
				continue;
			}
			int index = CraftingHelper.getMachedCraftingIndex(available, craftingInputs.get(i));
			if (index != -1)
			{
				inventoryIngredients.add(available.remove(index));
			}
			else
			{
				return new MatchReturn();
			}
		}
		return new MatchReturn(this, inventoryIngredients, machine.getCraftingStacks());
	}
	
	public CraftingStacks craft (MatchReturn recipe) {
		if (!recipe.matched || recipe.owner != this) return recipe.machineInventory;
		for (ItemStack stack : recipe.ingredients)
		{
			ItemStack container = ForgeHooks.getContainerItem(stack.copy());
			if (!container.isEmpty() && stack.getCount() > 1 && !ItemUtils.areItemStacksMergable(container, stack)) { return recipe.machineInventory; }
		}
		for (ItemStack stack : recipe.ingredients)
		{
			ItemStack container = ForgeHooks.getContainerItem(stack.copy());
			stack.setCount(stack.getCount() - 1);
			if (!container.isEmpty())
			{
				int index = recipe.machineInventory.inventory.indexOf(stack);
				recipe.machineInventory.inventory.set(index, ItemUtils.mergeStacks(64, true, stack, container));
			}
		}
		for (ItemStack stack : output)
		{
			recipe.machineInventory.output.add(stack.copy());
		}
		return recipe.machineInventory;
	}
	
	public static class MatchReturn {
		
		public final MachineRecipe owner;
		public final boolean matched;
		public final NonNullList<ItemStack> ingredients;
		public final CraftingStacks machineInventory;
		
		private MatchReturn (MachineRecipe owner, NonNullList<ItemStack> ingredients, CraftingStacks machineInventory) {
			this.owner = owner;
			this.matched = true;
			this.ingredients = ingredients;
			this.machineInventory = machineInventory;
		}
		
		private MatchReturn () {
			this.owner = null;
			this.matched = false;
			this.ingredients = NonNullList.create();
			this.machineInventory = null;
		}
	}
}
