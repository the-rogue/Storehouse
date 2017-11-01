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

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler;

public class EventHandlerCommon {
	
	public static final EventHandlerCommon INSTANCE = new EventHandlerCommon();
	
	@SubscribeEvent
	public void onPlayerJoinWorldEvent (EntityJoinWorldEvent event) {
	}
	
	@SubscribeEvent
	public void onServerTick (TickEvent.ServerTickEvent event) {
		MachineCraftingHandler.tickCrafters(event);
	}
	
	@SubscribeEvent
	public void onNeighbourNotified (BlockEvent.NeighborNotifyEvent event) {
		MultiBlockFormationHandler.onNeighbourNotifiedEvent(event);
	}
	
	@SubscribeEvent (priority = EventPriority.LOWEST)
	public void onRightClickBlockEvent (PlayerInteractEvent.RightClickBlock event) {
		MultiBlockFormationHandler.onRightClickBlockEvent(event);
	}
}
