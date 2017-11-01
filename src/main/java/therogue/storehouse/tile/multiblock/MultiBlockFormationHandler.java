
package therogue.storehouse.tile.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import therogue.storehouse.util.LOG;
import therogue.storehouse.util.WorldHelper;

public class MultiBlockFormationHandler {
	
	private static Map<IMultiBlockController, List<BlockPos>> multiblocks = new HashMap<IMultiBlockController, List<BlockPos>>();
	private static Map<BlockPos, TileEntity> oldTEs = new HashMap<BlockPos, TileEntity>();
	private static Map<World, List<BlockPos>> multiblockpositions = new HashMap<World, List<BlockPos>>();
	
	public static boolean formMultiBlock (IMultiBlockController controller) {
		LOG.debug("Trying to form a multiblock for controller: " + controller);
		World controllerWorld = controller.getPositionWorld();
		MultiBlockCheckResult result = checkStructure(controllerWorld, controller.getPosition(), controller.getStructure());
		if (!result.valid) return false;
		LOG.debug("Forming MultiBlock for controller: " + controller);
		List<BlockPos> worldPositions = result.worldPositions;
		multiblocks.put(controller, worldPositions);
		if (multiblockpositions.containsKey(controllerWorld))
		{
			multiblockpositions.get(controllerWorld).addAll(worldPositions);
		}
		else
		{
			multiblockpositions.put(controllerWorld, Lists.newArrayList(worldPositions));
		}
		worldPositions.remove(controller.getPosition());
		for (BlockPos pos : worldPositions)
		{
			TileEntity oldTE = controllerWorld.getTileEntity(pos);
			if (oldTE != null)
			{
				oldTEs.put(pos, oldTE);
			}
			WorldHelper.addTileWithoutBlockToWorld(controllerWorld, pos, new TileMultiblockPlaceholder().setController(controller));
		}
		LOG.debug("Formed MultiBlock");
		return true;
	}
	
	public static void removeMultiBlock (IMultiBlockController controller) {
		LOG.debug("Removing MultiBlock for Controller: " + controller);
		World controllerWorld = controller.getPositionWorld();
		List<BlockPos> worldPositions = multiblocks.remove(controller);
		if (worldPositions == null) return;
		if (multiblockpositions.containsKey(controllerWorld))
		{
			multiblockpositions.get(controllerWorld).removeAll(worldPositions);
		}
		worldPositions.remove(controller.getPosition());
		for (BlockPos pos : worldPositions)
		{
			TileEntity oldTE = oldTEs.remove(pos);
			controllerWorld.setTileEntity(pos, oldTE);
		}
		LOG.debug("Removed MultiBlock");
	}
	
	public static boolean checkStructure (IMultiBlockController controller) {
		if (sameStructure(controller)) return false;
		removeMultiBlock(controller);
		return true;
	}
	
	public static boolean sameStructure (IMultiBlockController controller) {
		MultiBlockCheckResult result = checkStructure(controller.getPositionWorld(), controller.getPosition(), controller.getStructure());
		if (result.valid && result.worldPositions.equals(multiblocks.get(controller))) return true;
		return false;
	}
	
	public static void onNeighbourNotifiedEvent (BlockEvent.NeighborNotifyEvent event) {
		// LOG.debug("Notified Event");
		if (multiblockpositions.containsKey(event.getWorld()) && multiblockpositions.get(event.getWorld()).contains(event.getPos()))
		{
			LOG.debug("contains key");
			TileEntity atPos = event.getWorld().getTileEntity(event.getPos());
			if (atPos instanceof IMultiBlockPart)
			{
				LOG.debug("IMultiBlockPart at pos");
				((IMultiBlockPart) atPos).getController().checkStructure();
			}
		}
	}
	
	public static void onRightClickBlockEvent (PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		BlockPos position = event.getPos();
		if (multiblockpositions.containsKey(world) && multiblockpositions.get(world).contains(position))
		{
			TileEntity atPos = world.getTileEntity(position);
			if (atPos instanceof IMultiBlockPart)
			{
				EntityPlayer player = event.getEntityPlayer();
				EnumHand hand = event.getHand();
				EnumFacing facing = event.getFace();
				boolean bypass = true; // TODO Keep Up-to-date with PlayerControllerMP.processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos stack, EnumFacing pos, Vec3d facing, EnumHand vec);
				for (ItemStack s : new ItemStack[] { player.getHeldItemMainhand(), player.getHeldItemOffhand() })
					bypass = bypass && (s.isEmpty() || s.getItem().doesSneakBypassUse(s, world, position, player));
				if (!player.isSneaking() || bypass || event.getUseBlock() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW)
				{
					IBlockState iblockstate = world.getBlockState(position);
					if (event.getUseBlock() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) ((IMultiBlockPart) atPos).getController().onMultiBlockActivatedAt(world, position, iblockstate, player, hand, facing);
				}
			}
		}
	}
	
