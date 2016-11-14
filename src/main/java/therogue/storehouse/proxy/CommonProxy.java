/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.proxy;

import therogue.storehouse.util.loghelper;


public abstract class CommonProxy implements IProxy
{
	/**
	 * Will Initialise all methods that are common, however not used at the moment as they are run in the Mod class
	 */
	public void init()
	{
		loghelper.log("debug", "Common Proxy Started Initialisation");
		loghelper.log("debug", "Common Proxy Finished Initialisation");
	}
}
