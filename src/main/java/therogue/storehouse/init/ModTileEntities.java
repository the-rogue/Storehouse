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
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.tile.machine.generator.TileCombustionGeneratorAdvanced;
import therogue.storehouse.tile.machine.generator.TileCombustionGeneratorBasic;
import therogue.storehouse.tile.machine.generator.TileCombustionGeneratorEnder;
import therogue.storehouse.tile.machine.generator.TileCombustionGeneratorInfused;
import therogue.storehouse.tile.machine.generator.TileCombustionGeneratorUltimate;
import therogue.storehouse.tile.machine.generator.TileLiquidGeneratorAdvanced;
import therogue.storehouse.tile.machine.generator.TileLiquidGeneratorBasic;
import therogue.storehouse.tile.machine.generator.TileLiquidGeneratorEnder;
import therogue.storehouse.tile.machine.generator.TileLiquidGeneratorInfused;
import therogue.storehouse.tile.machine.generator.TileLiquidGeneratorUltimate;
import therogue.storehouse.tile.machine.generator.TileSolarGeneratorAdvanced;
import therogue.storehouse.tile.machine.generator.TileSolarGeneratorBasic;
import therogue.storehouse.tile.machine.generator.TileSolarGeneratorEnder;
import therogue.storehouse.tile.machine.generator.TileSolarGeneratorInfused;
import therogue.storehouse.tile.machine.generator.TileSolarGeneratorUltimate;

public class ModTileEntities {
	
	public static void preInit () {
		GameRegistry.registerTileEntity(TileSolarGeneratorBasic.class, IDs.RESOURCENAMEPREFIX + "solar_generator_basic");
		GameRegistry.registerTileEntity(TileSolarGeneratorAdvanced.class, IDs.RESOURCENAMEPREFIX + "solar_generator_advanced");
		GameRegistry.registerTileEntity(TileSolarGeneratorInfused.class, IDs.RESOURCENAMEPREFIX + "solar_generator_infused");
		GameRegistry.registerTileEntity(TileSolarGeneratorEnder.class, IDs.RESOURCENAMEPREFIX + "solar_generator_ender");
		GameRegistry.registerTileEntity(TileSolarGeneratorUltimate.class, IDs.RESOURCENAMEPREFIX + "solar_generator_ultimate");
		GameRegistry.registerTileEntity(TileCombustionGeneratorBasic.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_basic");
		GameRegistry.registerTileEntity(TileCombustionGeneratorAdvanced.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_advanced");
		GameRegistry.registerTileEntity(TileCombustionGeneratorInfused.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_infused");
		GameRegistry.registerTileEntity(TileCombustionGeneratorEnder.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_ender");
		GameRegistry.registerTileEntity(TileCombustionGeneratorUltimate.class, IDs.RESOURCENAMEPREFIX + "combustion_generator_ultimate");
		GameRegistry.registerTileEntity(TileLiquidGeneratorBasic.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_basic");
		GameRegistry.registerTileEntity(TileLiquidGeneratorAdvanced.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_advanced");
		GameRegistry.registerTileEntity(TileLiquidGeneratorInfused.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_infused");
		GameRegistry.registerTileEntity(TileLiquidGeneratorEnder.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_ender");
		GameRegistry.registerTileEntity(TileLiquidGeneratorUltimate.class, IDs.RESOURCENAMEPREFIX + "liquid_generator_ultimate");
		GameRegistry.registerTileEntity(TileThermalPress.class, IDs.RESOURCENAMEPREFIX + "thermal_press");
	}
}
