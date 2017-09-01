
package therogue.storehouse.block.machine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.tile.machine.TileStamper;
import therogue.storehouse.util.ItemUtils;

public class BlockStamper extends StorehouseBaseFacingMachine {
	
	public BlockStamper (String name) {
		super(name);
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		TileStamper tile = new TileStamper();
		tile.setWorld(worldIn);
		return tile;
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileStamper)
		{
			TileStamper testamp = (TileStamper) te;
			player.setHeldItem(hand, testamp.getContainerCapability().insertItem(1, player.getHeldItem(hand), false));
			ItemStack machineStack = testamp.getContainerCapability().getStackInSlot(0);
			ItemUtils.mergeStacks(64, true, player.getHeldItem(hand), machineStack);
			testamp.getContainerCapability().setStackInSlot(0, machineStack);
		}
		return true;
	}
}
