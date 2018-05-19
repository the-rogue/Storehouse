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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.crafting.wrapper.IRecipeComponent;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.tile.ITile;

public class MachineRecipe<T extends ITile> implements IMachineRecipe<T> {
	
	public final Predicate<ICraftingManager<T>> correctMode;
	public final int timeTaken;
	private final List<IRecipeComponent> craftingOutputs;
	private final List<IRecipeComponent> craftingInputs;
	
	public static <T extends ITile> MachineRecipe<T> create (Class<T> crafterClass, int timeTaken, IRecipeComponent craftingOutputs,
			IRecipeComponent... craftingInputs) {
		MachineRecipe<T> recipe = new MachineRecipe<T>(timeTaken, craftingOutputs, craftingInputs);
		MachineCraftingHandler.register(crafterClass, recipe);
		return recipe;
	}
	
	public static <T extends ITile> MachineRecipe<T> create (Class<T> crafterClass, Predicate<ICraftingManager<T>> correctMode, int timeTaken,
			IRecipeComponent craftingOutputs,
			IRecipeComponent... craftingInputs) {
		MachineRecipe<T> recipe = new MachineRecipe<T>(correctMode, timeTaken, craftingOutputs, craftingInputs);
		MachineCraftingHandler.register(crafterClass, recipe);
		return recipe;
	}
	
	public static <T extends ITile> MachineRecipe<T> create (Class<T> crafterClass, int timeTaken, List<IRecipeComponent> craftingOutputs,
			IRecipeComponent... craftingInputs) {
		MachineRecipe<T> recipe = new MachineRecipe<T>(timeTaken, craftingOutputs, craftingInputs);
		MachineCraftingHandler.register(crafterClass, recipe);
		return recipe;
	}
	
	public static <T extends ITile> MachineRecipe<T> create (Class<T> crafterClass, Predicate<ICraftingManager<T>> correctMode, int timeTaken,
			List<IRecipeComponent> craftingOutputs, IRecipeComponent... craftingInputs) {
		MachineRecipe<T> recipe = new MachineRecipe<T>(correctMode, timeTaken, craftingOutputs, craftingInputs);
		MachineCraftingHandler.register(crafterClass, recipe);
		return recipe;
	}
	
	public MachineRecipe (int timeTaken, IRecipeComponent craftingOutputs, IRecipeComponent... craftingInputs) {
		this(timeTaken, Lists.newArrayList(craftingOutputs), craftingInputs);
	}
	
	public MachineRecipe (Predicate<ICraftingManager<T>> correctMode, int timeTaken, IRecipeComponent craftingOutputs, IRecipeComponent... craftingInputs) {
		this(correctMode, timeTaken, Lists.newArrayList(craftingOutputs), craftingInputs);
	}
	
	public MachineRecipe (int timeTaken, List<IRecipeComponent> craftingOutputs, IRecipeComponent... craftingInputs) {
		this.correctMode = (ICraftingManager<T> manager) -> true;
		this.timeTaken = timeTaken;
		this.craftingOutputs = craftingOutputs;
		this.craftingInputs = Arrays.asList(craftingInputs);
	}
	
	// All ordered slots must have a placeholder of IRecipeComponent.Empty if they are unused
	public MachineRecipe (Predicate<ICraftingManager<T>> correctMode, int timeTaken, List<IRecipeComponent> craftingOutputs, IRecipeComponent... craftingInputs) {
		this.correctMode = correctMode;
		this.timeTaken = timeTaken;
		this.craftingOutputs = craftingOutputs;
		this.craftingInputs = Arrays.asList(craftingInputs);
	}
	
	public IRecipeComponent getInputComponent (int index) {
		return craftingInputs.get(index);
	}
	
	public int getAmountOfInputs () {
		return craftingInputs.size();
	}
	
	public IRecipeComponent getOutputComponent (int index) {
		return craftingOutputs.get(index);
	}
	
	public int getAmountOfOutputs () {
		return craftingOutputs.size();
	}
	
