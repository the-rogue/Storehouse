
package therogue.storehouse.multiblock.structure;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;
import therogue.storehouse.multiblock.block.IBlockWrapper;

public class MultiBlockPartBuilder {
	
	public static final IMultiBlockElement ANY_BLOCK = new IMultiBlockElement() {
		
		@Override
		public boolean isValidBlock (IBlockState block, boolean sameBlock) {
			return !sameBlock;
		}
		
		@Override
		public String toString () {
			return "ANY_BLOCK";
		}
		
		@Override
		public IBlockState getMultiBlockState (IBlockState originalState) {
			return null;
		}
		
		@Override
		public List<ICapabilityWrapper<?>> getCapabilities (IBlockState originalState) {
			return new ArrayList<>();
		}
	};
	/**
	 * States
	 */
	private BlockPos startPos = new BlockPos(0, 0, 0);
	private IBlockWrapper currentWrapper;
	/**
	 * Variables
	 */
	private List<List<List<IMultiBlockElement>>> blocklist = new ArrayList<List<List<IMultiBlockElement>>>();
	private int yLevel = 0;
	
	public MultiBlockPartBuilder () {
	}
	
	public MultiBlockPartBuilder (IBlockWrapper wrapper) {
		setWrapper(wrapper);
	}
	
	public static MultiBlockPartBuilder newBuilder () {
		return new MultiBlockPartBuilder();
	}
	
	public static MultiBlockPartBuilder newBuilder (IBlockWrapper wrapper) {
		return new MultiBlockPartBuilder(wrapper);
	}
	
	public MultiBlockPartBuilder setWrapper (IBlockWrapper wrapper) {
		if (wrapper == null) throw new NullPointerException("The wrapper passed to setWrapper was null");
		currentWrapper = wrapper;
		return this;
	}
	
	public MultiBlockPartBuilder setStart (int x, int y, int z) {
		return setStart(new BlockPos(x, y, z));
	}
	
	public MultiBlockPartBuilder setStart (BlockPos pos) {
		this.startPos = pos;
		return this;
	}
	
	/**
	 * Should ONLY be used where the block's default state is used, null for any block in the specified position
	 * Add blocks to the current row, which is in the x direction.
	 * Uses the current Wrapper
	 */
	public MultiBlockPartBuilder addBlocksToRow (Block... row) {
		IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
		for (int i = 0; i < row.length; i++)
		{
			if (row[i] == null) elements[i] = ANY_BLOCK;
			else elements[i] = new NormalBlock(row[i], currentWrapper);
		}
		return addBlocksToRow(elements);
	}
	
	/**
	 * Add only one option for each slot specified, null for any block in the specified position
	 * Add blocks to the current row, which is in the x direction.
	 * Uses the current Wrapper
	 */
	public MultiBlockPartBuilder addBlocksToRow (IBlockState... row) {
		IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
		for (int i = 0; i < row.length; i++)
		{
			if (row[i] == null) elements[i] = ANY_BLOCK;
			else elements[i] = new NormalBlock(row[i], currentWrapper);
		}
		return addBlocksToRow(elements);
	}
	
	/**
	 * Should ONLY be used where the block's default state is used, null for any block in the specified position.
	 * Adds blocks to the current row, which is in the x direction
	 */
	public MultiBlockPartBuilder addBlocksToRow (IBlockWrapper wrapper, Block... row) {
		IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
		for (int i = 0; i < row.length; i++)
		{
			if (row[i] == null) elements[i] = ANY_BLOCK;
			else elements[i] = new NormalBlock(row[i], wrapper);
		}
		return addBlocksToRow(elements);
	}
	
	/**
	 * Add only one option for each slot specified, null for any block in the specified position.
	 * Adds blocks to the current row, which is in the x direction
	 */
	public MultiBlockPartBuilder addBlocksToRow (IBlockWrapper wrapper, IBlockState... row) {
		IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
		for (int i = 0; i < row.length; i++)
		{
			if (row[i] == null) elements[i] = ANY_BLOCK;
			else elements[i] = new NormalBlock(row[i], wrapper);
		}
		return addBlocksToRow(elements);
	}
	
