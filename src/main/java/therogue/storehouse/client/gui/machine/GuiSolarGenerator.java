/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.machine;

import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.TierIcons;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.TileData.CapabilityDataHandler;
import therogue.storehouse.tile.machine.TileSolarGenerator;

public class GuiSolarGenerator extends GuiBase {
	
	public GuiSolarGenerator (ContainerBase inventory, TileSolarGenerator linked) {
		super(linked.tier.guiLocation, inventory, linked);
		int tier = linked.getCapability(CapabilityDataHandler.DATAHANDLER, null, ModuleContext.GUI).getField(0);
		String s = "ENERGYBAR 8 8 %s,  ACTIVE_ICON 90 23 %s SOLARGEN_ON,  PROGRESS_BAR ITEM_CHARGE0 ITEM_MAXCHARGE0 RIGHT_PB 33 35 %s";
		Object[] sP = new Object[] { TierIcons.EnergyBar.getLocation(tier), TierIcons.SolarGenOn.getLocation(tier), TierIcons.EnergyIndicator.getLocation(tier) };
		ElementFactory.makeElements(this, elements, linked, s, sP);
	}
}
