
package therogue.storehouse.multiblock.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.Storehouse;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.IWrappedBlock;
import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.multiblock.structure.IMultiBlockElement;
import therogue.storehouse.multiblock.structure.MultiBlockPartBuilder;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.structure.MultiBlockStructure.Builder;
import therogue.storehouse.multiblock.structure.NormalBlock;
import therogue.storehouse.multiblock.structure.NormalPart;
import therogue.storehouse.multiblock.structure.VariableBlock;
import therogue.storehouse.multiblock.structure.VariablePart;
import therogue.storehouse.multiblock.tile.BasicMultiBlockTile;

public class MultiBlockCreationHandler {
	
	public static final MultiBlockCreationHandler INSTANCE = new MultiBlockCreationHandler();
	private static final IResourceManager RESOURCE_MANAGER = Minecraft.getMinecraft().getResourceManager();
	private Map<ResourceLocation, IMultiBlockElement> elementAliases = new HashMap<>();
	private final List<IStorehouseBaseBlock> blockList = new ArrayList<>();
	public final Map<String, ICapabilityWrapper<?>> capabilityMappings = new HashMap<>();
	private final Map<Block, List<ICapabilityWrapper<?>>> blockOverrides = new HashMap<>();
	private final Map<IBlockState, List<ICapabilityWrapper<?>>> blockStateOverrides = new HashMap<>();
	private final Map<ResourceLocation, Map<Block, List<ICapabilityWrapper<?>>>> blockPartOverrides = new HashMap<>();
	private final Map<ResourceLocation, Map<IBlockState, List<ICapabilityWrapper<?>>>> blockStatePartOverrides = new HashMap<>();
	
	private MultiBlockCreationHandler () {
		capabilityMappings.put("ENG-IN", EnergyCapabilityWrapper.RECIEVE);
		capabilityMappings.put("ENG-OUT", EnergyCapabilityWrapper.EXTRACT);
		capabilityMappings.put("FLD-IN", FluidCapabilityWrapper.FILL);
		capabilityMappings.put("FLD-OUT", FluidCapabilityWrapper.DRAIN);
		capabilityMappings.put("FLD-BOTH", FluidCapabilityWrapper.BOTH);
		capabilityMappings.put("ITM-IN", ItemCapabilityWrapper.INSERT);
		capabilityMappings.put("ITM-OUT", ItemCapabilityWrapper.EXTRACT);
		capabilityMappings.put("ITM-BOTH", ItemCapabilityWrapper.BOTH);
		GameRegistry.registerTileEntity(BasicMultiBlockTile.class, new ResourceLocation(Storehouse.MOD_ID, "generic_multiblock"));
	}
	
	public List<IStorehouseBaseBlock> getBlockList () {
		return new ArrayList<>(blockList);
	}
	
	public void registerAlias (String alias, Block block) {
		String modID = Loader.instance().activeModContainer().getModId();
		ResourceLocation locationAlias = new ResourceLocation(modID, alias);
		if (elementAliases.containsKey(locationAlias)) throw new RuntimeException(modID + ": Registered the same location twice");
		NormalBlock normalBlock = new NormalBlock(block, createWrapper(block));
		if (blockOverrides.containsKey(block)) normalBlock.addCapability(blockOverrides.get(block).toArray(new ICapabilityWrapper[0]));
		if (blockPartOverrides.containsKey(locationAlias) && blockPartOverrides.get(locationAlias).containsKey(block))
			normalBlock.addCapability(blockPartOverrides.get(locationAlias).get(block).toArray(new ICapabilityWrapper[0]));
		elementAliases.put(locationAlias, normalBlock);
	}
	
