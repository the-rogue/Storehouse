
package therogue.storehouse.client;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import therogue.storehouse.Storehouse;
import therogue.storehouse.client.connectedtextures.CTBlockRegistry;
import therogue.storehouse.client.connectedtextures.ConnectedBakedModel;
import therogue.storehouse.client.connectedtextures.ConnectedModel;

public enum StorehouseModelLoader implements ICustomModelLoader {
	INSTANCE;
	
	private Map<ResourceLocation, ConnectedModel> loadedModels = Maps.newHashMap();
	private static final ICustomModelLoader VANILLA_LOADER;
	private static final ICustomModelLoader VARIANT_LOADER;
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
	
	@Override
	public boolean accepts (ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(Storehouse.MOD_ID) && CTBlockRegistry.INSTANCE.containsModel(modelLocation);
	}
	
	@Override
	public IModel loadModel (ResourceLocation modelLocation) throws IOException {
		loadedModels.computeIfAbsent(modelLocation, res -> {
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
