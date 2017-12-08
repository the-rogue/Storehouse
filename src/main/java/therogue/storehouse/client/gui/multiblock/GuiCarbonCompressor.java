/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.multiblock;

import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.Icons;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.tile.multiblock.TileCarbonCompressor;

public class GuiCarbonCompressor extends GuiBase {
	
	public GuiCarbonCompressor (ContainerBase inventory, TileCarbonCompressor linked) {
		super(NORMAL_TEXTURE, inventory, linked.getGuiID());
		elements.add(new ProgressHandler(this, linked, 2, 3, new ElementEnergyBar(8, 8, Icons.EnergyBar.getLocation())));
	}
}
