
package therogue.storehouse.multiblock.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import therogue.storehouse.GeneralUtils;
import therogue.storehouse.LOG;
import therogue.storehouse.block.BlockUtils;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.structure.MultiBlockStructure.StructureTest;

public class WorldStates {
	
	public final BlockPos position;
	public final IBlockState nonMultiblockState;
	public final IBlockState inMultiblockState;
	public final List<ICapabilityWrapper<?>> capabilites;
	
	public WorldStates (BlockPos position, IBlockState nonMultiblockState, IBlockState inMultiblockState, List<ICapabilityWrapper<?>> capabilites) {
		this.position = position;
		this.nonMultiblockState = nonMultiblockState;
		this.inMultiblockState = inMultiblockState;
		this.capabilites = ImmutableList.copyOf(capabilites);
	}
	
	@Override
	public String toString () {
		return "PositionStateChanger at: " + position;
	}
	
	public static NBTTagList writeToNBT (List<WorldStates> states) {
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
	public static List<WorldStates> readFromNBT (MultiBlockData data, IMultiBlockController controller, NBTTagList nbt) {
		Map<BlockPos, WorldStates> adjustedStates = new HashMap<>();
		for (int i = 0; i < nbt.tagCount(); i++)
		{
			NBTTagCompound stateTag = nbt.getCompoundTagAt(i);
			BlockPos position = BlockPos.fromLong(stateTag.getLong("Position"));
			ResourceLocation NMBName = new ResourceLocation(stateTag.getString("NMBName"));
			IBlockState nonMultiBlockState = Blocks.AIR.getDefaultState();
			if (ForgeRegistries.BLOCKS.containsKey(NMBName))
			{
				nonMultiBlockState = ForgeRegistries.BLOCKS.getValue(NMBName).getStateFromMeta((int) stateTag.getByte("NMBMeta"));
			}
			ResourceLocation MBName = new ResourceLocation(stateTag.getString("MBName"));
			IBlockState inMultiBlockState = Blocks.AIR.getDefaultState();
			if (ForgeRegistries.BLOCKS.containsKey(MBName))
			{
				inMultiBlockState = ForgeRegistries.BLOCKS.getValue(MBName).getStateFromMeta((int) stateTag.getByte("MBMeta"));
			}
			BlockPos arrayPosition = position.subtract(data.originInWorld).rotate(BlockUtils.rotationInverse(data.rotation));
			adjustedStates.put(arrayPosition, new WorldStates(position, nonMultiBlockState, inMultiBlockState, new ArrayList<>()));
		}
		try
		{
			List<WorldStates> states = new ArrayList<>();
			for (MultiBlockStructure tryStructure : controller.getPossibleStructures())
			{
				StructureTest structure = tryStructure.newStructureTest();
				boolean blocksMatch = true;
				while (structure.next() && blocksMatch)
				{
					IBlockState blockstate = adjustedStates.get(structure.getCurrentPosition()).nonMultiblockState;
					if (!structure.isValidBlock(blockstate, false)) blocksMatch = false;
				}
				if (!blocksMatch) continue;
				structure.resetPosition();
				while (structure.next())
				{
					WorldStates oldState = adjustedStates.get(structure.getCurrentPosition());
					states.add(new WorldStates(oldState.position, oldState.nonMultiblockState, oldState.inMultiblockState, structure.getCapabilites(oldState.nonMultiblockState)));
				}
				return states;
			}
			throw new IllegalArgumentException(String.format("The states given are not valid for the controller: %s,\nwith states: %s,\nand position: %s", controller.getTile(), adjustedStates, controller.getPosition()));
		}
		catch (NullPointerException | IllegalArgumentException e)
		{
			LOG.warn("Failed to restore Capabilities for multiblock at: " + adjustedStates.values().toArray()[0] + ". Please break and replace a block in it.");
			return new ArrayList<>(adjustedStates.values());
		}
	}
	
	public static class MultiBlockData {
		
		public final BlockPos originInWorld;
		public final Rotation rotation;
		
		public MultiBlockData (BlockPos originInWorld, Rotation rotation) {
			this.originInWorld = originInWorld;
			this.rotation = rotation;
		}
		
		public static NBTTagCompound writeToNBT (MultiBlockData data) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setLong("origin_in_world", data.originInWorld.toLong());
			nbt.setInteger("rotation_ordinal", data.rotation.ordinal());
			return nbt;
		}
		
		public static MultiBlockData readFromNBT (NBTTagCompound nbt) {
			return new MultiBlockData(BlockPos.fromLong(nbt.getLong("origin_in_world")), GeneralUtils.getEnumFromNumber(Rotation.class, nbt.getInteger("rotation_ordinal")));
		}
	}
}