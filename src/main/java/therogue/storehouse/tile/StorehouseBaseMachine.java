/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile;

import java.util.function.Function;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.IGuiSupplier;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.inventory.InventoryManager;

public abstract class StorehouseBaseMachine extends StorehouseBaseTileEntity implements IGuiSupplier {
	
	protected InventoryManager inventory;
	protected TileEnergyStorage energyStorage = new TileEnergyStorage(this, 8000, 100, 0);
	protected Function<EntityPlayer, ContainerBase> containerFactory = (player) -> new ContainerBase(player.inventory, this);
	protected Function<EntityPlayer, GuiScreen> guiFactory = (player) -> new GuiBase(GuiBase.NORMAL_TEXTURE, containerFactory.apply(player), this);
	
	public StorehouseBaseMachine (IStorehouseBaseBlock block) {
		super(block);
		modules.add(energyStorage);
	}
	
	// -------------------------Inventory Methods-----------------------------------
	protected void setInventory (InventoryManager inventory) {
		this.inventory = inventory;
		modules.add(inventory);
	}
	
	protected void setEnergyStorage (TileEnergyStorage energy) {
		this.modules.remove(energyStorage);
		this.energyStorage = energy;
		this.modules.add(energyStorage);
	}
	
	// -------------------------Container/Gui Methods-----------------------------------
	@Override
	public String getGuiName () {
		return block.getUnlocalizedName(ItemStack.EMPTY) + ".name";
	}
	
	protected void setElementString (String elements, Object... args) {
		guiFactory = (player) -> {
			GuiBase gui = new GuiBase(GuiBase.NORMAL_TEXTURE, containerFactory.apply(player), this);
			ElementFactory.makeElements(gui, gui.elements, this, elements, args);
			return gui;
		};
	}
	
	@Override
	public GuiScreen getGUI (EntityPlayer player) {
		return guiFactory.apply(player);
	}
	
	@Override
	public Container getContainer (EntityPlayer player) {
		return containerFactory.apply(player);
	}
}
