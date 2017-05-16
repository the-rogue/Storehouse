/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine;

import net.minecraft.nbt.NBTTagCompound;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.network.GuiClientUpdatePacket;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.tile.IClientPacketReciever;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.util.GeneralUtils;

public class TileThermalPress extends StorehouseBaseMachine implements IClientPacketReciever {
	
	private Mode mode = Mode.PRESS;
	
	public TileThermalPress () {
		super(ModBlocks.thermal_press, MachineTier.advanced);
		inventory = new InventoryManager(this, 0, null, null);
	}
	
	@Override
	public void update () {
	}
	
	@Override
	public int getField (int id) {
		switch (id) {
			case 2:
				return mode.ordinal();
			default:
				return super.getField(id);
		}
	}
	
	@Override
	public void setField (int id, int value) {
		switch (id) {
			case 2:
				modeUpdate(value);
				NBTTagCompound sendtag = new NBTTagCompound();
				sendtag.setInteger("type", 0);
				sendtag.setInteger("mode", this.mode.ordinal());
				StorehousePacketHandler.INSTANCE.sendToServer(new GuiClientUpdatePacket(this.getPos(), sendtag));
			default:
				super.setField(id, value);
		}
	}
	
	private void modeUpdate (int mode) {
		Mode m = GeneralUtils.getEnumFromNumber(Mode.class, mode);
		this.mode = m != null ? m : this.mode;
	}
	
	@Override
	public void processGUIPacket (GuiClientUpdatePacket message) {
		NBTTagCompound nbt = message.getNbt();
		switch (nbt.getInteger("type")) {
			case 0:
				modeUpdate(message.getNbt().getInteger("mode"));
				break;
		}
	}
	
	public enum Mode
	{
		PRESS,
		JOIN,
		STAMP,
		HIGH_PRESSURE;
	}
}
