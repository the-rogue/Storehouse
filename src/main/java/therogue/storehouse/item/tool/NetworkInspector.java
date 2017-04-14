/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.item.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.client.gui.multisystem.IBoundedPage;
import therogue.storehouse.client.gui.multisystem.IEntry;
import therogue.storehouse.client.gui.multisystem.IInfoSupplier;
import therogue.storehouse.client.gui.multisystem.Page;
import therogue.storehouse.client.gui.multisystem.impl.ItemStackEntry;
import therogue.storehouse.energy.ItemEnergyCapabilityProvider;
import therogue.storehouse.item.StorehouseBaseActiveItem;
import therogue.storehouse.reference.ConfigValues;
import therogue.storehouse.type.CategoryEnum;
import cofh.api.energy.IEnergyContainerItem;

public class NetworkInspector extends StorehouseBaseActiveItem implements IEnergyContainerItem, IInfoSupplier {
	
	public NetworkInspector (String name) {
		super(name);
		this.setMaxStackSize(1);
		this.addShiftInfo("Allows you to see Information about your Storehouse System,");
		this.addShiftInfo("Right Click on a Storehouse Block to find out.");
	}
	
	@Override
	public ICapabilityProvider initCapabilities (ItemStack stack, NBTTagCompound nbt) {
		return new ItemEnergyCapabilityProvider(stack, ConfigValues.networkInspectorCapacity, ConfigValues.networkInspectorRecieveRate, 0);
	}
	
	@Override
	public int receiveEnergy (ItemStack container, int maxReceive, boolean simulate) {
		return container.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy (ItemStack container, int maxExtract, boolean simulate) {
		return container.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int getEnergyStored (ItemStack container) {
		return container.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored (ItemStack container) {
		return container.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick (ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		player.addChatComponentMessage(new TextComponentString("Energy Stored: " + itemStack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored()));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
	}
	
	@Override
	public IEntry getEntry () {
		return new ItemStackEntry(CategoryEnum.TOOLS.category.name, "Network Inspector", new ItemStack(this, 1, 0)) {
			
			@Override
			public Page getPage (IBoundedPage bounds, int xStart, int yStart, int pageWidth, int pageHeight) {
				Page thisPage = new Page(1);
				return thisPage;
			}
		};
	}
}
