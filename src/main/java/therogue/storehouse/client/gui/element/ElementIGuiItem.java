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

public class ElementIGuiItem extends ElementBase
{
	public IGuiItem item;
	
	public ElementIGuiItem(GuiBase gui, IGuiItem item)
	{
		super(gui);
		this.item = item;
	}

	@Override
	public void drawElement(int mouseX, int mouseY)
	{
		item.buildPage(gui, gui.width, gui.height)[0].drawPage(mouseX, mouseY);
	}
}
