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

import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.inventory.IGuiSupplier;

public class ProgressHandler extends ElementBase {
	
	public final IGuiSupplier stateChanger;
	public final int progressField;
	public final int maxProgressField;
	public final IProgressBar drawThing;
	
	public ProgressHandler (GuiBase gui, IGuiSupplier stateChanger, int progressField, int maxProgressField, IProgressBar drawThing) {
		super(gui);
		this.stateChanger = stateChanger;
		this.progressField = progressField;
		this.maxProgressField = maxProgressField;
		this.drawThing = drawThing;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		float progress = (float) stateChanger.getField(progressField) / (stateChanger.getField(maxProgressField) != 0 ? (float) stateChanger.getField(maxProgressField) : 1);
		drawThing.drawBar(this.gui, mouseX, mouseY, progress);
	}
	
	@Override
	public void drawTopLayer (int mouseX, int mouseY) {
		drawThing.drawTopLayer(gui, mouseX, mouseY, stateChanger.getField(progressField), stateChanger.getField(maxProgressField));
	}
	
	public boolean isVisible () {
		return drawThing.isVisible(gui);
	}
	
	public void onClick (int mouseX, int mouseY, int mouseButton) {
		drawThing.onClick(gui, mouseX, mouseY, mouseButton);
	}
	
	public void onRelease (int mouseX, int mouseY, int state) {
		drawThing.onRelease(gui, mouseX, mouseY, state);
	}
}
