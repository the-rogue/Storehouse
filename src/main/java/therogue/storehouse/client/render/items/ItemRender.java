/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.item.IStorehouseBaseItem;
import therogue.storehouse.util.loghelper;


public class ItemRender
{
	/**
	 * Goes through the list of blocks and calls the method within them to register their texture
	 */
	public static void Init()
	{
		loghelper.log("debug", "Registering Item Textures");
		for (IStorehouseBaseItem item : ModItems.itemlist)
		{
			item.registertexture();
		}
	}

	/**
	 * Useful Helper method to register the texture for each item, that all the items use
	 */
	public static void itemTexture(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getUnlocalizedName().substring(5), "inventory"));
	}
}
