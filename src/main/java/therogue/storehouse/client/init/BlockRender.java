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
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.render.ThermalPressTESR;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.tile.machine.TileThermalPress;

public class BlockRender {
	
	public static void preInit () {
		OBJLoader.INSTANCE.addDomain(General.MOD_ID);
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.stamper), 0, new ModelResourceLocation(IDs.RESOURCENAMEPREFIX + ModBlocks.stamper.getName(), "inventory"));
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			block.preInitClient();
		}
	}
	
	public static void Init () {
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			block.InitClient();
		}
		registerTESRS();
	}
	
	public static void postInit () {
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			block.postInitClient();
		}
	}
	
	/**
	 * Useful Helper method to register the texture for each block, that most blocks use
	 */
	public static void blockTexture (Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getUnlocalizedName().substring(5), "inventory"));
	}
	
	public static void registerTESRS () {
		ClientRegistry.bindTileEntitySpecialRenderer(TileThermalPress.class, new ThermalPressTESR());
		// ClientRegistry.bindTileEntitySpecialRenderer(TileStamper.class, new StamperTESR());
	}
}
