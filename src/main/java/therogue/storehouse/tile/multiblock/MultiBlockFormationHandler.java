
package therogue.storehouse.tile.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.block.multiblock.IBlockWrapper;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockStructure.StructureTest;
import therogue.storehouse.util.LOG;

public class MultiBlockFormationHandler {
	
	private static Map<IMultiBlockController, List<PositionStateChanger>> multiblocks = new HashMap<IMultiBlockController, List<PositionStateChanger>>();
	
	public static boolean formMultiBlock (IMultiBlockController controller) {
		World controllerWorld = controller.getPositionWorld();
		MultiBlockCheckResult result = checkStructure(controllerWorld, controller.getPosition(), controller.getStructure());
		if (!result.valid) return false;
		List<PositionStateChanger> worldPositionStates = result.worldPositionsStates;
		multiblocks.put(controller, worldPositionStates);
		for (PositionStateChanger positionState : worldPositionStates)
		{
			if (positionState.inMultiblockState != null)
			{
				controllerWorld.setBlockState(positionState.position, positionState.inMultiblockState);
				TileEntity te = controllerWorld.getTileEntity(positionState.position);
				if (te != null && te instanceof IMultiBlockTile)
				{
					((IMultiBlockTile) te).setController(controller);
				}
			}
		}
		return true;
	}
	
	public static void removeMultiBlock (IMultiBlockController controller, @Nullable BlockPos at) {
		World controllerWorld = controller.getPositionWorld();
		List<PositionStateChanger> worldPositionStates = multiblocks.remove(controller);
		if (worldPositionStates == null) return;
		for (PositionStateChanger positionState : worldPositionStates)
		{
			if (positionState.nonMultiblockState != null && !positionState.position.equals(controller.getPosition()) && !positionState.position.equals(at)) controllerWorld.setBlockState(positionState.position, positionState.nonMultiblockState);
		}
	}
	
	public static boolean sameStructure (IMultiBlockController controller) {
		MultiBlockCheckResult result = checkStructure(controller.getPositionWorld(), controller.getPosition(), controller.getStructure());
		if (result.valid && getPositionsFromPSCList(result.worldPositionsStates).equals(getPositionsFromPSCList(multiblocks.get(controller)))) return true;
		return false;
	}
	
