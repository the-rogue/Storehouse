
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
import therogue.storehouse.capabilitywrapper.EnergyCapabilityWrapper;
import therogue.storehouse.capabilitywrapper.FluidCapabilityWrapper;
import therogue.storehouse.capabilitywrapper.ICapabilityWrapper;
import therogue.storehouse.capabilitywrapper.ItemCapabilityWrapper;

public enum MultiblockBlocks {
	EJECTOR (ItemCapabilityWrapper.EXTRACT),
	CHUTE (ItemCapabilityWrapper.INSERT),
	ITEM_IO (ItemCapabilityWrapper.BOTH),
	TAP (FluidCapabilityWrapper.DRAIN),
	TANK (FluidCapabilityWrapper.FILL),
	FLUID_IO (FluidCapabilityWrapper.BOTH),
	ENERGY_TAP (EnergyCapabilityWrapper.EXTRACT),
	ENERGY_CONNECTOR (EnergyCapabilityWrapper.RECIEVE),
	ADVANCED_TAP (ItemCapabilityWrapper.BOTH, FluidCapabilityWrapper.BOTH, EnergyCapabilityWrapper.EXTRACT),
	ADVANCED_CONNECTOR (ItemCapabilityWrapper.BOTH, FluidCapabilityWrapper.BOTH, EnergyCapabilityWrapper.RECIEVE);
	
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
