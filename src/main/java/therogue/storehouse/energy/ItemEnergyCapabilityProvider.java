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

public class ItemEnergyCapabilityProvider implements ICapabilityProvider, IEnergyStorage {
	
	protected ItemStack stack;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	
	public ItemEnergyCapabilityProvider (ItemStack stack, int capacity, int maxReceive, int maxExtract) {
		this.stack = stack;
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}
	
	@Override
	public int receiveEnergy (int maxReceive, boolean simulate) {
		if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
		int energy = stack.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
		if (!simulate)
		{
			energy += energyReceived;
			stack.getTagCompound().setInteger("Energy", energy);
		}
		return energyReceived;
	}
	
	@Override
	public int extractEnergy (int maxExtract, boolean simulate) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("Energy")) return 0;
		int energy = stack.getTagCompound().getInteger("Energy");
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if (!simulate)
		{
			energy -= energyExtracted;
			stack.getTagCompound().setInteger("Energy", energy);
		}
		return energyExtracted;
	}
	
	@Override
	public int getEnergyStored () {
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("Energy")) return 0;
		return stack.getTagCompound().getInteger("Energy");
	}
	
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) return true;
		return false;
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) return (T) this;
		return null;
	}
	
	@Override
	public int getMaxEnergyStored () {
		return capacity;
	}
	
	@Override
	public boolean canExtract () {
		return this.maxExtract > 0;
	}
	
	@Override
	public boolean canReceive () {
		return this.maxReceive > 0;
	}
}
