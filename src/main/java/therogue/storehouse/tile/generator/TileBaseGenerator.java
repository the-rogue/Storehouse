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

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.state.GeneratorType;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.IDefaultSidedInventory;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.StorehouseBaseEnergyStorageTE;
import therogue.storehouse.util.EnergyUtils;
import therogue.storehouse.util.GeneralUtils;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;


public abstract class TileBaseGenerator extends StorehouseBaseEnergyStorageTE implements IEnergyProvider, IDefaultSidedInventory, IInteractionObject
{
	protected InventoryManager inventory;
	protected GeneratorType type;
	protected int RFPerTick;

	public TileBaseGenerator(IStorehouseBaseBlock block, GeneratorType type, int baseGeneration, boolean allowInsert)
	{
		super(block, type.getAppropriateEnergyStored(baseGeneration, allowInsert));
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
			if (energyStorage.getEnergyStored() <= 0) return;
			int sentRF = EnergyUtils.sendEnergy(worldObj, getPos(), facing, energyStorage.extractEnergy(energyStorage.getMaxExtract(), true));
			energyStorage.extractEnergy(sentRF, false);
		}
	}

	protected void sendEnergyToItems(int slot)
	{
		if (energyStorage.getEnergyStored() <= 0) return;
		int sentRF = EnergyUtils.sendItemEnergy(inventory.getStackInSlot(slot), energyStorage.extractEnergy(energyStorage.getMaxExtract() / 4, true));
		energyStorage.extractEnergy(sentRF, false);
	}

	@Override
	public void update()
	{
		if (GeneralUtils.isServerSide(worldObj))
		{
			if (isRunning())
			{
				doRunTick();
				energyStorage.modifyEnergyStored(RFPerTick);
			}
			tick();
			this.sendEnergyToNeighbours();
			if(worldObj != null) {
				this.worldObj.notifyBlockUpdate(this.getPos(), ModBlocks.azurite_dust_block.getDefaultState(), ModBlocks.azurite_dust_block.getDefaultState(), 0);
			}
		}
	}

	protected abstract void doRunTick();

	protected abstract void tick();

	public abstract boolean isRunning();

	@Override
	public int getField(int id)
	{
		ItemStack stack;
		switch (id)
		{
		case 1:
			return isRunning() ? 1 : 0;
		case 2:
			stack = inventory.getStackInSlot(0);
			if (stack != null && stack.getItem() instanceof IEnergyContainerItem)
			{
				return ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack);
			}
			else if (stack != null && stack.hasCapability(CapabilityEnergy.ENERGY, null))
			{
				return stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
			}
			else
			{
				return 0;
			}
		case 3:
			stack = inventory.getStackInSlot(0);
			if (stack != null && stack.getItem() instanceof IEnergyContainerItem)
			{
				return ((IEnergyContainerItem) stack.getItem()).getMaxEnergyStored(stack);
			}
			else if (stack != null && stack.hasCapability(CapabilityEnergy.ENERGY, null))
			{
				return stack.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
			}
			else
			{
				return 0;
			}
		case 4:
			return energyStorage.getEnergyStored();
		case 5:
			return energyStorage.getMaxEnergyStored();
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{

		}
	}

	@Override
	public int getFieldCount()
	{
		return 5;
	}

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
	public String getName()
	{
		return super.getName() + "_" + type.getName();
	}

	@Override
	public InventoryManager getInventoryManager()
	{
		if (inventory == null)
		{
			throw new NullPointerException("inventory is null for generator: " + getName());
		}
		return inventory;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return (T) inventory.getWrapper();
		}
		return super.getCapability(capability, facing);
	}

}
