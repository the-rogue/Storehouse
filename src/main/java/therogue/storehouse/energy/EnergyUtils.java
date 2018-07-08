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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

public class EnergyUtils {
	
	public static void sendEnergytoAll (IEnergyStorage energyStorage, World world, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.values())
		{
			if (energyStorage.getEnergyStored() <= 0) return;
			int sentRF = sendEnergy(world, pos, facing, energyStorage.extractEnergy(energyStorage.getEnergyStored(), true));
			energyStorage.extractEnergy(sentRF, false);
		}
	}
	
	public static int sendEnergy (World world, BlockPos from, EnumFacing side, int maxRFToGive) {
		EnumFacing opposite = side.getOpposite();
		BlockPos pos = from.offset(side);
		TileEntity te = world.getTileEntity(pos);
		int received = 0;
		if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, opposite))
		{
			IEnergyStorage capability = te.getCapability(CapabilityEnergy.ENERGY, opposite);
			if (capability.canReceive())
			{
				received = capability.receiveEnergy(maxRFToGive, false);
			}
		}
		return received;
	}
	
	public static void sendItemEnergy (IEnergyStorage energyStorage, IItemHandler inventory, int slot, int maxRFToGive) {
		maxRFToGive = energyStorage.extractEnergy(maxRFToGive, true);
		ItemStack energyItem = inventory.extractItem(slot, -1, false);
		if (energyItem.isEmpty()) return;
		if (maxRFToGive > 0 && energyItem.hasCapability(CapabilityEnergy.ENERGY, null)
				&& energyItem.getCapability(CapabilityEnergy.ENERGY, null).canReceive())
		{
			int sentRF = energyItem.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(maxRFToGive, false);
			energyStorage.extractEnergy(sentRF, false);
		}
		inventory.insertItem(slot, energyItem, false);
	}
	
	public static boolean isItemFull (ItemStack energyItem) {
		if (energyItem == null || energyItem.isEmpty()) return false;
		if (energyItem.hasCapability(CapabilityEnergy.ENERGY, null))
		{
			IEnergyStorage capability = energyItem.getCapability(CapabilityEnergy.ENERGY, null);
			return capability.getEnergyStored() == capability.getMaxEnergyStored();
		}
		else
		{
			return true;
		}
	}
}
