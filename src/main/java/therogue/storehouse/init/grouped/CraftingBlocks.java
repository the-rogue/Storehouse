
package therogue.storehouse.init.grouped;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseVariantBlock;

public enum CraftingBlocks {
	MACHINE_CASING,
	ADVANCED_MACHINE_CASING,
	REGULATOR,
	GRILL,
	CONTROL_CIRCUITRY,
	FAN;
	
	public static StorehouseBaseVariantBlock crafting_block;
	
	public ItemStack createStack () {
		return createStack(1);
	}
	
	public ItemStack createStack (int amount) {
		return new ItemStack(crafting_block, amount, this.ordinal());
	}
	
	public IBlockState getState () {
		return crafting_block.getStateFromMeta(this.ordinal());
	}
	
	public static IStorehouseBaseBlock addMaterials () {
		crafting_block = new StorehouseBaseVariantBlock("crafting_block");
		crafting_block.hasConnectedTextureGroup("cb");
		for (CraftingBlocks c : CraftingBlocks.values())
			crafting_block.addSubBlock(c.ordinal(), c.name().toLowerCase());
		return crafting_block;
	}
	
	public static void Init () {
	}
}
