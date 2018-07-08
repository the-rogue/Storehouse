
package therogue.storehouse.multiblock.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import therogue.storehouse.multiblock.block.IBlockWrapper;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;

public class VariableBlock implements IMultiBlockElement {
	
	private final IBlockState[] nonMultiBlockParts;
	private final IBlockWrapper multiblockPart;
	private final Map<IBlockState, List<ICapabilityWrapper<?>>> capabilities = new HashMap<>();
	public final int matchStateAfter;
	
	public VariableBlock (IBlockWrapper multiblockpartGetter, Block... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = multiblockpartGetter;
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
			capabilities.put(this.nonMultiBlockParts[i], new ArrayList<>());
		}
		matchStateAfter = nonMultiBlockParts.length;
	}
	
	public VariableBlock (IBlockWrapper multiblockpartGetter, Block[] nonMultiBlockBlocks, IBlockState... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length + nonMultiBlockBlocks.length];
		this.multiblockPart = multiblockpartGetter;
		for (int i = 0; i < nonMultiBlockBlocks.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockBlocks[i].getDefaultState();
			capabilities.put(nonMultiBlockBlocks[i].getDefaultState(), new ArrayList<>());
		}
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i + nonMultiBlockBlocks.length] = nonMultiBlockParts[i];
			capabilities.put(nonMultiBlockParts[i], new ArrayList<>());
		}
		matchStateAfter = nonMultiBlockBlocks.length;
	}
	
	public VariableBlock (IBlockWrapper multiblockpartGetter, IBlockState... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = multiblockpartGetter;
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i];
			capabilities.put(this.nonMultiBlockParts[i], new ArrayList<>());
		}
		matchStateAfter = 0;
	}
	
	public VariableBlock (Block[] multiblockParts, Block... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = (state) -> {
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				if (state.getBlock() == nonMultiBlockParts[i]) { return multiblockParts[i].getDefaultState(); }
			}
			throw new IllegalArgumentException("The modder obviously didnt check if it was a valid block before calling this method");
		};
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
			capabilities.put(this.nonMultiBlockParts[i], new ArrayList<>());
		}
		matchStateAfter = nonMultiBlockParts.length;
	}
	
	public VariableBlock (IBlockState[] multiblockParts, Block... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = (state) -> {
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				if (state.getBlock() == nonMultiBlockParts[i]) { return multiblockParts[i]; }
			}
			throw new IllegalArgumentException("The modder obviously didnt check if it was a valid block before calling this method");
		};
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i].getDefaultState();
			capabilities.put(this.nonMultiBlockParts[i], new ArrayList<>());
		}
		matchStateAfter = nonMultiBlockParts.length;
	}
	
	public VariableBlock (IBlockState[] multiblockParts, IBlockState... nonMultiBlockParts) {
		this.nonMultiBlockParts = new IBlockState[nonMultiBlockParts.length];
		this.multiblockPart = (state) -> {
			for (int i = 0; i < nonMultiBlockParts.length; i++)
			{
				if (state == nonMultiBlockParts[i]) { return multiblockParts[i]; }
			}
			throw new IllegalArgumentException("The modder obviously didnt check if it was a valid block before calling this method");
		};
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			this.nonMultiBlockParts[i] = nonMultiBlockParts[i];
			capabilities.put(this.nonMultiBlockParts[i], new ArrayList<>());
		}
		matchStateAfter = 0;
	}
	
	public VariableBlock addCapability (ICapabilityWrapper<?>... wrapperlist) {
		for (IBlockState state : nonMultiBlockParts)
		{
			for (ICapabilityWrapper<?> wrapper : wrapperlist)
			{
				capabilities.get(state).add(wrapper);
			}
		}
		return this;
	}
	
	public VariableBlock addCapability (IBlockState state, ICapabilityWrapper<?>... wrapperlist) {
		List<ICapabilityWrapper<?>> capabilityMap = capabilities.get(state);
		if (capabilityMap == null) throw new NullPointerException("The state the capability is being added for is not a multiblock state: " + state.getBlock());
		for (ICapabilityWrapper<?> wrapper : wrapperlist)
		{
			capabilityMap.add(wrapper);
		}
		return this;
	}
	
	@Override
	public boolean isValidBlock (IBlockState block, boolean sameBlock) {
		for (int i = 0; i < nonMultiBlockParts.length; i++)
		{
			IBlockState test = nonMultiBlockParts[i];
			if (matchStateAfter <= i ? test == block : test.getBlock() == block.getBlock()) return true;
		}
		return false;
	}
	
	@Override
	public IBlockState getMultiBlockState (IBlockState originalState) {
		return multiblockPart.getWrappedState(originalState);
	}
	
	@Override
	public String toString () {
		return nonMultiBlockParts.toString();
	}
	
	@Override
	public List<ICapabilityWrapper<?>> getCapabilities (IBlockState originalState) {
		boolean ignoreState = false;
		for (int i = 0; i < matchStateAfter; i++)
		{
			IBlockState test = nonMultiBlockParts[i];
			if (originalState.getBlock() == test.getBlock())
			{
				ignoreState = true;
			}
		}
		if (ignoreState)
		{
			for (Entry<IBlockState, List<ICapabilityWrapper<?>>> state : capabilities.entrySet())
			{
				if (state.getKey().getBlock() == originalState.getBlock()) { return state.getValue(); }
			}
			throw new IllegalArgumentException("Original State does not have a block entry in the capabilities set");
		}
		return ImmutableList.copyOf(capabilities.get(originalState));
	}
}