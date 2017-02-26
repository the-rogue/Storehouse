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

import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.multisystem.IGuiItem;

public class ElementGuiItem extends ElementBase
{
	private IGuiItem element;
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	
	public ElementGuiItem(GuiBase gui, int x, int y, int width, int height, IGuiItem element)
	{
		super(gui);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.element = element;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY)
	{
		element.renderIcon(gui, x, y, height, height);
		element.renderText(gui, x, y, width - height);
	}
}
