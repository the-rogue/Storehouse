
package therogue.storehouse.tile.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public interface IMultiBlockController extends IMultiBlockPart {
	
	public BlockPos getPosition ();
	
	public boolean onMultiBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ);
	
	public boolean hasCapability (Capability<?> capability, EnumFacing facing);
	
	public <T> T getCapability (Capability<T> capability, EnumFacing facing);
}
