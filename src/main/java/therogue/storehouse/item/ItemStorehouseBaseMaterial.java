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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

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
	public void getSubItems (Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for (int i = 0; i < materials.size(); i++)
		{
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public void preInitClient () {
		for (Entry<Integer, String> material : materials.entrySet())
		{
			ModelBakery.registerItemVariants(this, new ResourceLocation(getUnlocalizedName().substring(5) + "_" + material.getValue()));
		}
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public void InitClient () {
		for (Entry<Integer, String> material : materials.entrySet())
		{
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, material.getKey(), new ModelResourceLocation(getUnlocalizedName().substring(5) + "_" + material.getValue(), "inventory"));
		}
	}
	
	public StorehouseBaseItem setOredictEntry (String oredictEntry, int meta) {
		OreDictionary.registerOre(oredictEntry, new ItemStack(this, 1, meta));
		return this;
	}
}
