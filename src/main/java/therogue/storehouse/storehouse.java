/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse;

import therogue.storehouse.handlers.ConfigHandler;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.proxy.IProxy;
import therogue.storehouse.reference.General;
import therogue.storehouse.util.loghelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = General.MOD_ID, name = General.MOD_NAME, version = General.VERSION, acceptedMinecraftVersions = General.MC_VERSIONS, useMetadata = true, guiFactory = General.GUI_FACTORY, updateJSON = "https://raw.githubusercontent.com/the-rogue/Storehouse-Expansion/master/Misc%20Files/update.json")
public class storehouse
{
	@Instance
	public static storehouse instance;

	@SidedProxy(clientSide = General.CLIENT_PROXY_CLASS, serverSide = General.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new ConfigHandler());
		ModItems.init();
		ModBlocks.init();
		loghelper.log("info", "Pre Initialization Finished");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		loghelper.log("info", "Post Initialization Finished");
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		loghelper.log("info", "Initialization Finished");
	}
}
