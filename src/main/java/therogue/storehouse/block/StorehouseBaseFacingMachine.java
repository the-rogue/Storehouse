
package therogue.storehouse.block;

import java.util.function.BiFunction;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StorehouseBaseFacingMachine<T extends TileEntity> extends StorehouseBaseMachine<T> {
	
	public StorehouseBaseFacingMachine (String name, BiFunction<World, Integer, T> createTile) {
		super(name, createTile);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
	}
	
	public StorehouseBaseFacingMachine (String name, BiFunction<World, Integer, T> createTile, AxisAlignedBB boundingBox) {
		super(name, createTile, boundingBox);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
	}
	
	/**
	 * Gets the {@link IBlockState} to place
	 * 
	 * @param world The world the block is being placed in
	 * @param pos The position the block is being placed at
	 * @param facing The side the block is being placed on
	 * @param hitX The X coordinate of the hit vector
	 * @param hitY The Y coordinate of the hit vector
	 * @param hitZ The Z coordinate of the hit vector
	 * @param meta The metadata of {@link ItemStack} as processed by {@link Item#getMetadata(int)}
	 * @param placer The entity placing the block
	 * @param hand The player hand used to place this block
	 * @return The state to be placed in the world
	 */
	public IBlockState getStateForPlacement (World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(BlockHorizontal.FACING, placer.getAdjustedHorizontalFacing().getOpposite());
	}
	
	@Override
	public BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontal.FACING });
	}
	
	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta (int meta) {
		return this.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta));
	}
	
	/**
	 * Registers the texture for this block easily
	 */
	@Override
	@SideOnly (Side.CLIENT)
	public void preInitClient () {
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(this), 0, new ModelResourceLocation(getUnlocalizedName().substring(5), "facing=north"));
	}
}
