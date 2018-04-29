/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.capabilitywrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemCapabilityWrapper implements ICapabilityWrapper<IItemHandler> {
	
	public static final ItemCapabilityWrapper INSERT = new ItemCapabilityWrapper(true, false);
	public static final ItemCapabilityWrapper EXTRACT = new ItemCapabilityWrapper(false, true);
	public static final ItemCapabilityWrapper BOTH = new ItemCapabilityWrapper(true, true);
	public final boolean canInsert;
	public final boolean canExtract;
	
	public ItemCapabilityWrapper (boolean canInsert, boolean canExtract) {
		this.canInsert = canInsert;
		this.canExtract = canExtract;
	}
	
	@Override
	public IItemHandler getWrappedCapability (IItemHandler wrappable) {
		return new IItemHandler() {
			
			@Override
			public int getSlots () {
				return wrappable.getSlots();
			}
			
			@Override
			public ItemStack getStackInSlot (int slot) {
				return wrappable.getStackInSlot(slot);
			}
			
			@Override
			public ItemStack insertItem (int slot, ItemStack stack, boolean simulate) {
				return canInsert ? wrappable.insertItem(slot, stack, simulate) : stack;
			}
			
			@Override
			public ItemStack extractItem (int slot, int amount, boolean simulate) {
				return canExtract ? wrappable.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
			}
			
			@Override
			public int getSlotLimit (int slot) {
				return wrappable.getSlotLimit(slot);
			}
		};
	}
	
	@Override
	public Capability<IItemHandler> getSupportedCapability () {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}
}