	@Override
	public int timeTaken (ICraftingManager<T> machine) {
		return timeTaken;
	}
	
	@Override
	public boolean itemValidForRecipe (ICraftingManager<T> cm, int index, IRecipeWrapper stack) {
		if (!correctMode.test(cm)) return false;
		IRecipeInventory craftingInventory = cm.getCraftingInventory();
		Set<Integer> matchedIngredients = new HashSet<Integer>();
		int emptySlots = 0;
		for (int i = 0; i < craftingInventory.getSize(); i++)
		{
			if (craftingInventory.getComponent(i).isUnUsed())
			{
				++emptySlots;
				continue;
			}
			int ingredientIndex = matchesRecipeIngredient(cm, i, craftingInventory.getComponent(i), matchedIngredients);
			if (ingredientIndex != -1)
			{
				matchedIngredients.add(ingredientIndex);
			}
		}
		if (matchedIngredients.size() + emptySlots > getAmountOfInputs()
				&& matchesRecipeIngredient(cm, index, stack, null) != -1)
			return true;
		if (matchesRecipeIngredient(cm, index, stack, matchedIngredients) != -1) return true;
		return false;
	}
	
	private int matchesRecipeIngredient (ICraftingManager<T> machine, int index, IRecipeWrapper test, @Nullable Set<Integer> exclusions) {
		Set<Integer> orderedSlots = machine.getOrderMattersSlots();
		if (orderedSlots.contains(index))
		{
			if (getInputComponent(index).matches(test)) return index;
			return -1;
		}
		for (int i = 0; i < getAmountOfInputs(); i++)
		{
			if (exclusions != null && exclusions.contains(i)) continue;
			if (orderedSlots.contains(i)) continue;
			if (getInputComponent(i).isUnUsed()) continue;
			if (getInputComponent(i).matches(test)) return i;
		}
		return -1;
	}
	
