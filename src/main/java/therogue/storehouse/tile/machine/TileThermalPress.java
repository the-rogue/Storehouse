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

import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import therogue.storehouse.client.gui.GuiHelper.XYCoords;
import therogue.storehouse.container.machine.ContainerThermalPress;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineCraftingHandler.CraftingManager;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.network.GuiClientUpdatePacket;
import therogue.storehouse.network.GuiUpdateTEPacket;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.tile.IClientPacketReciever;
import therogue.storehouse.tile.StorehouseBaseMachine;
import therogue.storehouse.util.GeneralUtils;

public class TileThermalPress extends StorehouseBaseMachine implements IClientPacketReciever, ICrafter {
	
	public static final int RFPerTick = 40;
	private Mode mode = Mode.PRESS;
	private CraftingManager theCrafter = MachineCraftingHandler.getHandler(this.getClass()).newCrafter(this);
	
	public TileThermalPress () {
		super(ModBlocks.thermal_press);
		inventory = new InventoryManager(this, 6, new Integer[] { 1, 2, 3, 4, 5 }, new Integer[] { 0 }) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				return theCrafter.checkItemValidForSlot(index - 1, new ItemStackWrapper(stack));
			}
		};
	}
	
	// -------------------------Tile Specific Utility Methods-------------------------------------------
	private void modeUpdate (int mode) {
		Mode m = GeneralUtils.getEnumFromNumber(Mode.class, mode);
		if (m != this.mode)
		{
			this.mode = m != null ? m : this.mode;
			this.onInventoryChange();
		}
	}
	
	// -------------------------ICrafter Methods-----------------------------------
	@Override
	public Set<Integer> getOrderMattersSlots () {
		return mode.orderMattersSlots;
	}
	
	@Override
	public IRecipeInventory getCraftingInventory () {
		return new RangedItemInventory(getInventory(), 1, mode.craftingSlots + 1);
	}
	
	@Override
	public IRecipeInventory getOutputInventory () {
		return new RangedItemInventory(getInventory(), 0, 1);
	}
	
	@Override
	public boolean isRunning () {
		return energyStorage.getEnergyStored() >= RFPerTick;
	}
	
	@Override
	public void doRunTick () {
		if (isRunning())
		{
			energyStorage.modifyEnergyStored(-RFPerTick);
		}
	}
	
	// ------------------IClientPacketReciever Methods-------------------------------------------------------
	@Override
	public void processGUIPacket (GuiClientUpdatePacket message, EntityPlayerMP from) {
		NBTTagCompound nbt = message.getNbt();
		switch (nbt.getInteger("type"))
		{
			case 0:
				modeUpdate(message.getNbt().getInteger("mode"));
				break;
		}
	}
	
	// -------------------------Inventory Methods-----------------------------------
	@Override
	public void onInventoryChange () {
		super.onInventoryChange();
		theCrafter.checkRecipes();
	}
	
	// -------------------------Gui Methods----------------------------------------------------
	@Override
	public int getField (int id) {
		switch (id)
		{
			case 4:
				return mode.ordinal();
			case 5:
				return theCrafter.totalCraftingTime - theCrafter.craftingTime;
			case 6:
				return theCrafter.totalCraftingTime;
			default:
				return super.getField(id);
		}
	}
	
	@Override
	public void setField (int id, int value) {
		switch (id)
		{
			case 4:
				modeUpdate(value);
				NBTTagCompound sendtag = new NBTTagCompound();
				sendtag.setInteger("type", 0);
				sendtag.setInteger("mode", this.mode.ordinal());
				StorehousePacketHandler.INSTANCE.sendToServer(new GuiClientUpdatePacket(this.getPos(), sendtag));
			default:
				super.setField(id, value);
		}
	}
	
	@Override
	public int getFieldCount () {
		return super.getFieldCount() + 3;
	}
	
	// -------------------------Standard TE methods-----------------------------------
	@Override
	public GuiUpdateTEPacket getGUIPacket () {
		GuiUpdateTEPacket packet = super.getGUIPacket();
		packet.getNbt().setInteger("TPMode", mode.ordinal());
		packet.getNbt().setInteger("maxCraftingTime", theCrafter.totalCraftingTime);
		packet.getNbt().setInteger("craftingTime", theCrafter.craftingTime);
		return packet;
	}
	
	@Override
	public void processGUIPacket (GuiUpdateTEPacket packet) {
		super.processGUIPacket(packet);
		modeUpdate(packet.getNbt().getInteger("TPMode"));
		theCrafter.totalCraftingTime = packet.getNbt().getInteger("maxCraftingTime");
		theCrafter.craftingTime = packet.getNbt().getInteger("craftingTime");
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("TPMode", mode.ordinal());
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		modeUpdate(nbt.getInteger("TPMode"));
	}
	
	// ------------------Thermal Press Mode Enum-------------------------------------------------------
	public static enum Mode {
		PRESS (3, new Predicate<ICrafter>() {
			
			@Override
			public boolean test (ICrafter machine) {
				if (machine instanceof TileThermalPress) { return ((TileThermalPress) machine).mode == Mode.PRESS; }
				return false;
			}
		}, new XYCoords(65, 10), new XYCoords(65, 64), new XYCoords(Integer.MIN_VALUE, Integer.MIN_VALUE), new XYCoords(Integer.MIN_VALUE, Integer.MIN_VALUE)),
		JOIN (4, new Predicate<ICrafter>() {
			
			@Override
			public boolean test (ICrafter machine) {
				if (machine instanceof TileThermalPress) { return ((TileThermalPress) machine).mode == Mode.JOIN; }
				return false;
			}
		}, new XYCoords(65, 10), new XYCoords(47, 10), new XYCoords(83, 10), new XYCoords(Integer.MIN_VALUE, Integer.MIN_VALUE)),
		STAMP (2, new Predicate<ICrafter>() {
			
			@Override
			public boolean test (ICrafter machine) {
				if (machine instanceof TileThermalPress) { return ((TileThermalPress) machine).mode == Mode.STAMP; }
				return false;
			}
		}, new XYCoords(65, 10), new XYCoords(Integer.MIN_VALUE, Integer.MIN_VALUE), new XYCoords(Integer.MIN_VALUE, Integer.MIN_VALUE), new XYCoords(Integer.MIN_VALUE, Integer.MIN_VALUE)),
		HIGH_PRESSURE (5, new Predicate<ICrafter>() {
			
			@Override
			public boolean test (ICrafter machine) {
				if (machine instanceof TileThermalPress) { return ((TileThermalPress) machine).mode == Mode.HIGH_PRESSURE; }
				return false;
			}
		}, new XYCoords(47, 10), new XYCoords(83, 10), new XYCoords(47, 64), new XYCoords(83, 64));
		
		public final int craftingSlots;
		public final Set<Integer> orderMattersSlots = Sets.newHashSet(0);
		public final Predicate<ICrafter> modeTest;
		
		private Mode (int craftingSlots, Predicate<ICrafter> modeTest, XYCoords... coords) {
			this.craftingSlots = craftingSlots;
			this.modeTest = modeTest;
			ContainerThermalPress.addPositions(this, coords);
		}
	}
}
