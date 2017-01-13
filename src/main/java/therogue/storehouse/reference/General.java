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

import net.minecraft.util.text.TextFormatting;

public class General
{
	/**
	 * Stores all general values for the mod
	 */
	public static final String MOD_ID = "storehouse";
	public static final String MOD_NAME = "Storehouse Expansion";
	public static final String VERSION = "1.10.2-0.1.0";
	public static final String MC_VERSIONS = "[1.10.2]";
	public static final String FINGERPRINT = "";
	public static final String SERVER_PROXY_CLASS = "therogue.storehouse.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "therogue.storehouse.proxy.ClientProxy";
	public static final String GUI_FACTORY = "therogue.storehouse.client.gui.config.StorehouseGuiFactory";
	
	public static final String SHIFTINFO = TextFormatting.WHITE + "" + TextFormatting.ITALIC + "Hold Shift For More Info";
}
