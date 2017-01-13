/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.generator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.block.state.GeneratorType;
import therogue.storehouse.energy.EnergyStorageAdv;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.IDefaultSidedInventory;
import therogue.storehouse.tile.StorehouseBaseEnergyStorageTE;
import therogue.storehouse.util.EnergyUtils;
import therogue.storehouse.util.GeneralUtils;
import cofh.api.energy.IEnergyProvider;


public abstract class TileBaseGenerator extends StorehouseBaseEnergyStorageTE implements IEnergyProvider, IDefaultSidedInventory, IInteractionObject
{
	protected InventoryManager inventory;
	protected GeneratorType type;
	protected int RFPerTick;

	public TileBaseGenerator(GeneratorType type, EnergyStorageAdv energy, int baseGeneration)
	{
		super(energy);
		this.type = type;
		this.RFPerTick = type.getRecieve(baseGeneration);
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
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
	{
		return energyStorage.extractEnergy(maxExtract, simulate);
	}

	/**
	 * Sends energy to neighbouring energy blocks
	 */
	protected void sendEnergyToNeighbours()
	{
		for (EnumFacing facing : EnumFacing.values())
		{
			if (energyStorage.getEnergyStored() <= 0) break;
			int sentRF = EnergyUtils.sendEnergy(worldObj, getPos(), facing, energyStorage.extractEnergy(energyStorage.getMaxExtract(), true));
			energyStorage.extractEnergy(sentRF, false);
		}
	}

	@Override
	public void update()
	{
		if (GeneralUtils.isServerSide(worldObj)) {
			if (isRunning())
			{
				energyStorage.receiveEnergy(RFPerTick, false);
			}
			this.sendEnergyToNeighbours();
		}
	}
	public abstract boolean isRunning();

	@Override
	public boolean canExtract()
	{
		return true;
	}

	@Override
	public boolean canReceive()
	{
		return false;
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
			throw new NullPointerException("inventory is null for generator: " + getName());
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
