
package therogue.storehouse.crafting;

import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import therogue.storehouse.crafting.wrapper.EnergyWrapper;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.ModuleContext;

public class EnergyInventory implements IRecipeInventory {
	
	private final IEnergyStorage compose;
	
	public EnergyInventory (IEnergyStorage compose) {
		this.compose = compose;
	}
	
	@Override
	public IRecipeWrapper getComponent (int slot) {
		if (slot == 0) return new EnergyWrapper(compose.getEnergyStored());
		return IRecipeWrapper.NOTHING;
	}
	
	@Override
	public void insertComponent (int slot, IRecipeWrapper component, boolean simulate) {
		if (slot == 0)
		{
			EnergyWrapper wrapper = new EnergyWrapper(0);
			wrapper.merge(component, compose.getMaxEnergyStored());
			compose.receiveEnergy(wrapper.getSize(), simulate);
		}
	}
	
	@Override
	public IRecipeWrapper extractComponent (int slot, int amount, boolean simulate) {
		if (slot == 0) return new EnergyWrapper(compose.extractEnergy(amount, simulate));
		return IRecipeWrapper.NOTHING;
	}
	
	@Override
	public int getComponentSlotLimit (int slot) {
		if (slot == 0) return compose.getMaxEnergyStored();
		return 0;
	}
	
	@Override
	public int getSize () {
		return 1;
	}
	
	public static final EnergyInvConverter CONVERTER = new EnergyInvConverter();
	
	private static class EnergyInvConverter implements IRecipeInventoryConverter {
		
		public String getString () {
			return "ENG";
		}
		
		public IRecipeInventory getFromTile (ITile tile, int data[]) {
			return new EnergyInventory(tile.getCapability(CapabilityEnergy.ENERGY, null, ModuleContext.INTERNAL));
		}
	}
}
