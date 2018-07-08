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

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import therogue.storehouse.LOG;
import therogue.storehouse.client.ForgeTESR;
import therogue.storehouse.client.StorehouseModelLoader;
import therogue.storehouse.client.gui.StorehouseElements;
import therogue.storehouse.client.gui.element.ElementFunctions;
import therogue.storehouse.tile.machine.TileForge;

public class ClientProxy extends CommonProxy {
	
	/**
	 * PreInitialises all methods the client needs to run, and all common methods by calling super() to common proxy
	 */
	@Override
	public void preInit (FMLPreInitializationEvent event) {
		super.preInit(event);
		LOG.debug("Client Proxy Started PreInitialisation");
		MinecraftForge.EVENT_BUS.register(EventHandlerClient.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileForge.class, new ForgeTESR());
		ModelLoaderRegistry.registerLoader(StorehouseModelLoader.INSTANCE);
		MinecraftForge.EVENT_BUS.register(StorehouseModelLoader.INSTANCE);
		LOG.debug("Client Proxy Finished PreInitialisation");
	}
	
	/**
	 * Initialises all methods the client needs to run, and all common methods by calling super() to common proxy
	 */
	@Override
	public void init (FMLInitializationEvent event) {
		super.init(event);
		LOG.debug("Client Proxy Started Initialisation");
		ElementFunctions.setup();
		StorehouseElements.register();
		LOG.debug("Client Proxy Finished Initialisation");
	}
	
	/**
	 * PostInitialises all methods the client needs to run, and all common methods by calling super() to common proxy
	 */
	@Override
	public void postInit (FMLPostInitializationEvent event) {
		super.postInit(event);
		LOG.debug("Client Proxy Started PostInitialisation");
		LOG.debug("Client Proxy Finished PostInitialisation");
	}
}
