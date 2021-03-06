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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.registries.IForgeRegistry;
import therogue.storehouse.Storehouse;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.multiblock.block.MultiBlockCreationHandler;
import therogue.storehouse.tile.TileTickingRegistry;

public class EventHandlerCommon {
	
	public static final EventHandlerCommon INSTANCE = new EventHandlerCommon();
	
	@SubscribeEvent
	public void onPlayerJoinWorldEvent (EntityJoinWorldEvent event) {
	}
	
	@SubscribeEvent
	public void onServerTick (TickEvent.ServerTickEvent event) {
		TileTickingRegistry.INSTANCE.tick(event);
	}
	
	@SubscribeEvent
	public void registerBlocks (RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ModBlocks.blocklist.toArray(new Block[0]));
		MultiBlockCreationHandler.INSTANCE.getCapabilitiesFromFile(new ResourceLocation(Storehouse.MOD_ID, "multiblock/capabilities.txt"));
		MultiBlockCreationHandler.INSTANCE.getBlockAliasesFromFile(new ResourceLocation(Storehouse.MOD_ID, "multiblock/block_aliases.txt"));
		event.getRegistry().registerAll(MultiBlockCreationHandler.INSTANCE.getBlockList().toArray(new Block[0]));
	}
	
	@SubscribeEvent
	public void registerItems (RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> itemRegistry = event.getRegistry();
		ModBlocks.blocklist.forEach(block -> {
			if (block.getItemBlock() != null) itemRegistry.register(block.getItemBlock());
		});
		MultiBlockCreationHandler.INSTANCE.getBlockList().forEach( (IStorehouseBaseBlock block) -> {
			if (block.getItemBlock() != null) itemRegistry.register(block.getItemBlock());
		});
		itemRegistry.registerAll(ModItems.itemlist.toArray(new Item[0]));
	}
}
