
package therogue.storehouse.tile.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.util.LOG;

public class MultiBlockFormationHandler {
	
	private static Map<ResourceLocation, IMultiBlockElement> blockmappings = new HashMap<ResourceLocation, IMultiBlockElement>();
	
	public static void registerShortName (String modID, String shortName, IMultiBlockElement block) {
		registerShortName(new ResourceLocation(modID, shortName), block);
	}
	
	public static void registerShortName (ResourceLocation shortnameLocation, IMultiBlockElement block) {
		if (blockmappings.containsKey(shortnameLocation)) LOG.warn("Tried to register a block under the same location name: " + shortnameLocation.toString() + " This is a bug.");
		else blockmappings.put(shortnameLocation, block);
	}
	
	public static IMultiBlockElement[][][] getBlockList (ResourceLocation[][][] blocks) {
		IMultiBlockElement[][][] blocklist = new IMultiBlockElement[blocks.length][][];
		for (int x = 0; x < blocks.length; x++)
		{
			blocklist[x] = new IMultiBlockElement[blocks[x].length][];
			for (int y = 0; y < blocks[x].length; y++)
			{
				blocklist[x][y] = new IMultiBlockElement[blocks[x][y].length];
				for (int z = 0; z < blocks[x][y].length; z++)
				{
					blocklist[x][y][z] = blockmappings.get(blocks[x][y][z]);
				}
			}
		}
		return blocklist;
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
	
	public static boolean checkLocation (World world, BlockPos possibleLocationInWorld, BlockPos possibleLocationInArray, IMultiBlockElement[][][] blockarray) {
		for (Rotation rotation : Rotation.values())
		{
			if (checkLocation(world, possibleLocationInWorld, rotation, possibleLocationInArray, blockarray)) return true;
		}
		return false;
	}
	
	public static boolean checkStructure (IBlockState checker, IMultiBlockElement[][][] array, World world, BlockPos checkerPos) {
		for (BlockPos location : getPossibleLocations(checker, array))
		{
			if (checkLocation(world, checkerPos, location, array)) return true;
		}
		return false;
	}
	
	public static class MultiBlockBuilder {
		
		private List<List<IMultiBlockElement[]>> blocklist = new ArrayList<List<IMultiBlockElement[]>>();
		private IMultiBlockElement[][][] blockarray;
		private int yLevel = 0;
		private int maxX = 0;
		private int maxZ = 0;
		
		public MultiBlockBuilder () {
		}
		
		public MultiBlockBuilder addRow (IMultiBlockElement... row) {
			if (checkBlockArray("Error: Trying to add to a built blockarray")) return this;
			if (blocklist.get(yLevel) == null)
			{
				blocklist.add(new ArrayList<IMultiBlockElement[]>());
			}
			blocklist.get(yLevel).add(row);
			if (maxZ > blocklist.get(yLevel).size()) maxZ = blocklist.get(yLevel).size();
			if (maxX > row.length) maxX = row.length;
			return this;
		}
		
		public MultiBlockBuilder goUp () {
			return goUp(1);
		}
		
		public MultiBlockBuilder goUp (int amount) {
			if (checkBlockArray("Error: Trying to add to a built blockarray")) return this;
			for (int i = 0; i < amount; i++)
			{
				blocklist.add(new ArrayList<IMultiBlockElement[]>());
			}
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
				blocklist.get(yLevel).add(new IMultiBlockElement[] {});
			}
			return this;
		}
		
		public MultiBlockBuilder build () {
			if (checkBlockArray("Error: Trying to rebuild blockarray\n")) return this;
			Integer maxY = blocklist.size();
			blockarray = new IMultiBlockElement[maxY][][];
			for (int i = 0; i <= maxY; i++)
			{
				blockarray[i] = new IMultiBlockElement[maxZ][];
				for (int j = 0; j < blocklist.get(i).size(); j++)
				{
					blockarray[i][j] = new IMultiBlockElement[maxX];
					if (blocklist.get(i).get(j) != null)
					{
						for (int k = 0; k < blocklist.get(i).get(j).length; k++)
						{
							blockarray[i][j][k] = blocklist.get(i).get(j)[k];
						}
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
	}
}
