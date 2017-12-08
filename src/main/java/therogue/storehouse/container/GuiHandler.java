/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import therogue.storehouse.client.gui.machine.GuiAlloyFurnace;
import therogue.storehouse.client.gui.machine.GuiCombustionGenerator;
import therogue.storehouse.client.gui.machine.GuiCrystaliser;
import therogue.storehouse.client.gui.machine.GuiLiquidGenerator;
import therogue.storehouse.client.gui.machine.GuiSolarGenerator;
import therogue.storehouse.client.gui.machine.GuiThermalPress;
import therogue.storehouse.client.gui.multiblock.GuiCarbonCompressor;
import therogue.storehouse.container.machine.ContainerAlloyFurnace;
import therogue.storehouse.container.machine.ContainerCombustionGenerator;
import therogue.storehouse.container.machine.ContainerCrystaliser;
import therogue.storehouse.container.machine.ContainerLiquidGenerator;
import therogue.storehouse.container.machine.ContainerSolarGenerator;
import therogue.storehouse.container.machine.ContainerThermalPress;
import therogue.storehouse.container.multiblock.ContainerCarbonCompressor;
import therogue.storehouse.tile.machine.TileAlloyFurnace;
import therogue.storehouse.tile.machine.TileCombustionGenerator;
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileLiquidGenerator;
import therogue.storehouse.tile.machine.TileSolarGenerator;
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.tile.multiblock.TileCarbonCompressor;

public class GuiHandler implements IGuiHandler {
	
	public static final GuiHandler INSTANCE = new GuiHandler();
	public static final int NULL = -1;
	public static final int SOLARGENERATOR = 0;
	public static final int COMBUSTIONGENERATOR = 1;
	public static final int LIQUIDGENERATOR = 2;
	public static final int THERMALPRESS = 3;
	public static final int CRYSTALISER = 4;
	public static final int ALLOYFURNACE = 5;
	public static final int CARBONCOMPRESSOR = 6;
	
	@Override
	public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID)
		{
			case SOLARGENERATOR:
				return new ContainerSolarGenerator(player.inventory, (TileSolarGenerator) world.getTileEntity(new BlockPos(x, y, z)));
			case COMBUSTIONGENERATOR:
				return new ContainerCombustionGenerator(player.inventory, (TileCombustionGenerator) world.getTileEntity(new BlockPos(x, y, z)));
			case LIQUIDGENERATOR:
				return new ContainerLiquidGenerator(player.inventory, (TileLiquidGenerator) world.getTileEntity(new BlockPos(x, y, z)));
			case THERMALPRESS:
				return new ContainerThermalPress(player.inventory, (TileThermalPress) world.getTileEntity(new BlockPos(x, y, z)));
			case CRYSTALISER:
				return new ContainerCrystaliser(player.inventory, (TileCrystaliser) world.getTileEntity(new BlockPos(x, y, z)));
			case ALLOYFURNACE:
				return new ContainerAlloyFurnace(player.inventory, (TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)));
			case CARBONCOMPRESSOR:
				return new ContainerCarbonCompressor(player.inventory, (TileCarbonCompressor) world.getTileEntity(new BlockPos(x, y, z)));
			default:
				return null;
		}
	}
	
	@Override
	public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID)
		{
			case SOLARGENERATOR:
				TileSolarGenerator gen = (TileSolarGenerator) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiSolarGenerator(new ContainerSolarGenerator(player.inventory, gen), gen);
			case COMBUSTIONGENERATOR:
				TileCombustionGenerator gen1 = (TileCombustionGenerator) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiCombustionGenerator(new ContainerCombustionGenerator(player.inventory, gen1), gen1);
			case LIQUIDGENERATOR:
				TileLiquidGenerator gen2 = (TileLiquidGenerator) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiLiquidGenerator(new ContainerLiquidGenerator(player.inventory, gen2), gen2);
			case THERMALPRESS:
				TileThermalPress mach1 = (TileThermalPress) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiThermalPress(new ContainerThermalPress(player.inventory, mach1), mach1);
			case CRYSTALISER:
				TileCrystaliser mach2 = (TileCrystaliser) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiCrystaliser(new ContainerCrystaliser(player.inventory, mach2), mach2);
			case ALLOYFURNACE:
				TileAlloyFurnace mach3 = (TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiAlloyFurnace(new ContainerAlloyFurnace(player.inventory, mach3), mach3);
			case CARBONCOMPRESSOR:
				TileCarbonCompressor mach4 = (TileCarbonCompressor) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiCarbonCompressor(new ContainerCarbonCompressor(player.inventory, mach4), mach4);
			default:
				return null;
		}
	}
}
