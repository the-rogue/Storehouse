
package therogue.storehouse.tile.machine;

import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModMultiBlocks;
import therogue.storehouse.init.grouped.Materials;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.inventory.ItemStackUtils;
import therogue.storehouse.tile.StorehouseBaseTileMultiBlock;

public class TileCarbonCompressor extends StorehouseBaseTileMultiBlock {
	
	protected final MachineCraftingHandler<TileCarbonCompressor>.CraftingManager theCrafter;
	
	public TileCarbonCompressor () {
		super(ModBlocks.carbonCompressor, ModMultiBlocks.carbonCompressorStructure);
		this.setEnergyStorage(new TileEnergyStorage(this, 1200000, 640, 0));
		this.setInventory(new InventoryManager(this, 2, new Integer[] { 1 }, new Integer[] { 0 }));
		theCrafter = MachineCraftingHandler.getHandler(TileCarbonCompressor.class).newCrafter(this, "ITM 1 2", "ITM 0 1", energyStorage);
		modules.add(theCrafter);
		energyStorage.setRFPerTick(640);
		inventory.setItemValidForSlotChecks( (index, stack) -> ItemStackUtils.areStacksMergable(stack, Materials.CARBON.createStack()) ? true : false);
	}
}
