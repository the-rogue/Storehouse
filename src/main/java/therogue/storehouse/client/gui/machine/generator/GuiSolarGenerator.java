/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.machine.generator;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.element.ElementActiveIcon;
import therogue.storehouse.client.gui.element.ElementChargingBar;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.render.icons.StorehouseIcons;
import therogue.storehouse.reference.General;
import therogue.storehouse.tile.generator.TileSolarGenerator;

public class GuiSolarGenerator extends GuiBase
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(General.MOD_ID, "textures/gui/solar_generator_basic.png");

	public GuiSolarGenerator(Container inventorySlotsIn, TileSolarGenerator inventory)
	{
		super(TEXTURE, inventorySlotsIn);
		addElement(new ElementActiveIcon(this, 90, 23, StorehouseIcons.SolarGenOn.getLocation(), inventory, 1));
		addElement(new ElementChargingBar(this, 33, 35, StorehouseIcons.EnergyIndicator.getLocation(), inventory, 2, 3));
		addElement(new ElementEnergyBar(this, 8, 8, inventory, 4, 5));
	}

}
