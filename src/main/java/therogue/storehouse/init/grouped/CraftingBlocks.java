
package therogue.storehouse.init.grouped;

import net.minecraft.item.ItemStack;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseVariantBlock;
import therogue.storehouse.init.ModBlocks;

public enum CraftingBlocks {
	MACHINE_CASING,
	ADVANCED_MACHINE_CASING;
	
	public static StorehouseBaseVariantBlock crafting_block;
	
	public ItemStack createStack () {
		return createStack(1);
	}
	
	public ItemStack createStack (int amount) {
		return new ItemStack(crafting_block, amount, this.ordinal());
	}
	
	public static IStorehouseBaseBlock addMaterials () {
		crafting_block = new StorehouseBaseVariantBlock("crafting_block");
		ModBlocks.blocklist.add(crafting_block);
		for (CraftingBlocks c : CraftingBlocks.values())
			crafting_block.addSubBlock(c.ordinal(), c.name().toLowerCase());
		return crafting_block;
	}
	
	public static void Init () {
	}
}
