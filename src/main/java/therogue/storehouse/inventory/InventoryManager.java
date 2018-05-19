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
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.container.GuiItemCapability;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.ITileModule;
import therogue.storehouse.tile.ModuleContext;

public class InventoryManager implements ITileModule {
	
	private ITile owner;
	private NonNullList<ItemStack> inventory;
	private Set<Integer> extractable_slots;
	private Set<Integer> insertable_slots;
	private Set<Integer> insertable_gui_slots;
	/**
	 * Specify what items can go in which slots here for different inventories
	 * 
	 * @param slot The slot the stack would be inserted into
	 * @param stack The stack that would be inserted
	 * @return Whether or not this stack should be allowed to be placed into this inventory
	 */
	private BiPredicate<Integer, ItemStack> itemValidForSlotChecks = (slot, stack) -> true;
	
	public InventoryManager (ITile owner, int size, @Nullable Integer[] insertable_slots, @Nullable Integer[] extractable_slots) {
		this(owner, size, insertable_slots, null, extractable_slots);
	}
	
	public InventoryManager (ITile owner, int size, @Nullable Integer[] insertable_slots, @Nullable Integer[] insertable_gui_slots, @Nullable Integer[] extractable_slots) {
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
	
	/**
	 * Specify what items can go in which slots here for different inventories
	 * 
	 * @param slot The slot the stack would be inserted into
	 * @param stack The stack that would be inserted
	 * @return Whether or not this stack should be allowed to be placed into this inventory
	 */
	public void setItemValidForSlotChecks (BiPredicate<Integer, ItemStack> procedure) {
		this.itemValidForSlotChecks = procedure;
	}
	
	/**
	 * For use by the tile only
	 */
	public NonNullList<ItemStack> getInventory () {
		return inventory;
	}
	
	// ---------------------IItemHandlerModifiable functions---------------------------------------------------
	/**
	 * Returns the number of slots available
	 *
	 * @return The number of slots available
	 **/
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
	public ItemStack getStackInSlot (int index, ModuleContext context) {
		if (checkIndex(index)) return inventory.get(index).copy();
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
	public ItemStack insertItem (int slot, ItemStack stack, boolean simulate, ModuleContext context) {
		if (stack.isEmpty()) return stack;
		if ((context == ModuleContext.SIDE || context == ModuleContext.OTHER) && !insertable_slots.contains(slot)) return stack;
		if (context == ModuleContext.GUI && !insertable_gui_slots.contains(slot)) return stack;
		ItemStack stackInSlot = inventory.get(slot);
		if (context != ModuleContext.INTERNAL && !itemValidForSlotChecks.test(slot, stack) && !stack.isItemEqual(stackInSlot)) return stack;
		if (!ItemStackUtils.areStacksMergable(stack, stackInSlot)) return stack;
		ItemStack returns = stack.copy();
		ItemStack inSlot = ItemStackUtils.mergeStacks(Math.min(getSlotLimit(slot), stack.getMaxStackSize()), true, stackInSlot.copy(), returns);
		if (!simulate)
		{
			inventory.set(slot, inSlot);
			if (context != ModuleContext.INTERNAL) owner.notifyChange(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
		return returns;
	}
	
	/**
	 * Extracts an ItemStack from the given slot. The returned value must be an empty itemstack if nothing is extracted, otherwise it's stack size must not be greater than amount or the itemstacks getMaxStackSize().
	 *
	 * @param slot Slot to extract from.
	 * @param amount Amount to extract (may be greater than the current stacks max limit or -1 for the entire stack)
	 * @param simulate If true, the extraction is only simulated
	 * @return ItemStack extracted from the slot, must be an empty itemstack, if nothing can be extracted
	 **/
	@Nonnull
	public ItemStack extractItem (int slot, int amount, boolean simulate, ModuleContext context) {
		if (context != ModuleContext.INTERNAL && context != ModuleContext.GUI && !extractable_slots.contains(slot)) return ItemStack.EMPTY;
		if (amount == 0) return ItemStack.EMPTY;
		ItemStack stackInSlot = inventory.get(slot);
		if (stackInSlot.isEmpty()) return ItemStack.EMPTY;
		if (simulate)
		{
			stackInSlot = stackInSlot.copy();
		}
		int m = Math.min(stackInSlot.getMaxStackSize(), stackInSlot.getCount());
		if (amount != -1) m = Math.min(m, amount);
		ItemStack returns = stackInSlot.splitStack(m);
		if (context != ModuleContext.INTERNAL && !simulate)
		{
			owner.notifyChange(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
		return returns;
	}
	
	/**
	 * Retrieves the maximum stack size allowed to exist in the given slot.
	 *
	 * @param slot Slot to query.
	 * @return The maximum stack size allowed in the slot.
	 */
	public int getSlotLimit (int slot) {
		switch (slot)
		{
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
	public void setStackInSlot (int index, ItemStack stack, ModuleContext context) {
		if (checkIndex(index))
		{
			inventory.set(index, stack);
		}
		if (!stack.isEmpty() && stack.getCount() > this.getSlotLimit(index))
		{
			stack.setCount(this.getSlotLimit(index));
		}
		if (context != ModuleContext.INTERNAL) owner.notifyChange(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
	}
	
	// ---------------------------------Inventory Specific Methods------------------------------------
	/**
	 * Try to insert a stack at the same time as pulling one out,
	 * provided to make sure stacks don't disappear when trying to insert one and extract another
	 * 
	 * @param index Slot to extract from.
	 * @param stack ItemStack to insert.
	 * @param simulate If true, the swap is only simulated
	 * @return The remaining ItemStack that is not currently in the slot (or would not be in the slot if simulated)
	 */
	public ItemStack swapStacks (int index, ItemStack toInsert, boolean simulate, ModuleContext context) {
		if ((context == ModuleContext.SIDE || context == ModuleContext.OTHER) && !insertable_slots.contains(index)) return toInsert;
		if (context == ModuleContext.GUI && !insertable_gui_slots.contains(index)) return toInsert;
		if (context != ModuleContext.INTERNAL && !itemValidForSlotChecks.test(index, toInsert)) return toInsert;
		if (context != ModuleContext.INTERNAL && context != ModuleContext.GUI && !extractable_slots.contains(index)) return toInsert;
		ItemStack stackInSlot = inventory.get(index);
		if (!simulate)
		{
			inventory.set(index, toInsert);
			if (context != ModuleContext.INTERNAL) owner.notifyChange(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
		return stackInSlot;
	}
	
	// --------------------------------------INTERNAL METHODS------------------------------------------
	protected boolean checkIndex (int index) {
		if (index >= 0 && index < inventory.size()) { return true; }
		return false;
	}
	
	@Override
	public String toString () {
		return "Inventory of the InventoryManager: " + inventory.toString();
	}
	
	// ----------------------------NBT Functions-----------------------------------------------------
	/**
	 * Writes the data of the inventory to nbt, to be loaded back later
	 * 
	 * @param nbt The NBTTagCompound to write to.
	 */
	@Override
	public NBTTagCompound writeModuleToNBT (NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.getSlots(); ++i)
		{
			if (!inventory.get(i).isEmpty())
			{
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				inventory.get(i).writeToNBT(stackTag);
				list.appendTag(stackTag);
			}
		}
		nbt.setTag("Items", list);
		return nbt;
	}
	
	/**
	 * Reads Data back from an nbt tag, after it has been loaded back
	 * 
	 * @param nbt The nbt tag to read from.
	 */
	@Override
	public NBTTagCompound readModuleFromNBT (NBTTagCompound nbt) {
		inventory.clear();
		NBTTagList list = nbt.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound stackTag = list.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot") & 255;
			inventory.set(slot, new ItemStack(stackTag));
		}
		return nbt;
	}
	
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == GuiItemCapability.CAP;
	}
	
	@Override
	@SuppressWarnings ("unchecked")
	public <T> T getCapability (Capability<T> capability, EnumFacing facing, ModuleContext capacity) {
		return (T) new InventoryViewer(this, capacity);
	}
	
	protected class InventoryViewer implements IInventoryItemHandler {
		
		private final InventoryManager delegate;
		public final ModuleContext constraints;
		
		protected InventoryViewer (InventoryManager delegate, ModuleContext constraints) {
			this.delegate = delegate;
			this.constraints = constraints;
		}
		
		public int getSlots () {
			return delegate.getSlots();
		}
		
		public ItemStack getStackInSlot (int index) {
			return delegate.getStackInSlot(index, constraints);
		}
		
		public ItemStack insertItem (int slot, ItemStack stack, boolean simulate) {
			return delegate.insertItem(slot, stack, simulate, constraints);
		}
		
		public ItemStack extractItem (int slot, int amount, boolean simulate) {
			return delegate.extractItem(slot, amount, simulate, constraints);
		}
		
		public int getSlotLimit (int slot) {
			return delegate.getSlotLimit(slot);
		}
		
		@Override
		public ItemStack swapStacks (int index, ItemStack toInsert, boolean simulate) {
			return delegate.swapStacks(index, toInsert, simulate, constraints);
		}
	}
}
