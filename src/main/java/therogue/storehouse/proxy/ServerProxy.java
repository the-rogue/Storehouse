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


public class ServerProxy extends CommonProxy
{
	/**
	 * Will Initialise all methods the server needs to run, however not used at the moment as there are no server only methods
	 */
	public void init()
	{
		super.init();
		loghelper.log("debug", "Server Proxy Started Initialisation");
		loghelper.log("debug", "Server Proxy Finished Initialisation");
	}
}
