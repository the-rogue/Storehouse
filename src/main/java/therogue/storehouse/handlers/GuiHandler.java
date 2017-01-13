/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import therogue.storehouse.client.gui.machine.generator.GuiSolarGenerator;
import therogue.storehouse.container.machine.generator.ContainerSolarGenerator;
import therogue.storehouse.reference.Identification;
import therogue.storehouse.tile.generator.TileSolarGenerator;

public class GuiHandler implements IGuiHandler
{
	public static final GuiHandler INSTANCE = new GuiHandler();

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID) {
		case Identification.SOLARGENERATORGUI:
			return new ContainerSolarGenerator(player.inventory, (TileSolarGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID) {
		case Identification.SOLARGENERATORGUI:
			return new GuiSolarGenerator(new ContainerSolarGenerator(player.inventory, (TileSolarGenerator) world.getTileEntity(new BlockPos(x, y, z))));
		default:
			return null;
		}
	}

}
