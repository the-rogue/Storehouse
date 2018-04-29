/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.machine;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.Icons;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.machine.TilePotionBrewer;

public class GuiPotionBrewer extends GuiBase {
	
	public GuiPotionBrewer (ContainerBase inventory, TilePotionBrewer linked) {
		super(NORMAL_TEXTURE, inventory, linked);
		this.xSize = 198;
		this.ySize = 186;
		IEnergyStorage energy = linked.getCapability(CapabilityEnergy.ENERGY, null, ModuleContext.GUI);
		elements.add(new ProgressHandler(this, () -> energy.getEnergyStored(), () -> energy.getMaxEnergyStored(), new ElementEnergyBar(8, 18, Icons.EnergyBar.getLocation())));
		drawingInstructions.add( () -> {
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(linked.getNextIngredient(), 100, 37);
			BufferBuilder vertexBuffer = Tessellator.getInstance().getBuffer();
			GlStateManager.pushAttrib();
			GlStateManager.pushMatrix();
			GlStateManager.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
			vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			vertexBuffer.pos(99, 54, 0).endVertex();
			vertexBuffer.pos(117, 54, 0).endVertex();
			vertexBuffer.pos(117, 36, 0).endVertex();
			vertexBuffer.pos(99, 36, 0).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		});
	}
}
