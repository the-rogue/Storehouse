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
import therogue.storehouse.client.gui.machine.GuiCombustionGenerator;
import therogue.storehouse.client.gui.machine.GuiLiquidGenerator;
import therogue.storehouse.client.gui.machine.GuiSolarGenerator;
import therogue.storehouse.client.gui.machine.GuiThermalPress;
import therogue.storehouse.container.machine.ContainerCombustionGenerator;
import therogue.storehouse.container.machine.ContainerLiquidGenerator;
import therogue.storehouse.container.machine.ContainerSolarGenerator;
import therogue.storehouse.container.machine.ContainerThermalPress;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.tile.machine.generator.TileCombustionGenerator;
import therogue.storehouse.tile.machine.generator.TileLiquidGenerator;
import therogue.storehouse.tile.machine.generator.TileSolarGenerator;

public class GuiHandler implements IGuiHandler {
	
	public static final GuiHandler INSTANCE = new GuiHandler();
	
	@Override
	public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case IDs.SOLARGENERATORGUI:
				return new ContainerSolarGenerator(player.inventory, (TileSolarGenerator) world.getTileEntity(new BlockPos(x, y, z)));
			case IDs.COMBUSTIONGENERATORGUI:
				return new ContainerCombustionGenerator(player.inventory, (TileCombustionGenerator) world.getTileEntity(new BlockPos(x, y, z)));
			case IDs.LIQUIDGENERATORGUI:
				return new ContainerLiquidGenerator(player.inventory, (TileLiquidGenerator) world.getTileEntity(new BlockPos(x, y, z)));
			case IDs.THERMALPRESSGUI:
				return new ContainerThermalPress(player.inventory, (TileThermalPress) world.getTileEntity(new BlockPos(x, y, z)));
			default:
				return null;
		}
	}
	
	@Override
	public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case IDs.SOLARGENERATORGUI:
				TileSolarGenerator gen = (TileSolarGenerator) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiSolarGenerator(new ContainerSolarGenerator(player.inventory, gen), gen);
			case IDs.COMBUSTIONGENERATORGUI:
				TileCombustionGenerator gen1 = (TileCombustionGenerator) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiCombustionGenerator(new ContainerCombustionGenerator(player.inventory, gen1), gen1);
			case IDs.LIQUIDGENERATORGUI:
				TileLiquidGenerator gen2 = (TileLiquidGenerator) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiLiquidGenerator(new ContainerLiquidGenerator(player.inventory, gen2), gen2);
			case IDs.THERMALPRESSGUI:
				TileThermalPress mach1 = (TileThermalPress) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiThermalPress(new ContainerThermalPress(player.inventory, mach1), mach1);
			default:
				return null;
		}
	}
}
