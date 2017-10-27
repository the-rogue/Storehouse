
package therogue.storehouse.init.grouped;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.block.StorehouseBaseVariantBlock;

public enum Ores {
	AZURITE,
	COPPER,
	TIN,
	ALUMINIUM,
	SILVER,
	LEAD,
	TITANIUM;
	
	public static StorehouseBaseVariantBlock ore_block;
	
	public ItemStack createStack () {
		return createStack(1);
	}
	
	public ItemStack createStack (int amount) {
		return new ItemStack(ore_block, amount, this.ordinal());
	}
	
	public static StorehouseBaseVariantBlock addMaterials () {
		ore_block = new StorehouseBaseVariantBlock("ore_block");
		for (Ores o : Ores.values())
			ore_block.addSubBlock(o.ordinal(), o.name().toLowerCase());
		return ore_block;
	}
	
	public static void Init () {
		ore_block.addDrop(AZURITE.ordinal(), Resources.AZURITE_DUST.createStack(), 3, 6);
		OreDictionary.registerOre("oreAzurite", AZURITE.createStack());
		OreDictionary.registerOre("oreCopper", COPPER.createStack());
		OreDictionary.registerOre("oreTin", TIN.createStack());
		OreDictionary.registerOre("oreAluminum", ALUMINIUM.createStack());
		OreDictionary.registerOre("oreSilver", SILVER.createStack());
		OreDictionary.registerOre("oreLead", LEAD.createStack());
		OreDictionary.registerOre("oreTitanium", TITANIUM.createStack());
	}
}
