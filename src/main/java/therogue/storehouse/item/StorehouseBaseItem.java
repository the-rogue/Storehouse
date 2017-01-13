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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.client.render.items.ItemRender;
import therogue.storehouse.core.StorehouseCreativeTab;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.Identification;
import therogue.storehouse.util.loghelper;


public class StorehouseBaseItem extends Item implements IStorehouseBaseItem
{
	/**
	 * Initiates the item using the specified name
	 */
	public StorehouseBaseItem(String name)
	{
		super();
		loghelper.log("trace", "Creating new StorehouseBaseItem: " + name);
		this.setUnlocalizedName(name);
		this.setRegistryName(General.MOD_ID, name);
		this.setCreativeTab(StorehouseCreativeTab.CREATIVE_TAB);
	}

	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName()
	{
		return String.format("item.%s%s", Identification.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return String.format("item.%s%s", Identification.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	/**
	 * Useful method to make the code easier to read
	 */
	private String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}

	/**
	 * Gets the raw name as passed to the constructor of this class, useful in various places and also specified in IStorehouseBaseItem.
	 */
	public String getName()
	{
		return getUnwrappedUnlocalizedName(super.getUnlocalizedName());
	}

	/**
	 * Registers this item easily
	 */
	public void registeritem()
	{
		loghelper.log("trace", "Registering StorehouseBaseItem: " + getName());
		GameRegistry.register(this);
	}

	/**
	 * Registers the texture for this item easily
	 */
	@Override
	public void registertexture()
	{
		loghelper.log("trace", "Registering StorehouseBaseItem Texture: " + getName());
		ItemRender.itemTexture(this);
	}
}
