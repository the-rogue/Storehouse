/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.inventory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import therogue.storehouse.util.ItemUtils;

public abstract class InventoryManager implements IItemHandlerModifiable {
	
	public final ContainerHandler containerCapability = new ContainerHandler();
	private IInventoryCapability owner;
	private NonNullList<ItemStack> inventory;
	private Set<Integer> extractable_slots;
	private Set<Integer> insertable_slots;
	private Set<Integer> insertable_gui_slots;
	
	public InventoryManager (IInventoryCapability owner, int size, @Nullable Integer[] insertable_slots, @Nullable Integer[] extractable_slots) {
		this(owner, size, insertable_slots, null, extractable_slots);
	}
	
	public InventoryManager (IInventoryCapability owner, int size, @Nullable Integer[] insertable_slots, @Nullable Integer[] insertable_gui_slots, @Nullable Integer[] extractable_slots) {
		this.owner = owner;
		inventory = NonNullList.<ItemStack> withSize(size, ItemStack.EMPTY);
		this.extractable_slots = new HashSet<Integer>();
		this.insertable_slots = new HashSet<Integer>();
		this.insertable_gui_slots = new HashSet<Integer>();
		if (extractable_slots == null)
		{
			for (int i = 0; i < size; i++)
			{
				this.extractable_slots.add(i);
			}
		}
		else
		{
			this.extractable_slots.addAll(Arrays.asList(extractable_slots));
		}
		if (insertable_slots == null)
		{
			for (int i = 0; i < size; i++)
			{
				this.insertable_slots.add(i);
			}
		}
		else
		{
			this.insertable_slots.addAll(Arrays.asList(insertable_slots));
		}
		this.insertable_gui_slots.addAll(this.insertable_slots);
		if (insertable_gui_slots != null) this.insertable_gui_slots.addAll(Arrays.asList(insertable_gui_slots));
	}
	
	// ---------------------IItemHandlerModifiable functions---------------------------------------------------
	/**
	 * Returns the number of slots available
	 *
	 * @return The number of slots available
	 **/
	@Override
	public int getSlots () {
		return inventory.size();
	}
	
	/**
	 * Returns the ItemStack in a given slot.
	 *
	 * The result's stack size may be greater than the itemstacks max size.
	 *
	 * If the result is null, then the slot is empty. If the result is not null but the stack size is zero, then it represents an empty slot that will only accept* a specific itemstack.
	 *
	 * <p/>
	 * IMPORTANT: This ItemStack MUST NOT be modified. This method is not for altering an inventories contents. Any implementers who are able to detect modification through this method should throw an exception.
	 * <p/>
	 * SERIOUSLY: DO NOT MODIFY THE RETURNED ITEMSTACK
	 *
	 * @param slot Slot to query
	 * @return ItemStack in given slot. May be null.
	 **/
	@Override
	public ItemStack getStackInSlot (int index) {
		if (checkIndex(index)) { return inventory.get(index); }
		return ItemStack.EMPTY;
	}
	
