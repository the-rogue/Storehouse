/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.multiblock.block;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidCapabilityWrapper implements ICapabilityWrapper<IFluidHandler> {
	
	public static final FluidCapabilityWrapper FILL = new FluidCapabilityWrapper(true, false);
	public static final FluidCapabilityWrapper DRAIN = new FluidCapabilityWrapper(false, true);
	public static final FluidCapabilityWrapper BOTH = new FluidCapabilityWrapper(true, true);
	public final boolean canFill;
	public final boolean canDrain;
	
	private FluidCapabilityWrapper (boolean canFill, boolean canDrain) {
		this.canFill = canFill;
		this.canDrain = canDrain;
	}
	
	@Override
	public IFluidHandler getWrappedCapability (IFluidHandler wrappable) {
		return new IFluidHandler() {
			
			@Override
			public IFluidTankProperties[] getTankProperties () {
				IFluidTankProperties[] properties = wrappable.getTankProperties();
				IFluidTankProperties[] newProperties = new IFluidTankProperties[properties.length];
				for (int i = 0; i < properties.length; i++)
				{
					IFluidTankProperties element = properties[i];
					newProperties[i] = new IFluidTankProperties() {
						
						@Override
						public FluidStack getContents () {
							return element.getContents();
						}
						
						@Override
						public int getCapacity () {
							return element.getCapacity();
						}
						
						@Override
						public boolean canFill () {
							return canFill ? element.canFill() : false;
						}
						
						@Override
						public boolean canDrain () {
							return canDrain ? element.canDrain() : false;
						}
						
						@Override
						public boolean canFillFluidType (FluidStack fluidStack) {
							return canFill ? element.canFillFluidType(fluidStack) : false;
						}
						
						@Override
						public boolean canDrainFluidType (FluidStack fluidStack) {
							return canDrain ? element.canDrainFluidType(fluidStack) : false;
						}
					};
				}
				return newProperties;
			}
			
			@Override
			public int fill (FluidStack resource, boolean doFill) {
				return canFill ? wrappable.fill(resource, doFill) : 0;
			}
			
			@Override
			public FluidStack drain (FluidStack resource, boolean doDrain) {
				return canFill ? wrappable.drain(resource, doDrain) : null;
			}
			
			@Override
			public FluidStack drain (int maxDrain, boolean doDrain) {
				return canFill ? wrappable.drain(maxDrain, doDrain) : null;
			}
		};
	}
	
	@Override
	public Capability<IFluidHandler> getSupportedCapability () {
		return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}
}
