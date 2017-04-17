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
import therogue.storehouse.util.RGBAColor;

public class ElementCraftingGrid extends ElementBase {
	
	private int left;
	private int top;
	
	public ElementCraftingGrid (GuiBase gui, int left, int top) {
		super(gui);
		this.left = left;
		this.top = top;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		final RGBAColor black = new RGBAColor(0.0F, 0.0F, 0.0F, 1.0F);
		GuiBase.drawRect(left, top, left + 1, top + 52, black);
		GuiBase.drawRect(left + 51, top, left + 52, top + 52, black);
		GuiBase.drawRect(left + 1, top, left + 51, top + 1, black);
		GuiBase.drawRect(left + 1, top + 51, left + 51, top + 52, black);
		GuiBase.drawRect(left + 17, top + 1, left + 18, top + 51, black);
		GuiBase.drawRect(left + 34, top + 1, left + 35, top + 51, black);
		GuiBase.drawRect(left + 1, top + 17, left + 51, top + 18, black);
		GuiBase.drawRect(left + 1, top + 34, left + 51, top + 35, black);
	}
}
