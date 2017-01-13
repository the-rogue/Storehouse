/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.reference;

public class MachineStats
{
	public static final int SOLARGENPERTICK = 5;
	public static final int SOLARGENCAPACITY = SOLARGENPERTICK * 3600; // 3 Minutes Worth of capacity
	public static final int SOLARGENSEND = SOLARGENCAPACITY / 400; // 20 Seconds to clear buffer
	
	public static final int SOLIDGENPERTICK = 5;
	public static final int SOLIDGENCAPACITY = SOLIDGENPERTICK * 3600; // 3 Minutes Worth of capacity
	public static final int SOLIDGENSEND = SOLIDGENCAPACITY / 400; // 20 Seconds to clear buffer
	
	public static final int LIQUIDGENPERTICK = 5;
	public static final int LIQUIDGENCAPACITY = LIQUIDGENPERTICK * 3600; // 3 Minutes Worth of capacity
	public static final int LIQUIDGENSEND = LIQUIDGENCAPACITY / 400; // 20 Seconds to clear buffer
}
