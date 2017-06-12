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
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.energy.EnergyStorageAdv;
import therogue.storehouse.inventory.IGuiSupplier;
import therogue.storehouse.inventory.IInventoryCapability;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.network.GuiUpdateTEPacket;

public abstract class StorehouseBaseMachine extends StorehouseBaseTileEntity implements ITickable, IInventoryCapability, IGuiSupplier, IInteractionObject {
	
	protected InventoryManager inventory;
	protected EnergyStorageAdv energyStorage = new EnergyStorageAdv(8000, 100, 0);
	
	public StorehouseBaseMachine (IStorehouseBaseBlock block) {
		super(block);
	}
	
	// -------------------------Inventory Methods-----------------------------------
	@Override
	public IItemHandlerModifiable getInventory () {
		if (inventory == null) { throw new NullPointerException("inventory is null for machine: " + getName()); }
		return inventory;
	}
	
	@Override
	public void onInventoryChange () {
		this.markDirty();
	}
	
	// -------------------------Container/Gui Methods-----------------------------------
	/**
	 * Fields Used: #1 - Machine Tier #2 - Energy Stored #3 - Max Energy Storage available
	 */
	@Override
	public int getField (int id) {
		switch (id) {
			case 2:
				return energyStorage.getEnergyStored();
			case 3:
				return energyStorage.getMaxEnergyStored();
			default:
				return 0;
		}
	}
	
	@Override
	public void setField (int id, int value) {
		switch (id) {
		}
	}
	
	@Override
	public int getFieldCount () {
		return 3;
	}
	
	@Override
	public IItemHandlerModifiable getContainerCapability () {
		if (inventory == null) { throw new NullPointerException("inventory is null for machine: " + getName()); }
		return inventory.containerCapability;
	}
	
	// -------------------------Standard TE methods-----------------------------------
	@Override
	public GuiUpdateTEPacket getGUIPacket () {
		GuiUpdateTEPacket packet = super.getGUIPacket();
		packet.getNbt().setInteger("EnergyStored", energyStorage.getEnergyStored());
		return packet;
	}
	
	@Override
	public void processGUIPacket (GuiUpdateTEPacket packet) {
		super.processGUIPacket(packet);
		energyStorage.setEnergyStored(packet.getNbt().getInteger("EnergyStored"));
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		energyStorage.writeToNBT(nbt);
		inventory.writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		energyStorage.readFromNBT(nbt);
		inventory.readFromNBT(nbt);
	}
	
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		if (capability == CapabilityEnergy.ENERGY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) inventory;
		if (capability == CapabilityEnergy.ENERGY) return (T) energyStorage;
		return super.getCapability(capability, facing);
	}
}
