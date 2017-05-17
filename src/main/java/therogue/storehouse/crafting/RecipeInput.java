/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.util.ItemUtils;

public class RecipeInput {
	
	private ItemStack input;
	private boolean useMeta;
	private boolean useOreDict;
	private NonNullList<ItemStack> oreEntries;
	
	public RecipeInput (ItemStack input) {
		this(input, false);
	}
	
	public RecipeInput (ItemStack input, boolean useMeta) {
		this(input, useMeta, true);
	}
	
	public RecipeInput (ItemStack input, boolean useMeta, boolean useOreDict) {
		this.input = input;
		this.useMeta = useMeta;
		this.useOreDict = useOreDict;
	}
	
	public boolean matches (ItemStack comparison) {
		if (comparison.isEmpty()) return false;
		for (ItemStack test : getOreDictEntries())
		{
			ItemUtils.areItemStacksEqual(test, comparison, useMeta);
		}
		return false;
	}
	
	private NonNullList<ItemStack> getOreDictEntries () {
		if (oreEntries == null)
		{
			oreEntries = NonNullList.create();
			oreEntries.add(input);
			if (useOreDict)
			{
				for (int i : OreDictionary.getOreIDs(input))
				{
					oreEntries.addAll(OreDictionary.getOres(OreDictionary.getOreName(i)));
				}
			}
		}
		return oreEntries;
	}
}
