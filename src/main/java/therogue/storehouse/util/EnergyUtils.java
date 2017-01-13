/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import cofh.api.energy.IEnergyReceiver;

public class EnergyUtils
{
	public static int sendEnergy(World world, BlockPos from, EnumFacing side, int maxRFToGive){
		EnumFacing opposite = side.getOpposite();
		BlockPos pos = from.offset(side);
		TileEntity te = world.getTileEntity(pos);
		int received = 0;

		if (te instanceof IEnergyReceiver) {
			IEnergyReceiver teReceive = (IEnergyReceiver)te;
			if (teReceive.canConnectEnergy(opposite)) {
				received = teReceive.receiveEnergy(opposite, maxRFToGive, false);
			}
		} else if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, opposite)) {
			IEnergyStorage capability = te.getCapability(CapabilityEnergy.ENERGY, opposite);
			if (capability.canReceive()) {
				received = capability.receiveEnergy(maxRFToGive, false);
			}
		}
		return received;
	}
}
