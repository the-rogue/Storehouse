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

import net.minecraft.util.text.TextFormatting;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.reference.Icons;
import therogue.storehouse.util.GuiHelper;
import therogue.storehouse.util.TextureHelper;

public class ElementEnergyBar extends ElementProgressBar {
	
	public ElementEnergyBar (int x, int y) {
		super(x, y, Icons.EnergyBar.getLocation());
	}
	
	@Override
	public float getMinU (float progress) {
		return 0;
	}
	
	@Override
	public float getMinV (float progress) {
		return 1 - TextureHelper.scalePercentageToLength(icon.getHeight(), progress);
	}
	
	@Override
	public float getMaxU (float progress) {
		return 1;
	}
	
	@Override
	public float getMaxV (float progress) {
		return 1;
	}
	
	@Override
	public int getWidth (float progress) {
		return icon.getWidth();
	}
	
	@Override
	public int getHeight (float progress) {
		return TextureHelper.calculateLength(icon.getHeight(), progress);
	}
	
	@Override
	public void drawTopLayer (GuiBase gui, int mouseX, int mouseY, int progress, int maxProgress) {
		if (icon == null) return;
		if (gui.isPointInGuiRegion(this.x, this.y, this.icon.getWidth() / 2, this.icon.getHeight(), mouseX, mouseY))
		{
			ArrayList<String> textLines = new ArrayList<String>();
			// TODO Insert Commas
			textLines.add(TextFormatting.RED + "" + progress + " RF /");
			textLines.add(TextFormatting.RED + "" + maxProgress + " RF");
			GuiHelper.drawHoveringText(gui.getFontRenderer(), textLines, 0, 1, 0, gui.width, gui.height, -1, gui.getGuiLeft(), gui.getGuiTop(), true, false);
		}
	}
}
