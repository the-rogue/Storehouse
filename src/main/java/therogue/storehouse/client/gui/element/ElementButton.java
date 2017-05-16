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

import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.client.config.GuiUtils;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.util.TextureHelper;

public class ElementButton extends ElementBase {
	
	public final IconDefinition mainIcon;
	public final String commonToolTip;
	public final IconDefinition[] innerIcons;
	public final String[] toolTips;
	public final IInventory stateChanger;
	public final int modeField;
	public final int numModes;
	private boolean pressed = false;
	
	public ElementButton (GuiBase gui, IconDefinition mainIcon, String commonToolTip, IconDefinition[] innerIcons, String[] toolTips, IInventory stateChanger, int modeField, int numModes) {
		super(gui);
		this.mainIcon = mainIcon;
		this.commonToolTip = commonToolTip;
		this.innerIcons = innerIcons;
		this.toolTips = toolTips;
		this.stateChanger = stateChanger;
		this.modeField = modeField;
		this.numModes = numModes;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		int currentMode = stateChanger.getField(modeField);
		if (!pressed)
		{
			TextureHelper.bindTexture(this, mainIcon.icon);
			gui.drawTexturedModalRect(mainIcon.x, mainIcon.y, 0.0F, 0.0F, 0.5F, 1.0F, mainIcon.width, mainIcon.height);
		}
		else
		{
			TextureHelper.bindTexture(this, mainIcon.icon);
			gui.drawTexturedModalRect(mainIcon.x, mainIcon.y, 0.5F, 0.0F, 1.0F, 1.0F, mainIcon.width, mainIcon.height);
		}
		if (currentMode < innerIcons.length && innerIcons[currentMode] != null)
		{
			IconDefinition innerIcon = innerIcons[currentMode];
			TextureHelper.bindTexture(this, innerIcon.icon);
			gui.drawTexturedModalRect(innerIcon.x, innerIcon.y, innerIcon.width, innerIcon.height);
		}
	}
	
	@Override
	public void onClick (int mouseX, int mouseY, int mouseButton) {
		int currentMode = stateChanger.getField(modeField);
		if (this.pressed) return;
		if (gui.isPointInGuiRegion(mainIcon.x, mainIcon.y, mainIcon.width, mainIcon.height, mouseX, mouseY))
		{
			this.pressed = true;
			if (currentMode > numModes)
			{
				stateChanger.setField(modeField, 0);
			}
			else
			{
				stateChanger.setField(modeField, currentMode + 1);
			}
		}
	}
	
	@Override
	public void onRelease (int mouseX, int mouseY, int state) {
		this.pressed = false;
	}
	
	@Override
	public void drawTopLayer (int mouseX, int mouseY) {
		if (gui.isPointInGuiRegion(mainIcon.x, mainIcon.y, mainIcon.width, mainIcon.height, mouseX, mouseY))
		{
			int currentMode = stateChanger.getField(modeField);
			if (currentMode < toolTips.length && toolTips[currentMode] != null && !toolTips[currentMode].isEmpty())
			{
				GuiUtils.drawHoveringText(Arrays.asList(new String[] { commonToolTip, toolTips[currentMode] }), mainIcon.x + mainIcon.width, mainIcon.y + mainIcon.height, gui.width, gui.height, gui.width - mainIcon.x + mainIcon.width - 2, gui.getFontRenderer());
			}
		}
	}
}
