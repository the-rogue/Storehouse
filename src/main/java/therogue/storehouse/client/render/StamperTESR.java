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

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.reference.General;
import therogue.storehouse.tile.machine.TileStamper;

public class StamperTESR extends TileEntitySpecialRenderer<TileStamper> {
	
	public void renderTileEntityAt (TileStamper te, double x, double y, double z, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		Tessellator tes = Tessellator.getInstance();
		VertexBuffer vb = tes.getBuffer();
		this.bindTexture(new ResourceLocation(General.MOD_ID, "textures/blocks/machine/storehouse_iron.png"));
		vb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
		vb.pos(0.375, 0, 0).tex(0.625, 0.5);
		vb.pos(0.375, 16, 0).tex(0.625, 0);
		vb.pos(0.625, 16, 0).tex(u, v)
		tes.draw();
		GlStateManager.popMatrix();
	}
}
