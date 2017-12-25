/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.Storehouse;
import therogue.storehouse.multiblock.tile.BasicMultiBlockTile;
import therogue.storehouse.tile.machine.TileAlloyFurnace;
import therogue.storehouse.tile.machine.TileBurner;
import therogue.storehouse.tile.machine.TileCarbonCompressor;
import therogue.storehouse.tile.machine.TileCombustionGenerator;
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileForge;
import therogue.storehouse.tile.machine.TileLiquidGenerator;
import therogue.storehouse.tile.machine.TileSolarGenerator;
import therogue.storehouse.tile.machine.TileThermalPress;

public class ModTileEntities {
	
	public static void preInit () {
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorBasic.class, Storehouse.RESOURCENAMEPREFIX + "solar_generator_basic");
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorAdvanced.class, Storehouse.RESOURCENAMEPREFIX + "solar_generator_advanced");
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorInfused.class, Storehouse.RESOURCENAMEPREFIX + "solar_generator_infused");
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorEnder.class, Storehouse.RESOURCENAMEPREFIX + "solar_generator_ender");
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorUltimate.class, Storehouse.RESOURCENAMEPREFIX + "solar_generator_ultimate");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorBasic.class, Storehouse.RESOURCENAMEPREFIX + "combustion_generator_basic");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorAdvanced.class, Storehouse.RESOURCENAMEPREFIX + "combustion_generator_advanced");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorInfused.class, Storehouse.RESOURCENAMEPREFIX + "combustion_generator_infused");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorEnder.class, Storehouse.RESOURCENAMEPREFIX + "combustion_generator_ender");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorUltimate.class, Storehouse.RESOURCENAMEPREFIX + "combustion_generator_ultimate");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorBasic.class, Storehouse.RESOURCENAMEPREFIX + "liquid_generator_basic");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorAdvanced.class, Storehouse.RESOURCENAMEPREFIX + "liquid_generator_advanced");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorInfused.class, Storehouse.RESOURCENAMEPREFIX + "liquid_generator_infused");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorEnder.class, Storehouse.RESOURCENAMEPREFIX + "liquid_generator_ender");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorUltimate.class, Storehouse.RESOURCENAMEPREFIX + "liquid_generator_ultimate");
		GameRegistry.registerTileEntity(TileThermalPress.class, Storehouse.RESOURCENAMEPREFIX + "thermal_press");
		GameRegistry.registerTileEntity(TileCrystaliser.class, Storehouse.RESOURCENAMEPREFIX + "crystaliser");
		GameRegistry.registerTileEntity(TileForge.class, Storehouse.RESOURCENAMEPREFIX + "stamper");
		GameRegistry.registerTileEntity(TileAlloyFurnace.class, Storehouse.RESOURCENAMEPREFIX + "alloy_furnace");
		GameRegistry.registerTileEntity(TileCarbonCompressor.class, Storehouse.RESOURCENAMEPREFIX + "carbon_compressor");
		GameRegistry.registerTileEntity(TileBurner.class, Storehouse.RESOURCENAMEPREFIX + "burner");
		GameRegistry.registerTileEntity(BasicMultiBlockTile.class, Storehouse.RESOURCENAMEPREFIX + "generic_multiblock");
	}
}
