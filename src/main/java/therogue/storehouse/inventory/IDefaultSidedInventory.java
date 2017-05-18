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

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IDefaultSidedInventory extends ISidedInventory {
	
	/**
	 * Gets the inventory Manager that will deal with ISidedInventory Requests
	 */
	public InventoryManager getInventoryManager ();
	
	/**
	 * Gets the TileEntity this thing is attached to
	 */
	public TileEntity getTileEntity ();
	
	/**
	 * Called When the inventory of the Inventory Manager changes, useful for things such as crafting.
	 */
	default void onInventoryChange () {
	}
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	default int getSizeInventory () {
		return getInventoryManager().getSizeInventory();
	}
	
	/**
	 * Returns the stack in the given slot.
	 */
	@Nullable
	default ItemStack getStackInSlot (int index) {
		return getInventoryManager().getStackInSlot(index);
	}
	
	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Nullable
	default ItemStack decrStackSize (int index, int count) {
		return getInventoryManager().decrStackSize(index, count);
	}
	
	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Nullable
	default ItemStack removeStackFromSlot (int index) {
		return getInventoryManager().removeStackFromSlot(index);
	}
	
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	default void setInventorySlotContents (int index, @Nullable ItemStack stack) {
		getInventoryManager().setInventorySlotContents(index, stack);
	}
	
	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	default int getInventoryStackLimit () {
		return getInventoryManager().getInventoryStackLimit();
	}
	
	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	default boolean isUsableByPlayer (EntityPlayer player) {
		return getInventoryManager().isUseableByPlayer(player);
	}
	
	default void openInventory (EntityPlayer player) {
		getInventoryManager().openInventory(player);
	}
	
	default void closeInventory (EntityPlayer player) {
		getInventoryManager().closeInventory(player);
	}
	
	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For guis use Slot.isItemValid
	 */
	default boolean isItemValidForSlot (int index, ItemStack stack) {
		return getInventoryManager().isItemValidForSlot(index, stack);
	}
	
	default void clear () {
		getInventoryManager().clear();
	}
	
	default int[] getSlotsForFace (EnumFacing side) {
		return getInventoryManager().getSlotsForFace(side);
	}
	
	/**
	 * Returns true if automation can insert the given item in the given slot from the given side.
	 */
	default boolean canInsertItem (int index, ItemStack itemStackIn, EnumFacing direction) {
		return getInventoryManager().canInsertItem(index, itemStackIn, direction);
	}
	
	/**
	 * Returns true if automation can extract the given item in the given slot from the given side.
	 */
	default boolean canExtractItem (int index, ItemStack stack, EnumFacing direction) {
		return getInventoryManager().canExtractItem(index, stack, direction);
	}
	
	default boolean isEmpty () {
		return getInventoryManager().isEmpty();
	}
}
