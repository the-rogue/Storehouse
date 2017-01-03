/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile;

import net.minecraft.util.EnumFacing;
import therogue.storehouse.energy.EnergyStorageAdv;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

public abstract class StorehouseBasePoweredTileEntity extends StorehouseBaseTileEntity implements IEnergyProvider, IEnergyReceiver
{
	protected EnergyStorageAdv energyStorage;

	public StorehouseBasePoweredTileEntity(EnergyStorageAdv energyStorage)
	{
		this.energyStorage = energyStorage;
	}

	@Override
	public int getEnergyStored(EnumFacing from)
	{
		return energyStorage.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored(EnumFacing from)
	{
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from)
	{
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
	{
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
	{
		return energyStorage.extractEnergy(maxExtract, simulate);
	}

}
