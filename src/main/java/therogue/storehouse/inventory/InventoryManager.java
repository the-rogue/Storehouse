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

import therogue.storehouse.util.BlockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;


public class InventoryManager
{
	private IDefaultSidedInventory owner;
	private InvWrapper inventoryWrapper;
	private NonNullList<ItemStack> inventory;
	private int[] extractable_slots;
	private int[] insertable_slots;

	public InventoryManager(IDefaultSidedInventory owner, int size, @Nullable int[] insertable_slots, @Nullable int[] extractable_slots)
	{
		this.owner = owner;
		inventory = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);

		inventoryWrapper = new InvWrapper(owner) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
			{
				if (!canInsertItem(slot, stack, null)) return stack;
		        if (stack == null)
		            return null;

		        if (!isItemValidForSlot(slot, stack))
		            return stack;

		        ItemStack stackInSlot = getStackInSlot(slot);
		        
		        boolean flag = stackInSlot != null;
		        if (flag && !ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
		        	return stack;
		        
		        int m = Math.min(stack.getMaxStackSize(), getInventoryStackLimit(slot)) - (flag ? stackInSlot.getCount() : 0);
		        
		        ItemStack toInsert = stack.splitStack(m);
		        toInsert.setCount(toInsert.getCount()+ (flag ? stackInSlot.getCount() : 0));
		        setInventorySlotContents(slot, toInsert);
		        owner.markDirty();
		        return stack.getCount() <= 0 ? null : stack;
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate)
			{
				if (!canExtractItem(slot, null, null)) return null;
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

	public int getSizeInventory()
	{
		return inventory.size();
	}

	public ItemStack getStackInSlot(int index)
	{
		if (checkIndex(index))
		{
			return inventory.get(index);
		}
		return null;
	}

	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}
	
    public ItemStack pushItems(int slot, ItemStack stack)
    {
        if (stack == null)
            return null;

        if (!isItemValidForSlotChecks(slot, stack))
            return stack;

        ItemStack stackInSlot = getStackInSlot(slot);
        
        boolean flag = stackInSlot != null;
        if (flag && !ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
        	return stack;
        
        int m = Math.min(stack.getMaxStackSize(), getInventoryStackLimit(slot)) - (flag ? stackInSlot.getCount() : 0);
        
        ItemStack toInsert = stack.splitStack(m);
        toInsert.setCount(toInsert.getCount() + (flag ? stackInSlot.getCount() : 0));
        setInventorySlotContents(slot, toInsert);
        owner.markDirty();
        return stack.getCount() <= 0 ? null : stack;
    }

	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (checkIndex(index))
		{
			inventory.set(index, stack);
		}
		if (stack != null && stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	public int getInventoryStackLimit()
	{
		return 64;
	}
	public int getInventoryStackLimit(int slot)
	{
		switch (slot) {
		default:
			return 64;
		}
	}

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return BlockUtils.isUsableByPlayer(this.owner.getTileEntity(), player);
	}

	public void openInventory(EntityPlayer player)
	{
	}

	public void closeInventory(EntityPlayer player)
	{
	}

	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (isItemValidForSlotChecks(index, stack))
		{
			for (int i : insertable_slots)
			{
				if (i == index)
				{
					return true;
				}
			}
		}
		return false;
	}

	protected boolean isItemValidForSlotChecks(int index, ItemStack stack)
	{
		return true;
	}

	public void clear()
	{
		this.inventory.clear();
	}

	public int[] getSlotsForFace(EnumFacing side)
	{
		int[] ret = new int[inventory.size()];
		for (int i = 0; i < inventory.size(); i++)
		{
			ret[i] = i;
		}
		return ret;
	}

	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		if (isItemValidForSlot(index, itemStackIn))
		{
			return true;
		}
		return false;
	}

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
		if (index >= 0 && index < inventory.size())
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
			this.setInventorySlotContents(slot, new ItemStack(stackTag));
		}
	}
	public IDefaultSidedInventory getOwner(){
		return owner;
	}
	
	public boolean isEmpty() {
		return false;
	}

}