	public void registerAlias (String alias, IBlockState blockState) {
		String modID = Loader.instance().activeModContainer().getModId();
		ResourceLocation locationAlias = new ResourceLocation(modID, alias);
		if (elementAliases.containsKey(locationAlias)) throw new RuntimeException(modID + ": Registered the same location twice");
		NormalBlock normalBlock = new NormalBlock(blockState, createWrapper(blockState.getBlock()));
		if (blockStateOverrides.containsKey(blockState)) normalBlock.addCapability(blockStateOverrides.get(blockState).toArray(new ICapabilityWrapper[0]));
		if (blockStatePartOverrides.containsKey(locationAlias) && blockStatePartOverrides.get(locationAlias).containsKey(blockState))
			normalBlock.addCapability(blockStatePartOverrides.get(locationAlias).get(blockState).toArray(new ICapabilityWrapper[0]));
		elementAliases.put(locationAlias, normalBlock);
	}
	
	public void registerAlias (String alias, Block... blocks) {
		String modID = Loader.instance().activeModContainer().getModId();
		ResourceLocation locationAlias = new ResourceLocation(modID, alias);
		if (elementAliases.containsKey(locationAlias)) throw new RuntimeException(modID + ": Registered the same location twice");
		VariableBlock variableBlock = new VariableBlock(createWrapper(blocks), blocks);
		for (Block block : blocks)
		{
			if (blockOverrides.containsKey(block)) variableBlock.addCapability(block.getDefaultState(), blockOverrides.get(block).toArray(new ICapabilityWrapper[0]));
			if (blockPartOverrides.containsKey(locationAlias) && blockPartOverrides.get(locationAlias).containsKey(block))
				variableBlock.addCapability(block.getDefaultState(), blockPartOverrides.get(locationAlias).get(block).toArray(new ICapabilityWrapper[0]));
		}
		elementAliases.put(locationAlias, variableBlock);
	}
	
	public void registerAlias (String alias, Block[] blocks, IBlockState... blockStates) {
		String modID = Loader.instance().activeModContainer().getModId();
		ResourceLocation locationAlias = new ResourceLocation(modID, alias);
		List<Block> blocklist = Lists.newArrayList(blockStates).stream().map(state -> state.getBlock()).collect(Collectors.toList());
		blocklist.addAll(Lists.newArrayList(blocks));
		if (elementAliases.containsKey(locationAlias)) throw new RuntimeException(modID + ": Registered the same location twice");
		VariableBlock variableBlock = new VariableBlock(createWrapper(blocklist.toArray(new Block[0])), blocks, blockStates);
		for (Block block : blocks)
		{
			if (blockOverrides.containsKey(block)) variableBlock.addCapability(block.getDefaultState(), blockOverrides.get(block).toArray(new ICapabilityWrapper[0]));
			if (blockPartOverrides.containsKey(locationAlias) && blockPartOverrides.get(locationAlias).containsKey(block))
				variableBlock.addCapability(block.getDefaultState(), blockPartOverrides.get(locationAlias).get(block).toArray(new ICapabilityWrapper[0]));
		}
		for (IBlockState blockState : blockStates)
		{
			if (blockStateOverrides.containsKey(blockState)) variableBlock.addCapability(blockState, blockStateOverrides.get(blockState).toArray(new ICapabilityWrapper[0]));
			if (blockStatePartOverrides.containsKey(locationAlias) && blockStatePartOverrides.get(locationAlias).containsKey(blockState))
				variableBlock.addCapability(blockState, blockStatePartOverrides.get(locationAlias).get(blockState).toArray(new ICapabilityWrapper[0]));
		}
		elementAliases.put(locationAlias, variableBlock);
	}
	
