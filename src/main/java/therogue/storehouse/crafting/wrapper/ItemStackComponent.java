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
import therogue.storehouse.crafting.wrapper.IRecipeComponent.IItemComponent;
import therogue.storehouse.util.ItemStackUtils;

public class ItemStackComponent implements IItemComponent {
	
	public static final ItemStackComponent EMPTY = new ItemStackComponent(ItemStack.EMPTY, false, false);
	protected ItemStack input;
	protected boolean useMeta;
	protected boolean useOreDict;
	protected static Map<ItemStack, NonNullList<ItemStack>> oreEntries = new HashMap<ItemStack, NonNullList<ItemStack>>();
	
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
	
	public static ItemStackComponent[] convert (ItemStack[] stacks) {
		return convert(stacks, true);
	}
	
	public static ItemStackComponent[] convert (ItemStack[] stacks, boolean useMeta) {
		return convert(stacks, useMeta, true);
	}
	
	public static ItemStackComponent[] convert (ItemStack[] stacks, boolean useMeta, boolean useOreDict) {
		ItemStackComponent[] components = new ItemStackComponent[stacks.length];
		for (int i = 0; i < stacks.length; i++)
		{
			components[i] = new ItemStackComponent(stacks[i], useMeta, useOreDict);
		}
		return components;
	}
	
	@Override
	public boolean matches (@Nonnull IRecipeWrapper comparison) {
		if (comparison.isUnUsed() || !(comparison instanceof ItemStackWrapper)) return false;
		for (ItemStack test : getOreDictEntries(input, useOreDict))
		{
			if (ItemStackUtils.areStacksEqual(test, ((ItemStackWrapper) comparison).getStack(), useMeta)
					&& ((ItemStackWrapper) comparison).getStack().getCount() >= test.getCount())
				return true;
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
			int isc = entry.getCount();
			for (ItemStack is : entries)
			{
				is.setCount(isc);
			}
			oreEntries.put(entry, entries);
		}
		return oreEntries.get(entry);
	}
	
	@Override
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
	
	public static class ItemStackRemainComponent extends ItemStackComponent {
		
		public ItemStackRemainComponent (@Nonnull Block input) {
			this(new ItemStack(input));
		}
		
		public ItemStackRemainComponent (@Nonnull Item input) {
			this(new ItemStack(input));
		}
		
		public ItemStackRemainComponent (@Nonnull ItemStack input) {
			this(input, false);
		}
		
		public ItemStackRemainComponent (@Nonnull ItemStack input, boolean useMeta) {
			this(input, useMeta, true);
		}
		
		public ItemStackRemainComponent (@Nonnull ItemStack input, boolean useMeta, boolean useOreDict) {
			super(input, useMeta, useOreDict);
		}
		
		@Override
		public IRecipeWrapper getResidue () {
			return new ItemStackWrapper(this.input);
		}
	}
}
