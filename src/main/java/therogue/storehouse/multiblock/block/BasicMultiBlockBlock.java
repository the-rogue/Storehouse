
package therogue.storehouse.multiblock.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.multiblock.tile.BasicMultiBlockTile;
import therogue.storehouse.multiblock.tile.BasicMultiBlockTile.NoControllerException;
import therogue.storehouse.multiblock.tile.IMultiBlockController;
import therogue.storehouse.multiblock.tile.IMultiBlockTile;
import therogue.storehouse.util.LOG;

public class BasicMultiBlockBlock extends StorehouseBaseBlock implements IMultiBlockStateMapper, ITileEntityProvider {
	
	private final List<WrapperEntry> blocks = new ArrayList<WrapperEntry>();
	public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);
	public static final StorehouseBaseBlock placeholder = new StorehouseBaseBlock("placeholder");
	
	public BasicMultiBlockBlock (String name) {
		super(name);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(META, 0));
	}
	
	public BasicMultiBlockBlock (String name, Block... subStates) {
		this(name);
		addBlocks(subStates);
	}
	
	public BasicMultiBlockBlock addBlocks (Block... states) {
		if (blocks.size() + states.length > 16) throw new IllegalArgumentException("The number of subStates in a MultiBlock Vanilla Block Wrapper must be 16 or less for block: " + this.getUnlocalizedName());
		for (Block subState : states)
		{
			if (subState == null) throw new IllegalArgumentException("Tried to register a null Block to a multiblockstate wrapper");
			blocks.add(new WrapperEntry(subState.getDefaultState(), true));
		}
		return this;
	}
	
	public BasicMultiBlockBlock addMatchStates (IBlockState... states) {
		if (blocks.size() + states.length > 16) throw new IllegalArgumentException("The number of subStates in a MultiBlock Vanilla Block Wrapper must be 16 or less for block: " + this.getUnlocalizedName());
		for (IBlockState subState : states)
		{
			if (subState == null) throw new IllegalArgumentException("Tried to register a null IBlockState to a multiblockstate wrapper");
			blocks.add(new WrapperEntry(subState, true));
		}
		return this;
	}
	
	public IBlockState getSubBlockState (IBlockState thisState) {
		return blocks.get(thisState.getValue(META)).state;
	}
	
	@Override
	public Item getItemDropped (IBlockState blockstate, Random random, int fortune) {
		IBlockState subBlockState = getSubBlockState(blockstate);
		return subBlockState.getBlock().getItemDropped(subBlockState, random, fortune);
	}
	
	@Override
	public int quantityDropped (IBlockState blockstate, int fortune, Random random) {
		IBlockState subBlockState = getSubBlockState(blockstate);
		return subBlockState.getBlock().quantityDropped(subBlockState, fortune, random);
	}
	
	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped (IBlockState state) {
		IBlockState subBlockState = getSubBlockState(state);
		return subBlockState.getBlock().damageDropped(subBlockState);
	}
	
	@Override
	public BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, new IProperty[] { META });
	}
	
	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(META);
	}
	
	@Override
	public IBlockState getStateFromMeta (int meta) {
		return this.getDefaultState().withProperty(META, meta);
	}
	
	@Override
	public void getSubBlocks (CreativeTabs tab, NonNullList<ItemStack> list) {
	}
	
	@Override
	public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		IBlockState subBlockState = getSubBlockState(state);
		return subBlockState.getBlock().getPickBlock(subBlockState, target, world, pos, player);
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName (ItemStack stack) {
		IBlockState subBlockState = getSubBlockState(getDefaultState().withProperty(META, stack.getMetadata()));
		return subBlockState.getBlock().getUnlocalizedName();
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox (IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		IBlockState subBlockState = getSubBlockState(blockState);
		return subBlockState.getCollisionBoundingBox(worldIn, pos);
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		IBlockState subBlockState = getSubBlockState(state);
		return subBlockState.getBoundingBox(source, pos);
	}
	
	@Override
	public boolean isFullCube (IBlockState state) {
		IBlockState subBlockState = getSubBlockState(state);
		return subBlockState.isFullCube();
	}
	
	@Deprecated
	@Override
	public boolean isFullBlock (IBlockState state) {
		IBlockState subBlockState = getSubBlockState(state);
		return subBlockState.isFullBlock();
	}
	
	@Override
	public Item getItemBlock () {
		return null;
	}
	
	/**
	 * Registers any Model variants
	 */
	@Override
	public void registerModels () {
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote && te instanceof IMultiBlockTile)
		{
			try
			{
				IMultiBlockController controller = ((IMultiBlockTile) te).getController();
				return controller.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
			}
			catch (NoControllerException e)
			{
				LOG.warn("Could not Notify the controller of block at: " + pos + " activated");
			}
		}
		return true;
	}
	
	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote && te instanceof IMultiBlockTile)
		{
			try
			{
				IMultiBlockController controller = ((IMultiBlockTile) te).getController();
				controller.breakBlock(world, pos, state);
			}
			catch (NoControllerException e)
			{
				LOG.warn("Could not Notify the controller of block at: " + pos + " broken");
			}
		}
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public IBlockState getMultiBlockState (IBlockState state) {
		for (WrapperEntry element : blocks)
		{
			if (element.matchState ? element.state == state : element.state.getBlock() == state.getBlock()) return this.getDefaultState().withProperty(META, blocks.indexOf(element));
		}
		LOG.debug("Failed to get a wrapped state for state: " + state.getBlock() + " BlockWrapper: " + this);
		return state;
	}
	
	@Override
	public TileEntity createNewTileEntity (World world, int meta) {
		return new BasicMultiBlockTile();
	}
	
	public class WrapperEntry {
		
		@Nonnull
		public final IBlockState state;
		public final boolean matchState;
		
		public WrapperEntry (IBlockState state, boolean matchState) {
			this.state = state;
			this.matchState = matchState;
		}
	}
}
