
package therogue.storehouse.multiblock.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class WorldStates {
	
	private static IForgeRegistry<Block> blockRegistry;
	public final BlockPos position;
	public final IBlockState nonMultiblockState;
	public final IBlockState inMultiblockState;
	
	public WorldStates (BlockPos position, IBlockState nonMultiblockState, IBlockState inMultiblockState) {
		this.position = position;
		this.nonMultiblockState = nonMultiblockState;
		this.inMultiblockState = inMultiblockState;
	}
	
	@Override
	public String toString () {
		return "PositionStateChanger at: " + position;
	}
	
	public static NBTTagList writeToNBT (List<WorldStates> states) {
		if (blockRegistry == null) initBlockRegistry();
		NBTTagList tag = new NBTTagList();
		for (WorldStates state : states)
		{
			NBTTagCompound stateTag = new NBTTagCompound();
			stateTag.setLong("Position", state.position.toLong());
			if (state.nonMultiblockState != null)
			{
				stateTag.setString("NMBName", state.nonMultiblockState.getBlock().getRegistryName().toString());
				stateTag.setByte("NMBMeta", (byte) state.nonMultiblockState.getBlock().getMetaFromState(state.nonMultiblockState));
			}
			if (state.inMultiblockState != null)
			{
				stateTag.setString("MBName", state.inMultiblockState.getBlock().getRegistryName().toString());
				stateTag.setByte("MBMeta", (byte) state.inMultiblockState.getBlock().getMetaFromState(state.inMultiblockState));
			}
			tag.appendTag(stateTag);
		}
		return tag;
	}
	
	@SuppressWarnings ("deprecation")
	public static List<WorldStates> readFromNBT (NBTTagList nbt) {
		if (blockRegistry == null) initBlockRegistry();
		List<WorldStates> states = new ArrayList<WorldStates>();
		for (int i = 0; i < nbt.tagCount(); i++)
		{
			NBTTagCompound stateTag = nbt.getCompoundTagAt(i);
			BlockPos position = BlockPos.fromLong(stateTag.getLong("Position"));
			ResourceLocation NMBName = new ResourceLocation(stateTag.getString("NMBName"));
			IBlockState nonMultiBlockState = Blocks.AIR.getDefaultState();
			if (blockRegistry.containsKey(NMBName))
			{
				nonMultiBlockState = blockRegistry.getValue(NMBName).getStateFromMeta((int) stateTag.getByte("NMBMeta"));
			}
			ResourceLocation MBName = new ResourceLocation(stateTag.getString("MBName"));
			IBlockState inMultiBlockState = Blocks.AIR.getDefaultState();
			if (blockRegistry.containsKey(MBName))
			{
				inMultiBlockState = blockRegistry.getValue(MBName).getStateFromMeta((int) stateTag.getByte("MBMeta"));
			}
			states.add(new WorldStates(position, nonMultiBlockState, inMultiBlockState));
		}
		return states;
	}
	
	private static void initBlockRegistry () {
		if (blockRegistry != null) return;
		blockRegistry = GameRegistry.findRegistry(Block.class);
		if (blockRegistry == null) throw new IllegalStateException("Block Registry CANNOT be null when reading NBT");
	}
}