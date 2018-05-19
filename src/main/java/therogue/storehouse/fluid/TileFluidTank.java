
package therogue.storehouse.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.ITileModule;
import therogue.storehouse.tile.ModuleContext;

public class TileFluidTank extends FluidTank implements ITileModule {
	
	private ITile owner;
	
	public TileFluidTank (ITile owner, int capacity) {
		super(capacity);
		this.owner = owner;
	}
	
	public TileFluidTank (ITile owner, FluidStack fluidStack, int capacity) {
		super(fluidStack, capacity);
		this.owner = owner;
	}
	
	public TileFluidTank (ITile owner, Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
		this.owner = owner;
	}
	
	@Override
	public FluidStack drain (int maxDrain, boolean doDrain) {
		FluidStack fluid = super.drain(maxDrain, doDrain);
		if (fluid != null) owner.notifyChange(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		return fluid;
	}
	
	public FluidStack drain (FluidStack resource, boolean doDrain) {
		FluidStack fluid = super.drain(resource, doDrain);
		if (fluid != null) owner.notifyChange(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		return fluid;
	}
	
	@Override
	public int fill (FluidStack resource, boolean doFill) {
		int filled = super.fill(resource, doFill);
		if (filled != 0) owner.notifyChange(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		return filled;
	}
	
	/**
	 * Writes the data of the inventory to nbt, to be loaded back later
	 * 
	 * @param nbt The NBTTagCompound to write to.
	 */
	public NBTTagCompound writeModuleToNBT (NBTTagCompound nbt) {
		this.writeToNBT(nbt);
		return nbt;
	}
	
	/**
	 * Reads Data back from an nbt tag, after it has been loaded back
	 * 
	 * @param nbt The nbt tag to read from.
	 */
	@Override
	public NBTTagCompound readModuleFromNBT (NBTTagCompound nbt) {
		this.readFromNBT(nbt);
		return nbt;
	}
	
	/**
	 * @param capability the capability to test
	 * @param facing the direction to test it in
	 * @return whether the capability is applicable in this direction
	 */
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing, ModuleContext capacity) {
		return (T) new FluidTankViewer(this, capacity);
	}
	
	public IFluidTank getInternalTank () {
		return new InternalTank(this);
	}
	
	private static class InternalTank implements IFluidTank {
		
		private FluidTank delegate;
		
		public InternalTank (FluidTank delegate) {
			this.delegate = delegate;
		}
		
		@Override
		public FluidStack getFluid () {
			return delegate.getFluid().copy();
		}
		
		@Override
		public int getFluidAmount () {
			return delegate.getFluidAmount();
		}
		
		@Override
		public int getCapacity () {
			return delegate.getCapacity();
		}
		
		@Override
		public FluidTankInfo getInfo () {
			return new FluidTankInfo(this);
		}
		
		@Override
		public int fill (FluidStack resource, boolean doFill) {
			return delegate.fillInternal(resource, doFill);
		}
		
		@Override
		public FluidStack drain (int maxDrain, boolean doDrain) {
			return delegate.drainInternal(maxDrain, doDrain);
		}
	}
	
	public static class FluidTankViewer implements IFluidHandler {
		
		private ModuleContext context;
		private FluidTank delegate;
		
		public FluidTankViewer (FluidTank delegate, ModuleContext context) {
			this.context = context;
			this.delegate = delegate;
		}
		
		@Override
		public int fill (FluidStack resource, boolean doFill) {
			if (context == ModuleContext.INTERNAL) return delegate.fillInternal(resource, doFill);
			return delegate.fill(resource, doFill);
		}
		
		@Override
		public FluidStack drain (int maxDrain, boolean doDrain) {
			if (context == ModuleContext.INTERNAL) return delegate.drainInternal(maxDrain, doDrain);
			return delegate.drain(maxDrain, doDrain);
		}
		
		@Override
		public FluidStack drain (FluidStack resource, boolean doDrain) {
			if (context == ModuleContext.INTERNAL) return delegate.drainInternal(resource, doDrain);
			return delegate.drain(resource, doDrain);
		}
		
		@Override
		public IFluidTankProperties[] getTankProperties () {
			return delegate.getTankProperties();
		}
	}
}