/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.init;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.IStorehouseVariantBlock;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.util.LOG;


public class BlockRender
{
	public static void preInit()
	{
		LOG.log("debug", "Registering Block Variants");
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			if (block instanceof IStorehouseVariantBlock) {
				((IStorehouseVariantBlock)block).registervariants();
			}
		}
	}
	/**
	 * Goes through the list of blocks and calls the method within them to register their texture
	 */
	public static void Init()
	{
		LOG.log("debug", "Registering Block Textures");
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			block.registertexture();
		}
	}

	/**
	 * Useful Helper method to register the texture for each block, that most blocks use
	 */
	public static void blockTexture(Block block)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getUnlocalizedName().substring(5), "inventory"));
	}
}
