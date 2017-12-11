
package therogue.storehouse.init.grouped;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.multiblock.BlockSpecialMultiblock;
import therogue.storehouse.block.multiblock.IMultiblockCapabilityProvider;

public enum MultiblockBlocks implements IMultiblockCapabilityProvider {
	TAP (Lists.newArrayList(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)),
	TANK (Lists.newArrayList(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)),
	EJECTOR (Lists.newArrayList(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)),
	CHUTE (Lists.newArrayList(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)),
	POWER_SUPPLY (Lists.newArrayList()),
	POWER_CONNECTOR (Lists.newArrayList());
	
	public static BlockSpecialMultiblock crafting_block;
	public List<Capability<?>> capabilities;
	
	private MultiblockBlocks (List<Capability<?>> capabilities) {
		this.capabilities = capabilities;
	}
	
	public ItemStack createStack () {
		return createStack(1);
	}
	
	public ItemStack createStack (int amount) {
		return new ItemStack(crafting_block, amount, this.ordinal());
	}
	
	public static IStorehouseBaseBlock addMaterials () {
		crafting_block = new BlockSpecialMultiblock("crafting_block", MultiblockBlocks.values());
		for (MultiblockBlocks c : MultiblockBlocks.values())
			crafting_block.addSubBlock(c.ordinal(), c.name().toLowerCase());
		return crafting_block;
	}
	
	public static void Init () {
	}
	
	@Override
	public List<Capability<?>> getCapabilities () {
		return capabilities;
	}
}
