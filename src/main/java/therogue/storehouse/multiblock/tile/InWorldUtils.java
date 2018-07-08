
package therogue.storehouse.multiblock.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.structure.MultiBlockStructure.StructureTest;
import therogue.storehouse.multiblock.tile.WorldStates.MultiBlockData;

public class InWorldUtils {
	
	public static MultiBlockFormationResult formMultiBlock (IMultiBlockController controller) {
		World controllerWorld = controller.getPositionWorld();
		MultiBlockCheckResult result = new MultiBlockCheckResult(false, null, null); // TODO FINISH THIS
		for (MultiBlockStructure structure : controller.getPossibleStructures())
		{
			result = checkStructure(controllerWorld, controller.getPosition(), structure);
			if (result.valid) break;
		}
		if (!result.valid) return new MultiBlockFormationResult(false, null, null);
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
		return new MultiBlockFormationResult(true, worldPositionStates, result.info);
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
		return new MultiBlockCheckResult(false, null, null);
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
			worldPositionsStates.add(new WorldStates(worldPosition, worldBlockState, blocktest.getMultiBlockState(worldBlockState), blocktest.getCapabilites(worldBlockState)));
		}
		return new MultiBlockCheckResult(true, worldPositionsStates, new MultiBlockData(arrayOriginInWorld, rotation));
	}
	
	public static class MultiBlockFormationResult {
		
		public final boolean formed;
		@Nullable
		public final List<WorldStates> components;
		@Nullable
		public final MultiBlockData info;
		
		public MultiBlockFormationResult (boolean formed, @Nullable List<WorldStates> components, MultiBlockData info) {
			this.formed = formed;
			this.components = components;
			this.info = info;
		}
	}
	
	public static void removeMultiBlock (IMultiBlockController controller, List<WorldStates> worldPositionStates, @Nullable BlockPos at) {
		World controllerWorld = controller.getPositionWorld();
		BlockPos controllerPos = controller.getPosition();
		if (worldPositionStates == null) return;
		for (WorldStates state : worldPositionStates)
		{
			if (!state.position.equals(at) && !state.position.equals(controllerPos))
			{
				if (state.nonMultiblockState != null)
				{
					controllerWorld.setBlockState(state.position, state.nonMultiblockState);
				}
			}
		}
		// Stream<WorldStates> stream = worldPositionStates.parallelStream().filter(s -> s.nonMultiblockState != null).filter(s -> !s.position.equals(controllerPos)).filter(s -> !s.position.equals(at));
		// stream.forEach(s -> controllerWorld.setBlockState(s.position, s.nonMultiblockState));
	}
	
	public static boolean sameStructure (IMultiBlockController controller, List<WorldStates> worldPositionStates) {
		for (MultiBlockStructure structure : controller.getPossibleStructures())
		{
			MultiBlockCheckResult result = checkStructure(controller.getPositionWorld(), controller.getPosition(), structure);
			if (result.valid
					&& result.worldPositionsStates.stream().map(s -> s.position).collect(Collectors.toList()).equals(worldPositionStates.stream().map(s -> s.position).collect(Collectors.toList())))
				return true;
		}
		return false;
	}
	
	private static class MultiBlockCheckResult {
		
		public final boolean valid;
		@Nullable
		public final List<WorldStates> worldPositionsStates;
		@Nullable
		public final MultiBlockData info;
		
		public MultiBlockCheckResult (boolean valid, @Nullable List<WorldStates> worldPositionsStates, MultiBlockData info) {
			this.valid = valid;
			this.worldPositionsStates = worldPositionsStates;
			this.info = info;
		}
	}
}
