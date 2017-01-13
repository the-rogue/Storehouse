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
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.wrapper.InvWrapper;
import therogue.storehouse.tile.IDefaultSidedInventory;


public class InventoryManager implements ISidedInventory
{
	private IDefaultSidedInventory owner;
	private InvWrapper inventoryWrapper;
	private ItemStack[] inventory;
	private int[] extractable_slots;
	private int[] insertable_slots;

	public InventoryManager(IDefaultSidedInventory owner, int size, @Nullable int[] insertable_slots, @Nullable int[] extractable_slots)
	{
		this.owner = owner;
		inventory = new ItemStack[size];
		
		inventoryWrapper = new InvWrapper(this) {
			@Override
		    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if (!canInsertItem(slot, stack, null))
					return null;
				return super.insertItem(slot, stack, simulate);
			}
			@Override
		    public ItemStack extractItem(int slot, int amount, boolean simulate) {
				if (!canExtractItem(slot, null, null))
					return null;
				return super.extractItem(slot, amount, simulate);
			}
		};
		
		if (extractable_slots == null)
		{
			this.extractable_slots = new int[size];
			for (int i = 0; i < size; i++)
			{
				this.extractable_slots[i] = i;
			}
		}
		else
		{
			this.extractable_slots = extractable_slots;
		}
		if (insertable_slots == null)
		{
			this.insertable_slots = new int[size];
			for (int i = 0; i < size; i++)
			{
				this.insertable_slots[i] = i;
			}
		}
		else
		{
			this.insertable_slots = insertable_slots;
		}
	}

	public InvWrapper getWrapper()
	{
		return inventoryWrapper;
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		if (checkIndex(index))
		{
			return inventory[index];
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (checkIndex(index))
		{
			inventory[index] = stack;
		}
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{
		owner.getTileEntity().markDirty();

	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.owner.getTileEntity().getWorld().getTileEntity(this.owner.getTileEntity().getPos()) != this.owner ? false : player.getDistanceSq((double) this.owner.getTileEntity().getPos().getX() + 0.5D, (double) this.owner.getTileEntity().getPos().getY() + 0.5D, (double) this.owner
				.getTileEntity().getPos().getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		for (int i : insertable_slots)
		{
			if (i == index)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int getField(int id)
	{
		switch (id)
		{
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{

		}
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < this.inventory.length; ++i)
		{
			this.inventory[i] = null;
		}
	}

	@Override
	public String getName()
	{
		return owner.getName();
	}

	@Override
	public boolean hasCustomName()
	{
		return owner.hasCustomName();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return owner.getDisplayName();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		int[] ret = new int[inventory.length];
		for (int i = 0; i < inventory.length; i++)
		{
			ret[i] = i;
		}
		return ret;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		if (isItemValidForSlot(index, itemStackIn))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		for (int i : extractable_slots)
		{
			if (i == index)
			{
				return true;
			}
		}
		return false;
	}

	public boolean checkIndex(int index)
	{
		if (index >= 0 && index < inventory.length)
		{
			return true;
		}
		return false;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			if (this.getStackInSlot(i) != null)
			{
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				this.getStackInSlot(i).writeToNBT(stackTag);
				list.appendTag(stackTag);
			}
		}
		nbt.setTag("Items", list);
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound stackTag = list.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot") & 255;
			this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
		}
	}

}
