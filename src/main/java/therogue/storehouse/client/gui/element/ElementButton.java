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

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiHelper;

public class ElementButton extends ElementBase {
	
	public final int x, y, width, height;
	public final ResourceLocation mainIcon;
	public final String commonToolTip;
	public final int innerX, innerY, innerWidth, innerHeight;
	public final ResourceLocation[] innerIcons;
	public final String[] toolTips;
	public final Supplier<Integer> modeSupplier;
	public final Runnable onPress;
	private boolean pressed = false;
	
	public ElementButton (int x, int y, int width, int height, ResourceLocation mainIcon, String commonToolTip, int innerX, int innerY, int innerWidth, int innerHeight, ResourceLocation[] innerIcons, String[] toolTips, Supplier<Integer> modeSupplier, Runnable onPress) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mainIcon = mainIcon;
		this.commonToolTip = commonToolTip;
		this.innerX = innerX;
		this.innerY = innerY;
		this.innerWidth = innerWidth;
		this.innerHeight = innerHeight;
		this.innerIcons = innerIcons;
		this.toolTips = toolTips;
		this.modeSupplier = modeSupplier;
		this.onPress = onPress;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		int currentMode = modeSupplier.get();
		if (!pressed)
		{
			GuiHelper.bindTexture(this, mainIcon);
			gui.drawTexturedModalRect(x, y, 0.0F, 0.0F, 0.5F, 1.0F, width, height);
		}
		else
		{
			GuiHelper.bindTexture(this, mainIcon);
			gui.drawTexturedModalRect(x, y, 0.5F, 0.0F, 1.0F, 1.0F, width, height);
		}
		if (currentMode < innerIcons.length && innerIcons[currentMode] != null)
		{
			ResourceLocation innerIcon = innerIcons[currentMode];
			GuiHelper.bindTexture(this, innerIcon);
			gui.drawTexturedModalRect(innerX, innerY, innerWidth, innerHeight);
		}
	}
	
	@Override
	public void onClick (int mouseX, int mouseY, int mouseButton) {
		if (this.pressed) return;
		if (gui.isPointInGuiRegion(x, y, width, height, mouseX, mouseY))
		{
			this.pressed = true;
			this.onPress.run();
		}
	}
	
	@Override
	public void onRelease (int mouseX, int mouseY, int state) {
		this.pressed = false;
	}
	
	@Override
	public void drawTopLayer (int mouseX, int mouseY) {
		if (gui.isPointInGuiRegion(x, y, width, height, mouseX, mouseY))
		{
			int currentMode = modeSupplier.get();
			if (currentMode < toolTips.length && toolTips[currentMode] != null && !toolTips[currentMode].isEmpty())
			{
				GuiHelper.drawHoveringText(gui.getFontRenderer(), Arrays.asList(new String[] { commonToolTip, toolTips[currentMode] }), x
						+ width, 0, y + height, gui.width, gui.height, -1, gui.getGuiLeft(), gui.getGuiTop(), false, true);
			}
		}
	}
}
