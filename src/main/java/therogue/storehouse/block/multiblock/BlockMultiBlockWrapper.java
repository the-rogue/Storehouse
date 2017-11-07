
package therogue.storehouse.block.multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import therogue.storehouse.block.BlockUtils;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.tile.multiblock.IMultiBlockPart;
import therogue.storehouse.tile.multiblock.TileMultiblockPlaceholder;
import therogue.storehouse.util.LOG;

public class BlockMultiBlockWrapper extends StorehouseBaseBlock implements ITileEntityProvider, IBlockWrapper {
	
	private final List<WrapperEntry> blocks = new ArrayList<WrapperEntry>();
	public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);
	public static final StorehouseBaseBlock placeholder = new StorehouseBaseBlock("placeholder");
	
	public BlockMultiBlockWrapper (String name, Block... subStates) {
		super(name);
		if (subStates.length > 16) throw new IllegalArgumentException("The number of subStates in a MultiBlock Vanilla Block Wrapper must be 16 or less for block: " + name);
		for (Block subState : subStates)
		{
			if (subState == null) throw new IllegalArgumentException("Tried to register a null Block to a multiblockstate wrapper");
			blocks.add(new WrapperEntry(subState.getDefaultState(), false));
		}
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(META, 0));
	}
	
	public BlockMultiBlockWrapper addMatchStates (IBlockState... states) {
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
	public void getSubBlocks (Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
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
	
	/**
	 * Registers this block easily
	 */
	@Override
	public void preInit () {
		GameRegistry.register(this);
	}
	
	/**
	 * Registers any Model variants
	 */
	@Override
	@SideOnly (Side.CLIENT)
	public void preInitClient () {
		for (int i = 0; i < blocks.size(); i++)
		{
			ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(this), i, new ModelResourceLocation(getUnlocalizedName().substring(5), "meta=" + i));
		}
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		return new TileMultiblockPlaceholder();
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return BlockUtils.onMultiBlockActivated(world, pos, state, player, hand, side);
	}
	
	@Override
	public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof IMultiBlockPart)
		{
			((IMultiBlockPart) te).getController().onBlockBroken(pos);
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public IBlockState getWrappedState (IBlockState state) {
		for (WrapperEntry element : blocks)
		{
			if (element.matchState ? element.state == state : element.state.getBlock() == state.getBlock()) return this.getDefaultState().withProperty(META, blocks.indexOf(element));
		}
		LOG.debug("Failed to get a wrapped state for state: " + state.getBlock() + " BlockWrapper: " + this);
		return state;
	}
	
	public class WrapperEntry {
		
		public final IBlockState state;
		public final boolean matchState;
		
		public WrapperEntry (IBlockState state, boolean matchState) {
			this.state = state;
			this.matchState = matchState;
		}
	}
}
