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

import therogue.storehouse.LOG;
import therogue.storehouse.client.gui.GuiBase;

public class ElementMode extends ElementBase {
	
	private final ElementBase[] elements;
	private ElementBase current;
	public final Supplier<Integer> modeSupplier;
	private long lastCheckTime;
	
	public ElementMode (Supplier<Integer> modeSupplier, ElementBase... elements) {
		this.elements = elements;
		this.current = elements[0];
		this.modeSupplier = modeSupplier;
	}
	
	@Override
	public void setGUI (GuiBase gui) {
		this.gui = gui;
		for (ElementBase e : elements)
		{
			e.setGUI(gui);
		}
	}
	
	public boolean isVisible () {
		updateMode();
		if (current == null) return false;
		return current.isVisible();
	}
	
	public void drawBottomLayer (int mouseX, int mouseY) {
		updateMode();
		if (current == null) return;
		current.drawBottomLayer(mouseX, mouseY);
	}
	
	public void drawElement (int mouseX, int mouseY) {
		updateMode();
		if (current == null) return;
		current.drawElement(mouseX, mouseY);
	}
	
	public void drawTopLayer (int mouseX, int mouseY) {
		updateMode();
		if (current == null) return;
		current.drawTopLayer(mouseX, mouseY);
	}
	
	public void onClick (int mouseX, int mouseY, int mouseButton) {
		updateMode();
		if (current == null) return;
		current.onClick(mouseX, mouseY, mouseButton);
	}
	
	public void onRelease (int mouseX, int mouseY, int state) {
		updateMode();
		if (current == null) return;
		current.onRelease(mouseX, mouseY, state);
	}
	
	private void updateMode () {
		if (System.currentTimeMillis() < lastCheckTime + 50) return;
		lastCheckTime = System.currentTimeMillis();
		try
		{
			current = elements[modeSupplier.get()];
		}
		catch (Exception e)
		{
			LOG.warn("ElementMode failed with Mode: " + modeSupplier.get()
					+ ", Exception Message: "
					+ e.getMessage());
		}
	}
}
