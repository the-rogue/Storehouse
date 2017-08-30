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
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.Storehouse;
import therogue.storehouse.client.init.ItemRender;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.util.LOG;

public class StorehouseBaseItem extends Item implements IStorehouseBaseItem {
	
	/**
	 * Initiates the item using the specified name
	 */
	public StorehouseBaseItem (String name) {
		super();
		LOG.log("trace", "Creating new StorehouseBaseItem: " + name);
		this.setUnlocalizedName(name);
		this.setRegistryName(General.MOD_ID, name);
		this.setCreativeTab(Storehouse.CREATIVE_TAB);
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName () {
		return String.format("item.%s%s", IDs.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName (ItemStack stack) {
		return String.format("item.%s%s", IDs.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	/**
	 * Useful method to make the code easier to read
	 */
	private String getUnwrappedUnlocalizedName (String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
	
	/**
	 * Gets the raw name as passed to the constructor of this class, useful in various places and also specified in IStorehouseBaseItem.
	 */
	public String getName () {
		return getUnwrappedUnlocalizedName(super.getUnlocalizedName());
	}
	
	/**
	 * Registers this item easily
	 */
	@Override
	public void preInit () {
		GameRegistry.register(this);
	}
	
	/**
	 * Registers the texture for this item easily
	 */
	@Override
	public void InitClient () {
		ItemRender.itemTexture(this);
	}
	
	/**
	 * Registers a name in the Ore Dictionary for this item
	 */
	public StorehouseBaseItem setOredictEntry (String oredictEntry) {
		OreDictionary.registerOre(oredictEntry, this);
		return this;
	}
}
