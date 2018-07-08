
package therogue.storehouse.multiblock.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import therogue.storehouse.LOG;
import therogue.storehouse.block.DelagateBlockStateContainer;
import therogue.storehouse.block.IWrappedBlock;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.multiblock.tile.BasicMultiBlockTile;
import therogue.storehouse.multiblock.tile.BasicMultiBlockTile.NoControllerException;
import therogue.storehouse.multiblock.tile.IMultiBlockController;
import therogue.storehouse.multiblock.tile.IMultiBlockTile;

public class BasicMultiBlockBlock extends StorehouseBaseBlock implements IBlockWrapper, ITileEntityProvider, IWrappedBlock {
	
	private final Block wrappedBlock;
	
	public BasicMultiBlockBlock (String name, Block subBlock) {
		super(name);
		this.wrappedBlock = subBlock;
		this.blockState = new DelagateBlockStateContainer(this, wrappedBlock);
		this.setDefaultState(this.blockState.getBaseState());
		this.fullBlock = this.getDefaultState().isOpaqueCube();
		this.lightOpacity = this.fullBlock ? 255 : 0;
	}
	
	public BasicMultiBlockBlock (String name, IBlockState subBlockState) {
		super(name);
		this.wrappedBlock = subBlockState.getBlock();
	}
	
	@Override
	public IBlockState unwrap (IBlockState wrapped) {
		return DelagateBlockStateContainer.convertFromProperties(wrappedBlock.getDefaultState(), wrapped.getProperties());
	}
	
	@Override
	public Item getItemDropped (IBlockState blockstate, Random random, int fortune) {
		return wrappedBlock.getItemDropped(blockstate, random, fortune);
	}
	
	@Override
	public int quantityDropped (IBlockState blockstate, int fortune, Random random) {
		return wrappedBlock.quantityDropped(blockstate, fortune, random);
	}
	
	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped (IBlockState state) {
		return wrappedBlock.damageDropped(state);
	}
	
	@Override
	public void getSubBlocks (CreativeTabs tab, NonNullList<ItemStack> list) {
	}
	
	@Override
	public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return wrappedBlock.getPickBlock(state, target, world, pos, player);
	}
	
	@Override
	public int getMetaFromState (IBlockState state) {
		return wrappedBlock.getMetaFromState(state);
	}
	
	@SuppressWarnings ("deprecation")
	@Override
	public IBlockState getStateFromMeta (int meta) {
		return DelagateBlockStateContainer.convertFromProperties(this.getDefaultState(), wrappedBlock.getStateFromMeta(meta).getProperties());
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName (ItemStack stack) {
		return wrappedBlock.getUnlocalizedName();
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
	public boolean onBlockActivated (
			World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote && te instanceof IMultiBlockTile)
		{
			try
			{
				BlockPos controllerPos = ((IMultiBlockTile) te).getController().getPosition();
				return world.getBlockState(controllerPos).getBlock().onBlockActivated(world, controllerPos, state, player, hand, side, hitX, hitY, hitZ);
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
	public IBlockState getWrappedState (IBlockState state) {
		if (state.getBlock() == wrappedBlock) return DelagateBlockStateContainer.convertFromProperties(this.getDefaultState(), state.getProperties());
		LOG.debug("Failed to get a wrapped state for state: " + state.getBlock() + " BlockWrapper: " + this);
		return state;
	}
	
	@Override
	public TileEntity createNewTileEntity (World world, int meta) {
		return new BasicMultiBlockTile();
	}
}
