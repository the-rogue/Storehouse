/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.element;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;
import therogue.storehouse.client.gui.GuiBase;

public class ElementFadingProgressBar implements IProgressBar {
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected Color becomeColour;
	
	public ElementFadingProgressBar (int x, int y, int width, int height, Color becomeColour) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.becomeColour = becomeColour;
	}
	
	@Override
	public int getNumberOfPixels () {
		return (int) Math.round(Math.sqrt(height * width));
	}
	
	@Override
	public void drawBottomLayer (GuiBase gui, int mouseX, int mouseY, float progress) {
		GlStateManager.color(becomeColour.getRed() / 255.0F, becomeColour.getGreen() / 255.0F, becomeColour.getBlue() / 255.0F, progress);
	}
	
	@Override
	public void drawBar (GuiBase gui, int mouseX, int mouseY, float progress) {
		GlStateManager.color(becomeColour.getRed() / 255.0F, becomeColour.getGreen() / 255.0F, becomeColour.getBlue() / 255.0F, progress);
	}
}
