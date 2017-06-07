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
import therogue.storehouse.reference.IDs;
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.tile.machine.generator.TileCombustionGenerator;
import therogue.storehouse.tile.machine.generator.TileLiquidGenerator;
import therogue.storehouse.tile.machine.generator.TileSolarGenerator;

public class ModTileEntities {
	
	public static void preInit () {
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorBasic.class, IDs.RESOURCENAMEPREFIX + "solar_generator_basic");
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorAdvanced.class, IDs.RESOURCENAMEPREFIX + "solar_generator_advanced");
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorInfused.class, IDs.RESOURCENAMEPREFIX + "solar_generator_infused");
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorEnder.class, IDs.RESOURCENAMEPREFIX + "solar_generator_ender");
		GameRegistry.registerTileEntity(TileSolarGenerator.TileSolarGeneratorUltimate.class, IDs.RESOURCENAMEPREFIX + "solar_generator_ultimate");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorBasic.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_basic");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorAdvanced.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_advanced");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorInfused.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_infused");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorEnder.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_ender");
		GameRegistry.registerTileEntity(TileCombustionGenerator.TileCombustionGeneratorUltimate.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_ultimate");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorBasic.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_basic");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorAdvanced.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_advanced");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorInfused.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_infused");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorEnder.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_ender");
		GameRegistry.registerTileEntity(TileLiquidGenerator.TileLiquidGeneratorUltimate.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_ultimate");
		GameRegistry.registerTileEntity(TileThermalPress.class, IDs.RESOURCENAMEPREFIX + "thermal_press");
		GameRegistry.registerTileEntity(TileCrystaliser.class, IDs.RESOURCENAMEPREFIX + "crystaliser");
	}
}
