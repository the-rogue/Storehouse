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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Sets;

import net.minecraft.inventory.Slot;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.Storehouse;
import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.GuiHelper;
import therogue.storehouse.client.gui.GuiHelper.XYCoords;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.crafting.ItemInventory;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.ClientButton;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseMachine;

public class TileThermalPress extends StorehouseBaseMachine {
	
	public static final int RFPerTick = 40;
	private ClientButton<Mode> mode = new ClientButton<Mode>("TPMode", Mode.class, Mode.PRESS);
	protected final MachineCraftingHandler<TileThermalPress>.CraftingManager theCrafter;
	
	public TileThermalPress () {
		super(ModBlocks.thermal_press);
		modules.add(mode);
		this.setInventory(new InventoryManager(this, 6, new Integer[] { 1, 2, 3, 4, 5 }, new Integer[] { 0 }));
		theCrafter = MachineCraftingHandler.getHandler(TileThermalPress.class).newCrafter(this, "", "ITM 0 1", energyStorage).setOrderMattersSlots( () -> mode.getMode().orderMattersSlots);
		theCrafter.setDynamicCraftingInventory( () -> new ItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 1, mode.getMode().craftingSlots
				+ 1));
		modules.add(theCrafter);
		mode.setOnUpdate( () -> theCrafter.checkRecipes());
		energyStorage.setRFPerTick(40);
		inventory.setItemValidForSlotChecks( (index, stack) -> theCrafter.checkItemValidForSlot(index - 1, new ItemStackWrapper(stack)));
		containerFactory = (player) -> {
			ContainerBase container = new ContainerBase(player.inventory, this).setTESlotList(inventory.guiAccess, new int[] { 0, 120, 37, 1, 65, 37, 2, 65, 10, 3, 65, 64, 4, Integer.MIN_VALUE, Integer.MIN_VALUE, 5, Integer.MIN_VALUE, Integer.MIN_VALUE });
			container.getTESlot(3).slotNumber = container.getTESlot(2).slotNumber;
			container.updateProcedure = () -> {
				XYCoords[] positions = slotPositions[mode.getOrdinal()];
				for (int i = 0; i < container.getTESlotSize() - 2; i++)
				{
					Slot s = container.getTESlot(i + 2);
					XYCoords coords = positions[i];
					s.xPos = coords.x;
					s.yPos = coords.y;
					if (s.xPos == Integer.MIN_VALUE && s.yPos == Integer.MIN_VALUE)
					{
						if (container.inventorySlots.contains(s))
						{
							container.inventorySlots.remove(s);
						}
					}
					else
					{
						if (!container.inventorySlots.contains(s))
						{
							container.inventorySlots.add(s);
						}
					}
				}
			};
			container.detectAndSendChanges();
			container.updateProcedure.run();
			return container;
		};
		this.guiFactory = (player) -> {
			GuiBase gui = new GuiBase(GuiBase.NORMAL_TEXTURE, containerFactory.apply(player), this);
			String s = "ENERGYBAR 8 8, ELEMENT_BUTTON %s 4 16 16 %s %s %s 7 10 10 BUTTON BUTTON %s \"Current Setting: Press\" %s \"Current Setting: Join\" %s \"Current Setting: Stamp\" %s \"Current Setting: High Pressure\"";
			List<Object> strElements = new ArrayList<>();
			strElements.add(gui.getXSize() - 20);
			strElements.add(GuiHelper.makeStorehouseLocation("textures/gui/icons/button.png"));
			strElements.add("\"Click to change the mode of the Thermal Press\"");
			strElements.add(gui.getXSize() - 17);
			strElements.add(Storehouse.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/press_mode.png");
			strElements.add(Storehouse.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/join_mode.png");
			strElements.add(Storehouse.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/stamp_mode.png");
			strElements.add(Storehouse.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/high_pressure_mode.png");
			s += ", ELEMENT_MODE BUTTON ";
			s += "PROGRESS_BAR CRFT_TL CRFT_TT J( D( UARROW 67 55 )D( DARROW 67 27 ) )J( J( RBAR 92 44 8 2 )J( RARROW 100 39 ) ) E ";
			s += "PROGRESS_BAR CRFT_TL CRFT_TT J( DARROW 67 27 )J( J( RBAR 92 44 8 2 )J( RARROW 100 39 ) ) E ";
			s += "PROGRESS_BAR CRFT_TL CRFT_TT J( DARROW 67 27 )J( J( RBAR 92 44 8 2 )J( RARROW 100 39 ) ) E ";
			// FINISH HIGH PRESSURE BAR
			s += "PROGRESS_BAR CRFT_TL CRFT_TT J( D( J( J( D( RBAR 65 23 8 2 )D( LBAR 73 23 8 2 ) )J( DBAR 72 25 2 2 ) )J( DARROW 67 27 ) )D( J( J( D( RBAR 65 65 8 2 )D( LBAR 73 65 8 2 ) )J( UBAR 72 63 2 2 ) )J( UARROW 67 55 ) ) )J( J( RBAR 92 44 8 2 )J( RARROW 100 39 ) )";
			ElementFactory.makeElements(gui, gui.elements, this, s, strElements.toArray());
			return gui;
		};
	}
	
	protected static final XYCoords[][] slotPositions = new XYCoords[4][];
	
	// ------------------Thermal Press Mode Enum-------------------------------------------------------
	public static enum Mode {
		PRESS (3, new XYCoords(65, 10), new XYCoords(65, 64), XYCoords.OFF_SCREEN, XYCoords.OFF_SCREEN),
		JOIN (4, new XYCoords(65, 10), new XYCoords(47, 10), new XYCoords(83, 10), XYCoords.OFF_SCREEN),
		STAMP (2, new XYCoords(65, 10), XYCoords.OFF_SCREEN, XYCoords.OFF_SCREEN, XYCoords.OFF_SCREEN),
		HIGH_PRESSURE (5, new XYCoords(47, 10), new XYCoords(83, 10), new XYCoords(47, 64), new XYCoords(83, 64));
		
		public final int craftingSlots;
		public final Set<Integer> orderMattersSlots = Sets.newHashSet(0);
		public final Predicate<ICraftingManager<TileThermalPress>> modeTest;
		
		private Mode (int craftingSlots, XYCoords... coords) {
			this.craftingSlots = craftingSlots;
			this.modeTest = machine -> machine.getAttachedTile() instanceof TileThermalPress ? ((TileThermalPress) machine.getAttachedTile()).mode.getMode() == this : false;
			slotPositions[this.ordinal()] = coords;
		}
	}
}
