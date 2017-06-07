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

import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.element.ElementButton;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.IconDefinition;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.reference.Icons;
import therogue.storehouse.tile.machine.TileThermalPress;

public class GuiThermalPress extends GuiBase {
	
	public GuiThermalPress (ContainerBase inventory, TileThermalPress linked) {
		super(linked, inventory);
		elements.add(new ProgressHandler(this, linked, 2, 3, new ElementEnergyBar(8, 8)));
		IconDefinition[] innerIcons = new IconDefinition[] { new IconDefinition(new ResourceLocation("textures/gui/thermalpress/press_mode.png"), this.xSize - 17, 7, 10, 10), new IconDefinition(new ResourceLocation("textures/gui/thermalpress/join_mode.png"), this.xSize - 17, 7, 10, 10),
				new IconDefinition(new ResourceLocation("textures/gui/thermalpress/stamp_mode.png"), this.xSize - 17, 7, 10, 10), new IconDefinition(new ResourceLocation("textures/gui/thermalpress/high_pressure_mode.png"), this.xSize - 17, 7, 10, 10), };
		elements.add(new ElementButton(this, new IconDefinition(Icons.Button.getLocation(), this.xSize - 20, 4, 16, 16), "Click to change the mode of the Thermal Press", innerIcons,
				new String[] { "Current Setting: Press", "Current Setting: Join", "Current Setting: Stamp", "Current Setting: High Pressure" }, linked, 4, 4));
	}
}
