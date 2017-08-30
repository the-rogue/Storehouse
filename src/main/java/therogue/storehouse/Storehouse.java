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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import therogue.storehouse.command.DebugResetLogger;
import therogue.storehouse.handlers.ConfigHandler;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.proxy.IProxy;
import therogue.storehouse.reference.General;
import therogue.storehouse.util.LOG;

@Mod (modid = General.MOD_ID, name = General.MOD_NAME, version = General.VERSION, acceptedMinecraftVersions = General.MC_VERSIONS, useMetadata = true, guiFactory = General.GUI_FACTORY, updateJSON = "https://raw.githubusercontent.com/the-rogue/Storehouse-Expansion/master/Misc_Files/update.json", dependencies = "before:guideapi")
public class Storehouse {
	
	/**
	 * Creates an instance of Storehouse so I can reference it later
	 */
	@Instance
	public static Storehouse instance;
	/**
	 * Initiates a Proxy
	 */
	@SidedProxy (clientSide = General.CLIENT_PROXY_CLASS, serverSide = General.SERVER_PROXY_CLASS)
	public static IProxy proxy;
	
	/**
	 * PreInitialisation Stage
	 */
	@EventHandler
	public void preinit (FMLPreInitializationEvent event) {
		LOG.log("debug", "Pre Initialization Started");
		proxy.preInit(event);
		MinecraftForge.EVENT_BUS.register(new ConfigHandler());
		LOG.log("info", "Pre Initialization Finished");
	}
	
	/**
	 * Initialisation Stage
	 */
	@EventHandler
	public void init (FMLInitializationEvent event) {
		LOG.log("debug", "Initialization Started");
		proxy.init(event);
		LOG.log("info", "Initialization Finished");
	}
	
	/**
	 * PostInitialisation Stage
	 */
	@EventHandler
	public void postinit (FMLPostInitializationEvent event) {
		LOG.log("debug", "Post Initialization Started");
		proxy.postInit(event);
		LOG.log("info", "Post Initialization Finished");
	}
	
	@EventHandler
	public void startServer (FMLServerStartingEvent event) {
		event.registerServerCommand(DebugResetLogger.INSTANCE);
	}
	
	/**
	 * This mod's creative tab
	 */
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(General.MOD_ID) {
		
		@Override
		public ItemStack getTabIconItem () {
			return ModItems.azurite_dust_itemstack;
		}
		
		@Override
		public String getTranslatedTabLabel () {
			return General.MOD_NAME;
		}
	};
}
