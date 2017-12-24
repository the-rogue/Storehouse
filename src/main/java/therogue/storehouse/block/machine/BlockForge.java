
package therogue.storehouse.block.machine;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import therogue.storehouse.block.StorehouseBaseFacingMachine;
import therogue.storehouse.tile.machine.TileForge;

public class BlockForge extends StorehouseBaseFacingMachine {
	
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.21875D, 0.0D, 0.21875D, 0.78125D, 0.5875D, 0.78125D);
	
	public BlockForge (String name) {
		super(name);
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		TileForge tile = new TileForge();
		tile.setWorld(worldIn);
		return tile;
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileForge)
		{
			TileForge teforge = (TileForge) te;
			return teforge.onBlockActivated(player, hand);
		}
		return false;
	}
	
	@Override
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube (IBlockState state) {
		return false;
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox (IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
}
