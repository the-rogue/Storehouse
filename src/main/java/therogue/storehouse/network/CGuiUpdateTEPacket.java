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
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import therogue.storehouse.tile.StorehouseBaseTileEntity;
import therogue.storehouse.util.LOG;

public class CGuiUpdateTEPacket implements IMessage {
	
	private BlockPos pos;
	private NBTTagCompound nbt;
	
	public CGuiUpdateTEPacket () {
	}
	
	public CGuiUpdateTEPacket (BlockPos pos, NBTTagCompound nbt) {
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
	
	public static class GuiUpdateTEPacketHandler implements IMessageHandler<CGuiUpdateTEPacket, IMessage> {
		
		@Override
		public IMessage onMessage (CGuiUpdateTEPacket message, MessageContext ctx) {
			if (ctx.netHandler instanceof INetHandlerPlayClient)
			{
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					
					@Override
					public void run () {
						WorldClient world = Minecraft.getMinecraft().world;
						if (world.isBlockLoaded(message.getPos()))
						{
							TileEntity te = world.getTileEntity(message.getPos());
							if (te != null && te instanceof StorehouseBaseTileEntity)
							{
								StorehouseBaseTileEntity stoTE = (StorehouseBaseTileEntity) te;
								stoTE.processCGUIPacket(message);
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
