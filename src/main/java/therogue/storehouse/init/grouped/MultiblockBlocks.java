
package therogue.storehouse.init.grouped;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseVariantBlock;

public enum MultiblockBlocks {
	EJECTOR,
	CHUTE,
	ITEM_IO,
	TAP,
	TANK,
	FLUID_IO,
	ENERGY_TAP,
	ENERGY_CONNECTOR,
	ADVANCED_TAP,
	ADVANCED_CONNECTOR;
	
	public static StorehouseBaseVariantBlock crafting_block;
	
	private MultiblockBlocks () {
	}
	
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
		crafting_block = new StorehouseBaseVariantBlock("multiblock_blocks");
		crafting_block.hasConnectedTextureGroup("cb");
		for (MultiblockBlocks c : MultiblockBlocks.values())
			crafting_block.addSubBlock(c.ordinal(), c.name().toLowerCase());
		return crafting_block;
	}
	
	public static void Init () {
	}
}
