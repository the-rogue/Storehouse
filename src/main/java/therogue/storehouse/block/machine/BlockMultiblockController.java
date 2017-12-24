
package therogue.storehouse.block.machine;

import java.util.function.Supplier;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.block.BlockUtils;
import therogue.storehouse.block.StorehouseBaseFacingMachine;
import therogue.storehouse.multiblock.tile.IMultiBlockTile;

public class BlockMultiblockController<T extends TileEntity> extends StorehouseBaseFacingMachine {
	
	protected Supplier<T> createTile;
	
	public BlockMultiblockController (String name, Supplier<T> createTile) {
		super(name);
		this.createTile = createTile;
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		TileEntity tile = createTile.get();
		tile.setWorld(worldIn);
		return tile;
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
		float hitY, float hitZ) {
		return BlockUtils.onMultiBlockActivated(world, pos, state, player, hand, side);
	}
	
	@Override
	public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof IMultiBlockTile)
		{
			((IMultiBlockTile) te).getController().onBlockBroken(pos);
		}
		super.breakBlock(worldIn, pos, state);
	}
}