	/**
	 * Should ONLY be used where the block's default state is used, null for any block in the specified position.
	 * Add blocks to the current row, which is in the x direction
	 */
	public MultiBlockPartBuilder addBlocksToRow (List<Block> multiblockStates, Block... row) {
		IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
		for (int i = 0; i < row.length; i++)
		{
			if (row[i] == null) elements[i] = ANY_BLOCK;
			else elements[i] = new NormalBlock(row[i], multiblockStates.get(i));
		}
		return addBlocksToRow(elements);
	}
	
	/**
	 * Add only one option for each slot specified, null for any block in the specified position.
	 * Add blocks to the current row, which is in the x direction
	 */
	public MultiBlockPartBuilder addBlocksToRow (List<IBlockState> multiblockStates, IBlockState... row) {
		IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
		for (int i = 0; i < row.length; i++)
		{
			if (row[i] == null) elements[i] = ANY_BLOCK;
			else elements[i] = new NormalBlock(row[i], multiblockStates.get(i));
		}
		return addBlocksToRow(elements);
	}
	
	/**
	 * Add blocks to the current row, which is in the x direction
	 */
	public MultiBlockPartBuilder addBlocksToRow (IMultiBlockElement... row) {
		while (blocklist.size() <= yLevel)
		{
			blocklist.add(new ArrayList<List<IMultiBlockElement>>());
		}
		List<IMultiBlockElement> rowlist = blocklist.get(yLevel).get(blocklist.get(yLevel).size() - 1);
		for (int i = 0; i < row.length; i++)
		{
			if (row[i] != null)
			{
				rowlist.add(row[i]);
			}
			else
			{
				rowlist.add(ANY_BLOCK);
			}
		}
		return this;
	}
	
	/**
	 * Adds a new row (increases the z by 1)
	 */
	public MultiBlockPartBuilder newRow () {
		while (blocklist.size() <= yLevel)
		{
			blocklist.add(new ArrayList<List<IMultiBlockElement>>());
		}
		blocklist.get(blocklist.size() - 1).add(new ArrayList<IMultiBlockElement>());
		return this;
	}
	
	/**
	 * Increases the y by 1
	 */
	public MultiBlockPartBuilder goUp () {
		return goUp(1);
	}
	
	/**
	 * Increases the y by the set amount
	 */
	public MultiBlockPartBuilder goUp (int amount) {
		yLevel += amount;
		return this;
	}
	
	/**
	 * Adds a set number of new rows (increases the z by amount)
	 */
	public MultiBlockPartBuilder newRow (int amount) {
		for (int i = 0; i < amount; i++)
		{
			newRow();
		}
		return this;
	}
	
	public NormalPart build () {
		int maxX = 0, maxZ = 0;
		for (List<List<IMultiBlockElement>> zlist : blocklist)
		{
			if (zlist.size() > maxZ) maxZ = zlist.size();
			for (List<IMultiBlockElement> xlist : zlist)
			{
				if (xlist.size() > maxX) maxX = xlist.size();
			}
		}
		for (List<List<IMultiBlockElement>> zlist : blocklist)
		{
			while (zlist.size() <= maxZ)
			{
				zlist.add(new ArrayList<IMultiBlockElement>());
			}
			for (List<IMultiBlockElement> e : zlist)
			{
				while (e.size() <= maxX)
				{
					e.add(ANY_BLOCK);
				}
			}
		}
		int maxY = blocklist.size();
		IMultiBlockElement[][][] blockarray = new IMultiBlockElement[maxX][][];
		List<BlockPos> importantBlocks = new ArrayList<BlockPos>();
		for (int x = 0; x < maxX; x++)
		{
			blockarray[x] = new IMultiBlockElement[maxY][];
			for (int y = 0; y < maxY; y++)
			{
				blockarray[x][y] = new IMultiBlockElement[maxZ];
				for (int z = 0; z < maxZ; z++)
				{
					IMultiBlockElement m = blocklist.get(y).get(z).get(maxX - x - 1);
					blockarray[x][y][z] = m;
					if (!m.equals(ANY_BLOCK))
					{
						importantBlocks.add(new BlockPos(x, y, z).add(startPos));
					}
				}
			}
		}
		BlockPos endPos = new BlockPos(maxX, maxY, maxZ).add(startPos);
		yLevel = 0;
		blocklist.clear();
		return new NormalPart(startPos, endPos, importantBlocks, blockarray);
	}
}
