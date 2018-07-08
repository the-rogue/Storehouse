
package therogue.storehouse.client;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;
import therogue.storehouse.Storehouse;
import therogue.storehouse.block.IWrappedBlock;
import therogue.storehouse.client.connectedtextures.CTBlockRegistry;
import therogue.storehouse.client.connectedtextures.ConnectedBakedModel;
import therogue.storehouse.client.connectedtextures.ConnectedModel;

public enum StorehouseModelLoader implements ICustomModelLoader {
	INSTANCE;
	
	private static IForgeRegistry<Block> blockRegistry;
	private Map<ResourceLocation, IModel> loadedModels = new HashMap<>();
	private static final ICustomModelLoader VANILLA_LOADER;
	private static final ICustomModelLoader VARIANT_LOADER;
	private static ModelManager MODEL_MANAGER;
	static
	{
		try
		{
			Class<?> cls1 = Class.forName("net.minecraftforge.client.model.ModelLoader$VanillaLoader");
			VANILLA_LOADER = (ICustomModelLoader) ReflectionHelper.getPrivateValue(cls1, null, "INSTANCE");
			Class<?> cls2 = Class.forName("net.minecraftforge.client.model.ModelLoader$VariantLoader");
			VARIANT_LOADER = (ICustomModelLoader) ReflectionHelper.getPrivateValue(cls2, null, "INSTANCE");
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onResourceManagerReload (@Nonnull IResourceManager resourceManager) {
		loadedModels.clear();
		ConnectedBakedModel.invalidateCaches();
	}
	
	public static ModelManager getModelManager () {
		if (MODEL_MANAGER != null) return MODEL_MANAGER;
		try
		{
			Field modelManager = Minecraft.getMinecraft().getClass().getDeclaredField("modelManager");
			modelManager.setAccessible(true);
			MODEL_MANAGER = (ModelManager) modelManager.get(Minecraft.getMinecraft());
		}
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}
		return MODEL_MANAGER;
	}
	
	@Override
	public boolean accepts (ResourceLocation modelLocation) {
		if (blockRegistry == null) initBlockRegistry();
		if (!modelLocation.getResourceDomain().equals(Storehouse.MOD_ID)) return false;
		if (modelLocation.getResourcePath().contains("_mb") && modelLocation instanceof ModelResourceLocation
				&& blockRegistry.getValue(new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath())) instanceof IWrappedBlock)
			return true;
		return CTBlockRegistry.INSTANCE.containsModel(modelLocation);
	}
	
	private <T extends Comparable<T>> IBlockState processProperty (IBlockState original, String value, IProperty<T> prop) {
		com.google.common.base.Optional<T> valueObject = prop.parseValue(value);
		if (valueObject.isPresent()) return original.withProperty(prop, valueObject.get());
		return original;
	}
	
	@Override
	public IModel loadModel (ResourceLocation modelLocation) throws Exception {
		loadedModels.computeIfAbsent(modelLocation, res -> {
			if (blockRegistry == null) initBlockRegistry();
			if (modelLocation.getResourcePath().contains("_mb") && modelLocation instanceof ModelResourceLocation)
			{
				Block block = blockRegistry.getValue(new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath()));
				if (block instanceof IWrappedBlock)
				{
					IWrappedBlock mb_block = (IWrappedBlock) block;
					ModelResourceLocation mrl = (ModelResourceLocation) modelLocation;
					List<String> variants = Lists.newArrayList(mrl.getVariant().split(","));
					IBlockState location = mb_block.unwrap(block.getDefaultState());
					Collection<IProperty<?>> propertyKeys = location.getPropertyKeys();
					for (String variant : variants)
					{
						int index = variant.indexOf("=");
						if (index == -1) continue;
						String key = variant.substring(0, index);
						String value = variant.substring(index + 1);
						List<IProperty<?>> properties = propertyKeys.stream().filter(property -> property.getName().equalsIgnoreCase(key)).collect(Collectors.toList());
						if (properties.size() > 0) location = processProperty(location, value, properties.get(0));
					}
					if (MODEL_MANAGER == null) getModelManager();
					BlockStateMapper blockLocations = MODEL_MANAGER.getBlockModelShapes().getBlockStateMapper();
					// BlockStateMapper blockLocations = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getBlockStateMapper();
					ModelResourceLocation actualModel = blockLocations.getVariants(location.getBlock()).get(location);
					try
					{
						return ModelLoaderRegistry.getModel(actualModel);
					}
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}
				}
				throw new RuntimeException("Problem Finding a multiblock location");
			}
			try
			{
				IModel model;
				if (VARIANT_LOADER.accepts(res))
				{
					model = VARIANT_LOADER.loadModel(res);
				}
				else
				{
					model = VANILLA_LOADER.loadModel(res);
				}
				return new ConnectedModel(model, res);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		});
		return loadedModels.get(modelLocation);
	}
	
	private static void initBlockRegistry () {
		if (blockRegistry != null) return;
		blockRegistry = GameRegistry.findRegistry(Block.class);
		if (blockRegistry == null) throw new IllegalStateException("Block Registry CANNOT be null when reading NBT");
	}
	/*
	 * Unused currently, but may be useful in the future
	 */
	/*
	 * @SubscribeEvent (priority = EventPriority.LOWEST) // low priority to capture all event-registered models
	 * public void onModelBake (ModelBakeEvent event) {
	 * Map<ModelResourceLocation, IModel> stateModels = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(), "stateModels");
	 * for (ModelResourceLocation mrl : event.getModelRegistry().getKeys())
	 * {
	 * IModel model = stateModels.get(mrl);
	 * if (model != null && !(model instanceof ConnectedModel)
	 * && !StorehouseModelLoader.parsedLocations.contains(mrl)
	 * && CTBlockRegistry.INSTANCE.containsModel(mrl))
	 * {
	 * LOG.info("ModelLocation Wrapped: " + mrl);
	 * ConnectedModel connectedModel = new ConnectedModel(model, mrl);
	 * connectedModel.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, rl -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(rl.toString()));
	 * event.getModelRegistry().putObject(mrl, new ConnectedBakedModel(connectedModel, event.getModelRegistry().getObject(mrl)));
	 * }
	 * }
	 * }
	 */
}