	public void registerAlias (String alias, IBlockState... blockStates) {
		String modID = Loader.instance().activeModContainer().getModId();
		ResourceLocation locationAlias = new ResourceLocation(modID, alias);
		Block[] blocks = Lists.newArrayList(blockStates).stream().map(state -> state.getBlock()).collect(Collectors.toList()).toArray(new Block[0]);
		if (elementAliases.containsKey(locationAlias)) throw new RuntimeException(modID + ": Registered the same location twice");
		VariableBlock variableBlock = new VariableBlock(createWrapper(blocks), blockStates);
		for (IBlockState blockState : blockStates)
		{
			if (blockStateOverrides.containsKey(blockState)) variableBlock.addCapability(blockState, blockStateOverrides.get(blockState).toArray(new ICapabilityWrapper[0]));
			if (blockStatePartOverrides.containsKey(locationAlias) && blockStatePartOverrides.get(locationAlias).containsKey(blockState))
				variableBlock.addCapability(blockState, blockStatePartOverrides.get(locationAlias).get(blockState).toArray(new ICapabilityWrapper[0]));
		}
		elementAliases.put(locationAlias, variableBlock);
	}
	
	public void registerCapabilityOverride (String capability, Block block) throws MultiBlockCreationException {
		if (!capabilityMappings.containsKey(capability))
			throw new MultiBlockCreationException("Tried to find a capability that hasn't been registered: " + capability + " List: " + capabilityMappings);
		blockOverrides.putIfAbsent(block, new ArrayList<>());
		blockOverrides.get(block).add(capabilityMappings.get(capability));
	}
	
	public void registerCapabilityOverride (String capability, IBlockState block) throws MultiBlockCreationException {
		if (!capabilityMappings.containsKey(capability))
			throw new MultiBlockCreationException("Tried to find a capability that hasn't been registered: " + capability + " List: " + capabilityMappings);
		blockStateOverrides.putIfAbsent(block, new ArrayList<>());
		blockStateOverrides.get(block).add(capabilityMappings.get(capability));
	}
	
	public void registerPartCapOverride (String capability, ResourceLocation part, Block block) throws MultiBlockCreationException {
		if (!capabilityMappings.containsKey(capability))
			throw new MultiBlockCreationException("Tried to find a capability that hasn't been registered: " + capability + " List: " + capabilityMappings);
		blockPartOverrides.putIfAbsent(part, new HashMap<>());
		blockPartOverrides.get(part).put(block, new ArrayList<>());
		blockPartOverrides.get(part).get(block).add(capabilityMappings.get(capability));
	}
	
	public void registerPartCapOverride (String capability, ResourceLocation part, IBlockState block) throws MultiBlockCreationException {
		if (!capabilityMappings.containsKey(capability))
			throw new MultiBlockCreationException("Tried to find a capability that hasn't been registered: " + capability + " List: " + capabilityMappings);
		blockStatePartOverrides.putIfAbsent(part, new HashMap<>());
		blockStatePartOverrides.get(part).put(block, new ArrayList<>());
		blockStatePartOverrides.get(part).get(block).add(capabilityMappings.get(capability));
	}
	
	private IBlockWrapper createWrapper (Block block) {
		for (int i = 0; i < blockList.size(); i++)
		{
			Block unWrappedBlock = ((IWrappedBlock) blockList.get(i)).unwrap(((Block) blockList.get(i)).getDefaultState()).getBlock();
			if (unWrappedBlock == block) return (IBlockWrapper) blockList.get(i);
		}
		String name = block instanceof IStorehouseBaseBlock ? ((IStorehouseBaseBlock) block).getName() : block.getRegistryName().getResourcePath();
		BasicMultiBlockBlock BMBB = new BasicMultiBlockBlock(name + "_mb", block);
		blockList.add(BMBB);
		return BMBB;
	}
	
