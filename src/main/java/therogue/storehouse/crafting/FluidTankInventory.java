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

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import therogue.storehouse.crafting.wrapper.FluidStackWrapper;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.ModuleContext;

public class FluidTankInventory implements IRecipeInventory {
	
	private final IFluidHandler compose;
	
	public FluidTankInventory (IFluidHandler compose) {
		this.compose = compose;
	}
	
	@Override
	public IRecipeWrapper getComponent (int slot) {
		FluidStack contents = compose.getTankProperties()[slot].getContents();
		if (checkSlot(slot)) return new FluidStackWrapper(compose.drain(contents != null ? contents.amount : 1, false));
		return IRecipeWrapper.NOTHING;
	}
	
	@Override
	public void insertComponent (int slot, IRecipeWrapper component, boolean simulate) {
		if (checkSlot(slot))
		{
			FluidStackWrapper wrapper = new FluidStackWrapper();
			wrapper.merge(component, compose.getTankProperties()[slot].getCapacity());
			compose.fill(wrapper.getStack(), !simulate);
		}
	}
	
	@Override
	public IRecipeWrapper extractComponent (int slot, int amount, boolean simulate) {
		if (checkSlot(slot)) return new FluidStackWrapper(compose.drain(amount, !simulate));
		return IRecipeWrapper.NOTHING;
	}
	
	@Override
	public int getComponentSlotLimit (int slot) {
		if (checkSlot(slot)) return compose.getTankProperties()[slot].getCapacity();
		return 0;
	}
	
	@Override
	public int getSize () {
		return 1;
	}
	
	private boolean checkSlot (int slot) {
		return slot >= 0 && slot < compose.getTankProperties().length;
	}
	
	public static final FluidInvConverter CONVERTER = new FluidInvConverter();
	
	private static class FluidInvConverter implements IRecipeInventoryConverter {
		
		public String getString () {
			return "FLD";
		}
		
		public IRecipeInventory getFromTile (ITile tile, int data[]) {
			return new FluidTankInventory(tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL));
		}
	}
}
