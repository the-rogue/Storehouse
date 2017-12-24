
package therogue.storehouse.multiblock.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import therogue.storehouse.multiblock.block.IMultiBlockStateMapper;

public class NormalBlock implements IMultiBlockElement {
	
	private final IBlockState nonMultiBlockPart;
	private final IBlockState multiblockPart;
	public final boolean matchState;
	
	public NormalBlock (Block nonMultiBlockPart, IMultiBlockStateMapper multiblockpartGetter) {
		this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
		this.multiblockPart = multiblockpartGetter.getMultiBlockState(nonMultiBlockPart.getDefaultState());
		matchState = false;
	}
	
	public NormalBlock (IBlockState nonMultiBlockPart, IMultiBlockStateMapper multiblockpartGetter) {
		this.nonMultiBlockPart = nonMultiBlockPart;
		this.multiblockPart = multiblockpartGetter.getMultiBlockState(nonMultiBlockPart);
		matchState = true;
	}
	
	public NormalBlock (Block nonMultiBlockPart, Block multiblockPart) {
		this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
		this.multiblockPart = multiblockPart.getDefaultState();
		matchState = false;
	}
	
	public NormalBlock (Block nonMultiBlockPart, IBlockState multiblockPart) {
		this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
		this.multiblockPart = multiblockPart;
		matchState = false;
	}
	
	public NormalBlock (IBlockState nonMultiBlockPart, IBlockState multiblockPart) {
		this.nonMultiBlockPart = nonMultiBlockPart;
		this.multiblockPart = multiblockPart;
		matchState = true;
	}
	
	@Override
	public boolean isValidBlock (IBlockState state, boolean sameBlock) {
		return matchState ? this.nonMultiBlockPart == state : this.nonMultiBlockPart.getBlock() == state.getBlock();
	}
	
	@Override
	public IBlockState getMultiBlockState (IBlockState originalState) {
		return multiblockPart;
	}
	
	@Override
	public String toString () {
		return nonMultiBlockPart.getBlock().toString();
	}
}
