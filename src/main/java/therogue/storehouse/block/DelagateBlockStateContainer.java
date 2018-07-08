
package therogue.storehouse.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DelagateBlockStateContainer extends BlockStateContainer {
	
	List<DelagateBlockState> delagateStates;
	
	public DelagateBlockStateContainer (Block blockIn, Block delegateBlock) {
		super(blockIn, delegateBlock.getBlockState().getProperties().toArray(new IProperty[0]));
		delagateStates.forEach(state -> state.setWrappedState(delegateBlock.getDefaultState()));
		delagateStates = null;
	}
	
	protected StateImplementation createState (Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties,
			@Nullable ImmutableMap<net.minecraftforge.common.property.IUnlistedProperty<?>, java.util.Optional<?>> unlistedProperties) {
		if (delagateStates == null) delagateStates = new ArrayList<>();
		DelagateBlockState state = new DelagateBlockState(block, properties);
		delagateStates.add(state);
		return state;
	}
	
	public static IBlockState convertFromProperties (IBlockState previous, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
		for (Entry<IProperty<?>, Comparable<?>> propertyValue : propertiesIn.entrySet())
		{
			previous = convertBlockState(previous, propertyValue.getKey(), propertyValue.getValue());
		}
		return previous;
	}
	
	@SuppressWarnings ("unchecked")
	public static <T extends Comparable<T>> IBlockState convertBlockState (IBlockState convertable, IProperty<T> property, Comparable<?> value) {
		return convertable.withProperty(property, (T) value);
	}
	
	@SuppressWarnings ("deprecation")
	public class DelagateBlockState extends StateImplementation implements IBlockState {
		
		private final Block block;
		private IBlockState wrappedState;
		
		public DelagateBlockState (Block block, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
			super(block, propertiesIn);
			this.block = block;
		}
		
		private void setWrappedState (IBlockState defaultWrappedState) {
			this.wrappedState = convertFromProperties(defaultWrappedState, this.getProperties());
		}
		
		@Override
		public boolean onBlockEventReceived (World worldIn, BlockPos pos, int id, int param) {
			return wrappedState.onBlockEventReceived(worldIn, pos, id, param);
		}
		
		@Override
		public void neighborChanged (World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
			wrappedState.neighborChanged(worldIn, pos, blockIn, fromPos);
		}
		
		public IBlockState getActualState (IBlockAccess blockAccess, BlockPos pos) {
			return convertFromProperties(this, wrappedState.getActualState(blockAccess, pos).getProperties());
		}
		
		@Override
		public Material getMaterial () {
			return wrappedState.getMaterial();
		}
		
		@Override
		public boolean isFullBlock () {
			return wrappedState.isFullBlock();
		}
		
		@Override
		public boolean canEntitySpawn (Entity entityIn) {
			return wrappedState.canEntitySpawn(entityIn);
		}
		
		@Override
		public int getLightOpacity () {
			return wrappedState.getLightOpacity();
		}
		
		@Override
		public Block getBlock () {
			return block;
		}
		
		@Override
		public int getLightOpacity (IBlockAccess world, BlockPos pos) {
			return wrappedState.getLightOpacity(world, pos);
		}
		
		@Override
		public int getLightValue () {
			return wrappedState.getLightValue();
		}
		
		@Override
		public int getLightValue (IBlockAccess world, BlockPos pos) {
			return wrappedState.getLightValue(world, pos);
		}
		
		@Override
		public boolean isTranslucent () {
			return wrappedState.isTranslucent();
		}
		
		@Override
		public boolean useNeighborBrightness () {
			return wrappedState.useNeighborBrightness();
		}
		
		@Override
		public MapColor getMapColor (IBlockAccess p_185909_1_, BlockPos p_185909_2_) {
			return wrappedState.getMapColor(p_185909_1_, p_185909_2_);
		}
		
		@Override
		public boolean isFullCube () {
			return wrappedState.isFullCube();
		}
		
		@Override
		public boolean hasCustomBreakingProgress () {
			return wrappedState.hasCustomBreakingProgress();
		}
		
		@Override
		public EnumBlockRenderType getRenderType () {
			return wrappedState.getRenderType();
		}
		
		@Override
		public int getPackedLightmapCoords (IBlockAccess source, BlockPos pos) {
			return wrappedState.getPackedLightmapCoords(source, pos);
		}
		
		@Override
		public float getAmbientOcclusionLightValue () {
			return wrappedState.getAmbientOcclusionLightValue();
		}
		
		@Override
		public boolean isBlockNormalCube () {
			return wrappedState.isBlockNormalCube();
		}
		
		@Override
		public boolean isNormalCube () {
			return wrappedState.isNormalCube();
		}
		
		@Override
		public boolean canProvidePower () {
			return wrappedState.canProvidePower();
		}
		
		@Override
		public int getWeakPower (IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
			return wrappedState.getWeakPower(blockAccess, pos, side);
		}
		
		@Override
		public boolean hasComparatorInputOverride () {
			return wrappedState.hasComparatorInputOverride();
		}
		
		@Override
		public int getComparatorInputOverride (World worldIn, BlockPos pos) {
			return wrappedState.getComparatorInputOverride(worldIn, pos);
		}
		
		@Override
		public float getBlockHardness (World worldIn, BlockPos pos) {
			return wrappedState.getBlockHardness(worldIn, pos);
		}
		
		@Override
		public float getPlayerRelativeBlockHardness (EntityPlayer player, World worldIn, BlockPos pos) {
			return wrappedState.getPlayerRelativeBlockHardness(player, worldIn, pos);
		}
		
		@Override
		public int getStrongPower (IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
			return wrappedState.getStrongPower(blockAccess, pos, side);
		}
		
		@Override
		public EnumPushReaction getMobilityFlag () {
			return wrappedState.getMobilityFlag();
		}
		
		@Override
		public AxisAlignedBB getSelectedBoundingBox (World worldIn, BlockPos pos) {
			return wrappedState.getSelectedBoundingBox(worldIn, pos);
		}
		
		@Override
		public boolean shouldSideBeRendered (IBlockAccess blockAccess, BlockPos pos, EnumFacing facing) {
			return wrappedState.shouldSideBeRendered(blockAccess, pos, facing);
		}
		
		@Override
		public boolean isOpaqueCube () {
			return wrappedState.isOpaqueCube();
		}
		
		@Override
		public boolean isTopSolid () {
			return wrappedState.isTopSolid();
		}
		
		@Override
		public AxisAlignedBB getCollisionBoundingBox (IBlockAccess worldIn, BlockPos pos) {
			return wrappedState.getCollisionBoundingBox(worldIn, pos);
		}
		
		@Override
		public void addCollisionBoxToList (World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185908_6_) {
			wrappedState.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_);
		}
		
		@Override
		public AxisAlignedBB getBoundingBox (IBlockAccess blockAccess, BlockPos pos) {
			return wrappedState.getBoundingBox(blockAccess, pos);
		}
		
		@Override
		public RayTraceResult collisionRayTrace (World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
			return wrappedState.collisionRayTrace(worldIn, pos, start, end);
		}
		
		@Override
		public boolean doesSideBlockRendering (IBlockAccess world, BlockPos pos, EnumFacing side) {
			return wrappedState.doesSideBlockRendering(world, pos, side);
		}
		
		@Override
		public boolean isSideSolid (IBlockAccess world, BlockPos pos, EnumFacing side) {
			return wrappedState.isSideSolid(world, pos, side);
		}
		
		@Override
		public boolean doesSideBlockChestOpening (IBlockAccess world, BlockPos pos, EnumFacing side) {
			return wrappedState.doesSideBlockChestOpening(world, pos, side);
		}
		
		@Override
		public Vec3d getOffset (IBlockAccess access, BlockPos pos) {
			return wrappedState.getOffset(access, pos);
		}
		
		@Override
		public boolean causesSuffocation () {
			return wrappedState.causesSuffocation();
		}
		
		@Override
		public BlockFaceShape getBlockFaceShape (IBlockAccess p_193401_1_, BlockPos p_193401_2_, EnumFacing p_193401_3_) {
			return wrappedState.getBlockFaceShape(p_193401_1_, p_193401_2_, p_193401_3_);
		}
	}
}
