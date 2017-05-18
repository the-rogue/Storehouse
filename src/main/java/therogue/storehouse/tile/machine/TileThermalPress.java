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

import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import therogue.storehouse.crafting.CraftingStacks;
import therogue.storehouse.crafting.IRecipeUser;
import therogue.storehouse.crafting.MachineRecipe;
import therogue.storehouse.crafting.MachineRecipe.MatchReturn;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.network.GuiClientUpdatePacket;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.tile.IClientPacketReciever;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.util.GeneralUtils;
import therogue.storehouse.util.ItemUtils;
import therogue.storehouse.util.loghelper;

public class TileThermalPress extends StorehouseBaseMachine implements IClientPacketReciever, IRecipeUser {
	
	public static final List<MachineRecipe> RECIPES = Lists.newArrayList();
	private Mode mode = Mode.PRESS;
	private boolean isCrafting;
	
	public TileThermalPress () {
		super(ModBlocks.thermal_press, MachineTier.advanced);
		inventory = new InventoryManager(this, 8, new int[] { 1, 2, 3, 4, 5 }, new int[] { 0 }) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				return true;
			}
		};
	}
	
	@Override
	public void update () {
	}
	
	public void onInventoryChange () {
		if (isCrafting) return;
		isCrafting = true;
		boolean hascrafted = false;
		for (MachineRecipe recipe : RECIPES)
		{
			if (!ItemUtils.areItemStacksMergableWithLimit(inventory.getInventoryStackLimit(0), recipe.getResult()[0], inventory.getStackInSlot(0))) continue;
			MatchReturn isValidRecipe = recipe.matches(this);
			loghelper.log("info", isValidRecipe.matched);
			loghelper.log("info", isValidRecipe.ingredients);
			if (isValidRecipe.matched)
			{
				setCraftingStacks(recipe.craft(isValidRecipe));
				hascrafted = true;
			}
		}
		isCrafting = false;
		if (hascrafted) onInventoryChange();
	}
	
	@Override
	public int getNumberOrderMattersSlots () {
		return mode.orderMattersSlots;
	}
	
	@Override
	public int getNumberCraftingSlots () {
		return mode.craftingSlots;
	}
	
	public void setCraftingStacks (CraftingStacks stacks) {
		inventory.setInventorySlotContents(0, ItemUtils.mergeStacks(inventory.getInventoryStackLimit(0), true, stacks.output.get(0), inventory.getStackInSlot(0)));
		for (int i = 1; i <= getNumberCraftingSlots(); i++)
		{
			inventory.setInventorySlotContents(i, stacks.inventory.get(i - 1));
		}
	}
	
	@Override
	public CraftingStacks getCraftingStacks () {
		return new CraftingStacks(inventory.getStacksInSlots(1, getNumberCraftingSlots()));
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
	
	public static enum Mode
	{
		PRESS (3, new Predicate<IRecipeUser>() {
			
			@Override
			public boolean test (IRecipeUser machine) {
				if (machine instanceof TileThermalPress) { return ((TileThermalPress) machine).mode == Mode.PRESS; }
				return false;
			}
		}),
		JOIN (4, new Predicate<IRecipeUser>() {
			
			@Override
			public boolean test (IRecipeUser machine) {
				if (machine instanceof TileThermalPress) { return ((TileThermalPress) machine).mode == Mode.JOIN; }
				return false;
			}
		}),
		STAMP (2, new Predicate<IRecipeUser>() {
			
			@Override
			public boolean test (IRecipeUser machine) {
				if (machine instanceof TileThermalPress) { return ((TileThermalPress) machine).mode == Mode.STAMP; }
				return false;
			}
		}),
		HIGH_PRESSURE (5, new Predicate<IRecipeUser>() {
			
			@Override
			public boolean test (IRecipeUser machine) {
				if (machine instanceof TileThermalPress) { return ((TileThermalPress) machine).mode == Mode.HIGH_PRESSURE; }
				return false;
			}
		});
		
		public final int craftingSlots;
		public final int orderMattersSlots = 1;
		public final Predicate<IRecipeUser> modeTest;
		
		private Mode (int craftingSlots, Predicate<IRecipeUser> modeTest) {
			this.craftingSlots = craftingSlots;
			this.modeTest = modeTest;
		}
	}
}
