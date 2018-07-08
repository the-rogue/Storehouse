
package therogue.storehouse.tile.machine;

import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.StorehouseBaseTileMultiBlock;

public class TileBurner extends StorehouseBaseTileMultiBlock {
	
	protected final MachineCraftingHandler<TileBurner>.CraftingManager theCrafter;
	
	public TileBurner () {
		super(ModBlocks.burner);
		this.setEnergyStorage(new TileEnergyStorage(this, 1200000, 640, 0));
		this.setInventory(new InventoryManager(this, 6, new Integer[] { 1, 2, 3, 4, 5 }, new Integer[] { 0 }));
		theCrafter = MachineCraftingHandler.getHandler(TileBurner.class).newCrafter(this, "ITM 1 6", "ITM 0 1", energyStorage);
		modules.add(theCrafter);
		energyStorage.setRFPerTick(640);
		inventory.setItemValidForSlotChecks( (index, stack) -> {
			int[] IDs = OreDictionary.getOreIDs(stack);
			for (int id : IDs)
			{
				String name = OreDictionary.getOreName(id);
				if (name.contains("crop") || name.contains("tree")
						|| name.contains("vine")
						|| name.contains("sugarcane")
						|| name.contains("cactus"))
					return true;
			}
			return false;
		});
		containerFactory = (player) -> new ContainerBase(player.inventory, this).setTESlotList(inventory.guiAccess, new int[] { 0, 120, 37, 1, 50, 17, 2, 50, 37, 3, 50, 57, 4, 70, 27, 5, 70, 47 });
		this.setElementString("ENERGYBAR 8 8,  PROGRESS_BAR CRFT_TL CRFT_TT J( RBAR 92 44 12 2 )J( RARROW 104 39 )");
	}
}
