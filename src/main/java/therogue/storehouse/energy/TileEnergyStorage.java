
package therogue.storehouse.energy;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.tile.ITileModule;
import therogue.storehouse.tile.ModuleContext;

public class TileEnergyStorage extends EnergyStorageAdv implements ITileModule {
	
	private int RFPerTick = 0;
	
	public TileEnergyStorage (int capacity) {
		super(capacity);
	}
	
	public TileEnergyStorage (int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}
	
	public TileEnergyStorage (int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
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
