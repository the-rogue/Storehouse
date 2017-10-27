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
import therogue.storehouse.config.ConfigValues;
import therogue.storehouse.energy.ItemEnergyCapabilityProvider;
import therogue.storehouse.item.StorehouseBaseActiveItem;

public class NetworkInspector extends StorehouseBaseActiveItem {
	
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
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		player.sendStatusMessage(new TextComponentString("Energy Stored: " + player.getHeldItem(hand).getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored()), false);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
