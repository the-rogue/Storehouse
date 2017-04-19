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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.energy.EnergyStorageAdv;
import therogue.storehouse.network.GuiUpdateTEPacket;


public abstract class StorehouseBaseEnergyStorageTE extends StorehouseBaseTileEntity implements ITickable, IEnergyStorage
{
	protected EnergyStorageAdv energyStorage;
	public final MachineTier tier;

	public StorehouseBaseEnergyStorageTE(IStorehouseBaseBlock block, EnergyStorageAdv energyStorage, MachineTier tier)
	{
		super(block);
		this.energyStorage = energyStorage;
		this.tier = tier;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		energyStorage.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		energyStorage.readFromNBT(nbt);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
		{
			return (T) energyStorage;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return energyStorage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored()
	{
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored()
	{
		return energyStorage.getMaxEnergyStored();
	}
	
	public GuiUpdateTEPacket getGUIPacket(){
		GuiUpdateTEPacket packet = super.getGUIPacket();
		packet.getNbt().setInteger("EnergyStored", energyStorage.getEnergyStored());
		return packet;
	}
	
	public void processGUIPacket(GuiUpdateTEPacket packet){
		super.processGUIPacket(packet);
		energyStorage.setEnergyStored(packet.getNbt().getInteger("EnergyStored"));
	}
}
