
package therogue.storehouse.block.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.block.BlockUtils;
import therogue.storehouse.block.machine.StorehouseBaseFacingMachine;
import therogue.storehouse.tile.multiblock.TileCarbonCompressor;

public class BlockCarbonCompressor extends StorehouseBaseFacingMachine {
	
	public BlockCarbonCompressor (String name) {
		super(name);
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		TileCarbonCompressor tile = new TileCarbonCompressor();
		tile.setWorld(worldIn);
		return tile;
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return BlockUtils.onMultiBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}
}
