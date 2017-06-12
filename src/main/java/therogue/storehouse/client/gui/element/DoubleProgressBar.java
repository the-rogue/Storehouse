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

public class DoubleProgressBar implements IProgressBar {
	
	private IProgressBar progressBar1;
	private IProgressBar progressBar2;
	
	public DoubleProgressBar (IProgressBar progressBar1, IProgressBar progressBar2) {
		this.progressBar1 = progressBar1;
		this.progressBar2 = progressBar2;
	}
	
	@Override
	public void drawBottomLayer (GuiBase gui, int mouseX, int mouseY, float progress) {
		progressBar1.drawBottomLayer(gui, mouseX, mouseY, progress);
		progressBar2.drawBottomLayer(gui, mouseX, mouseY, progress);
	}
	
	@Override
	public void drawBar (GuiBase gui, int mouseX, int mouseY, float progress) {
		progressBar1.drawBar(gui, mouseX, mouseY, progress);
		progressBar2.drawBar(gui, mouseX, mouseY, progress);
	}
	
	@Override
	public void drawTopLayer (GuiBase gui, int mouseX, int mouseY, int progress, int maxprogress) {
		progressBar1.drawTopLayer(gui, mouseX, mouseY, progress, maxprogress);
		progressBar2.drawTopLayer(gui, mouseX, mouseY, progress, maxprogress);
	}
	
	public void onClick (GuiBase gui, int mouseX, int mouseY, int mouseButton) {
		progressBar1.onClick(gui, mouseX, mouseY, mouseButton);
		progressBar2.onClick(gui, mouseX, mouseY, mouseButton);
	}
	
	public void onRelease (GuiBase gui, int mouseX, int mouseY, int state) {
		progressBar1.onRelease(gui, mouseX, mouseY, state);
		progressBar2.onRelease(gui, mouseX, mouseY, state);
	}
	
	public boolean isVisible (GuiBase gui) {
		return progressBar1.isVisible(gui) && progressBar2.isVisible(gui);
	}
}
