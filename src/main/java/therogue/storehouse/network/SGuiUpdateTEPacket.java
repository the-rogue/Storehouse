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
import therogue.storehouse.LOG;
import therogue.storehouse.tile.StorehouseBaseMachine;

public class SGuiUpdateTEPacket implements IMessage {
	
	private byte item;
	private BlockPos pos;
	private NBTTagCompound nbt;
	
	public SGuiUpdateTEPacket () {
	}
	
	public SGuiUpdateTEPacket (int item, BlockPos pos, NBTTagCompound nbt) {
		this.item = (byte) item;
		this.pos = pos;
		this.nbt = nbt;
	}
	
	@Override
	public void fromBytes (ByteBuf buf) {
		this.item = buf.readByte();
		this.pos = BlockPos.fromLong(buf.readLong());
		this.nbt = NetworkUtils.readNBTTagCompound(buf);
	}
	
	@Override
	public void toBytes (ByteBuf buf) {
		buf.writeByte(item);
		buf.writeLong(pos.toLong());
		NetworkUtils.writeNBTTagCompoundToBuffer(buf, nbt);
	}
	
	public BlockPos getPos () {
		return pos;
	}
	
	public NBTTagCompound getNbt () {
		return nbt;
	}
	
	public int getItem () {
		return item;
	}
	
	public static class GuiClientUpdatePacketHandler implements IMessageHandler<SGuiUpdateTEPacket, IMessage> {
		
		@Override
		public IMessage onMessage (SGuiUpdateTEPacket message, MessageContext ctx) {
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
							if (te != null && te instanceof StorehouseBaseMachine)
							{
								StorehouseBaseMachine stoTE = (StorehouseBaseMachine) te;
								stoTE.processSGUIPacket(message, fromPlayer);
							}
							else
							{
								LOG.log("error", String.format("SGuiUpdateTEPacket Received invalid update packet for null tile entity at %s with data: %s", message.getPos(), message.getNbt()));
							}
						}
					}
				});
			}
			return null;
		}
	}
}
