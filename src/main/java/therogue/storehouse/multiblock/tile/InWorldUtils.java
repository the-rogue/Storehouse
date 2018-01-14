
package therogue.storehouse.multiblock.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.capabilitywrapper.ICapabilityWrapper;
import therogue.storehouse.multiblock.block.IMultiBlockCapabilityBlock;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.structure.MultiBlockStructure.StructureTest;
import therogue.storehouse.util.LOG;

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
			if (!blocktest.isValidBlock(world.getBlockState(arrayOriginInWorld.add(blocktest.getCurrentPosition().rotate(rotation))), false))
			{
				//LOG.info("MultiBlock Failed To Form at: " + "Pos: " + blocktest.getCurrentPosition() + " WorldPos: " + arrayOriginInWorld.add(blocktest.getCurrentPosition().rotate(rotation)) + " State: " + world.getBlockState(arrayOriginInWorld.add(blocktest.getCurrentPosition().rotate(rotation))));
				return false;
			}
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
		Map<BlockPos, Map<Capability<?>, ICapabilityWrapper<?>>> capabilities = new HashMap<BlockPos, Map<Capability<?>, ICapabilityWrapper<?>>>();
		for (WorldStates positionState : worldPositionStates)
		{
			IBlockState prevState = positionState.nonMultiblockState;
			if (prevState != null && prevState.getBlock() instanceof IMultiBlockCapabilityBlock)
			{
				Block prevBlock = prevState.getBlock();
				capabilities.put(positionState.position, ((IMultiBlockCapabilityBlock) prevBlock).getCapabilities(prevState));
			}
		}
		return capabilities;
	}
	
	public static void removeMultiBlock (IMultiBlockController controller, List<WorldStates> worldPositionStates, @Nullable BlockPos at) {
		World controllerWorld = controller.getPositionWorld();
		if (worldPositionStates == null) return;
		for (WorldStates positionState : worldPositionStates)
		{
			if (positionState.nonMultiblockState != null && !positionState.position.equals(controller.getPosition()) && !positionState.position.equals(at))
			{
				controllerWorld.setBlockState(positionState.position, positionState.nonMultiblockState);
			}
		}
	}
	
	public static boolean sameStructure (IMultiBlockController controller, List<WorldStates> worldPositionStates) {
		MultiBlockCheckResult result = checkStructure(controller.getPositionWorld(), controller.getPosition(), controller.getStructure());
		if (result.valid && getPositionsFromPSCList(result.worldPositionsStates).equals(getPositionsFromPSCList(worldPositionStates))) return true;
		return false;
	}
	
	private static List<BlockPos> getPositionsFromPSCList (List<WorldStates> positionStates) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		for (WorldStates psc : positionStates)
		{
			positions.add(psc.position);
		}
		return positions;
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
