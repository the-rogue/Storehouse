
package therogue.storehouse.multiblock.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import therogue.storehouse.util.LOG;

public class MultiBlockStructure {
	
	public static final IMultiBlockPart NO_PART = new IMultiBlockPart() {
		
		@Override
		public boolean isValidBlock (List<Integer> validParts, IBlockState state, int x, int y, int z, boolean sameBlock) {
			return !sameBlock;
		}
		
		@Override
		public List<Integer> getPartNos () {
			return Lists.newArrayList(0);
		}
		
		@Override
		public IBlockState getMultiBlockState (IBlockState originalState, int partNo, int x, int y, int z) {
			return MultiBlockPartBuilder.ANY_BLOCK.getMultiBlockState(originalState);
		}
		
		@Override
		public BlockPos getStartPosition () {
			return new BlockPos(0, 0, 0);
		}
		
		@Override
		public BlockPos getEndPosition () {
			return new BlockPos(0, 0, 0);
		}
		
		@Override
		public List<BlockPos> getImportantBlocks () {
			return Collections.emptyList();
		}
	};
	private final Integer[][][] partPointers;
	public final List<IMultiBlockPart> parts;
	private int maxX;
	private int maxY;
	private int maxZ;
	
	public MultiBlockStructure (IMultiBlockPart... partlist) {
		this.parts = ImmutableList.copyOf(Lists.asList(NO_PART, partlist));
		for (IMultiBlockPart part : parts)
		{
			BlockPos partEndPosition = part.getEndPosition();
			if (partEndPosition.getX() > maxX) maxX = partEndPosition.getX();
			if (partEndPosition.getY() > maxY) maxY = partEndPosition.getY();
			if (partEndPosition.getZ() > maxZ) maxZ = partEndPosition.getZ();
		}
		partPointers = new Integer[maxX][][];
		for (int x = 0; x < partPointers.length; x++)
		{
			partPointers[x] = new Integer[maxY][];
			for (int y = 0; y < partPointers[x].length; y++)
			{
				partPointers[x][y] = new Integer[maxZ];
				for (int z = 0; z < partPointers[x][y].length; z++)
				{
					partPointers[x][y][z] = 0;
				}
			}
		}
		for (int i = 0; i < parts.size(); i++)
		{
			IMultiBlockPart part = parts.get(i);
			BlockPos startPos = part.getStartPosition();
			BlockPos endPos = part.getEndPosition();
			List<BlockPos> importantBlocks = part.getImportantBlocks();
			for (int x = startPos.getX(); x < endPos.getX(); x++)
			{
				for (int y = startPos.getY(); y < endPos.getY(); y++)
				{
					for (int z = startPos.getZ(); z < endPos.getZ(); z++)
					{
						if (importantBlocks.contains(new BlockPos(x, y, z)))
						{
							if (partPointers[x][y][z] == 0) partPointers[x][y][z] = i;
							else throw new RuntimeException("The position at: " + x + ", " + y + ", " + z + " has already been assigned to a part");
						}
					}
				}
			}
		}
	}
	
	public StructureTest newStructureTest () {
		return new StructureTest();
	}
	
	public class StructureTest {
		
		private Map<IMultiBlockPart, List<Integer>> partNos = new HashMap<IMultiBlockPart, List<Integer>>();
		private int nextX;
		private int nextY;
		private int nextZ;
		private int x;
		private int y;
		private int z;
		private IMultiBlockPart part;
		private List<Integer> currentPartNo;
		
		public StructureTest () {
			resetAll();
		}
		
		public BlockPos getCurrentPosition () {
			return new BlockPos(x, y, z);
		}
		
		public boolean isValidBlock (IBlockState state, boolean sameBlock) {
			return part.isValidBlock(currentPartNo, state, x, y, z, sameBlock);
		}
		
		public IBlockState getMultiBlockState (IBlockState originalState) {
			if (currentPartNo.size() != 1) throw new IllegalStateException("Cannot be > 1 parts that match the same world configuration, " + "either 2 identical parts were registered, or this class was not used to check the world previously");
			return part.getMultiBlockState(originalState, currentPartNo.get(0), x, y, z);
		}
		
		public void resetPosition () {
			nextX = 0;
			nextY = 0;
			nextZ = 0;
		}
		
		public void resetAll () {
			partNos.clear();
			for (IMultiBlockPart part : parts)
			{
				partNos.put(part, part.getPartNos());
			}
			resetPosition();
		}
		
		public boolean next () {
			x = nextX;
			y = nextY;
			z = nextZ;
			if (nextX == maxX && nextY == (maxY - 1) && nextZ == (maxZ - 1)) return false;
			part = parts.get(partPointers[x][y][z]);
			currentPartNo = partNos.get(part);
			if (nextX == (maxX - 1) && nextY == (maxY - 1) && nextZ == (maxZ - 1))
			{
				nextX++;
				return true;
			}
			if (nextZ == (maxZ - 1))
			{
				nextZ = 0;
				if (nextY == (maxY - 1))
				{
					nextY = 0;
					nextX++;
				}
				else nextY++;
			}
			else nextZ++;
			try
			{
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				LOG.info("X: " + x + " Y: " + y + " Z: " + z + " NextX: " + nextX + " NextY: " + nextY + " NextZ: " + nextZ);
			}
			return true;
		}
	}
	
	public static class Builder {
		
		List<IMultiBlockPart> parts = new ArrayList<IMultiBlockPart>();
		
		public Builder () {
		}
		
		public static Builder newBuilder () {
			return new Builder();
		}
		
		public Builder addPart (IMultiBlockPart part) {
			parts.add(part);
			return this;
		}
		
		public MultiBlockStructure getStructure () {
			if (parts.size() < 1)
			{
				LOG.warn("Error: Structure size < 1, this is probably not intended\n" + Thread.currentThread().getStackTrace());
			}
			MultiBlockStructure structure = new MultiBlockStructure(parts.toArray(new IMultiBlockPart[] {}));
			parts.clear();
			return structure;
		}
	}
}