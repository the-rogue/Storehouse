
package therogue.storehouse.multiblock.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.LOG;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;
import therogue.storehouse.multiblock.block.IMultiBlockCapabilityBlock;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.structure.MultiBlockStructure.StructureTest;

public class InWorldUtils {
	
	public static MultiBlockFormationResult formMultiBlock (IMultiBlockController controller) {
		World controllerWorld = controller.getPositionWorld();
		MultiBlockCheckResult result = checkStructure(controllerWorld, controller.getPosition(), controller.getStructure());
		if (!result.valid) return new MultiBlockFormationResult(false, null);
		List<WorldStates> worldPositionStates = result.worldPositionsStates;
		for (WorldStates positionState : worldPositionStates)
		{
			if (positionState.inMultiblockState != null && !positionState.position.equals(controller.getPosition()))
			{
				controllerWorld.setBlockState(positionState.position, positionState.inMultiblockState);
				TileEntity te = controllerWorld.getTileEntity(positionState.position);
				if (te != null && te instanceof IMultiBlockTile)
				{
					((IMultiBlockTile) te).setController(controller);
				}
			}
		}
		return new MultiBlockFormationResult(true, worldPositionStates);
	}
	
	private static MultiBlockCheckResult checkStructure (World world, BlockPos checkerPos, MultiBlockStructure array) {
		for (BlockPos location : getPossibleLocations(world.getBlockState(checkerPos), array))
		{
			for (Rotation rotation : Rotation.values())
			{
				StructureTest blocktest = array.newStructureTest();
				if (checkLocation(world, checkerPos, rotation, location, blocktest))
				{
					blocktest.resetPosition();
					return getPositions(world, checkerPos, rotation, location, blocktest);
				}
			}
		}
		return new MultiBlockCheckResult(false, null);
	}
	
	private static List<BlockPos> getPossibleLocations (IBlockState block, MultiBlockStructure blockarray) {
		List<BlockPos> possibleLocations = new ArrayList<BlockPos>();
		StructureTest blocktest = blockarray.newStructureTest();
		while (blocktest.next())
		{
			if (blocktest.isValidBlock(block, true)) possibleLocations.add(blocktest.getCurrentPosition());
		}
		return possibleLocations;
	}
	
	private static boolean checkLocation (World world, BlockPos possibleLocationInWorld, Rotation rotation, BlockPos possibleLocationInArray, StructureTest blocktest) {
		BlockPos arrayOriginInWorld = possibleLocationInWorld.subtract(possibleLocationInArray.rotate(rotation));
		while (blocktest.next())
		{
			if (!blocktest.isValidBlock(world.getBlockState(arrayOriginInWorld.add(blocktest.getCurrentPosition().rotate(rotation))), false)) return false;
		}
		return true;
	}
	
	private static MultiBlockCheckResult getPositions (World world, BlockPos possibleLocationInWorld, Rotation rotation, BlockPos possibleLocationInArray, StructureTest blocktest) {
		BlockPos arrayOriginInWorld = possibleLocationInWorld.subtract(possibleLocationInArray.rotate(rotation));
		List<WorldStates> worldPositionsStates = new ArrayList<WorldStates>();
		while (blocktest.next())
		{
			BlockPos worldPosition = arrayOriginInWorld.add(blocktest.getCurrentPosition().rotate(rotation));
			IBlockState worldBlockState = world.getBlockState(worldPosition);
			worldPositionsStates.add(new WorldStates(worldPosition, worldBlockState, blocktest.getMultiBlockState(worldBlockState)));
		}
		return new MultiBlockCheckResult(true, worldPositionsStates);
	}
	
	public static class MultiBlockFormationResult {
		
		public final boolean formed;
		@Nullable
		public final List<WorldStates> components;
		
		public MultiBlockFormationResult (boolean formed, @Nullable List<WorldStates> components) {
			this.formed = formed;
			this.components = components;
		}
	}
	
	public static Map<BlockPos, Map<Capability<?>, ICapabilityWrapper<?>>> getWorldMultiblockCapabilities (List<WorldStates> worldPositionStates) {
		if (worldPositionStates == null)
		{
			LOG.error("Could not load capabilities from a null list of world position states");
			return new HashMap<BlockPos, Map<Capability<?>, ICapabilityWrapper<?>>>();
		}
		Stream<WorldStates> stream = worldPositionStates.parallelStream().filter(s -> s.nonMultiblockState != null).filter(s -> s.nonMultiblockState.getBlock() instanceof IMultiBlockCapabilityBlock);
		Map<BlockPos, Map<Capability<?>, ICapabilityWrapper<?>>> capabilities = stream.collect(Collectors.toMap(s -> s.position, s -> ((IMultiBlockCapabilityBlock) s.nonMultiblockState.getBlock()).getCapabilities(s.nonMultiblockState)));
		return capabilities;
	}
	
	public static void removeMultiBlock (IMultiBlockController controller, List<WorldStates> worldPositionStates, @Nullable BlockPos at) {
		World controllerWorld = controller.getPositionWorld();
		BlockPos controllerPos = controller.getPosition();
		if (worldPositionStates == null) return;
		Stream<WorldStates> stream = worldPositionStates.parallelStream().filter(s -> s.nonMultiblockState != null).filter(s -> !s.position.equals(controllerPos)).filter(s -> !s.position.equals(at));
		stream.forEach(s -> controllerWorld.setBlockState(s.position, s.nonMultiblockState));
	}
	
	public static boolean sameStructure (IMultiBlockController controller, List<WorldStates> worldPositionStates) {
		MultiBlockCheckResult result = checkStructure(controller.getPositionWorld(), controller.getPosition(), controller.getStructure());
		if (result.valid
				&& result.worldPositionsStates.stream().map(s -> s.position).collect(Collectors.toList()).equals(worldPositionStates.stream().map(s -> s.position).collect(Collectors.toList())))
			return true;
		return false;
	}
	
	private static class MultiBlockCheckResult {
		
		public final boolean valid;
		@Nullable
		public final List<WorldStates> worldPositionsStates;
		
		public MultiBlockCheckResult (boolean valid, @Nullable List<WorldStates> worldPositionsStates) {
			this.valid = valid;
			this.worldPositionsStates = worldPositionsStates;
		}
	}
}
