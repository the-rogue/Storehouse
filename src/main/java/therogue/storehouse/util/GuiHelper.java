/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import java.awt.Color;

import therogue.storehouse.tile.MachineTier;


public class GuiHelper
{
	public static Color getColor(MachineTier tier){
		switch (tier) {
			case basic:
				return new Color(106, 78, 45);
			case advanced:
				return new Color(116, 116, 116);
			case ender:
				return new Color(197, 215, 195);
			case infused:
				return new Color(182, 182, 182);
			case ultimate:
				return new Color(144, 142, 151);
			default:
				return new Color(255, 255, 255);
		}
	}
}