	private IBlockWrapper createWrapper (Block... blocks) {
		Map<Block, IBlockWrapper> wrappers = new HashMap<>();
		for (Block block : blocks)
		{
			boolean found = false;
			for (int i = 0; i < blockList.size(); i++)
			{
				Block unWrappedBlock = ((IWrappedBlock) blockList.get(i)).unwrap(((Block) blockList.get(i)).getDefaultState()).getBlock();
				if (unWrappedBlock == block)
				{
					wrappers.put(unWrappedBlock, (IBlockWrapper) blockList.get(i));
					found = true;
					break;
				}
			}
			if (found) continue;
			String name = block instanceof IStorehouseBaseBlock ? ((IStorehouseBaseBlock) block).getName() : block.getRegistryName().getResourcePath();
			BasicMultiBlockBlock BMBB = new BasicMultiBlockBlock(name + "_mb", block);
			blockList.add(BMBB);
			wrappers.put(block, BMBB);
		}
		return (state) -> wrappers.get(state.getBlock()).getWrappedState(state);
	}
	
	public MultiBlockStructure getStructure (String string) throws MultiBlockCreationException {
		String modID = Loader.instance().activeModContainer().getModId();
		List<String> seperateRows = ElementFactory.seperateByDelimeter(string, "\n");
		List<List<String>> seperateParts = ElementFactory.splitListAtItem(seperateRows, "Normal-Part", "Variable-Part");
		Builder multiBlockBuilder = Builder.newBuilder();
		for (List<String> part : seperateParts)
		{
			List<List<String>> subParts = ElementFactory.seperateListByItem(part, "Part");
			List<NormalPart> finishedPartList = new ArrayList<>();
			for (List<String> subPart : subParts)
			{
				List<List<String>> seperateLayers = ElementFactory.seperateListByItem(subPart, "New-Layer");
				MultiBlockPartBuilder partBuilder = MultiBlockPartBuilder.newBuilder();
				for (List<String> layer : seperateLayers)
				{
					for (String row : layer)
					{
						List<String> elements = ElementFactory.seperateByDelimeter(row, " ");
						partBuilder.newRow();
						for (String blockName : elements)
						{
							if (!elementAliases.containsKey(new ResourceLocation(modID, blockName.toLowerCase()))) processRegisterLine(modID, blockName);
							IMultiBlockElement element = elementAliases.get(new ResourceLocation(modID, blockName));
							if (element == null) throw new MultiBlockCreationException("Couldn't find a multiblock element for alias: " + blockName + " in string:" + string);
							partBuilder.addBlocksToRow(element);
						}
					}
					partBuilder.goUp();
				}
				finishedPartList.add(partBuilder.build());
			}
			if (finishedPartList.size() == 1) multiBlockBuilder.addPart(finishedPartList.get(0));
			else multiBlockBuilder.addPart(new VariablePart(finishedPartList));
		}
		return multiBlockBuilder.getStructure();
	}
	
