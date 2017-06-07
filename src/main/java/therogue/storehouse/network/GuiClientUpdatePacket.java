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

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import therogue.storehouse.tile.IClientPacketReciever;
import therogue.storehouse.util.LOG;
import therogue.storehouse.util.NetworkUtils;

public class GuiClientUpdatePacket implements IMessage {
	
	private BlockPos pos;
	private NBTTagCompound nbt;
	
	public GuiClientUpdatePacket () {
	}
	
	public GuiClientUpdatePacket (BlockPos pos, NBTTagCompound nbt) {
		this.pos = pos;
		this.nbt = nbt;
	}
	
	@Override
	public void fromBytes (ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.nbt = NetworkUtils.readNBTTagCompound(buf);
	}
	
	@Override
	public void toBytes (ByteBuf buf) {
		buf.writeLong(pos.toLong());
		NetworkUtils.writeNBTTagCompoundToBuffer(buf, nbt);
	}
	
	public BlockPos getPos () {
		return pos;
	}
	
	public NBTTagCompound getNbt () {
		return nbt;
	}
	
	public static class GuiClientUpdatePacketHandler implements IMessageHandler<GuiClientUpdatePacket, IMessage> {
		
		@Override
		public IMessage onMessage (GuiClientUpdatePacket message, MessageContext ctx) {
			if (ctx.netHandler instanceof INetHandlerPlayServer)
			{
				EntityPlayerMP fromPlayer = ctx.getServerHandler().playerEntity;
				fromPlayer.getServerWorld().addScheduledTask(new Runnable() {
					
					@Override
					public void run () {
						WorldServer world = fromPlayer.getServerWorld();
						if (world.isBlockLoaded(message.getPos()))
						{
							TileEntity te = world.getTileEntity(message.getPos());
							if (te != null && te instanceof IClientPacketReciever)
							{
								IClientPacketReciever stoTE = (IClientPacketReciever) te;
								stoTE.processGUIPacket(message, fromPlayer);
							}
							else
							{
								LOG.log("error", String.format("Received invalid update packet for null tile entity at {} with data: {}", message.getPos(), message.getNbt()));
							}
						}
					}
				});
			}
			return null;
		}
	}
}
