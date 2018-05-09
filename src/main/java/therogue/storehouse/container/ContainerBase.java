/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import therogue.storehouse.block.BlockUtils;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.tile.StorehouseBaseTileEntity;
import therogue.storehouse.util.GeneralUtils;

public class ContainerBase extends Container {
	
	private StorehouseBaseTileEntity teInv;
	protected List<IInventorySlot> smartTileEntitySlots = new ArrayList<IInventorySlot>();
	protected List<IInventorySlot> smartInventorySlots = new ArrayList<IInventorySlot>();
	protected List<Slot> tileEntitySlots = new ArrayList<Slot>();
	
	public ContainerBase (IInventory playerInv, StorehouseBaseTileEntity teInv) {
		this(176, 166, playerInv, teInv);
	}
	
	public ContainerBase (int xSize, int ySize, IInventory playerInv, StorehouseBaseTileEntity teInv) {
		this.teInv = teInv;
		// Player Inventory, Slot 0-8, Slot IDs 0-8
		int xOffset = (xSize - 18 * 9) / 2 + 1;
		int yOffset = ySize - 6;
		for (int x = 0; x < 9; ++x)
		{
			this.addSlot(new LegacySlot(playerInv, x, xOffset + x * 18, yOffset - 18));
		}
		// Player Inventory, Slot 9-35, Slot IDs 9-35
		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				this.addSlot(new LegacySlot(playerInv, x + y * 9 + 9, xOffset + x * 18, yOffset - 18 * 4 - 4 + y * 18));
			}
		}
	}
	
	protected IInventorySlot addSlot (IInventorySlot slot) {
		this.addSlotToContainer(new SlotAdapter(slot));
		smartInventorySlots.add(slot);
		return slot;
	}
	
	protected IInventorySlot addTESlot (IInventorySlot slot) {
		Slot slotAdapter = new SlotAdapter(slot);
		this.addSlotToContainer(slotAdapter);
		tileEntitySlots.add(slotAdapter);
		smartInventorySlots.add(slot);
		smartTileEntitySlots.add(slot);
		return slot;
	}
	
	public void update () {
	}
	
	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges () {
		update();
		for (IContainerListener listener : this.listeners)
		{
			if (listener instanceof EntityPlayerMP && GeneralUtils.isServerSide(((EntityPlayerMP) listener).getEntityWorld()))
			{
				StorehousePacketHandler.INSTANCE.sendTo(teInv.getCGUIPacket(), (EntityPlayerMP) listener);
			}
		}
	}
	
	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith (EntityPlayer player) {
		return BlockUtils.isUsableByPlayer(teInv, player);
	}
	
	// ----------------------Slot Click Fix Below -------------------------------------------------------------------------------------------------------------------------------------
	/** The current drag mode (0 : evenly split, 1 : one item by slot, 2 : max stack size in slot (creative only) */
	private int dragMode = 0;
	/** The current drag event (0 : start, 1 : add slot : 2 : end) */
	private int dragTime = 0;
	/** The list of slots where the itemstack holds will be distributed */
	private final Set<IInventorySlot> dragSlots = Sets.<IInventorySlot> newHashSet();
	
	public ItemStack slotClick (int slotId, int dragType, ClickType clickType, EntityPlayer player) {
		IInventorySlot slot = null;
		if (slotId >= 0 && slotId < this.smartInventorySlots.size())
		{
			slot = this.smartInventorySlots.get(slotId);
		}
		if (clickType != ClickType.QUICK_CRAFT && this.dragTime != 0)
		{
			this.dragTime = 0;
			this.dragSlots.clear();
		}
		if ((clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1) && slotId == -999 && !player.inventory.getItemStack().isEmpty())
		{
			ItemStack playerDragStack = player.inventory.getItemStack();
			player.dropItem(playerDragStack.splitStack(dragType == 0 ? playerDragStack.getCount() : 1), true);
			return ItemStack.EMPTY;
		}
		switch (clickType)
		{
			case QUICK_CRAFT:
				return this.dragOverSlots(slot, dragType, player);
			case QUICK_MOVE:
				return shiftClickSlot(slot, slotId, player);
			case PICKUP:
				return clickStack(slot, dragType, player);
			case SWAP:
				return numpadSwap(slot, dragType, player);
			case CLONE:
				return cloneStack(slot, player);
			case THROW:
				return throwStack(slot, dragType, player);
			case PICKUP_ALL:
				return pickUpAllInInventory(slot, dragType, player);
			default:
				return ItemStack.EMPTY;
		}
	}
	
	protected ItemStack dragOverSlots (IInventorySlot slot, int dragType, EntityPlayer player) {
		ItemStack playerDragStack = player.inventory.getItemStack();
		if (playerDragStack.isEmpty()) return ItemStack.EMPTY;
		if (this.dragTime == 0 && (dragType & 3) == 0 && isValidDragMode(this.dragMode = dragType >> 2 & 3, player))
		{
			this.dragTime = 1;
			this.dragSlots.clear();
		}
		else if (this.dragTime == 1 && (dragType & 3) == 1 && canDragIntoSlot(slot, playerDragStack)) this.dragSlots.add(slot);
		else if (this.dragTime == 1 && (dragType & 3) == 2 && !this.dragSlots.isEmpty())
		{
			ItemStack endPlayerStack = playerDragStack.copy();
			for (IInventorySlot slot8 : this.dragSlots)
			{
				if (canDragIntoSlot(slot8, playerDragStack))
				{
					ItemStack playerDragStackCopy = playerDragStack.copy();
					switch (this.dragMode)
					{
						case 0:
							playerDragStackCopy.setCount(MathHelper.floor((float) playerDragStackCopy.getCount() / (float) this.dragSlots.size()));
							break;
						case 1:
							playerDragStackCopy.setCount(1);
							break;
						case 2:
							playerDragStackCopy.setCount(playerDragStackCopy.getMaxStackSize());
					}
					endPlayerStack.shrink(playerDragStackCopy.getCount());
					ItemStack notInserted = slot8.insertItem(playerDragStackCopy, false);
					endPlayerStack.grow(notInserted.getCount());
				}
			}
			player.inventory.setItemStack(endPlayerStack);
			this.dragTime = 2;
		}
		if (this.dragTime != 1)
		{
			this.dragTime = 0;
			this.dragSlots.clear();
		}
		return ItemStack.EMPTY;
	}
	
	protected ItemStack numpadSwap (IInventorySlot slot, int dragType, EntityPlayer player) {
		if (dragType < 0 && dragType >= 9) return ItemStack.EMPTY;
		InventoryPlayer playerInventory = player.inventory;
		playerInventory.setInventorySlotContents(dragType, slot.swapStacks(playerInventory.getStackInSlot(dragType), false));
		return ItemStack.EMPTY;
	}
	
	protected ItemStack pickUpAllInInventory (IInventorySlot slot, int dragType, EntityPlayer player) {
		ItemStack playerDragStack = player.inventory.getItemStack();
		if (slot == null || playerDragStack.isEmpty() || (slot != null && !slot.getStack().isEmpty() && slot.canTakeStack(player))) return ItemStack.EMPTY;
		int i = dragType == 0 ? 0 : this.smartInventorySlots.size() - 1;
		int j = dragType == 0 ? 1 : -1;
		for (int k = 0; k < 2; ++k)
		{
			for (int l = i; l >= 0 && l < this.smartInventorySlots.size() && playerDragStack.getCount() < playerDragStack.getMaxStackSize(); l += j)
			{
				IInventorySlot thisSlot = this.smartInventorySlots.get(l);
				if (!thisSlot.getStack().isEmpty() && canAddItemToSlot(thisSlot, playerDragStack, true)
						&& thisSlot.canTakeStack(player)
						&& this.canMergeSlot(playerDragStack, thisSlot))
				{
					ItemStack thisSlotStack = thisSlot.getStack();
					if (k != 0 || thisSlotStack.getCount() != thisSlotStack.getMaxStackSize())
					{
						int i1 = Math.min(playerDragStack.getMaxStackSize() - playerDragStack.getCount(), thisSlotStack.getCount());
						playerDragStack.grow(thisSlot.extractItem(i1, false).getCount());
					}
				}
			}
		}
		this.detectAndSendChanges();
		return ItemStack.EMPTY;
	}
	
	protected ItemStack shiftClickSlot (IInventorySlot slot, int slotId, EntityPlayer player) {
		if (slot == null || !slot.canTakeStack(player)) return ItemStack.EMPTY;
		if (slot == null || slot.getStack().isEmpty()) return ItemStack.EMPTY;
		ItemStack current = slot.extractItem(-1, true);
		ItemStack oldpreviousExtractedStack = current.copy();
		for (int j = 0; j < 2; j++)
		{
			ItemStack previousExtractedStack = current.copy();
			int startIndex = 0;
			int endIndex = 36;
			int i = endIndex - 1;
			if (slotId < 36)
			{
				startIndex = 36;
				endIndex = this.smartInventorySlots.size();
				i = startIndex;
			}
			while (current.getCount() > 0 && (slotId < 36 && i < endIndex || slotId >= 36 && i >= startIndex))
			{
				IInventorySlot checkSlot = this.smartInventorySlots.get(i);
				if (!checkSlot.getStack().isEmpty() || j == 1)
				{
					current = checkSlot.insertItem(current, false);
				}
				if (slotId >= 36) --i;
				else++i;
			}
			slot.extractItem(previousExtractedStack.getCount() - current.getCount(), false);
		}
		if (current.getCount() == oldpreviousExtractedStack.getCount()) return ItemStack.EMPTY;
		return oldpreviousExtractedStack.copy();
	}
	
	protected ItemStack clickStack (IInventorySlot slot, int dragType, EntityPlayer player) {
		if (slot == null) return ItemStack.EMPTY;
		if (dragType != 0 && dragType != 1) return ItemStack.EMPTY;
		ItemStack slotStack = slot.getStack();
		InventoryPlayer playerInventory = player.inventory;
		ItemStack playerDragStack = playerInventory.getItemStack();
		ItemStack returns = slotStack.copy();
		if (!ItemStack.areItemStacksEqual(slot.insertItem(playerDragStack.copy(), true), playerDragStack) && !playerDragStack.isEmpty())
		{
			if (dragType != 0)
			{
				ItemStack insertStack = playerDragStack.copy();
				insertStack.setCount(1);
				if (slot.insertItem(insertStack, false).isEmpty())
				{
					playerDragStack.shrink(1);
				}
			}
			else
			{
				playerInventory.setItemStack(slot.insertItem(playerDragStack, false));
			}
		}
		else if (!slot.canTakeStack(player)) return returns;
		else if (playerDragStack.isEmpty())
		{
			playerInventory.setItemStack(slot.extractItem(dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2, false));
		}
		else if (ItemStack.areItemStacksEqual(slot.insertItem(playerDragStack.copy(), true), playerDragStack))
		{
			playerInventory.setItemStack(slot.swapStacks(playerDragStack, false));
		}
		else if (slotStack.getItem() == playerDragStack.getItem() && playerDragStack.getMaxStackSize() > 1
				&& (!slotStack.getHasSubtypes() || slotStack.getMetadata() == playerDragStack.getMetadata())
				&& ItemStack.areItemStackTagsEqual(slotStack, playerDragStack)
				&& !slotStack.isEmpty())
		{
			int amountInSlot = slotStack.getCount();
			if (amountInSlot + playerDragStack.getCount() <= playerDragStack.getMaxStackSize())
			{
				playerDragStack.grow(slot.extractItem(-1, false).getCount());
			}
		}
		return returns;
	}
	
	protected ItemStack cloneStack (IInventorySlot slot, EntityPlayer player) {
		ItemStack playerDragStack = player.inventory.getItemStack();
		if (!player.capabilities.isCreativeMode || !playerDragStack.isEmpty() || slot == null || slot.getStack().isEmpty()) return ItemStack.EMPTY;
		ItemStack slotDuplicate = slot.getStack().copy();
		slotDuplicate.setCount(slotDuplicate.getMaxStackSize());
		player.inventory.setItemStack(slotDuplicate);
		return ItemStack.EMPTY;
	}
	
	protected ItemStack throwStack (IInventorySlot slot, int dragType, EntityPlayer player) {
		ItemStack playerDragStack = player.inventory.getItemStack();
		if (!playerDragStack.isEmpty() || slot == null || slot.getStack().isEmpty() || !slot.canTakeStack(player)) return ItemStack.EMPTY;
		ItemStack thrownStack = slot.extractItem(dragType == 0 ? 1 : slot.getStack().getCount(), false);
		player.dropItem(thrownStack, true);
		return ItemStack.EMPTY;
	}
	
	private boolean canDragIntoSlot (IInventorySlot slot, ItemStack playerStack) {
		return slot != null && canAddItemToSlot(slot, playerStack, true)
				&& slot.insertItem(playerStack.copy(), true).getCount() < playerStack.getCount()
				&& (this.dragMode == 2 || playerStack.getCount() >= this.dragSlots.size())
				&& this.canDragIntoSlot(slot);
	}
	
	/**
	 * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
	 * is null for the initial slot that was double-clicked.
	 */
	public boolean canMergeSlot (ItemStack stack, IInventorySlot slotIn) {
		return true;
	}
	
	/**
	 * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if
	 * the slot can be added to a list of Slots to split the held ItemStack across.
	 */
	public boolean canDragIntoSlot (IInventorySlot slotIn) {
		return true;
	}
	
	/**
	 * Checks if it's possible to add the given itemstack to the given slot.
	 */
	public static boolean canAddItemToSlot (@Nullable IInventorySlot slotIn, ItemStack stack, boolean stackSizeMatters) {
		boolean flag = slotIn == null || slotIn.getStack().isEmpty();
		if (!flag && stack.isItemEqual(slotIn.getStack()) && ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack))
		{
			return slotIn.getStack().getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= stack.getMaxStackSize();
		}
		else
		{
			return flag;
		}
	}
}
