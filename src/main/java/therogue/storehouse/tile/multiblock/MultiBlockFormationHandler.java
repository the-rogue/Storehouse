
package therogue.storehouse.tile.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.block.multiblock.IBlockWrapper;
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
				if (te != null && te instanceof IMultiBlockPart)
				{
					((IMultiBlockPart) te).setController(controller);
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
	
	public static List<BlockPos> getPositionsFromPSCList (List<PositionStateChanger> positionStates) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		for (PositionStateChanger psc : positionStates)
		{
			positions.add(psc.position);
		}
		return positions;
	}
	
	public static List<BlockPos> getPossibleLocations (IBlockState block, IMultiBlockElement[][][] blockarray) {
		List<BlockPos> possibleLocations = new ArrayList<BlockPos>();
		for (int x = 0; x < blockarray.length; x++)
		{
			for (int y = 0; y < blockarray[x].length; y++)
			{
				for (int z = 0; z < blockarray[x][y].length; z++)
				{
					if (blockarray[x][y][z].isValidBlock(block) && blockarray[x][y][z].getMultiBlockState() != null)
					{
						possibleLocations.add(new BlockPos(x, y, z));
					}
				}
			}
		}
		return possibleLocations;
	}
	
	public static boolean checkLocation (World world, BlockPos possibleLocationInWorld, Rotation rotation, BlockPos possibleLocationInArray, IMultiBlockElement[][][] blockarray) {
		BlockPos arrayOriginInWorld = possibleLocationInWorld.subtract(possibleLocationInArray.rotate(rotation));
		for (int x = 0; x < blockarray.length; x++)
		{
			for (int y = 0; y < blockarray[x].length; y++)
			{
				for (int z = 0; z < blockarray[x][y].length; z++)
				{
					if (!blockarray[x][y][z].isValidBlock(world.getBlockState(arrayOriginInWorld.add(new BlockPos(x, y, z).rotate(rotation))))) return false;
				}
			}
		}
		return true;
	}
	
	public static MultiBlockCheckResult getPositions (BlockPos possibleLocationInWorld, Rotation rotation, BlockPos possibleLocationInArray, IMultiBlockElement[][][] blockarray) {
		BlockPos arrayOriginInWorld = possibleLocationInWorld.subtract(possibleLocationInArray.rotate(rotation));
		List<PositionStateChanger> worldPositionsStates = new ArrayList<PositionStateChanger>();
		for (int x = 0; x < blockarray.length; x++)
		{
			for (int y = 0; y < blockarray[x].length; y++)
			{
				for (int z = 0; z < blockarray[x][y].length; z++)
				{
					worldPositionsStates.add(new PositionStateChanger(arrayOriginInWorld.add(new BlockPos(x, y, z).rotate(rotation)), blockarray[x][y][z].getNonMultiBlockState(), blockarray[x][y][z].getMultiBlockState()));
				}
			}
		}
		return new MultiBlockCheckResult(true, worldPositionsStates);
	}
	
	public static MultiBlockCheckResult checkLocation (World world, BlockPos possibleLocationInWorld, BlockPos possibleLocationInArray, IMultiBlockElement[][][] blockarray) {
		for (Rotation rotation : Rotation.values())
		{
			if (checkLocation(world, possibleLocationInWorld, rotation, possibleLocationInArray, blockarray)) return getPositions(possibleLocationInWorld, rotation, possibleLocationInArray, blockarray);
		}
		return new MultiBlockCheckResult(false, null);
	}
	
	public static MultiBlockCheckResult checkStructure (World world, BlockPos checkerPos, IMultiBlockElement[][][] array) {
		for (BlockPos location : getPossibleLocations(world.getBlockState(checkerPos), array))
		{
			MultiBlockCheckResult result = checkLocation(world, checkerPos, location, array);
			if (result.valid) return result;
		}
		return new MultiBlockCheckResult(false, null);
	}
	
	public static class MultiBlockCheckResult {
		
		public final boolean valid;
		@Nullable
		public final List<PositionStateChanger> worldPositionsStates;
		
		public MultiBlockCheckResult (boolean valid, @Nullable List<PositionStateChanger> worldPositionsStates) {
			this.valid = valid;
			this.worldPositionsStates = worldPositionsStates;
		}
	}
	
	public static class PositionStateChanger {
		
		public final BlockPos position;
		public final IBlockState nonMultiblockState;
		public final IBlockState inMultiblockState;
		
		public PositionStateChanger (BlockPos position, IBlockState nonMultiblockState, IBlockState inMultiblockState) {
			this.position = position;
			this.nonMultiblockState = nonMultiblockState;
			this.inMultiblockState = inMultiblockState;
		}
	}
	
	public static class MultiBlockBuilder {
		
		public static final IMultiBlockElement ANY_BLOCK = new IMultiBlockElement() {
			
			@Override
			public boolean isValidBlock (IBlockState block) {
				return true;
			}
			
			@Override
			public String toString () {
				return "ANY_BLOCK";
			}
			
			@Override
			public IBlockState getNonMultiBlockState () {
				return null;
			}
			
			@Override
			public IBlockState getMultiBlockState () {
				return null;
			}
		};
		private List<List<List<IMultiBlockElement>>> blocklist = new ArrayList<List<List<IMultiBlockElement>>>();
		private IMultiBlockElement[][][] blockarray;
		private int yLevel = 0;
		
		public MultiBlockBuilder () {
		}
		
		public static MultiBlockBuilder newBuilder () {
			return new MultiBlockBuilder();
		}
		
		/**
		 * Should ONLY be used where the block's default state is used, null for any block in the specified position
		 */
		public MultiBlockBuilder addBlocksToRow (IBlockWrapper wrapper, Block... row) {
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
		public MultiBlockBuilder addBlocksToRow (IBlockWrapper wrapper, IBlockState... row) {
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
		public MultiBlockBuilder addBlocksToRow (List<Block> multiblockStates, Block... row) {
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
		public MultiBlockBuilder addBlocksToRow (List<IBlockState> multiblockStates, IBlockState... row) {
			IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
			for (int i = 0; i < row.length; i++)
			{
				if (row[i] == null) elements[i] = ANY_BLOCK;
				else elements[i] = new NormalBlock(row[i], multiblockStates.get(i));
			}
			return addBlocksToRow(elements);
		}
		
		public MultiBlockBuilder addBlocksToRow (IMultiBlockElement... row) {
			if (checkBlockArray("Error: Trying to add to a built blockarray")) return this;
			while (blocklist.size() <= yLevel)
			{
				blocklist.add(new ArrayList<List<IMultiBlockElement>>());
			}
			List<IMultiBlockElement> rowlist = blocklist.get(yLevel).get(blocklist.get(yLevel).size() - 1);
			for (IMultiBlockElement rowElement : row)
			{
				rowlist.add(rowElement);
			}
			return this;
		}
		
		public MultiBlockBuilder newRow () {
			while (blocklist.size() <= yLevel)
			{
				blocklist.add(new ArrayList<List<IMultiBlockElement>>());
			}
			blocklist.get(blocklist.size() - 1).add(new ArrayList<IMultiBlockElement>());
			return this;
		}
		
		public MultiBlockBuilder goUp () {
			return goUp(1);
		}
		
		public MultiBlockBuilder goUp (int amount) {
			if (checkBlockArray("Error: Trying to add to a built blockarray")) return this;
			yLevel += amount;
			return this;
		}
		
		public MultiBlockBuilder goAcross () {
			return goAcross(1);
		}
		
		public MultiBlockBuilder goAcross (int amount) {
			if (checkBlockArray("Error: Trying to add to a built blockarray")) return this;
			for (int i = 0; i < amount; i++)
			{
				blocklist.get(yLevel).add(new ArrayList<IMultiBlockElement>());
			}
			return this;
		}
		
		public MultiBlockBuilder build () {
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
		
		private boolean checkBlockArray (String message) {
			if (blockarray != null)
			{
				LOG.warn("Error: Trying to rebuild blockarray\n" + Thread.currentThread().getStackTrace());
				return true;
			}
			return false;
		}
	}
	
	public static interface IMultiBlockElement {
		
		public boolean isValidBlock (IBlockState block);
		
		public IBlockState getNonMultiBlockState ();
		
		public IBlockState getMultiBlockState ();
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
		public boolean isValidBlock (IBlockState state) {
			return matchState ? this.nonMultiBlockPart == state : this.nonMultiBlockPart.getBlock() == state.getBlock();
		}
		
		@Override
		public IBlockState getNonMultiBlockState () {
			return nonMultiBlockPart;
		}
		
		@Override
		public IBlockState getMultiBlockState () {
			return multiblockPart;
		}
		
		@Override
		public String toString () {
			return nonMultiBlockPart.getBlock().toString();
		}
	}
	
	public static class ChoiceBlock implements IMultiBlockElement {
		
		private final IBlockState[] nonMultiBlockParts;
		private final IBlockState multiblockPart;
		
		public ChoiceBlock (IBlockState multiblockPart, IBlockState... nonMultiBlockParts) {
			this.nonMultiBlockParts = nonMultiBlockParts;
			this.multiblockPart = multiblockPart;
		}
		
		public boolean isValidBlock (IBlockState block) {
			for (IBlockState test : nonMultiBlockParts)
			{
				if (test == block) return true;
			}
			return false;
		}
		
		@Override
		public IBlockState getNonMultiBlockState () {
			return nonMultiBlockParts[0];
		}
		
		@Override
		public IBlockState getMultiBlockState () {
			return multiblockPart;
		}
		
		@Override
		public String toString () {
			return nonMultiBlockParts.toString();
		}
	}
}
