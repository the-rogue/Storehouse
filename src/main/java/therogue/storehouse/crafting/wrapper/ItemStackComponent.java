/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting.wrapper;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.util.ItemUtils;

public class ItemStackComponent implements IRecipeComponent {
	
	public static final ItemStackComponent EMPTY = new ItemStackComponent(ItemStack.EMPTY, false, false);
	private ItemStack input;
	private boolean useMeta;
	private boolean useOreDict;
	private static Map<ItemStack, NonNullList<ItemStack>> oreEntries = new HashMap<ItemStack, NonNullList<ItemStack>>();
	
	public ItemStackComponent (@Nonnull Block input) {
		this(new ItemStack(input));
	}
	
	public ItemStackComponent (@Nonnull Item input) {
		this(new ItemStack(input));
	}
	
	public ItemStackComponent (@Nonnull ItemStack input) {
		this(input, false);
	}
	
	public ItemStackComponent (@Nonnull ItemStack input, boolean useMeta) {
		this(input, useMeta, true);
	}
	
	public ItemStackComponent (@Nonnull ItemStack input, boolean useMeta, boolean useOreDict) {
		this.input = input;
		this.useMeta = useMeta;
		this.useOreDict = useOreDict;
	}
	
	@Override
	public boolean matches (@Nonnull IRecipeWrapper comparison) {
		if (comparison.isUnUsed() || !(comparison instanceof ItemStackWrapper)) return false;
		for (ItemStack test : getOreDictEntries(input, useOreDict))
		{
			if (ItemUtils.areStacksEqual(test, ((ItemStackWrapper) comparison).getStack(), useMeta)) return true;
		}
		return false;
	}
	
	private static NonNullList<ItemStack> getOreDictEntries (ItemStack entry, boolean useOreDict) {
		if (!useOreDict) return NonNullList.create();
		if (oreEntries.get(entry) == null)
		{
			NonNullList<ItemStack> entries = NonNullList.create();
			entries.add(entry);
			for (int i : OreDictionary.getOreIDs(entry))
			{
				entries.addAll(OreDictionary.getOres(OreDictionary.getOreName(i)));
			}
			oreEntries.put(entry, entries);
		}
		return oreEntries.get(entry);
	}
	
	public ItemStack getInput () {
		return input.copy();
	}
	
	@Override
	public IRecipeComponent copy () {
		return new ItemStackComponent(input, useMeta, useOreDict);
	}
	
	@Override
	public IRecipeWrapper getWrapper () {
		return new ItemStackWrapper(input);
	}
	
	@Override
	public IRecipeWrapper getResidue () {
		return IRecipeWrapper.NOTHING;
	}
	
	@Override
	public int getSize () {
		return input.getCount();
	}
	
	public boolean isUnUsed () {
		return input.isEmpty();
	}
	
	@Override
	public String toString () {
		return input.toString() + " (Uses meta: " + useMeta + ", Uses OreDict: " + useOreDict + ") ";
	}
}
