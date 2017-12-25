
package therogue.storehouse.multiblock.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import therogue.storehouse.multiblock.block.IMultiBlockStateMapper;

public class VariableBlock implements IMultiBlockElement {
	
	private final IBlockState[] nonMultiBlockParts;
	private final IBlockState[] multiblockPart;
	public final boolean matchState;
	
	public VariableBlock (IMultiBlockStateMapper multiblockpartGetter, Block... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
			this.multiblockPart[i] = multiblockpartGetter.getMultiBlockState(nonMultiBlockParts[i].getDefaultState());
		}
		matchState = false;
	}
	
	public VariableBlock (IMultiBlockStateMapper multiblockpartGetter, IBlockState... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i];
			this.multiblockPart[i] = multiblockpartGetter.getMultiBlockState(nonMultiBlockParts[i]);
		}
		matchState = true;
	}
	
	public VariableBlock (Block[] multiblockParts, Block... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
			this.multiblockPart[i] = multiblockParts[i].getDefaultState();
		}
		matchState = false;
	}
	
	public VariableBlock (IBlockState[] multiblockParts, Block... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
			this.multiblockPart[i] = multiblockParts[i];
		}
		matchState = false;
	}
	
	public VariableBlock (IBlockState[] multiblockParts, IBlockState... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = new IBlockState[nonMultiBlockParts.length];
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i];
			this.multiblockPart[i] = multiblockParts[i];
		}
		matchState = true;
	}
	
	@Override
	public boolean isValidBlock (IBlockState block, boolean sameBlock) {
		for (IBlockState test : nonMultiBlockParts)
		{
			if (matchState ? test == block : test.getBlock() == block.getBlock()) return true;
		}
		return false;
	}
	
	@Override
	public IBlockState getMultiBlockState (IBlockState originalState) {
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			if (matchState ? nonMultiBlockParts[i] == originalState : nonMultiBlockParts[i].getBlock() == originalState.getBlock()) return multiblockPart[i];
		}
		throw new IllegalArgumentException("The modder obviously didnt check if it was a valid block before calling this method");
	}
	
	@Override
	public String toString () {
		return nonMultiBlockParts.toString();
	}
}