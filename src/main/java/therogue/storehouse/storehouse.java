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
import therogue.storehouse.proxy.IProxy;
import therogue.storehouse.reference.General;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = General.MODID, name = General.NAME, version = General.VERSION, acceptedMinecraftVersions = General.MCVERSIONS, useMetadata = true, guiFactory = General.GUIFACTORY)
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
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}
}
