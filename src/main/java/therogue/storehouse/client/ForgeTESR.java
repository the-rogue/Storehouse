/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.machine.TileForge;

public class ForgeTESR extends TileEntitySpecialRenderer<TileForge> {
	
	public void renderTileEntityAt (TileForge te, double x, double y, double z, float partialTicks, int destroyStage) {
		IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.GUI);
		if (inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(1).isEmpty()) return;
		RenderItem renderitem = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate(x + 0.5, y + 0.5875, z + 0.5);
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.scale(0.25, 0.25, 0.25);
		RenderHelper.enableStandardItemLighting();
		ItemStack getstack = inventory.getStackInSlot(0);
		if (getstack.isEmpty()) getstack = inventory.getStackInSlot(1);
		if (!getstack.isEmpty())
		{
			renderitem.renderItem(getstack, ItemCameraTransforms.TransformType.FIXED);
		}
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
}
