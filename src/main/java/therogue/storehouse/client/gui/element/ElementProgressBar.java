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

import java.awt.image.BufferedImage;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.util.TextureHelper;

public abstract class ElementProgressBar implements IProgressBar {
	
	protected ResourceLocation iconLocation;
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	protected int foregroundColour = 0;
	protected int backgroundColour = 0;
	
	public ElementProgressBar (int x, int y, ResourceLocation iconLocation) {
		this.iconLocation = iconLocation;
		BufferedImage icon = TextureHelper.getImageAt(iconLocation);
		this.x = x;
		this.y = y;
		if (icon != null)
		{
			this.width = icon.getWidth();
			this.height = icon.getHeight();
		}
		else
		{
			this.width = this.height = 0;
		}
	}
	
	public ElementProgressBar (int x, int y, int width, int height, int foregroundColour, int backgroundColour) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.foregroundColour = foregroundColour;
		this.backgroundColour = backgroundColour;
	}
	
	@Override
	public void drawBar (GuiBase gui, int mouseX, int mouseY, float progress) {
		if (iconLocation != null)
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			TextureHelper.bindTexture(gui, iconLocation);
			gui.drawTexturedModalRect(x, y, 0.5F, 0.0F, 1.0F, 1.0F, width / 2, height);
			TextureHelper.bindTexture(gui, iconLocation);
			gui.drawTexturedModalRect(x + (width - getWidth(progress)) / 2, y + height - getHeight(progress), getMinU(progress) / 2, getMinV(progress), getMaxU(progress) / 2, getMaxV(progress), getWidth(progress) / 2, getHeight(progress));
		}
		else
		{
			Gui.drawRect(x, y, x + width, y + height, backgroundColour);
			Gui.drawRect(x + getWidth(progress), y + getHeight(progress), x + getWidth(progress), y + getHeight(progress), foregroundColour);
		}
	}
	
	public abstract float getMinU (float progress);
	
	public abstract float getMinV (float progress);
	
	public abstract float getMaxU (float progress);
	
	public abstract float getMaxV (float progress);
	
	public abstract int getWidth (float progress);
	
	public abstract int getHeight (float progress);
	
	@Override
	public void drawTopLayer (GuiBase gui, int mouseX, int mouseY, int progress, int maxprogress) {
	}
}
