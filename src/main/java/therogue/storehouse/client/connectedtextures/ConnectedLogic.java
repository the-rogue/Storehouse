
package therogue.storehouse.client.connectedtextures;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import therogue.storehouse.client.connectedtextures.IConnectedTextureLogic.IConnectionTest;
import therogue.storehouse.client.util.FlatFacing;

/**
 * The CTM renderer will draw the block's FACE using by assembling 4 quadrants from the 5 available block
 * textures. The normal Texture.png is the blocks "unconnected" texture, and is used when CTM is disabled or the block
 * has nothing to connect to. This texture has all of the outside corner quadrants The texture-ctm.png contains the
 * rest of the quadrants.
 * 
 * <pre>
 * ┌─────────────────┐ ┌────────────────────────────────┐
 * │ texture.png     │ │ texture-ctm.png                │
 * │ ╔══════╤══════╗ │ │  ──────┼────── ║ ─────┼───── ║ │
 * │ ║      │      ║ │ │ │      │      │║      │      ║ │
 * │ ║ 16   │ 17   ║ │ │ │ 0    │ 1    │║ 2    │ 3    ║ │
 * │ ╟──────┼──────╢ │ │ ┼──────┼──────┼╟──────┼──────╢ │
 * │ ║      │      ║ │ │ │      │      │║      │      ║ │
 * │ ║ 18   │ 19   ║ │ │ │ 4    │ 5    │║ 6    │ 7    ║ │
 * │ ╚══════╧══════╝ │ │  ──────┼────── ║ ─────┼───── ║ │
 * └─────────────────┘ │ ═══════╤═══════╝ ─────┼───── ╚ │
 *                     │ │      │      ││      │      │ │
 *                     │ │ 8    │ 9    ││ 10   │ 11   │ │
 *                     │ ┼──────┼──────┼┼──────┼──────┼ │
 *                     │ │      │      ││      │      │ │
 *                     │ │ 12   │ 13   ││ 14   │ 15   │ │
 *                     │ ═══════╧═══════╗ ─────┼───── ╔ │
 *                     └────────────────────────────────┘
 * </pre>
 * 
 * combining { 18, 13, 9, 16 }, we can generate a texture connected to the right!
 * 
 * <pre>
 * ╔══════╤═══════
 * ║      │      │
 * ║ 16   │ 9    │
 * ╟──────┼──────┼
 * ║      │      │
 * ║ 18   │ 13   │
 * ╚══════╧═══════
 * </pre>
 *
 * combining { 18, 13, 11, 2 }, we can generate a texture, in the shape of an L (connected to the right, and up
 * 
 * <pre>
 * ║ ─────┼───── ╚
 * ║      │      │
 * ║ 2    │ 11   │
 * ╟──────┼──────┼
 * ║      │      │
 * ║ 18   │ 13   │
 * ╚══════╧═══════
 * </pre>
 *
 * HAVE FUN!
 * -CptRageToaster-
 */
@ParametersAreNonnullByDefault
public class ConnectedLogic {
	
	/**
	 * The Uvs for the specific "magic number" value
	 */
	public static final float[][] uvs = new float[][] {
			// Ctm texture
			new float[] { 0.25f, 0.25f, 0, 0 }, // 0
			new float[] { 0.25f, 0.25f, 0.25f, 0 }, // 1
			new float[] { 0.25f, 0.25f, 0.5f, 0 }, // 2
			new float[] { 0.25f, 0.25f, 0.75f, 0 }, // 3
			new float[] { 0.25f, 0.25f, 0, 0.25f }, // 4
			new float[] { 0.25f, 0.25f, 0.25f, 0.25f }, // 5
			new float[] { 0.25f, 0.25f, 0.5f, 0.25f }, // 6
			new float[] { 0.25f, 0.25f, 0.75f, 0.25f }, // 7
			new float[] { 0.25f, 0.25f, 0, 0.5f }, // 8
			new float[] { 0.25f, 0.25f, 0.25f, 0.5f }, // 9
			new float[] { 0.25f, 0.25f, 0.5f, 0.5f }, // 10
			new float[] { 0.25f, 0.25f, 0.75f, 0.5f }, // 11
			new float[] { 0.25f, 0.25f, 0, 0.75f }, // 12
			new float[] { 0.25f, 0.25f, 0.25f, 0.75f }, // 13
			new float[] { 0.25f, 0.25f, 0.5f, 0.75f }, // 14
			new float[] { 0.25f, 0.25f, 0.75f, 0.75f }, // 15
			// Default texture
			new float[] { 0.5f, 0.5f, 0, 0 }, // 16
			new float[] { 0.5f, 0.5f, 0.5f, 0 }, // 17
			new float[] { 0.5f, 0.5f, 0, 0.5f }, // 18
			new float[] { 0.5f, 0.5f, 0.5f, 0.5f } // 19
	};
	public static final float[] FULL_TEXTURE = new float[] { 1.0f, 1.0f, 0, 0 };
	/** Some hardcoded offset values for the different corner indeces */
	protected static int[] submapOffsets = { 4, 5, 1, 0 };
	// Mapping the different corner indeces to their respective dirs
	protected static final FlatFacing[][] submapMap = new FlatFacing[][] { { FlatFacing.BOTTOM, FlatFacing.LEFT, FlatFacing.BOTTOM_LEFT }, { FlatFacing.BOTTOM, FlatFacing.RIGHT, FlatFacing.BOTTOM_RIGHT }, { FlatFacing.TOP, FlatFacing.RIGHT, FlatFacing.TOP_RIGHT }, { FlatFacing.TOP, FlatFacing.LEFT, FlatFacing.TOP_LEFT }
	};
	public final int[] submapCache;
	public final long serialized;
	
