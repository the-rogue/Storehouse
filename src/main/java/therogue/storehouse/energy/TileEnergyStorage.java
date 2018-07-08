
package therogue.storehouse.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.tile.ITickableModule;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.ModuleContext;

public class TileEnergyStorage extends EnergyStorageAdv implements ITickableModule {
	
	private ITile owner;
	private int RFPerTick = 0;
	private boolean shouldRemove = false;
	public final List<BlockPos> sendEnergyFrom = new ArrayList<>();
	
	public TileEnergyStorage (ITile owner, int capacity) {
		super(capacity);
		this.owner = owner;
	}
	
	public TileEnergyStorage (ITile owner, int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
		this.owner = owner;
	}
	
	public TileEnergyStorage (ITile owner, int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
		this.owner = owner;
	}
	
	public void setRFPerTick (int RFPerTick) {
		this.RFPerTick = RFPerTick;
	}
	
	public boolean canRun () {
		return 0 <= energy - RFPerTick && energy - RFPerTick <= capacity;
	}
	
	public void runTick () {
		if (canRun()) this.modifyEnergyStored(-RFPerTick);
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
	
	@Override
	public void update () {
		sendEnergyFrom.forEach(pos -> EnergyUtils.sendEnergytoAll(this, owner.getTileWorld(), pos));
	}
	
	@Override
	public boolean stillTicking () {
		return sendEnergyFrom.size() != 0;
	}
	
	/**
	 * When the tile is removed from the world (i.e. invalidated)
	 */
	@Override
	public void onRemove () {
		shouldRemove = true;
	}
	
	@Override
	public boolean shouldRemove () {
		return shouldRemove;
	}
}
