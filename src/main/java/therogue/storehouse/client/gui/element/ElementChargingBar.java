/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.element;

import net.minecraft.inventory.IInventory;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.render.icons.Icon;


public class ElementChargingBar extends ElementProgressBar
{

	public ElementChargingBar(GuiBase gui, int x, int y, Icon icon, IInventory stateChanger, int progressField, int maxProgressField)
	{
		super(gui, x, y, icon, stateChanger, progressField, maxProgressField);
	}

	@Override
	public float getMinU(float progress)
	{
		return icon.getMinU();
	}

	@Override
	public float getMinV(float progress)
	{
		return (icon.getMaxV() - icon.getMinV()) * (1 - ((float) getHeight(progress) / (float) icon.getIconHeight())) + icon.getMinV();
	}

	@Override
	public float getMaxU(float progress)
	{
		return icon.getMaxU();
	}

	@Override
	public float getMaxV(float progress)
	{
		return icon.getMaxV();
	}

	@Override
	public int getWidth(float progress)
	{
		return icon.getIconWidth();
	}

	@Override
	public int getHeight(float progress)
	{
		return Math.round(icon.getIconHeight() * progress);
	}
}
