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

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.util.TextureHelper;

public abstract class ElementProgressBar implements IProgressBar {
	
	public final BufferedImage icon;
	public final ResourceLocation iconLocation;
	public final int x;
	public final int y;
	
	public ElementProgressBar (int x, int y, ResourceLocation iconLocation) {
		this.iconLocation = iconLocation;
		this.icon = TextureHelper.getImageAt(iconLocation);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void drawBar (GuiBase gui, int mouseX, int mouseY, float progress) {
		if (icon == null) return;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		TextureHelper.bindTexture(gui, iconLocation);
		gui.drawTexturedModalRect(x, y, 0.5F, 0.0F, 1.0F, 1.0F, icon.getWidth() / 2, icon.getHeight());
		TextureHelper.bindTexture(gui, iconLocation);
		gui.drawTexturedModalRect(x + (icon.getWidth() - getWidth(progress)) / 2, y + icon.getHeight() - getHeight(progress), getMinU(progress) / 2, getMinV(progress), getMaxU(progress) / 2, getMaxV(progress), getWidth(progress) / 2, getHeight(progress));
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
