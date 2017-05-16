/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import therogue.storehouse.reference.General;

public class StorehousePacketHandler {
	
	private static int ID = 0;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(General.MOD_ID);
	
	public static <REQ extends IMessage, REPLY extends IMessage> void registerPacket (Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
		StorehousePacketHandler.INSTANCE.registerMessage(messageHandler, requestMessageType, ID++, side);
	}
	
	static
	{
		StorehousePacketHandler.registerPacket(GuiUpdateTEPacket.GuiUpdateTEPacketHandler.class, GuiUpdateTEPacket.class, Side.CLIENT);
		StorehousePacketHandler.registerPacket(GuiClientUpdatePacket.GuiClientUpdatePacketHandler.class, GuiClientUpdatePacket.class, Side.SERVER);
	}
}
