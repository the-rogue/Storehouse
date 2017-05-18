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

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.util.ItemUtils;

public class RecipeInput {
	
	public static final RecipeInput EMPTY = new RecipeInput(ItemStack.EMPTY, false, false);
	private ItemStack input;
	private boolean useMeta;
	private boolean useOreDict;
	private NonNullList<ItemStack> oreEntries;
	
	public RecipeInput (@Nonnull Block input) {
		this(new ItemStack(input));
	}
	
	public RecipeInput (@Nonnull Item input) {
		this(new ItemStack(input));
	}
	
	public RecipeInput (@Nonnull ItemStack input) {
		this(input, false);
	}
	
	public RecipeInput (@Nonnull ItemStack input, boolean useMeta) {
		this(input, useMeta, true);
	}
	
	public RecipeInput (@Nonnull ItemStack input, boolean useMeta, boolean useOreDict) {
		this.input = input;
		this.useMeta = useMeta;
		this.useOreDict = useOreDict;
	}
	
	public boolean matches (@Nonnull ItemStack comparison) {
		if (comparison.isEmpty()) return false;
		for (ItemStack test : getOreDictEntries())
		{
			if (ItemUtils.areItemStacksEqual(test, comparison, useMeta)) return true;
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
	
	public ItemStack getInput () {
		return input.copy();
	}
	
	public boolean isEmpty () {
		return input.isEmpty();
	}
}