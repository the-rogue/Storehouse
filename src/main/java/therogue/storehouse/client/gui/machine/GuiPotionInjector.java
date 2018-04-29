/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.machine;

import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.tile.machine.TilePotionInjector;

public class GuiPotionInjector extends GuiBase {
	
	public GuiPotionInjector (ContainerBase inventory, TilePotionInjector linked) {
		super(NORMAL_TEXTURE, inventory, linked);
	}
}
