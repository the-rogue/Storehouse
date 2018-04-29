
package therogue.storehouse.init.grouped;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.Storehouse;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseVariantBlock;
import therogue.storehouse.client.connectedtextures.CTBlockRegistry;
import therogue.storehouse.client.connectedtextures.IConnectedTextureLogic;

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
		crafting_block = new StorehouseBaseVariantBlock.VBCT("crafting_block");
		CTBlockRegistry.INSTANCE.register(new IConnectedTextureLogic() {
			
			@Override
			public ResourceLocation getModelLocation () {
				return crafting_block.getRegistryName();
			}
			
			@Override
			public List<ResourceLocation> getTextures () {
				return Lists.newArrayList(new ResourceLocation(Storehouse.MOD_ID, "blocks/machine/iron_casing"));
			}
		});
		for (CraftingBlocks c : CraftingBlocks.values())
			crafting_block.addSubBlock(c.ordinal(), c.name().toLowerCase());
		return crafting_block;
	}
	
	public static void Init () {
	}
}
