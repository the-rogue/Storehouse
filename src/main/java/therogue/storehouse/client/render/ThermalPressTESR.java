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

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.particle.SteamParticle;
import therogue.storehouse.reference.General;
import therogue.storehouse.tile.machine.TileThermalPress;

public class ThermalPressTESR extends TileEntitySpecialRenderer<TileThermalPress> {
	
	public void renderTileEntityAt (TileThermalPress te, double x, double y, double z, float partialTicks, int destroyStage) {
		Random random = te.getWorld().rand;
		for (int j1 = 0; j1 < 100; ++j1)
		{
			double d16 = te.getPos().getX() + 0.5D + random.nextGaussian() * 0.2;
			double d19 = te.getPos().getY() + 0.5D + random.nextGaussian() * 0.2;
			double d22 = te.getPos().getZ() + 0.5D + random.nextGaussian() * 0.2;
			double d24 = random.nextGaussian() * 0.01D;
			double d26 = random.nextGaussian() * 0.01D;
			double d27 = random.nextGaussian() * 0.01D;
			Minecraft.getMinecraft().effectRenderer.addEffect(new SteamParticle(te.getWorld(), d16, d19, d22, d24, d26, d27));
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		Tessellator tes = Tessellator.getInstance();
		VertexBuffer vb = tes.getBuffer();
		this.bindTexture(new ResourceLocation(General.MOD_ID, "textures/blocks/machine/storehouse_iron.png"));
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tes.draw();
		GlStateManager.popMatrix();
	}
}
