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

public class ElementStandardProgressBar extends ElementProgressBar
{

	public ElementStandardProgressBar(GuiBase gui, int x, int y, Icon icon, IInventory stateChanger, int progressField, int maxProgressField)
	{
		super(gui, x, y, icon, stateChanger, progressField, maxProgressField);
	}

	@Override
	public float getMinU(float progress)
	{
		return 0;
	}

	@Override
	public float getMinV(float progress)
	{
		return 0;
	}

	@Override
	public float getMaxU(float progress)
	{
		return (int)(icon.getIconWidth() * progress);
	}

	@Override
	public float getMaxV(float progress)
	{
		return icon.getIconHeight();
	}

	@Override
	public int getWidth(float progress)
	{
		return (int)(icon.getIconWidth() * progress);
	}

	@Override
	public int getHeight(float progress)
	{
		return icon.getIconHeight();
	}

}
