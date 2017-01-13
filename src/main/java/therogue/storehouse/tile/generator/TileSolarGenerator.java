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
import net.minecraft.util.ITickable;
import therogue.storehouse.block.state.GeneratorType;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.reference.MachineStats;
import therogue.storehouse.util.BlockUtils;


public class TileSolarGenerator extends TileBaseGenerator implements ITickable
{
	public TileSolarGenerator(GeneratorType type)
	{
		super(type, type.getAppropriateEnergyStored(MachineStats.SOLARGENCAPACITY, MachineStats.SOLARGENPERTICK, MachineStats.SOLARGENSEND), MachineStats.SOLARGENPERTICK);
		inventory = new InventoryManager(this, 2, new int[]{0}, new int[]{1});
	}
	
	@Override
	public boolean isRunning()
	{
		return BlockUtils.canBlockSeeSky(this.pos, this.worldObj) && this.worldObj.isDaytime();
	}

	@Override
	public String getDefaultName()
	{
		return "container." + ModBlocks.solar_generator.getName() + "_" + type.getName();
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return null;
	}

	@Override
	public String getGuiID()
	{
		return "storehouse:" + ModBlocks.solar_generator.getName() +"." + type.getName();
	}
}
