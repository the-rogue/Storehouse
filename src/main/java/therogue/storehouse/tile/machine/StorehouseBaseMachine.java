/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.energy.EnergyStorageAdv;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.IDefaultSidedInventory;
import therogue.storehouse.tile.StorehouseBaseEnergyStorageTE;
import cofh.api.energy.IEnergyReceiver;


public abstract class StorehouseBaseMachine extends StorehouseBaseEnergyStorageTE implements IEnergyReceiver, IDefaultSidedInventory
{
	protected InventoryManager inventory;

	public StorehouseBaseMachine()
	{
		super(new EnergyStorageAdv(8000, 100, 0));
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
	public boolean canExtract()
	{
		return false;
	}

	@Override
	public boolean canReceive()
	{
		return true;
	}

	@Override
	public TileEntity getTileEntity()
	{
		return this;
	}
	
	@Override
	public InventoryManager getInventoryManager()
	{
		if (inventory == null) {
			throw new NullPointerException("inventory is null for machine: " + getName());
		}
		return inventory;
	}
	
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) inventory.getWrapper();
        }
        return super.getCapability(capability, facing);
    }

}
