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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.tile.StorehouseBaseTileEntity;
import therogue.storehouse.util.BlockUtils;
import therogue.storehouse.util.ItemUtils;

public class ContainerBase extends Container {
	
	private StorehouseBaseTileEntity teInv;
	
	public ContainerBase (IInventory playerInv, StorehouseBaseTileEntity teInv) {
		this.teInv = teInv;
		// Player Inventory, Slot 0-8, Slot IDs 0-8
		for (int x = 0; x < 9; ++x)
		{
			this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
		}
		// Player Inventory, Slot 9-35, Slot IDs 9-35
		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
	}
	
	@Override
	public void detectAndSendChanges () {
		super.detectAndSendChanges();
		for (IContainerListener listener : this.listeners)
		{
			if (listener instanceof EntityPlayerMP)
			{
				StorehousePacketHandler.INSTANCE.sendTo(teInv.getGUIPacket(), (EntityPlayerMP) listener);
			}
		}
	}
	
	@Override
	public boolean canInteractWith (EntityPlayer player) {
		return BlockUtils.isUsableByPlayer(teInv, player);
	}
	
	@Override
	public ItemStack transferStackInSlot (EntityPlayer playerIn, int fromSlot) {
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(fromSlot);
		if (slot != null && slot.getHasStack())
		{
			ItemStack current = slot.getStack();
			previous = current.copy();
			if (fromSlot >= 36)
			{
				// From TE Inventory to Player Inventory (HotBar)
				if (!this.mergeItemStack(current, 0, 36, true)) return ItemStack.EMPTY;
			}
			else
			{
				// From Player Inventory to TE Inventory
				if (!this.mergeItemStack(current, 36, this.inventorySlots.size(), false)) return ItemStack.EMPTY;
			}
			if (current.getCount() == 0) slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();
			if (current.getCount() == previous.getCount()) return ItemStack.EMPTY;
			slot.onTake(playerIn, current);
		}
		return previous;
	}
	
	// Should check slot stack size limit, so copied code to fix this
	/**
	 * Merges provided ItemStack with the first avaliable one in the container/player inventor between minIndex (included) and maxIndex (excluded). Args : stack, minIndex, maxIndex, negativDirection. /!\ the Container implementation do not check if the item is valid for the slot
	 */
	@Override
	protected boolean mergeItemStack (ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean flag = false;
		int i = reverseDirection ? endIndex - 1 : startIndex;
		while (stack.getCount() > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex))
		{
			Slot slot = (Slot) this.inventorySlots.get(i);
			ItemStack itemstack = slot.getStack();
			if (ItemUtils.areItemStacksMergableWithLimit(Math.min(slot.getItemStackLimit(itemstack), itemstack.getMaxStackSize()), stack, itemstack) && slot.isItemValid(ItemUtils.mergeStacks(slot.getSlotStackLimit(), false, stack, itemstack)))
			{
				slot.putStack(ItemUtils.mergeStacks(slot.getSlotStackLimit(), true, stack, itemstack));
				slot.onSlotChanged();
				flag = true;
			}
			if (reverseDirection)
			{
				--i;
			}
			else
			{
				++i;
			}
		}
		return flag;
	}
}
