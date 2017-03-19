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
import java.util.HashMap;

import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.util.loghelper;

public class Page {
	
	private HashMap<Integer, ArrayList<ElementBase>> pages = new HashMap<Integer, ArrayList<ElementBase>>();
	private int pageNumber;
	
	public Page (int number) {
		for (int i = 1; i < number + 1; i++)
		{
			pages.put(i, new ArrayList<ElementBase>());
		}
		this.pageNumber = number;
	}
	
	public void addElement (int pageNumber, ElementBase element) {
		ArrayList<ElementBase> list = pages.get(pageNumber);
		if (list == null)
		{
			loghelper.log("warn", "Page: " + pageNumber + ", does not exist, when adding element ");
			return;
		}
		list.add(element);
	}
	
	public void addPage () {
		pages.put(++pageNumber, new ArrayList<ElementBase>());
	}
	
	public int getNumberOfPages () {
		return pageNumber;
	}
	
	public void drawPage (int pageNumber, int mouseX, int mouseY) {
		ArrayList<ElementBase> list = pages.get(pageNumber);
		if (list == null)
		{
			loghelper.log("warn", "Page: " + pageNumber + ", does not exist, when drawing page");
			return;
		}
		for (ElementBase e : list)
		{
			e.drawElement(mouseX, mouseY);
		}
		for (ElementBase e : list)
		{
			e.drawTopLayer(mouseX, mouseY);
		}
	}
	
	public void joinPages (Page endSet) {
		for (int i = 1; i <= endSet.pages.size(); i++)
		{
			pages.put(pageNumber + i, endSet.pages.get(i));
		}
		pageNumber += endSet.pages.size();
	}
}
