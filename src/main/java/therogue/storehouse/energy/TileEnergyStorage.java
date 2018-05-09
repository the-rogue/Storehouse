
package therogue.storehouse.energy;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.tile.ITileModule;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseTileEntity;

public class TileEnergyStorage extends EnergyStorageAdv implements ITileModule {
	
	private StorehouseBaseTileEntity owner;
	private int RFPerTick = 0;
	
	public TileEnergyStorage (StorehouseBaseTileEntity owner, int capacity) {
		super(capacity);
		this.owner = owner;
	}
	
	public TileEnergyStorage (StorehouseBaseTileEntity owner, int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
		this.owner = owner;
	}
	
	public TileEnergyStorage (StorehouseBaseTileEntity owner, int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
		this.owner = owner;
	}
	
	public void setRFPerTick (int RFPerTick) {
		this.RFPerTick = RFPerTick;
	}
	
	public boolean hasSufficientRF () {
		return energy >= this.RFPerTick;
	}
	
	public void runTick () {
		if (hasSufficientRF()) this.modifyEnergyStored(-RFPerTick);
	}
	
	@Override
	public int receiveEnergy (int maxReceive, boolean simulate) {
		int recieved = super.receiveEnergy(maxReceive, simulate);
		if (recieved > 0) owner.notifyChange(CapabilityEnergy.ENERGY);
		return recieved;
	}
	
	@Override
	public int extractEnergy (int maxExtract, boolean simulate) {
		int extracted = super.extractEnergy(maxExtract, simulate);
		if (extracted > 0) owner.notifyChange(CapabilityEnergy.ENERGY);
		return extracted;
	}
	
	/**
	 * @param capability the capability to test
	 * @param facing the direction to test it in
	 * @return whether the capability is applicable in this direction
	 */
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY;
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing, ModuleContext capacity) {
		return (T) this;
	}
}
