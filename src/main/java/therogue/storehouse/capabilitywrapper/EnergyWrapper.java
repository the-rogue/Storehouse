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

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyWrapper implements ICapabilityWrapper<IEnergyStorage> {
	
	public static final EnergyWrapper RECIEVE = new EnergyWrapper(true, false);
	public static final EnergyWrapper EXTRACT = new EnergyWrapper(false, true);
	public static final EnergyWrapper BOTH = new EnergyWrapper(true, true);
	public final boolean canRecieve;
	public final boolean canExtract;
	
	public EnergyWrapper (boolean canRecieve, boolean canExtract) {
		this.canRecieve = canRecieve;
		this.canExtract = canExtract;
	}
	
	@Override
	public IEnergyStorage getWrappedCapability (IEnergyStorage wrappable) {
		return new IEnergyStorage() {
			
			@Override
			public int receiveEnergy (int maxReceive, boolean simulate) {
				return canRecieve ? wrappable.receiveEnergy(maxReceive, simulate) : 0;
			}
			
			@Override
			public int extractEnergy (int maxExtract, boolean simulate) {
				return canExtract ? wrappable.extractEnergy(maxExtract, simulate) : 0;
			}
			
			@Override
			public int getEnergyStored () {
				return wrappable.getEnergyStored();
			}
			
			@Override
			public int getMaxEnergyStored () {
				return wrappable.getMaxEnergyStored();
			}
			
			@Override
			public boolean canExtract () {
				return canExtract;
			}
			
			@Override
			public boolean canReceive () {
				return canRecieve;
			}
		};
	}
	
	@Override
	public Capability<IEnergyStorage> getSupportedCapability () {
		return CapabilityEnergy.ENERGY;
	}
}
