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

import java.util.function.Supplier;

import therogue.storehouse.client.gui.GuiBase;

public class ProgressHandler extends ElementBase {
	
	public final Supplier<Integer> progressSupplier;
	public final Supplier<Integer> maxProgressSupplier;
	public final IProgressBar drawThing;
	
	public ProgressHandler (GuiBase gui, Supplier<Integer> progressSupplier, Supplier<Integer> maxProgressSupplier, IProgressBar drawThing) {
		super(gui);
		this.progressSupplier = progressSupplier;
		this.maxProgressSupplier = maxProgressSupplier;
		this.drawThing = drawThing;
	}
	
	@Override
	public void drawBottomLayer (int mouseX, int mouseY) {
		float progress = (float) progressSupplier.get() / (maxProgressSupplier.get() != 0 ? (float) maxProgressSupplier.get() : 1.0F);
		drawThing.drawBottomLayer(gui, mouseX, mouseY, progress);
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		float progress = (float) progressSupplier.get() / (maxProgressSupplier.get() != 0 ? (float) maxProgressSupplier.get() : 1.0F);
		// LOG.info(progress);
		drawThing.drawBar(this.gui, mouseX, mouseY, progress);
	}
	
	@Override
	public void drawTopLayer (int mouseX, int mouseY) {
		drawThing.drawTopLayer(gui, mouseX, mouseY, progressSupplier.get(), maxProgressSupplier.get());
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
