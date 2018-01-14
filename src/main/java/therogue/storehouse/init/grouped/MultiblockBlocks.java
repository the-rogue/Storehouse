
package therogue.storehouse.init.grouped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseCapabilityVariantBlock;
import therogue.storehouse.capabilitywrapper.EnergyWrapper;
import therogue.storehouse.capabilitywrapper.FluidWrapper;
import therogue.storehouse.capabilitywrapper.ICapabilityWrapper;
import therogue.storehouse.capabilitywrapper.ItemWrapper;

public enum MultiblockBlocks {
	EJECTOR (ItemWrapper.EXTRACT),
	CHUTE (ItemWrapper.INSERT),
	ITEM_IO (ItemWrapper.BOTH),
	TAP (FluidWrapper.DRAIN),
	TANK (FluidWrapper.FILL),
	FLUID_IO (FluidWrapper.BOTH),
	ENERGY_TAP (EnergyWrapper.EXTRACT),
	ENERGY_CONNECTOR (EnergyWrapper.RECIEVE),
	ADVANCED_TAP (ItemWrapper.BOTH, FluidWrapper.BOTH, EnergyWrapper.EXTRACT),
	ADVANCED_CONNECTOR (ItemWrapper.BOTH, FluidWrapper.BOTH, EnergyWrapper.RECIEVE);
	
	public static StorehouseBaseCapabilityVariantBlock crafting_block;
	public Map<Capability<?>, ICapabilityWrapper<?>> capabilities = new HashMap<Capability<?>, ICapabilityWrapper<?>>();
	
	private MultiblockBlocks (ICapabilityWrapper<?>... capabilities) {
		for (ICapabilityWrapper<?> c : capabilities)
		{
			this.capabilities.put(c.getSupportedCapability(), c);
		}
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
		List<Map<Capability<?>, ICapabilityWrapper<?>>> capabilites = new ArrayList<Map<Capability<?>, ICapabilityWrapper<?>>>();
		for (MultiblockBlocks c : MultiblockBlocks.values())
			capabilites.add(c.capabilities);
		crafting_block = new StorehouseBaseCapabilityVariantBlock("multiblock_blocks", capabilites);
		for (MultiblockBlocks c : MultiblockBlocks.values())
			crafting_block.addSubBlock(c.ordinal(), c.name().toLowerCase());
		return crafting_block;
	}
	
	public static void Init () {
	}
}