	/**
	 * Inserts an ItemStack into the given slot and return the remainder. The ItemStack should not be modified in this function! Note: This behaviour is subtly different from IFluidHandlers.fill()
	 *
	 * @param slot Slot to insert into.
	 * @param stack ItemStack to insert.
	 * @param simulate If true, the insertion is only simulated
	 * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return null). May be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
	 **/
	@Nonnull
	@Override
	public ItemStack insertItem (int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) return stack;
		if (!insertable_slots.contains(slot)) return stack;
		if (!isItemValidForSlotChecks(slot, stack)) return stack;
		ItemStack stackInSlot = getStackInSlot(slot);
		if (!ItemUtils.areStacksMergable(stack, stackInSlot)) return stack;
		ItemStack returns = stack.copy();
		ItemStack inSlot = ItemUtils.mergeStacks(Math.min(getSlotLimit(slot), stack.getMaxStackSize()), true, stackInSlot.copy(), returns);
		if (!simulate)
		{
			setStackInSlot(slot, inSlot);
		}
		owner.onInventoryChange();
		return returns;
	}
	
	/**
	 * Extracts an ItemStack from the given slot. The returned value must be an empty itemstack if nothing is extracted, otherwise it's stack size must not be greater than amount or the itemstacks getMaxStackSize().
	 *
	 * @param slot Slot to extract from.
	 * @param amount Amount to extract (may be greater than the current stacks max limit)
	 * @param simulate If true, the extraction is only simulated
	 * @return ItemStack extracted from the slot, must be an empty itemstack, if nothing can be extracted
	 **/
	@Override
	@Nonnull
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		if (!extractable_slots.contains(slot)) return ItemStack.EMPTY;
		if (amount == 0) return ItemStack.EMPTY;
		ItemStack stackInSlot = getStackInSlot(slot);
		if (stackInSlot.isEmpty()) return ItemStack.EMPTY;
		if (simulate)
		{
			stackInSlot = stackInSlot.copy();
		}
		int m = Math.min(stackInSlot.getMaxStackSize(), Math.min(stackInSlot.getCount(), amount));
		ItemStack returns = stackInSlot.splitStack(m);
		owner.onInventoryChange();
		return returns;
	}
	
	/**
	 * Retrieves the maximum stack size allowed to exist in the given slot.
	 *
	 * @param slot Slot to query.
	 * @return The maximum stack size allowed in the slot.
	 */
	@Override
	public int getSlotLimit (int slot) {
		switch (slot) {
			default:
				return 64;
		}
	}
	
	/**
	 * Overrides the stack in the given slot. This method is used by the standard Forge helper methods and classes. It is not intended for general use by other mods, and the handler may throw an error if it is called unexpectedly. It is also used by Internal Methods, but not to be used externally
	 * other than by forge, use insert and extract and the simulate functions.
	 *
	 * @param slot Slot to modify
	 * @param stack ItemStack to set slot to (may be an empty itemstack)
	 * @throws RuntimeException if the handler is called in a way that the handler was not expecting.
	 **/
	@Override
	public void setStackInSlot (int index, ItemStack stack) {
		if (checkIndex(index))
		{
			inventory.set(index, stack);
		}
		if (!stack.isEmpty() && stack.getCount() > this.getSlotLimit(index))
		{
			stack.setCount(this.getSlotLimit(index));
		}
		owner.onInventoryChange();
	}
	
	// ---------------------------------Inventory Specific Methods------------------------------------
	/**
	 * Specify what items can go in which slots here for different inventories
	 * 
	 * @param slot The slot the stack would be inserted into
	 * @param stack The stack that would be inserted
	 * @return Whether or not this stack should be allowed to be placed into this inventory
	 */
	protected abstract boolean isItemValidForSlotChecks (int slot, ItemStack stack);
	
	// --------------------------------------INTERNAL METHODS------------------------------------------
	// FOR USE BY THE TILE ENTITY ONLY
	public void pushItems (int fromSlot, int toSlot) {
		ItemStack fromStack = getStackInSlot(fromSlot);
		if (fromStack.isEmpty()) return;
		if (!isItemValidForSlotChecks(toSlot, fromStack)) return;
		ItemStack toStack = getStackInSlot(toSlot);
		if (!ItemUtils.areStacksMergable(fromStack, toStack)) return;
		setStackInSlot(toSlot, ItemUtils.mergeStacks(Math.min(getSlotLimit(toSlot), fromStack.getMaxStackSize()), true, toStack, fromStack));
		owner.onInventoryChange();
	}
	
	private boolean checkIndex (int index) {
		if (index >= 0 && index < inventory.size()) { return true; }
		return false;
	}
	
	public String toString () {
		return "Inventory of the InventoryManager: " + inventory.toString();
	}
	
	// ----------------------------NBT Functions-----------------------------------------------------
	/**
	 * Writes the data of the inventory to nbt, to be loaded back later
	 * 
	 * @param nbt The NBTTagCompound to write to.
	 */
	public void writeToNBT (NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.getSlots(); ++i)
		{
			if (!this.getStackInSlot(i).isEmpty())
			{
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				this.getStackInSlot(i).writeToNBT(stackTag);
				list.appendTag(stackTag);
			}
		}
		nbt.setTag("Items", list);
	}
	
	/**
	 * Reads Data back from an nbt tag, after it has been loaded back
	 * 
	 * @param nbt The nbt tag to read from.
	 */
	public void readFromNBT (NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound stackTag = list.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot") & 255;
			this.setStackInSlot(slot, new ItemStack(stackTag));
		}
	}
	
	private class ContainerHandler implements IItemHandlerModifiable {
		
		// ---------------------IItemHandlerModifiable functions---------------------------------------------------
		/**
		 * Returns the number of slots available
		 *
		 * @return The number of slots available
		 **/
		@Override
		public int getSlots () {
			return InventoryManager.this.getSlots();
		}
		
		/**
		 * Returns the ItemStack in a given slot.
		 *
		 * The result's stack size may be greater than the itemstacks max size.
		 *
		 * If the result is null, then the slot is empty. If the result is not null but the stack size is zero, then it represents an empty slot that will only accept* a specific itemstack.
		 *
		 * <p/>
		 * IMPORTANT: This ItemStack MUST NOT be modified. This method is not for altering an inventories contents. Any implementers who are able to detect modification through this method should throw an exception.
		 * <p/>
		 * SERIOUSLY: DO NOT MODIFY THE RETURNED ITEMSTACK
		 *
		 * @param slot Slot to query
		 * @return ItemStack in given slot. May be null.
		 **/
		@Override
		public ItemStack getStackInSlot (int index) {
			owner.onInventoryChange();
			return InventoryManager.this.getStackInSlot(index);
		}
		
		/**
		 * Inserts an ItemStack into the given slot and return the remainder. The ItemStack should not be modified in this function! Note: This behaviour is subtly different from IFluidHandlers.fill()
		 *
		 * @param slot Slot to insert into.
		 * @param stack ItemStack to insert.
		 * @param simulate If true, the insertion is only simulated
		 * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return null). May be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
		 **/
		@Nonnull
		@Override
		public ItemStack insertItem (int slot, ItemStack stack, boolean simulate) {
			if (stack.isEmpty()) return stack;
			if (!insertable_gui_slots.contains(slot)) return stack;
			if (!isItemValidForSlotChecks(slot, stack)) return stack;
			ItemStack stackInSlot = getStackInSlot(slot);
			if (!ItemUtils.areStacksMergable(stack, stackInSlot)) return stack;
			ItemStack returns = stack.copy();
			ItemStack inSlot = ItemUtils.mergeStacks(Math.min(getSlotLimit(slot), stack.getMaxStackSize()), true, stackInSlot.copy(), returns);
			if (!simulate)
			{
				setStackInSlot(slot, inSlot);
			}
			owner.onInventoryChange();
			return returns;
		}
		
		/**
		 * Extracts an ItemStack from the given slot. The returned value must be an empty itemstack if nothing is extracted, otherwise it's stack size must not be greater than amount or the itemstacks getMaxStackSize().
		 *
		 * @param slot Slot to extract from.
		 * @param amount Amount to extract (may be greater than the current stacks max limit)
		 * @param simulate If true, the extraction is only simulated
		 * @return ItemStack extracted from the slot, must be an empty itemstack, if nothing can be extracted
		 **/
		@Override
		@Nonnull
		public ItemStack extractItem (int slot, int amount, boolean simulate) {
			if (amount == 0) return ItemStack.EMPTY;
			ItemStack stackInSlot = getStackInSlot(slot);
			if (stackInSlot.isEmpty()) return ItemStack.EMPTY;
			if (simulate)
			{
				stackInSlot = stackInSlot.copy();
			}
			int m = Math.min(stackInSlot.getMaxStackSize(), Math.min(stackInSlot.getCount(), amount));
			ItemStack returns = stackInSlot.splitStack(m);
			owner.onInventoryChange();
			return returns;
		}
		
		/**
		 * Retrieves the maximum stack size allowed to exist in the given slot.
		 *
		 * @param slot Slot to query.
		 * @return The maximum stack size allowed in the slot.
		 */
		@Override
		public int getSlotLimit (int slot) {
			return InventoryManager.this.getSlotLimit(slot);
		}
		
		/**
		 * Overrides the stack in the given slot. This method is used by the standard Forge helper methods and classes. It is not intended for general use by other mods, and the handler may throw an error if it is called unexpectedly. It is also used by Internal Methods, but not to be used
		 * externally other than by forge, use insert and extract and the simulate functions.
		 *
		 * @param slot Slot to modify
		 * @param stack ItemStack to set slot to (may be an empty itemstack)
		 * @throws RuntimeException if the handler is called in a way that the handler was not expecting.
		 **/
		@Override
		public void setStackInSlot (int index, ItemStack stack) {
			InventoryManager.this.setStackInSlot(index, stack);
		}
	}
}
