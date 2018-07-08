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

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.item.IStorehouseBaseItem;

public class EventHandlerClient {
	
	public static final EventHandlerClient INSTANCE = new EventHandlerClient();
	
	@SubscribeEvent
	public void onTextureStitchEvent (TextureStitchEvent.Pre event) {
	}
	
	@SubscribeEvent
	public void onRegisterModelsEvent (ModelRegistryEvent event) {
		ModBlocks.blocklist.forEach( (IStorehouseBaseBlock block) -> block.registerModels());
		ModItems.itemlist.forEach( (IStorehouseBaseItem item) -> item.registerModels());
	}
}