	private Map<Integer, Integer> getCorrespondingInventorySlots (IRecipeInventory inventory,
			@Nullable List<Integer> slotLimits) {
		Map<Integer, Integer> slots = new HashMap<Integer, Integer>();
		NonNullList<IRecipeWrapper> availableIngredients = NonNullList.create();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			availableIngredients.add(inventory.getComponent(i));
		}
		for (int i = 0; i < getAmountOfOutputs(); i++)
		{
			IRecipeComponent thisInput = getOutputComponent(i);
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
				availableIngredients.get(index).increaseSize(-thisInput.getSize());
			}
			slots.put(i, index);
		}
		return slots;
	}
	
	// TODO : Make sure this recognises that there may be multiple items in an ingredient stack
	@Override
	public boolean matches (ICraftingManager<T> cm) {
		if (!correctMode.test(cm)) return false;
		IRecipeInventory craftInventory = cm.getCraftingInventory();
		IRecipeInventory outputInventory = cm.getOutputInventory();
		Map<Integer, Integer> ingredientSlots = new HashMap<Integer, Integer>();
		Set<Integer> orderedSlots = cm.getOrderMattersSlots();
		if (getAmountOfOutputs() > outputInventory.getSize()) return false;
		Map<Integer, Integer> outputMap = getCorrespondingInventorySlots(outputInventory, getSlotLimitsList(outputInventory));
		if (!checkMatchingSlots(outputMap)) return false;
		for (Integer i : orderedSlots)
		{
			if (getInputComponent(i).isUnUsed()) continue;
			if (!getInputComponent(i).matches(craftInventory.getComponent(i))) return false;
			ingredientSlots.put(i, i);
		}
		NonNullList<IRecipeWrapper> availableIngredients = NonNullList.create();
		for (int i = 0; i < craftInventory.getSize(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			availableIngredients.add(craftInventory.getComponent(i));
		}
		for (int i = 0; i < getAmountOfInputs(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			if (getInputComponent(i).isUnUsed()) continue;
			int index = getMachedCraftingIndex(availableIngredients, getInputComponent(i));
			if (index != -1)
			{
				availableIngredients.get(index).increaseSize(-getInputComponent(i).getSize());
				ingredientSlots.put(i, index);
			}
			else
			{
				return false;
			}
		}
		for (Integer ingredient : ingredientSlots.keySet())
		{
			IRecipeComponent component = getInputComponent(ingredient);
			IRecipeWrapper stack = craftInventory.getComponent(ingredientSlots.get(ingredient) + orderedSlots.size());
			IRecipeWrapper testStack = stack.copy();
			testStack.increaseSize(-component.getSize());
			IRecipeWrapper container = component.getResidue();
			if (!container.isUnUsed())
			{
				if (stack.getSize() > component.getSize() && !testStack.mergable(container, craftInventory.getComponentSlotLimit(ingredient)))
					return false;
			}
		}
		return true;
	}
	
	@Override
	public Result doTick (ICraftingManager<T> cm) {
		if (!matches(cm)) return Result.RESET;
		T tile = cm.getAttachedTile();
		if (!tile.hasCapability(CapabilityEnergy.ENERGY, null)) return Result.RESET;
		Result res = cm.canRun();
		if (res == Result.CONTINUE) cm.doSpecifiedRunTick();
		return res;
	}
	
	@Override
	public Result end (ICraftingManager<T> cm) {
		if (!matches(cm)) return Result.RESET;
		IRecipeInventory craftingInventory = cm.getCraftingInventory();
		IRecipeInventory outputInventory = cm.getOutputInventory();
		Map<Integer, Integer> ingredientSlots = new HashMap<Integer, Integer>();
		Set<Integer> orderedSlots = cm.getOrderMattersSlots();
		// Map Ordered slots to the machine slot
		for (Integer i : orderedSlots)
		{
			if (!getInputComponent(i).isUnUsed()) ingredientSlots.put(i, i);
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
				availableIngredients.add(craftingInventory.getComponent(i));
			}
		}
		// Map Unordered Slots to the machine slot with the ingredient
		for (int i = 0; i < getAmountOfInputs(); i++)
		{
			if (orderedSlots.contains(i)) continue;
			if (getInputComponent(i).isUnUsed()) continue;
			int index = getMachedCraftingIndex(availableIngredients, getInputComponent(i));
			if (index != -1)
			{
				availableIngredients.get(index).increaseSize(-getInputComponent(i).getSize());
				ingredientSlots.put(i, index);
			}
		}
		// Remove all the items that are used up in the process and deposit any left-overs
		for (Integer ingredient : ingredientSlots.keySet())
		{
			craftingInventory.extractComponent(ingredientSlots.get(ingredient), getInputComponent(ingredient).getSize(), false);
			IRecipeWrapper container = getInputComponent(ingredient).getResidue();
			if (!container.isUnUsed())
			{
				craftingInventory.insertComponent(ingredientSlots.get(ingredient), container, false);
			}
		}
		// Put all output items in the output slots
		Map<Integer, Integer> outputMap = getCorrespondingInventorySlots(outputInventory, getSlotLimitsList(outputInventory));
		for (int i = 0; i < getAmountOfOutputs(); i++)
		{
			outputInventory.insertComponent(outputMap.get(i), getOutputComponent(i).getWrapper(), false);
		}
		return Result.CONTINUE;
	}
	
	private boolean checkMatchingSlots (Map<Integer, Integer> slotMap) {
		for (Integer i : slotMap.keySet())
		{
			if (slotMap.get(i) < 0) return false;
		}
		return true;
	}
	
	private static List<Integer> getSlotLimitsList (IRecipeInventory inventory) {
		List<Integer> listInventory = new ArrayList<Integer>();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			listInventory.add(inventory.getComponentSlotLimit(i));
		}
		return listInventory;
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
	
	@Override
	public String toString () {
		return "Inputs: " + craftingInputs.toString() + "\nOutputs: " + craftingOutputs.toString();
	}
}
