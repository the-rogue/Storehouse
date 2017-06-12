/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting.inventory;

import net.minecraftforge.fluids.IFluidTank;
import therogue.storehouse.crafting.wrapper.FluidStackWrapper;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;

public class FluidTankInventory implements IRecipeInventory {
	
	private final IFluidTank compose;
	
	public FluidTankInventory (IFluidTank compose) {
		this.compose = compose;
	}
	
	@Override
	public IRecipeWrapper getComponent (int slot) {
		if (slot == 0) return new FluidStackWrapper(compose.getFluid());
		return IRecipeWrapper.NOTHING;
	}
	
	@Override
	public void insertComponent (int slot, IRecipeWrapper component) {
		if (slot == 0)
		{
			FluidStackWrapper wrapper = new FluidStackWrapper(compose.getFluid());
			wrapper.merge(component, compose.getCapacity());
			compose.fill(wrapper.getStack(), true);
		}
	}
	
	@Override
	public int getComponentSlotLimit (int slot) {
		if (slot == 0) return compose.getCapacity();
		return 0;
	}
	
	@Override
	public int getSize () {
		return 1;
	}
}
