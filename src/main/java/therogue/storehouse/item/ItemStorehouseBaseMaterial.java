/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStorehouseBaseMaterial extends StorehouseBaseItem {
	
	private final Map<Integer, String> materials = new HashMap<Integer, String>();
	
	/**
	 * Constructs a generic item used in crafting
	 */
	public ItemStorehouseBaseMaterial (String name) {
		super(name);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	public ItemStorehouseBaseMaterial addMaterial (int id, String name) {
		materials.put(id, name);
		return this;
	}
	
	@Override
	public int getMetadata (int damage) {
		return damage;
	}
	
	@Override
	public String getUnlocalizedName (ItemStack stack) {
		return super.getUnlocalizedName(stack) + "_" + materials.get(stack.getMetadata());
	}
	
	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	@SideOnly (Side.CLIENT)
	public void getSubItems (CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for (int i = 0; i < materials.size(); i++)
		{
			subItems.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public void registerModels () {
		for (Entry<Integer, String> material : materials.entrySet())
		{
			ModelLoader.setCustomModelResourceLocation(this, material.getKey(), new ModelResourceLocation(getUnlocalizedName().substring(5) + "/" + material.getValue()));
		}
	}
}