	private static List<BlockPos> getPositionsFromPSCList (List<PositionStateChanger> positionStates) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		for (PositionStateChanger psc : positionStates)
		{
			positions.add(psc.position);
		}
		return positions;
	}
	
	private static List<BlockPos> getPossibleLocations (IBlockState block, MultiBlockStructure blockarray) {
		List<BlockPos> possibleLocations = new ArrayList<BlockPos>();
		StructureTest blocktest = blockarray.newStructureTest();
		while (blocktest.next())
		{
			if (blocktest.isValidBlock(block, true)) possibleLocations.add(blocktest.getCurrentPosition());
		}
		return possibleLocations;
	}
	
	private static boolean checkLocation (World world, BlockPos possibleLocationInWorld, Rotation rotation, BlockPos possibleLocationInArray, StructureTest blocktest) {
		BlockPos arrayOriginInWorld = possibleLocationInWorld.subtract(possibleLocationInArray.rotate(rotation));
		while (blocktest.next())
		{
			if (!blocktest.isValidBlock(world.getBlockState(arrayOriginInWorld.add(blocktest.getCurrentPosition().rotate(rotation))), false)) return false;
		}
		return true;
	}
	
	private static MultiBlockCheckResult getPositions (World world, BlockPos possibleLocationInWorld, Rotation rotation, BlockPos possibleLocationInArray, StructureTest blocktest) {
		BlockPos arrayOriginInWorld = possibleLocationInWorld.subtract(possibleLocationInArray.rotate(rotation));
		List<PositionStateChanger> worldPositionsStates = new ArrayList<PositionStateChanger>();
		while (blocktest.next())
		{
			BlockPos worldPosition = arrayOriginInWorld.add(blocktest.getCurrentPosition().rotate(rotation));
			IBlockState worldBlockState = world.getBlockState(worldPosition);
			worldPositionsStates.add(new PositionStateChanger(worldPosition, worldBlockState, blocktest.getMultiBlockState(worldBlockState)));
		}
		return new MultiBlockCheckResult(true, worldPositionsStates);
	}
	
	private static MultiBlockCheckResult checkLocation (World world, BlockPos possibleLocationInWorld, BlockPos possibleLocationInArray, MultiBlockStructure blockarray) {
		for (Rotation rotation : Rotation.values())
		{
			StructureTest blocktest = blockarray.newStructureTest();
			if (checkLocation(world, possibleLocationInWorld, rotation, possibleLocationInArray, blocktest))
			{
				blocktest.resetPosition();
				return getPositions(world, possibleLocationInWorld, rotation, possibleLocationInArray, blocktest);
			}
		}
		return new MultiBlockCheckResult(false, null);
	}
	
	private static MultiBlockCheckResult checkStructure (World world, BlockPos checkerPos, MultiBlockStructure array) {
		for (BlockPos location : getPossibleLocations(world.getBlockState(checkerPos), array))
		{
			MultiBlockCheckResult result = checkLocation(world, checkerPos, location, array);
			if (result.valid) return result;
		}
		return new MultiBlockCheckResult(false, null);
	}
	
	private static class MultiBlockCheckResult {
		
		public final boolean valid;
		@Nullable
		public final List<PositionStateChanger> worldPositionsStates;
		
		public MultiBlockCheckResult (boolean valid, @Nullable List<PositionStateChanger> worldPositionsStates) {
			this.valid = valid;
			this.worldPositionsStates = worldPositionsStates;
		}
	}
	
	private static class PositionStateChanger {
		
		public final BlockPos position;
		public final IBlockState nonMultiblockState;
		public final IBlockState inMultiblockState;
		
		public PositionStateChanger (BlockPos position, IBlockState nonMultiblockState, IBlockState inMultiblockState) {
			this.position = position;
			this.nonMultiblockState = nonMultiblockState;
			this.inMultiblockState = inMultiblockState;
		}
	}
	
	public static class MultiBlockPartBuilder {
		
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
		};
		private List<List<List<IMultiBlockElement>>> blocklist = new ArrayList<List<List<IMultiBlockElement>>>();
		private IMultiBlockElement[][][] blockarray;
		private BlockPos endPos;
		private List<BlockPos> importantBlocks = new ArrayList<BlockPos>();
		private int yLevel = 0;
		
		public MultiBlockPartBuilder () {
		}
		
		public static MultiBlockPartBuilder newBuilder () {
			return new MultiBlockPartBuilder();
		}
		
		/**
		 * Should ONLY be used where the block's default state is used, null for any block in the specified position
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
		 * Add only one option for each slot specified, null for any block in the specified position
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
		 * Should ONLY be used where the block's default state is used, null for any block in the specified position
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
		 * Add only one option for each slot specified, null for any block in the specified position
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
		
		public MultiBlockPartBuilder addBlocksToRow (IMultiBlockElement... row) {
			if (checkBlockArray("Error: Trying to add to a built blockarray")) return this;
			while (blocklist.size() <= yLevel)
			{
				blocklist.add(new ArrayList<List<IMultiBlockElement>>());
			}
			List<IMultiBlockElement> rowlist = blocklist.get(yLevel).get(blocklist.get(yLevel).size() - 1);
			for (int i = 0; i < row.length; i++)
			{
				if (!row[i].toString().equals("ANY_BLOCK"))
				{
					importantBlocks.add(new BlockPos(i, yLevel, blocklist.get(yLevel).size() - 1));
					LOG.info("Inclusion" + i + " " + yLevel + " " + (blocklist.get(yLevel).size() - 1));
				}
				rowlist.add(row[i]);
			}
			return this;
		}
		
		public MultiBlockPartBuilder newRow () {
			while (blocklist.size() <= yLevel)
			{
				blocklist.add(new ArrayList<List<IMultiBlockElement>>());
			}
			blocklist.get(blocklist.size() - 1).add(new ArrayList<IMultiBlockElement>());
			return this;
		}
		
		public MultiBlockPartBuilder goUp () {
			return goUp(1);
		}
		
		public MultiBlockPartBuilder goUp (int amount) {
			if (checkBlockArray("Error: Trying to add to a built blockarray")) return this;
			yLevel += amount;
			return this;
		}
		
		public MultiBlockPartBuilder goAcross () {
			return goAcross(1);
		}
		
		public MultiBlockPartBuilder goAcross (int amount) {
			if (checkBlockArray("Error: Trying to add to a built blockarray")) return this;
			for (int i = 0; i < amount; i++)
			{
				blocklist.get(yLevel).add(new ArrayList<IMultiBlockElement>());
			}
			return this;
		}
		
		public MultiBlockPartBuilder build () {
			if (checkBlockArray("Error: Trying to rebuild blockarray\n")) return this;
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
			blockarray = new IMultiBlockElement[maxX][][];
			for (int x = 0; x < maxX; x++)
			{
				blockarray[x] = new IMultiBlockElement[maxY][];
				for (int y = 0; y < maxY; y++)
				{
					blockarray[x][y] = new IMultiBlockElement[maxZ];
					for (int z = 0; z < maxZ; z++)
					{
						blockarray[x][y][z] = blocklist.get(y).get(z).get(x);
					}
				}
			}
			endPos = new BlockPos(maxX, maxY, maxZ);
			return this;
		}
		
		public IMultiBlockElement[][][] getBlockArray () {
			if (blockarray == null)
			{
				build();
				LOG.warn("Error: Block Array Not Already Built\n" + Thread.currentThread().getStackTrace());
			}
			return blockarray;
		}
		
		public BlockPos getEndBlockPos () {
			if (endPos == null)
			{
				build();
				LOG.warn("Error: Block Array Not Already Built\n" + Thread.currentThread().getStackTrace());
			}
			return endPos;
		}
		
		public List<BlockPos> getImportantBlocks () {
			if (importantBlocks == null)
			{
				build();
				LOG.warn("Error: Block Array Not Already Built\n" + Thread.currentThread().getStackTrace());
			}
			return importantBlocks;
		}
		
		public IMultiBlockPart getNormalPart (BlockPos startPos) {
			return new NormalMultiBlockPart(startPos, startPos.add(getEndBlockPos()), getImportantBlocks(), getBlockArray());
		}
		
		private boolean checkBlockArray (String message) {
			if (blockarray != null)
			{
				LOG.warn(message + Thread.currentThread().getStackTrace());
				return true;
			}
			return false;
		}
	}
	
	public static interface IMultiBlockElement {
		
		public boolean isValidBlock (IBlockState state, boolean sameBlock);
		
		public IBlockState getMultiBlockState (IBlockState originalState);
	}
	
	public static class NormalBlock implements IMultiBlockElement {
		
		private final IBlockState nonMultiBlockPart;
		private final IBlockState multiblockPart;
		private final boolean matchState;
		
		public NormalBlock (Block nonMultiBlockPart, IBlockWrapper multiblockpartGetter) {
			this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
			this.multiblockPart = multiblockpartGetter.getWrappedState(nonMultiBlockPart.getDefaultState());
			matchState = false;
		}
		
		public NormalBlock (IBlockState nonMultiBlockPart, IBlockWrapper multiblockpartGetter) {
			this.nonMultiBlockPart = nonMultiBlockPart;
			this.multiblockPart = multiblockpartGetter.getWrappedState(nonMultiBlockPart);
			matchState = true;
		}
		
		public NormalBlock (Block nonMultiBlockPart, Block multiblockPart) {
			this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
			this.multiblockPart = multiblockPart.getDefaultState();
			matchState = false;
		}
		
		public NormalBlock (Block nonMultiBlockPart, IBlockState multiblockPart) {
			this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
			this.multiblockPart = multiblockPart;
			matchState = false;
		}
		
		public NormalBlock (IBlockState nonMultiBlockPart, IBlockState multiblockPart) {
			this.nonMultiBlockPart = nonMultiBlockPart;
			this.multiblockPart = multiblockPart;
			matchState = true;
		}
		
		@Override
		public boolean isValidBlock (IBlockState state, boolean sameBlock) {
			return matchState ? this.nonMultiBlockPart == state : this.nonMultiBlockPart.getBlock() == state.getBlock();
		}
		
		@Override
		public IBlockState getMultiBlockState (IBlockState originalState) {
			return multiblockPart;
		}
		
		@Override
		public String toString () {
			return nonMultiBlockPart.getBlock().toString();
		}
	}
	
	public static class ChoiceBlock implements IMultiBlockElement {
		
		private final IBlockState[] nonMultiBlockParts;
		private final IBlockState[] multiblockPart;
		private final boolean matchState;
		
		public ChoiceBlock (IBlockWrapper multiblockpartGetter, Block... nonMultiBlockParts) {
			this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
			this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
				this.multiblockPart[i] = multiblockpartGetter.getWrappedState(nonMultiBlockParts[i].getDefaultState());
			}
			matchState = false;
		}
		
		public ChoiceBlock (IBlockWrapper multiblockpartGetter, IBlockState... nonMultiBlockParts) {
			this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
			this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				this.nonMultiBlockParts[i] = nonMultiBlockParts[i];
				this.multiblockPart[i] = multiblockpartGetter.getWrappedState(nonMultiBlockParts[i]);
			}
			matchState = true;
		}
		
		public ChoiceBlock (Block[] multiblockParts, Block... nonMultiBlockParts) {
			this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
			this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
				this.multiblockPart[i] = multiblockParts[i].getDefaultState();
			}
			matchState = false;
		}
		
		public ChoiceBlock (IBlockState[] multiblockParts, Block... nonMultiBlockParts) {
			this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
			this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
				this.multiblockPart[i] = multiblockParts[i];
			}
			matchState = false;
		}
		
		public ChoiceBlock (IBlockState[] multiblockParts, IBlockState... nonMultiBlockParts) {
			this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
			this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				this.nonMultiBlockParts[i] = nonMultiBlockParts[i];
				this.multiblockPart[i] = multiblockParts[i];
			}
			matchState = true;
		}
		
		@Override
		public boolean isValidBlock (IBlockState block, boolean sameBlock) {
			for (IBlockState test : nonMultiBlockParts)
			{
				if (matchState ? test == block : test.getBlock() == block.getBlock()) return true;
			}
			return false;
		}
		
		@Override
		public IBlockState getMultiBlockState (IBlockState originalState) {
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				if (nonMultiBlockParts[i] == originalState) return multiblockPart[i];
			}
			throw new IllegalArgumentException("The modder obviously didnt check if it was a valid block before calling this method");
		}
		
		@Override
		public String toString () {
			return nonMultiBlockParts.toString();
		}
	}
	
	public static interface IMultiBlockPart {
		
		public boolean isValidBlock (List<Integer> validParts, IBlockState state, int x, int y, int z, boolean sameBlock);
		
		public List<Integer> getPartNos ();
		
		public IBlockState getMultiBlockState (IBlockState originalState, int partNo, int x, int y, int z);
		
		/**
		 * The position where this block starts inclusive
		 */
		public BlockPos getStartPosition ();
		
		/**
		 * The position where this block ends inclusive
		 */
		public BlockPos getEndPosition ();
		
		/**
		 * Any Positions that are blank to this part
		 */
		public List<BlockPos> getImportantBlocks ();
	}
	
	public static class NormalMultiBlockPart implements IMultiBlockPart {
		
		private final IMultiBlockElement[][][] part;
		private final BlockPos startPosition;
		private final BlockPos endPosition;
		private final List<BlockPos> importantBlocks;
		
		public NormalMultiBlockPart (BlockPos startPosition, BlockPos endPosition, List<BlockPos> importantBlocks, IMultiBlockElement[][][] part) {
			this.part = part;
			this.startPosition = startPosition;
			this.endPosition = endPosition;
			this.importantBlocks = importantBlocks;
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
	}
	
	public static class ChoicePart implements IMultiBlockPart {
		
		private final IMultiBlockElement[][][][] parts;
		private final BlockPos startPosition;
		private final BlockPos endPosition;
		private final List<BlockPos> importantBlocks;
		
		public ChoicePart (BlockPos startPosition, MultiBlockPartBuilder... parts) {
			this(startPosition, parts[0].getEndBlockPos(), parts);
		}
		
		public ChoicePart (BlockPos startPosition, BlockPos endPosition, MultiBlockPartBuilder... parts) {
			this.parts = new IMultiBlockElement[parts.length][][][];
			this.importantBlocks = new ArrayList<BlockPos>();
			for (int i = 0; i < parts.length; i++)
			{
				List<BlockPos> importantBlocks = parts[i].getImportantBlocks();
				this.parts[i] = parts[i].getBlockArray();
				this.importantBlocks.addAll(importantBlocks);
			}
			this.startPosition = startPosition;
			this.endPosition = endPosition;
		}
		
		public ChoicePart (BlockPos startPosition, BlockPos endPosition, List<BlockPos> importantBlocks, IMultiBlockElement[][][]... parts) {
			this.parts = parts;
			this.startPosition = startPosition;
			this.endPosition = endPosition;
			this.importantBlocks = importantBlocks;
		}
		
		@Override
		public boolean isValidBlock (List<Integer> validParts, IBlockState state, int x, int y, int z, boolean sameBlock) {
			Boolean matches = false;
			for (int i = 0; i < validParts.size(); i++)
			{
				if (parts[validParts.get(i)][x - startPosition.getX()][y - startPosition.getY()][z - startPosition.getZ()].isValidBlock(state, sameBlock))
				{
					matches = true;
					continue;
				}
				validParts.remove(validParts.get(i));
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
	}
	
	public static class MultiBlockStructure {
		
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
			partPointers = new Integer[maxX + 1][][];
			for (int x = 0; x < partPointers.length; x++)
			{
				partPointers[x] = new Integer[maxY + 1][];
				for (int y = 0; y < partPointers[x].length; y++)
				{
					partPointers[x][y] = new Integer[maxZ + 1];
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
				if (part instanceof ChoicePart)
				{
					LOG.info(currentPartNo);
					LOG.info(partNos.get(part));
				}
				part = parts.get(partPointers[x][y][z]);
				currentPartNo = partNos.get(part);
				if (nextX == maxX && nextY == maxY && nextZ == maxZ) return false;
				if (nextZ == maxZ)
				{
					nextZ = 0;
					if (nextY == maxY)
					{
						nextY = 0;
						nextX++;
					}
					else nextY++;
				}
				else nextZ++;
				return true;
			}
		}
	}
}
