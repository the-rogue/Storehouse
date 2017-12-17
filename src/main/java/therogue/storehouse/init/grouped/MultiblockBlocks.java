
package therogue.storehouse.init.grouped;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.multiblock.BlockSpecialMultiblock;
import therogue.storehouse.block.multiblock.IMultiblockCapabilityProvider;
import therogue.storehouse.capabilityWrapper.EnergyWrapper;
import therogue.storehouse.capabilityWrapper.FluidWrapper;
import therogue.storehouse.capabilityWrapper.ItemWrapper;
import therogue.storehouse.tile.multiblock.ICapabilityWrapper;

public enum MultiblockBlocks implements IMultiblockCapabilityProvider {
	TAP (FluidWrapper.DRAIN),
	TANK (FluidWrapper.FILL),
	EJECTOR (ItemWrapper.EXTRACT),
	CHUTE (ItemWrapper.INSERT),
	POWER_SUPPLY (EnergyWrapper.EXTRACT),
	POWER_CONNECTOR (EnergyWrapper.RECIEVE);
	
	public static BlockSpecialMultiblock crafting_block;
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
	
	public static IStorehouseBaseBlock addMaterials () {
		crafting_block = new BlockSpecialMultiblock("multiblock_blocks", MultiblockBlocks.values());
		for (MultiblockBlocks c : MultiblockBlocks.values())
			crafting_block.addSubBlock(c.ordinal(), c.name().toLowerCase());
		return crafting_block;
	}
	
	public static void Init () {
	}
	
	@Override
	public Map<Capability<?>, ICapabilityWrapper<?>> getCapabilities () {
		return capabilities;
	}
}
