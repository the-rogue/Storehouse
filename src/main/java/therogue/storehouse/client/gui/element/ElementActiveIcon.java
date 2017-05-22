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
import therogue.storehouse.inventory.IGuiSupplier;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.util.GuiHelper;
import therogue.storehouse.util.TextureHelper;

public class ElementActiveIcon extends ElementBase {
	
	public final ResourceLocation iconLocation;
	public final BufferedImage icon;
	public final int x;
	public final int y;
	public final IGuiSupplier stateChanger;
	public final int activeField;
	
	public ElementActiveIcon (GuiBase gui, int x, int y, ResourceLocation iconLocation, IGuiSupplier stateChanger, int activeField) {
		super(gui);
		this.iconLocation = iconLocation;
		this.icon = TextureHelper.getImageAt(iconLocation);
		this.x = x;
		this.y = y;
		this.stateChanger = stateChanger;
		this.activeField = activeField;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (icon != null)
		{
			TextureHelper.bindTexture(this, iconLocation);
			gui.drawTintedTexturedModalRect(x, y, 0.5F, 0.0F, 1.0F, 1.0F, icon.getWidth() / 2, icon.getHeight(), GuiHelper.getColor(MachineTier.values()[stateChanger.getField(1)]));
			if (stateChanger.getField(activeField) == 1)
			{
				TextureHelper.bindTexture(this, iconLocation);
				gui.drawTexturedModalRect(x, y, 0.0F, 0.0F, 0.5F, 1.0F, icon.getWidth() / 2, icon.getHeight());
			}
		}
	}
}
