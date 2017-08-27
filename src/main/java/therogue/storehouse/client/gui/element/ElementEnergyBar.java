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

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.util.GuiHelper;
import therogue.storehouse.util.TextureHelper;

public class ElementEnergyBar extends ElementProgressBar {
	
	public ElementEnergyBar (int x, int y, ResourceLocation location) {
		super(x, y, location);
	}
	
	@Override
	public int getNumberOfPixels () {
		return height;
	}
	
	@Override
	public float getMinV (float progress) {
		return 1 - TextureHelper.scalePercentageToLength(height, progress);
	}
	
	@Override
	public int getY (float progress) {
		return y + height - getHeight(progress);
	}
	
	@Override
	public int getHeight (float progress) {
		return TextureHelper.calculateLength(height, progress);
	}
	
	@Override
	public void drawTopLayer (GuiBase gui, int mouseX, int mouseY, int progress, int maxProgress) {
		if (gui.isPointInGuiRegion(this.x, this.y, width, height, mouseX, mouseY))
		{
			ArrayList<String> textLines = new ArrayList<String>();
			// TODO Insert Commas
			textLines.add(TextFormatting.RED + "" + progress + " RF /");
			textLines.add(TextFormatting.RED + "" + maxProgress + " RF");
			GuiHelper.drawHoveringText(gui.getFontRenderer(), textLines, 0, 1, 0, gui.width, gui.height, -1, gui.getGuiLeft(), gui.getGuiTop(), true, false);
		}
	}
}
