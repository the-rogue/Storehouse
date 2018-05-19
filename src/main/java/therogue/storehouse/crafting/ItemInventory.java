/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting;

import com.google.common.base.Preconditions;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.ModuleContext;

public class ItemInventory implements IRecipeInventory {
	
	private final IItemHandler compose;
	private final int minSlot;
	private final int maxSlot;
	
	public ItemInventory (IItemHandler compose, int minSlot, int maxSlotExclusive) {
		Preconditions.checkArgument(maxSlotExclusive > minSlot, "Max slot must be greater than min slot");
		this.compose = compose;
		this.minSlot = minSlot;
		this.maxSlot = maxSlotExclusive;
	}
	
	@Override
	public IRecipeWrapper getComponent (int slot) {
		if (checkSlot(slot)) return new ItemStackWrapper(compose.getStackInSlot(slot + minSlot).copy());
		return IRecipeWrapper.NOTHING;
	}
	
	@Override
	public void insertComponent (int slot, IRecipeWrapper component, boolean simulate) {
		if (checkSlot(slot))
		{
			ItemStackWrapper wrapper = new ItemStackWrapper(ItemStack.EMPTY);
			wrapper.merge(component, compose.getSlotLimit(slot));
			compose.insertItem(slot + minSlot, wrapper.getStack(), false);
		}
	}
	
	@Override
	public IRecipeWrapper extractComponent (int slot, int amount, boolean simulate) {
		if (!checkSlot(slot)) return IRecipeWrapper.NOTHING;
		return new ItemStackWrapper(compose.extractItem(slot + minSlot, amount, simulate));
	}
	
	@Override
	public int getComponentSlotLimit (int slot) {
		if (checkSlot(slot)) return compose.getSlotLimit(slot + minSlot);
		return 0;
	}
	
	@Override
	public int getSize () {
		return maxSlot - minSlot;
	}
	
	private boolean checkSlot (int localSlot) {
		return localSlot + minSlot < maxSlot;
	}
	
	public static final ItemInvConverter CONVERTER = new ItemInvConverter();
	
	private static class ItemInvConverter implements IRecipeInventoryConverter {
		
		public String getString () {
			return "ITM";
		}
		
		public IRecipeInventory getFromTile (ITile tile, int data[]) {
			return new ItemInventory(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), data[0], data[1]);
		}
		
		@Override
		public int numOfDataItems () {
			return 2;
		}
	}
}
