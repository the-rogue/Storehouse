
package therogue.storehouse.block.multiblock;

import net.minecraft.block.state.IBlockState;

public interface IBlockWrapper {
	
	public IBlockState getWrappedState (IBlockState state);
}
