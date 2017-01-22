/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import cofh.api.energy.IEnergyContainerItem;


public class ItemEnergyCapabilityProvider implements ICapabilityProvider, IEnergyContainerItem, IEnergyStorage
{
	protected ItemStack stack;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
	
	public ItemEnergyCapabilityProvider(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
		this.stack = stack;
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
	{
        if (container.getTagCompound() == null) {
            container.setTagCompound(new NBTTagCompound());
        }
        int energy = container.getTagCompound().getInteger("Energy");
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            container.getTagCompound().setInteger("Energy", energy);
        }
        return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
	{
        if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy")) {
            return 0;
        }
        int energy = container.getTagCompound().getInteger("Energy");
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

        if (!simulate) {
            energy -= energyExtracted;
            container.getTagCompound().setInteger("Energy", energy);
        }
        return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container)
	{
        if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy")) {
            return 0;
        }
        return container.getTagCompound().getInteger("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return capacity;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) this;
        }
        return null;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return this.receiveEnergy(stack, maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return this.extractEnergy(stack, maxExtract, simulate);
	}

	@Override
	public int getEnergyStored()
	{
		return this.getEnergyStored(stack);
	}

	@Override
	public int getMaxEnergyStored()
	{
		return this.getMaxEnergyStored(stack);
	}

	@Override
	public boolean canExtract()
	{
		return this.maxExtract > 0;
	}

	@Override
	public boolean canReceive()
	{
		return this.maxReceive > 0;
	}

}
