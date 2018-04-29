
package therogue.storehouse.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import therogue.storehouse.tile.ITileModule;
import therogue.storehouse.tile.ModuleContext;

public class TileFluidTank extends FluidTank implements ITileModule {
	
	public TileFluidTank (int capacity) {
		super(capacity);
	}
	
	public TileFluidTank (FluidStack fluidStack, int capacity) {
		super(fluidStack, capacity);
	}
	
	public TileFluidTank (Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
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
		return (T) this;
	}
}
