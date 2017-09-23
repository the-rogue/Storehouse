/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import therogue.storehouse.tile.machine.TileForge;

public class ForgeTESR extends TileEntitySpecialRenderer<TileForge> {
	
	public void renderTileEntityAt (TileForge te, double x, double y, double z, float partialTicks, int destroyStage) {
		RenderItem renderitem = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(0.5, 0.5875, 0.5);
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.scale(0.25, 0.25, 0.25);
		RenderHelper.enableStandardItemLighting();
		ItemStack getstack = te.getInventory().getStackInSlot(1).copy();
		renderitem.renderItem(getstack, renderitem.getItemModelMesher().getItemModel(getstack));
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}
