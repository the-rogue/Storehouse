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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import therogue.storehouse.block.state.GeneratorType;
import therogue.storehouse.reference.MachineStats;


public class TileSolidGenerator extends TileBaseGenerator
{
	
	public TileSolidGenerator(GeneratorType type) {
		super(type, type.getAppropriateEnergyStored(MachineStats.SOLIDGENCAPACITY, 0, MachineStats.SOLIDGENSEND), MachineStats.SOLIDGENPERTICK);
	}

	@Override
	public boolean isRunning()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDefaultName()
	{
		return "container.solid_generator_" + type.getName();
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGuiID()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
