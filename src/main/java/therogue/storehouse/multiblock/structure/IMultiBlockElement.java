
package therogue.storehouse.multiblock.structure;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;

public interface IMultiBlockElement {
	
	public boolean isValidBlock (IBlockState state, boolean sameBlock);
	
	public IBlockState getMultiBlockState (IBlockState originalState);
	
	public List<ICapabilityWrapper<?>> getCapabilities (IBlockState originalState);
}
