
package therogue.storehouse.multiblock.structure;

import net.minecraft.block.state.IBlockState;

public interface IMultiBlockElement {
	
	public boolean isValidBlock (IBlockState state, boolean sameBlock);
	
	public IBlockState getMultiBlockState (IBlockState originalState);
}
