
package therogue.storehouse.multiblock.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;

public class VariablePart implements IMultiBlockPart {
	
	private final IMultiBlockElement[][][][] parts;
	private final BlockPos startPosition;
	private final BlockPos endPosition;
	private final List<BlockPos> importantBlocks;
	
	public VariablePart (NormalPart... parts) {
		this(Lists.newArrayList(parts));
	}
	
	public VariablePart (List<NormalPart> parts) {
		this.parts = new IMultiBlockElement[parts.size()][][][];
		this.importantBlocks = new ArrayList<BlockPos>();
		this.startPosition = parts.get(0).startPosition;
		this.endPosition = parts.get(0).endPosition;
		Set<BlockPos> setImportantBlocks = new HashSet<BlockPos>();
		for (int i = 0; i < parts.size(); i++)
		{
			setImportantBlocks.addAll(parts.get(i).getImportantBlocks());
			this.parts[i] = parts.get(i).part;
		}
		this.importantBlocks.addAll(setImportantBlocks);
	}
	
	public VariablePart (BlockPos startPosition, BlockPos endPosition, List<BlockPos> importantBlocks, IMultiBlockElement[][][]... parts) {
		this.parts = parts;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.importantBlocks = importantBlocks;
	}
	
	@Override
	public boolean isValidBlock (List<Integer> validParts, IBlockState state, int x, int y, int z, boolean sameBlock) {
		Boolean matches = false;
		List<Integer> invalidParts = new ArrayList<Integer>();
		for (Integer part : validParts)
		{
			if (parts[part][x - startPosition.getX()][y - startPosition.getY()][z - startPosition.getZ()].isValidBlock(state, sameBlock))
			{
				matches = true;
				continue;
			}
			invalidParts.add(part);
		}
		for (Integer invalidPart : invalidParts)
		{
			validParts.remove(invalidPart);
		}
		return matches;
	}
	
	@Override
	public List<Integer> getPartNos () {
		List<Integer> validParts = new ArrayList<Integer>();
		for (int i = 0; i < parts.length; i++)
		{
			validParts.add(i);
		}
		return validParts;
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
		return parts[partNo][x - startPosition.getX()][y - startPosition.getY()][z - startPosition.getZ()].getMultiBlockState(originalState);
	}
	
	@Override
	public List<ICapabilityWrapper<?>> getCapbilities (IBlockState originalState, int partNo, int x, int y, int z) {
		return parts[partNo][x - startPosition.getX()][y - startPosition.getY()][z - startPosition.getZ()].getCapabilities(originalState);
	}
	
	@Override
	public String toString () {
		return "Part: " + Arrays.deepToString(parts);
	}
}
