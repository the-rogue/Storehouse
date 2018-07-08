
package therogue.storehouse.block;

import net.minecraft.block.state.IBlockState;

public interface IWrappedBlock {
	
	public IBlockState unwrap (IBlockState wrapped);
}
