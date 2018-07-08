
package therogue.storehouse.multiblock.block;

import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface IBlockWrapper {
	
	public IBlockState getWrappedState (IBlockState state);
}
