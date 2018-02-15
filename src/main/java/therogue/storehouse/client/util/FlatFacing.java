
package therogue.storehouse.client.util;

import static net.minecraft.util.EnumFacing.DOWN;
import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.UP;
import static net.minecraft.util.EnumFacing.WEST;

import java.util.EnumMap;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;

/**
 * <p>
 * It represents the eight different directions a face of a block can connect, and contains the logic for determining if a block is indeed connected in that direction.
 * <p>
 * Note that, for example, {@link #TOP_RIGHT} does not mean connected to the {@link #TOP} and {@link #RIGHT}, but connected in the diagonal direction represented by {@link #TOP_RIGHT}. This is used
 * for inner corner rendering.
 */
@ParametersAreNonnullByDefault
public enum FlatFacing {
	TOP (UP),
	TOP_RIGHT (UP, EAST),
	RIGHT (EAST),
	BOTTOM_RIGHT (DOWN, EAST),
	BOTTOM (DOWN),
	BOTTOM_LEFT (DOWN, WEST),
	LEFT (WEST),
	TOP_LEFT (UP, WEST);
	
	/**
	 * All values of this enum, used to prevent unnecessary allocation via {@link #values()}.
	 */
	public static final FlatFacing[] VALUES = values();
	private EnumFacing[] dirs;
	
	private FlatFacing (EnumFacing... dirs) {
		this.dirs = dirs;
	}
	
	private final EnumMap<EnumFacing, EnumFacing[]> normalizedCache = new EnumMap<>(EnumFacing.class);
	
	public EnumFacing[] getNormalizedDirs (EnumFacing normal) {
		if (!normalizedCache.containsKey(normal))
		{
			if (normal == SOUTH)
			{
				normalizedCache.put(normal, dirs);
			}
			else if (normal == SOUTH.getOpposite())
			{
				// If this is the opposite direction of the default normal, we
				// need to mirror the dirs
				// A mirror version does not affect y+ and y- so we ignore those
				EnumFacing[] ret = new EnumFacing[dirs.length];
				for (int i = 0; i < ret.length; i++)
				{
					ret[i] = dirs[i].getFrontOffsetY() != 0 ? dirs[i] : dirs[i].getOpposite();
				}
				normalizedCache.put(normal, ret);
			}
			else
			{
				EnumFacing axis = null;
				// Next, we need different a different rotation axis depending
				// on if this is up/down or not
				if (normal.getFrontOffsetY() == 0)
				{
					// If it is not up/down, pick either the left or right-hand
					// rotation
					axis = normal == SOUTH.rotateY() ? UP : DOWN;
				}
				else
				{
					// If it is up/down, pick either the up or down rotation.
					axis = normal == UP ? SOUTH.rotateYCCW() : SOUTH.rotateY();
				}
				EnumFacing[] ret = new EnumFacing[dirs.length];
				// Finally apply all the rotations
				for (int i = 0; i < ret.length; i++)
				{
					ret[i] = rotate(dirs[i], axis);
				}
				normalizedCache.put(normal, ret);
			}
		}
		return normalizedCache.get(normal);
	}
	
	private EnumFacing rotate (EnumFacing facing, EnumFacing axisFacing) {
		Axis axis = axisFacing.getAxis();
		AxisDirection axisDir = axisFacing.getAxisDirection();
		if (axisDir == AxisDirection.POSITIVE) return facing.rotateAround(axis);
		if (facing.getAxis() != axis)
		{
			switch (axis)
			{
				case X:
					// I did some manual testing and this is what worked...I don't get it either
					switch (facing)
					{
						case UP:
							return SOUTH;
						case DOWN:
							return NORTH;
						default:
					}
				case Y:
					return facing.rotateYCCW();
				case Z:
					switch (facing)
					{
						case UP:
							return DOWN;
						case DOWN:
							return UP;
						default:
					}
			}
		}
		return facing;
	}
}