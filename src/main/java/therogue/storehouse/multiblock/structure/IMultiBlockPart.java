
package therogue.storehouse.multiblock.structure;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;

public interface IMultiBlockPart {
	
	public boolean isValidBlock (List<Integer> validParts, IBlockState state, int x, int y, int z, boolean sameBlock);
	
	public List<Integer> getPartNos ();
	
	public IBlockState getMultiBlockState (IBlockState originalState, int partNo, int x, int y, int z);
	
	public List<ICapabilityWrapper<?>> getCapbilities (IBlockState originalState, int partNo, int x, int y, int z);
	
	/**
	 * The position where this block starts inclusive
	 */
	public BlockPos getStartPosition ();
	
	/**
	 * The position where this block ends inclusive
	 */
	public BlockPos getEndPosition ();
	
	/**
	 * Any Positions that are not blank to this part
	 */
	public List<BlockPos> getImportantBlocks ();
}