	public static List<BlockPos> getPossibleLocations (IBlockState block, IMultiBlockElement[][][] blockarray) {
		List<BlockPos> possibleLocations = new ArrayList<BlockPos>();
		for (int x = 0; x < blockarray.length; x++)
		{
			for (int y = 0; y < blockarray[x].length; y++)
			{
				for (int z = 0; z < blockarray[x][y].length; z++)
				{
					if (blockarray[x][y][z].isValidBlock(block))
					{
						possibleLocations.add(new BlockPos(x, y, z));
					}
				}
			}
		}
		return possibleLocations;
	}
	
	public static boolean checkLocation (World world, BlockPos possibleLocationInWorld, Rotation rotation, BlockPos possibleLocationInArray, IMultiBlockElement[][][] blockarray) {
		BlockPos arrayOriginInWorld = possibleLocationInWorld.add(possibleLocationInArray.rotate(rotation));
		for (int x = 0; x < blockarray.length; x++)
		{
			for (int y = 0; y < blockarray[x].length; y++)
			{
				for (int z = 0; z < blockarray[x][y].length; z++)
				{
					if (!blockarray[x][y][z].isValidBlock(world.getBlockState(arrayOriginInWorld.add(x, y, z)))) return false;
				}
			}
		}
		return true;
	}
	
	public static MultiBlockCheckResult getPositions (BlockPos possibleLocationInWorld, Rotation rotation, BlockPos possibleLocationInArray, IMultiBlockElement[][][] blockarray) {
		BlockPos arrayOriginInWorld = possibleLocationInWorld.add(possibleLocationInArray.rotate(rotation));
		List<BlockPos> positionsInWorld = new ArrayList<BlockPos>();
		for (int x = 0; x < blockarray.length; x++)
		{
			for (int y = 0; y < blockarray[x].length; y++)
			{
				for (int z = 0; z < blockarray[x][y].length; z++)
				{
					positionsInWorld.add(arrayOriginInWorld.add(x, y, z));
				}
			}
		}
		return new MultiBlockCheckResult(true, positionsInWorld);
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
		public final List<BlockPos> worldPositions;
		
		public MultiBlockCheckResult (boolean valid, @Nullable List<BlockPos> worldPositions) {
			this.valid = valid;
			this.worldPositions = worldPositions;
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
		public MultiBlockBuilder addBlocksToRow (Block... row) {
			IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
			for (int i = 0; i < row.length; i++)
			{
				if (row[i] == null) elements[i] = ANY_BLOCK;
				else elements[i] = new NormalBlock(row[i]);
			}
			return addBlocksToRow(elements);
		}
		
		/**
		 * Add only one option for each slot specified, null for any block in the specified position
		 */
		public MultiBlockBuilder addBlocksToRow (IBlockState... row) {
			IMultiBlockElement[] elements = new IMultiBlockElement[row.length];
			for (int i = 0; i < row.length; i++)
			{
				if (row[i] == null) elements[i] = ANY_BLOCK;
				else elements[i] = new NormalBlock(row[i]);
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
	}
	
	public static class NormalBlock implements IMultiBlockElement {
		
		private final IBlockState block;
		
		public NormalBlock (Block block) {
			this(block.getDefaultState());
		}
		
		public NormalBlock (IBlockState block) {
			this.block = block;
		}
		
		@Override
		public boolean isValidBlock (IBlockState block) {
			return this.block == block;
		}
		
		@Override
		public String toString () {
			return block.getBlock().toString();
		}
	}
	
	public static class ChoiceBlock implements IMultiBlockElement {
		
		private final IBlockState[] blocks;
		
		public ChoiceBlock (IBlockState... blocks) {
			this.blocks = blocks;
		}
		
		public boolean isValidBlock (IBlockState block) {
			for (IBlockState test : blocks)
			{
				if (test == block) return true;
			}
			return false;
		}
		
		@Override
		public String toString () {
			return blocks.toString();
		}
	}
}
