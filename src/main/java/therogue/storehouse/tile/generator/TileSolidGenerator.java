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
import therogue.storehouse.reference.MachineStats;
import therogue.storehouse.tile.MachineTier;


public class TileSolidGenerator extends TileBaseGenerator
{

	public TileSolidGenerator(MachineTier type)
	{
		super(null, type, MachineStats.SOLIDGENPERTICK, false);
	}

	@Override
	public boolean isRunning()
	{
		// TODO Auto-generated method stub
		return false;
	}

	protected void tick()
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void doRunTick()
	{

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
