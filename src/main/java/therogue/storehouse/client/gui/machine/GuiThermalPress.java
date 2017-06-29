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
import therogue.storehouse.client.gui.element.DoubleProgressBar;
import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.client.gui.element.ElementButton;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ElementVerticalProgressBar;
import therogue.storehouse.client.gui.element.IProgressBar;
import therogue.storehouse.client.gui.element.IconDefinition;
import therogue.storehouse.client.gui.element.JoinProgressBar;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.inventory.IGuiSupplier;
import therogue.storehouse.reference.Gui;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.reference.Icons;
import therogue.storehouse.tile.machine.TileThermalPress;

public class GuiThermalPress extends GuiBase {
	
	public GuiThermalPress (ContainerBase inventory, TileThermalPress linked) {
		super(NORMAL_TEXTURE, inventory);
		elements.add(new ProgressHandler(this, linked, 2, 3, new ElementEnergyBar(8, 8, Icons.EnergyBar.getLocation())));
		IconDefinition[] innerIcons = new IconDefinition[] { new IconDefinition(new ResourceLocation(IDs.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/press_mode.png"), this.xSize - 17, 7, 10, 10),
				new IconDefinition(new ResourceLocation(IDs.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/join_mode.png"), this.xSize - 17, 7, 10, 10),
				new IconDefinition(new ResourceLocation(IDs.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/stamp_mode.png"), this.xSize - 17, 7, 10, 10),
				new IconDefinition(new ResourceLocation(IDs.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/high_pressure_mode.png"), this.xSize - 17, 7, 10, 10), };
		elements.add(new ElementButton(this, new IconDefinition(Icons.Button.getLocation(), this.xSize - 20, 4, 16, 16), "Click to change the mode of the Thermal Press", innerIcons,
				new String[] { "Current Setting: Press", "Current Setting: Join", "Current Setting: Stamp", "Current Setting: High Pressure" }, linked, 4, 4));
		elements.add(createProgressBar(this, linked));
	}
	
	public static ElementBase createProgressBar (GuiBase gui, IGuiSupplier linked) {
		IProgressBar rect1 = new ElementVerticalProgressBar(55, 57, 2, 7, Gui.NORMAL_COLOUR, Gui.WHITE);
		IProgressBar rect2 = new ElementVerticalProgressBar(73, 57, 2, 7, Gui.NORMAL_COLOUR, Gui.WHITE);
		IProgressBar rect3 = new ElementVerticalProgressBar(91, 57, 2, 7, Gui.NORMAL_COLOUR, Gui.WHITE);
		IProgressBar rectCombo1 = new DoubleProgressBar(rect1, rect2);
		IProgressBar rectCombo2 = new DoubleProgressBar(rectCombo1, rect3);
		IProgressBar barConnector = new ElementVerticalProgressBar(55, 55, 38, 2, Gui.NORMAL_COLOUR, Gui.WHITE);
		IProgressBar pointer = new ElementVerticalProgressBar(68, 47, Icons.ProgressUp.getLocation());
		IProgressBar combo1 = new JoinProgressBar(rectCombo2, barConnector);
		IProgressBar combo2 = new JoinProgressBar(combo1, pointer);
		return new ProgressHandler(gui, linked, 5, 6, combo2);
	}
}
