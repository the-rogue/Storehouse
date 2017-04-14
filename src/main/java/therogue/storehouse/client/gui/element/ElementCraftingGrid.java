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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.util.loghelper;

public class ElementCraftingGrid extends ElementBase {
	
	private int left;
	private int top;
	
	public ElementCraftingGrid (GuiBase gui, int left, int top) {
		super(gui);
		loghelper.log("info", "creating elementcraftinggrid left: " + left + ", top: " + top);
		this.left = left;
		this.top = top;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		loghelper.logSetTimes("drawcraftinggrid", 2, "drawcraftinggrid");
		RenderHelper.enableGUIStandardItemLighting();
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(ModItems.azurite_dust, 1, 0), left, top);
		RenderHelper.disableStandardItemLighting();
		Gui.drawRect(left + 52, top + 52, left + 72, top + 72, 0);
		Gui.drawRect(left, top, left + 1, top + 52, 0);
		Gui.drawRect(left + 1, top, left + 50, top + 1, 0);
		Gui.drawRect(left + 51, top, left + 52, top + 52, 0);
		Gui.drawRect(left + 1, top + 51, left + 50, top + 52, 0);
		Gui.drawRect(left + 17, top + 1, left + 18, top + 51, 0);
		Gui.drawRect(left + 34, top + 1, left + 35, top + 51, 0);
		Gui.drawRect(left + 1, top + 17, left + 51, top + 18, 0);
		Gui.drawRect(left + 1, top + 34, left + 51, top + 35, 0);
	}
}
