
package therogue.storehouse.tile.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileMultiblockPlaceholder extends TileEntity implements IMultiBlockPart {
	
	private IMultiBlockController controller;
	private BlockPos multiblockPos;
	
	public TileMultiblockPlaceholder () {
	}
	
	@Override
	public boolean shouldRefresh (World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return false;
	}
	
	public void setController (IMultiBlockController controller) {
		this.controller = controller;
	}
	
	public IMultiBlockController getController () {
		if (controller == null && multiblockPos == null) throw new NoControllerException("The TileEntity at: " + this.getPos() + " could not find an IMultiblock");
		if (controller == null)
		{
			TileEntity atPos = getWorld().getTileEntity(multiblockPos);
			if (atPos instanceof IMultiBlockController) controller = (IMultiBlockController) atPos;
			else throw new NoControllerException("The TileEntity at: " + this.getPos() + " contains a blockPos that is not an IMultiBlock");
		}
		return controller;
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		BlockPos multiblockPos = getController().getPosition();
		nbt.setInteger("mbpx", multiblockPos.getX());
		nbt.setInteger("mbpy", multiblockPos.getY());
		nbt.setInteger("mbpz", multiblockPos.getZ());
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.multiblockPos = new BlockPos(nbt.getInteger("mbpx"), nbt.getInteger("mbpy"), nbt.getInteger("mbpz"));
	}
	
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return getController().hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		return getController().getCapability(capability, facing);
	}
	
	public class NoControllerException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		public NoControllerException (String message) {
			super(message);
		}
	}
	
	@Override
	public boolean isFormed () {
		try
		{
			getController();
			return true;
		}
		catch (NoControllerException e)
		{
			return false;
		}
	}
}
