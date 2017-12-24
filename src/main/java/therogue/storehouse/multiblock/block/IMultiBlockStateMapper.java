
package therogue.storehouse.multiblock.block;

import net.minecraft.block.state.IBlockState;

public interface IMultiBlockStateMapper {
	
	public IBlockState getMultiBlockState (IBlockState state);
}
