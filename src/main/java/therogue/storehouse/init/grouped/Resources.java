
package therogue.storehouse.init.grouped;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.item.ItemStorehouseBaseMaterial;

public enum Resources
{
	AZURITE_DUST,
	AZURITE_CRYSTAL,
	COPPER_INGOT,
	TIN_INGOT,
	ALUMINUM_INGOT,
	SILVER_INGOT,
	LEAD_INGOT,
	TITANIUM_INGOT,
	STEEL_INGOT,
	BRASS_INGOT,
	BRONZE_INGOT,
	DURALIUM_INGOT;
	
	public static ItemStorehouseBaseMaterial resources;
	
	public ItemStack createStack () {
		return createStack(1);
	}
	
	public ItemStack createStack (int amount) {
		return new ItemStack(resources, amount, this.ordinal());
	}
	
	public static void addMaterials () {
		resources = new ItemStorehouseBaseMaterial("resources");
		ModItems.itemlist.add(resources);
		for (Resources r : Resources.values())
			resources.addMaterial(r.ordinal(), r.name().toLowerCase());

	}
	
	public static void Init () {
		OreDictionary.registerOre("dustAzurite", AZURITE_DUST.createStack());
		OreDictionary.registerOre("ingotCopper", COPPER_INGOT.createStack());
		OreDictionary.registerOre("ingotTin", TIN_INGOT.createStack());
		OreDictionary.registerOre("ingotAluminum", ALUMINUM_INGOT.createStack());
		OreDictionary.registerOre("ingotSilver", SILVER_INGOT.createStack());
		OreDictionary.registerOre("ingotLead", LEAD_INGOT.createStack());
		OreDictionary.registerOre("ingotTitanium", TITANIUM_INGOT.createStack());
		OreDictionary.registerOre("ingotSteel", STEEL_INGOT.createStack());
		OreDictionary.registerOre("ingotBrass", BRASS_INGOT.createStack());
		OreDictionary.registerOre("ingotBronze", BRONZE_INGOT.createStack());
		OreDictionary.registerOre("ingotDuralium", DURALIUM_INGOT.createStack());
	}
}
