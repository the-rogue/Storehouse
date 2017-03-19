/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import therogue.storehouse.core.Storehouse;
import therogue.storehouse.handlers.ConfigHandler;
import therogue.storehouse.handlers.EventHandlerCommon;
import therogue.storehouse.handlers.GuiHandler;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.init.ModTileEntities;
import therogue.storehouse.init.Recipes;
import therogue.storehouse.init.RegOreDictionary;
import therogue.storehouse.type.CategoryEnum;
import therogue.storehouse.util.loghelper;
import therogue.storehouse.world.StorehouseWorldGen;

public abstract class CommonProxy implements IProxy {
	
	/**
	 * Will PreInitialise all methods that are common
	 */
	@Override
	public void preInit (FMLPreInitializationEvent event) {
		loghelper.log("debug", "Common Proxy Started PreInitialisation");
		MinecraftForge.EVENT_BUS.register(EventHandlerCommon.INSTANCE);
		CategoryEnum.initCategories();
		ModItems.preInit();
		ModBlocks.preInit();
		ModTileEntities.preInit();
		ConfigHandler.preInit(event.getSuggestedConfigurationFile());
		loghelper.log("debug", "Common Proxy Finished PreInitialisation");
	}
	
	/**
	 * Will Initialise all methods that are common
	 */
	@Override
	public void init (FMLInitializationEvent event) {
		loghelper.log("debug", "Common Proxy Started Initialisation");
		RegOreDictionary.init();
		Recipes.init();
		StorehouseWorldGen.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(Storehouse.instance, GuiHandler.INSTANCE);
		loghelper.log("debug", "Common Proxy Finished Initialisation");
	}
	
	/**
	 * Will PostInitialise all methods that are common
	 */
	@Override
	public void postInit (FMLPostInitializationEvent event) {
		loghelper.log("debug", "Common Proxy Started PostInitialisation");
		loghelper.log("debug", "Common Proxy Finished PostInitialisation");
	}
}
