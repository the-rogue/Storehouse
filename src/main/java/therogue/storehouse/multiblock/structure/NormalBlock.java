
package therogue.storehouse.multiblock.structure;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import therogue.storehouse.multiblock.block.IBlockWrapper;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;

public class NormalBlock implements IMultiBlockElement {
	
	private final IBlockState nonMultiBlockPart;
	private final IBlockWrapper multiblockPart;
	private final List<ICapabilityWrapper<?>> capabilities = new ArrayList<>();
	public final boolean matchState;
	
	public NormalBlock (Block nonMultiBlockPart, IBlockWrapper multiblockpartGetter) {
		this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
		this.multiblockPart = multiblockpartGetter;
		matchState = false;
	}
	
	public NormalBlock (IBlockState nonMultiBlockPart, IBlockWrapper multiblockpartGetter) {
		this.nonMultiBlockPart = nonMultiBlockPart;
		this.multiblockPart = multiblockpartGetter;
		matchState = true;
	}
	
	public NormalBlock (Block nonMultiBlockPart, Block multiblockPart) {
		this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
		this.multiblockPart = (state) -> multiblockPart.getDefaultState();
		matchState = false;
	}
	
	public NormalBlock (Block nonMultiBlockPart, IBlockState multiblockPart) {
		this.nonMultiBlockPart = nonMultiBlockPart.getDefaultState();
		this.multiblockPart = (state) -> multiblockPart;
		matchState = false;
	}
	
	public NormalBlock (IBlockState nonMultiBlockPart, IBlockState multiblockPart) {
		this.nonMultiBlockPart = nonMultiBlockPart;
		this.multiblockPart = (state) -> multiblockPart;
		matchState = true;
	}
	
	public NormalBlock addCapability (ICapabilityWrapper<?>... wrapperlist) {
		for (ICapabilityWrapper<?> wrapper : wrapperlist)
		{
			capabilities.add(wrapper);
		}
		return this;
	}
	
	@Override
	public boolean isValidBlock (IBlockState state, boolean sameBlock) {
		return matchState ? this.nonMultiBlockPart == state : this.nonMultiBlockPart.getBlock() == state.getBlock();
	}
	
	@Override
	public IBlockState getMultiBlockState (IBlockState originalState) {
		return multiblockPart.getWrappedState(originalState);
	}
	
	@Override
	public String toString () {
		return nonMultiBlockPart.getBlock().toString();
	}
	
	@Override
	public List<ICapabilityWrapper<?>> getCapabilities (IBlockState originalState) {
		return ImmutableList.copyOf(capabilities);
	}
}
