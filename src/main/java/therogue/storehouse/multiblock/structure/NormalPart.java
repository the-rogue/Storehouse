
package therogue.storehouse.multiblock.structure;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class NormalPart implements IMultiBlockPart {
	
	public final IMultiBlockElement[][][] part;
	public final BlockPos startPosition;
	public final BlockPos endPosition;
	public final ImmutableList<BlockPos> importantBlocks;
	
	public NormalPart (BlockPos startPosition, BlockPos endPosition, List<BlockPos> importantBlocks, IMultiBlockElement[][][] part) {
		this.part = part;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.importantBlocks = ImmutableList.copyOf(importantBlocks);
	}
	
	@Override
	public boolean isValidBlock (List<Integer> validParts, IBlockState state, int x, int y, int z, boolean sameBlock) {
		return part[x - startPosition.getX()][y - startPosition.getY()][z - startPosition.getZ()].isValidBlock(state, sameBlock);
	}
	
	@Override
	public List<Integer> getPartNos () {
		return Lists.newArrayList(0);
	}
	
	@Override
	public BlockPos getStartPosition () {
		return startPosition;
	}
	
	@Override
	public BlockPos getEndPosition () {
		return endPosition;
	}
	
	@Override
	public List<BlockPos> getImportantBlocks () {
		return importantBlocks;
	}
	
	@Override
	public IBlockState getMultiBlockState (IBlockState originalState, int partNo, int x, int y, int z) {
		return part[x - startPosition.getX()][y - startPosition.getY()][z - startPosition.getZ()].getMultiBlockState(originalState);
	}
	
	@Override
	public String toString () {
		return "Part: " + Arrays.deepToString(part);
	}
}
