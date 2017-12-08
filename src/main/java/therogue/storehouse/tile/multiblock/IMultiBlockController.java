
package therogue.storehouse.tile.multiblock;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockStructure;

public interface IMultiBlockController extends IMultiBlockTile {
	
	public TileEntity getTile ();
	
	default public World getPositionWorld () {
		return getTile().getWorld();
	}
	
	default public BlockPos getPosition () {
		return getTile().getPos();
	}
	
	public MultiBlockStructure getStructure ();
	
	public void onBlockBroken (@Nullable BlockPos at);
	
	public boolean onMultiBlockActivatedAt (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side);
	
	default boolean hasCapability (BlockPos pos, Capability<?> capability, EnumFacing facing) {
		return getTile().hasCapability(capability, facing);
	}
	
	default public <T> T getCapability (BlockPos pos, Capability<T> capability, EnumFacing facing) {
		return getTile().getCapability(capability, facing);
	}
	
	default public IMultiBlockController getController () {
		return this;
	}
	
	default public void setController (IMultiBlockController controller) {
		// NOOP
	}
}
