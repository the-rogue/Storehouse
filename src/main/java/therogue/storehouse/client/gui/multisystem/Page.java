/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.multisystem;

import java.util.ArrayList;

import therogue.storehouse.client.gui.element.ElementBase;

public class Page implements IPage
{
	private ArrayList<Runnable> drawQueue = new ArrayList<Runnable>();
	private ArrayList<ElementBase> elementList = new ArrayList<ElementBase>();
	
	@Override
	public void addToDrawQueue(Runnable drawing)
	{
		drawQueue.add(drawing);
	}
	
	@Override
	public void addToElementList(ElementBase element)
	{
		elementList.add(element);
	}
	
	@Override
	public void drawPage(int mouseX, int mouseY)
	{
		for (ElementBase e : elementList) {
			e.drawElement(mouseX, mouseY);
		}
		for (Runnable r : drawQueue) {
			r.run();
		}
		for (ElementBase e : elementList) {
			e.drawTopLayer(mouseX, mouseY);
		}
	}

	@Override
	public IPage copy()
	{
		IPage page = new Page();
		for (Runnable r : drawQueue) {
			page.addToDrawQueue(r);
		}
		for (ElementBase e : elementList) {
			page.addToElementList(e);
		}
		return page;
	}

}
