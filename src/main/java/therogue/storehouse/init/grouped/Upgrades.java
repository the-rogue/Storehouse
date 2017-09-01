
package therogue.storehouse.init.grouped;

import net.minecraft.item.ItemStack;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.item.ItemStorehouseBaseMaterial;

public enum Upgrades
{
	TIMER_UPGRADE,
	VARIABLE_CHANCE_UPGRADE,
	SPEED_UPGRADE,
	CRAFTING_LOOP_UPGRADE,
	WIRELESS_REDSTONE_UPGRADE,
	EJECTION_UPGRADE,
	IMPORTER_UPGRADE,
	INTERDIMENTIONAL_UPGRADE;
	
	public static ItemStorehouseBaseMaterial upgrades;
	
	public ItemStack createStack () {
		return createStack(1);
	}
	
	public ItemStack createStack (int amount) {
		return new ItemStack(upgrades, amount, this.ordinal());
	}
	
	public static void addMaterials () {
		upgrades = new ItemStorehouseBaseMaterial("upgrades");
		ModItems.itemlist.add(upgrades);
		for (Upgrades u : Upgrades.values())
			upgrades.addMaterial(u.ordinal(), u.name().toLowerCase());
	}
	
	public static void Init () {
	}
}
