
package therogue.storehouse.multiblock.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.tile.ModuleContext;

public class BasicMultiBlockTile extends TileEntity implements IMultiBlockTile {
	
	private IMultiBlockController controller;
	private BlockPos multiblockPos;
	
	public BasicMultiBlockTile () {
	}
	
	@Override
	public void setController (IMultiBlockController controller) {
		this.controller = controller;
	}
	
	@Override
	public IMultiBlockController getController () {
		if (world.isRemote) throw new NoControllerException("Trying to get a controller in a client world");
		if (controller == null && multiblockPos == null) throw new NoControllerException("The TileEntity at: " + this.getPos() + " could not find an IMultiblockController");
		if (controller == null)
		{
			TileEntity atPos = getWorld().getTileEntity(multiblockPos);
			if (atPos instanceof IMultiBlockController) controller = (IMultiBlockController) atPos;
			else throw new NoControllerException("The TileEntity at: " + this.getPos() + " contains a blockPos that is not an IMultiBlockController");
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
		return getController().hasCapability(this.pos, capability, facing, ModuleContext.SIDE);
	}
	
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		return getController().getCapability(this.pos, capability, facing, ModuleContext.SIDE);
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
