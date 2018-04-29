
package therogue.storehouse.crafting.inventory;

import therogue.storehouse.crafting.wrapper.EnergyWrapper;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.energy.EnergyStorageAdv;

public class EnergyInventory implements IRecipeInventory {
	
	private final EnergyStorageAdv compose;
	
	public EnergyInventory (EnergyStorageAdv compose) {
		this.compose = compose;
	}
	
	@Override
	public IRecipeWrapper getComponent (int slot, boolean simulate) {
		if (slot == 0) return new EnergyWrapper(compose.getEnergyStored());
		return IRecipeWrapper.NOTHING;
	}
	
	@Override
	public void insertComponent (int slot, IRecipeWrapper component) {
		if (slot == 0)
		{
			EnergyWrapper wrapper = new EnergyWrapper(compose.getEnergyStored());
			wrapper.merge(component, compose.getMaxEnergyStored());
			compose.setEnergyStored(wrapper.getSize());
		}
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
}