	public void processRegisterFile (String file) {
		String modID = Loader.instance().activeModContainer().getModId();
		List<String> lines = ElementFactory.seperateByDelimeter(file, "\n");
		for (String line : lines)
		{
			try
			{
				processRegisterLine(modID, line);
			}
			catch (MultiBlockCreationException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void processCapabilityFile (String file) {
		String modID = Loader.instance().activeModContainer().getModId();
		List<String> lines = ElementFactory.seperateByDelimeter(file, "\n");
		for (String line : lines)
		{
			try
			{
				processMultiBlockCapability(modID, line);
			}
			catch (MultiBlockCreationException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public MultiBlockStructure getStructureSafe (String structure) {
		try
		{
			return getStructure(structure);
		}
		catch (MultiBlockCreationException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void processRegisterLine (String modID, String register) throws MultiBlockCreationException {
		if (register.toUpperCase().contains("000") || register.toUpperCase().contains("NUL"))
		{
			elementAliases.put(new ResourceLocation(modID, register), MultiBlockPartBuilder.ANY_BLOCK);
		}
		String name = register;
		List<String> elements = ElementFactory.seperateByDelimeter(register, " ");
		if (elements.size() > 1)
		{
			name = elements.remove(0);
		}
		if (!elementAliases.containsKey(new ResourceLocation(modID, name)))
		{
			List<IBlockState> blockStates = new ArrayList<>();
			List<Block> blocks = new ArrayList<>();
			for (String line : elements)
			{
				getBlockOrState(modID, line, blocks, blockStates);
			}
			if (!blockStates.isEmpty() && !blocks.isEmpty()) registerAlias(name, blocks.toArray(new Block[0]), blockStates.toArray(new IBlockState[0]));
			else if (blockStates.size() > 1) registerAlias(name, blockStates.toArray(new IBlockState[0]));
			else if (blocks.size() > 1) registerAlias(name, blocks.toArray(new Block[0]));
			else if (blockStates.size() == 1) registerAlias(name, blockStates.get(0));
			else if (blocks.size() == 1) registerAlias(name, blocks.get(0));
			else throw new MultiBlockCreationException("Could not find any Entries to register for name: " + name + " and String: " + register);
		}
	}
	
	public void processMultiBlockCapability (String modID, String register) throws MultiBlockCreationException {
		List<String> capabilities = ElementFactory.seperateByDelimeter(register, " ");
		List<Block> blockList = new ArrayList<>();
		List<IBlockState> blockStateList = new ArrayList<>();
		String partString = capabilities.remove(0);
		try
		{
			getBlockOrState(modID, partString, blockList, blockStateList);
			partString = null;
		}
		catch (MultiBlockCreationException e)
		{
			getBlockOrState(modID, capabilities.remove(0), blockList, blockStateList);
		}
		for (String capString : capabilities)
		{
			if (partString == null)
			{
				if (!blockList.isEmpty()) registerCapabilityOverride(capString, blockList.get(0));
				else if (!blockStateList.isEmpty()) registerCapabilityOverride(capString, blockStateList.get(0));
			}
			else
			{
				ResourceLocation partLocation = new ResourceLocation(modID, partString);
				if (!blockList.isEmpty()) registerPartCapOverride(capString, partLocation, blockList.get(0));
				else if (!blockStateList.isEmpty()) registerPartCapOverride(capString, partLocation, blockStateList.get(0));
			}
		}
	}
	
	@SuppressWarnings ("deprecation")
	public void getBlockOrState (String modID, String blockState, List<Block> blockList, List<IBlockState> blockStateList) throws MultiBlockCreationException {
		Matcher m = Pattern.compile("\\d+").matcher(blockState);
		if (m.find())
		{
			blockState = blockState.replaceAll("\\d+", "");
			blockState = blockState.trim();
			int meta = Integer.parseInt(m.group());
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modID, blockState));
			if (block.equals(Blocks.AIR)) block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockState));
			if (!block.equals(Blocks.AIR)) blockStateList.add(block.getStateFromMeta(meta));
			else throw new MultiBlockCreationException("Could not find a block for the specified entry: " + blockState + " containing meta: " + meta);
		}
		else
		{
			blockState = blockState.trim();
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modID, blockState));
			if (block.equals(Blocks.AIR)) block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockState));
			if (!block.equals(Blocks.AIR)) blockList.add(block);
			else throw new MultiBlockCreationException("Could not find a block for the specified entry: " + blockState);
		}
	}
	
	public void getBlockAliasesFromFile (ResourceLocation file) {
		this.processRegisterFile(getFileString(file));
	}
	
	public void getCapabilitiesFromFile (ResourceLocation file) {
		this.processCapabilityFile(getFileString(file));
	}
	
	public MultiBlockStructure getStructureFromFile (ResourceLocation file) {
		return this.getStructureSafe(getFileString(file));
	}
	
	private String getFileString (ResourceLocation fileLocation) {
		String result = null;
		try (IResource resource = RESOURCE_MANAGER.getResource(fileLocation))
		{
			result = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines().parallel().collect(Collectors.joining("\n"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public class MultiBlockCreationException extends Exception {
		
		private static final long serialVersionUID = 1L;
		
		public MultiBlockCreationException (String message) {
			super(message);
		}
	}
}