	/**
	 * @return The indeces of the typical 4x4 submap to use for the given face at the given location.
	 * 
	 *         Indeces are in counter-clockwise order starting at bottom left.
	 */
	public ConnectedLogic (@Nullable IBlockAccess world, BlockPos pos, EnumFacing side, IConnectedTextureLogic theBlock, IConnectionTest tester) {
		if (world == null)
		{
			this.serialized = 0;
			this.submapCache = new int[] { 18, 19, 17, 16 };
			return;
		}
		IBlockState state = world.getBlockState(pos);
		byte connectionMap = 0;
		int[] submapCache = new int[] { 18, 19, 17, 16 };
		if (state.shouldSideBeRendered(world, pos, side))
		{
			for (FlatFacing dir : FlatFacing.VALUES)
			{
				BlockPos connectionPos = pos;
				for (EnumFacing dirTest : dir.getNormalizedDirs(side))
				{
					connectionPos = connectionPos.offset(dirTest);
				}
				IBlockState connectedState = world.getBlockState(connectionPos);
				IBlockState obscuringState = world.getBlockState(connectionPos.add(side.getDirectionVec()));
				boolean ret = tester.connects(theBlock.ignoreStates(), state, connectedState, side);
				// no block obscuring this face
				if (obscuringState != null && !connectedState.shouldSideBeRendered(world, connectionPos, side))
				{
					// check that we aren't already connected outwards from this side
					ret &= !tester.connects(theBlock.ignoreStates(), state, obscuringState, side);
				}
				if (ret)
				{
					connectionMap = (byte) (connectionMap | (1 << dir.ordinal()));
				}
				else
				{
					connectionMap = (byte) (connectionMap & ~(1 << dir.ordinal()));
				}
			}
		}
		// Map connections to submap indeces
		for (int i = 0; i < 4; i++)
		{
			FlatFacing[] dirs = submapMap[i];
			if (connectedOr(connectionMap, dirs[0], dirs[1]))
			{
				if (connectedAnd(connectionMap, dirs))
				{
					// If all dirs are connected, we use the fully connected face,
					// the base offset value.
					submapCache[i] = submapOffsets[i];
				}
				else
				{
					// This is a bit magic-y, but basically the array is ordered so
					// the first dir requires an offset of 2, and the second dir
					// requires an offset of 8, plus the initial offset for the
					// corner.
					submapCache[i] = submapOffsets[i] + (connected(connectionMap, dirs[0]) ? 2 : 0) + (connected(connectionMap, dirs[1]) ? 8 : 0);
				}
			}
		}
		this.submapCache = submapCache;
		this.serialized = Byte.toUnsignedLong(connectionMap);
	}
	
	private boolean connected (byte connectionMap, FlatFacing dir) {
		return ((connectionMap >> dir.ordinal()) & 1) == 1;
	}
	
	private boolean connectedAnd (byte connectionMap, FlatFacing... dirs) {
		for (FlatFacing dir : dirs)
		{
			if (!connected(connectionMap, dir)) { return false; }
		}
		return true;
	}
	
	private boolean connectedOr (byte connectionMap, FlatFacing... dirs) {
		for (FlatFacing dir : dirs)
		{
			if (connected(connectionMap, dir)) { return true; }
		}
		return false;
	}
}