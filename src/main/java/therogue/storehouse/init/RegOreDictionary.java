/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.init;

import java.util.LinkedList;


public class RegOreDictionary
{
	/**
	 * Creates a list of the Ore Dictionary Entries I am using
	 */
	public static LinkedList<String> oreDictEntries = new LinkedList<String>();
	/**
	 * Adds all the entries to the list
	 */
	static
	{
		oreDictEntries.add(0, "blockazuritecrystal");
	}

	/**
	 * Sets Ore Dictionary Entries for various blocks
	 */
	public static void init()
	{
		ModBlocks.azurite_crystal_block.setOredictEntry(oreDictEntries.get(0));
		ModBlocks.azurite_crystal_block_chiseled.setOredictEntry(oreDictEntries.get(0));
		ModBlocks.azurite_crystal_block_pillar.setOredictEntry(oreDictEntries.get(0));
	}
}
