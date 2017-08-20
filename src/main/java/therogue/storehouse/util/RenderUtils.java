/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.EnumFacing;

public class RenderUtils {
	
	public static void drawPlane(EnumFacing face, float offset, VertexBuffer vb) {
		switch(face) {
			case DOWN:
			case UP:
				
				break;
			case EAST:
			case WEST:
				break;
			case NORTH:
			case SOUTH:
				break;
			
		}
		// Up
		vb.pos(1, 1, 0).tex(0, 1).endVertex();
		vb.pos(0, 1, 0).tex(1, 1).endVertex();
		vb.pos(0, 1, 1).tex(1, 0).endVertex();
		vb.pos(1, 1, 1).tex(0, 0).endVertex();
		// East
		vb.pos(1, 0, 1).tex(0, 1).endVertex();
		vb.pos(1, 0, 0).tex(1, 1).endVertex();
		vb.pos(1, 1, 0).tex(1, 0).endVertex();
		vb.pos(1, 1, 1).tex(0, 0).endVertex();
		// Down
		vb.pos(1, 0, 0).tex(0, 1).endVertex();
		vb.pos(1, 0, 1).tex(0, 0).endVertex();
		vb.pos(0, 0, 1).tex(1, 0).endVertex();
		vb.pos(0, 0, 0).tex(1, 1).endVertex();
		// West
		vb.pos(0, 0, 1).tex(0, 1).endVertex();
		vb.pos(0, 1, 1).tex(0, 0).endVertex();
		vb.pos(0, 1, 0).tex(1, 0).endVertex();
		vb.pos(0, 0, 0).tex(1, 1).endVertex();
		// North
		vb.pos(1, 0, 0).tex(0, 1).endVertex();
		vb.pos(0, 0, 0).tex(1, 1).endVertex();
		vb.pos(0, 1, 0).tex(1, 0).endVertex();
		vb.pos(1, 1, 0).tex(0, 0).endVertex();
		// South
		vb.pos(1, 0, 1).tex(0, 1).endVertex();
		vb.pos(1, 1, 1).tex(0, 0).endVertex();
		vb.pos(0, 1, 1).tex(1, 0).endVertex();
		vb.pos(0, 0, 1).tex(1, 1).endVertex();
	}
}
