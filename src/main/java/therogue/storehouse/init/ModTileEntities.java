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
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorAdvanced;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorBasic;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorEnder;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorInfused;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorUltimate;

public class ModTileEntities
{
	public static void preInit() {
		GameRegistry.registerTileEntity(TileSolarGeneratorBasic.class, IDs.RESOURCENAMEPREFIX + "solar_generator_basic");
		GameRegistry.registerTileEntity(TileSolarGeneratorAdvanced.class, IDs.RESOURCENAMEPREFIX + "solar_generator_advanced");
		GameRegistry.registerTileEntity(TileSolarGeneratorInfused.class, IDs.RESOURCENAMEPREFIX + "solar_generator_infused");
		GameRegistry.registerTileEntity(TileSolarGeneratorEnder.class, IDs.RESOURCENAMEPREFIX + "solar_generator_ender");
		GameRegistry.registerTileEntity(TileSolarGeneratorUltimate.class, IDs.RESOURCENAMEPREFIX + "solar_generator_ultimate");
	}

}
