
package therogue.storehouse.client.connectedtextures;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.WeightedBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IRegistryDelegate;
import therogue.storehouse.client.StorehouseModelLoader;
import therogue.storehouse.client.connectedtextures.ConnectionState.ConnectedTexturePair;
import therogue.storehouse.client.connectedtextures.ConnectionState.RenderProperty;

public class ConnectedBakedModel implements IBakedModel {
	
	private static Cache<ModelResourceLocation, ConnectedBakedModel> itemcache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.SECONDS).<ModelResourceLocation, ConnectedBakedModel> build();
	private static Cache<State, ConnectedBakedModel> modelcache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).maximumSize(5000).<State, ConnectedBakedModel> build();
	
	public static void invalidateCaches () {
		itemcache.invalidateAll();
		modelcache.invalidateAll();
	}
	
	private static final MethodHandle _locations;
	static
	{
		try
		{
			_locations = MethodHandles.lookup().unreflectGetter(ReflectionHelper.findField(ItemModelMesherForge.class, "locations"));
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@ParametersAreNonnullByDefault
	private class Overrides extends ItemOverrideList {
		
		public Overrides () {
			super(Lists.newArrayList());
		}
		
		@Override
		public IBakedModel handleItemState (IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			Block block = null;
			if (stack.getItem() instanceof ItemBlock)
			{
				block = ((ItemBlock) stack.getItem()).getBlock();
			}
			final IBlockState state = block == null ? null : block.getDefaultState();
			ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
			// First try simple damage overrides
			ModelResourceLocation modelResourceLocation = null;
			try
			{
				modelResourceLocation = Optional.ofNullable(((Map<IRegistryDelegate<Item>, TIntObjectHashMap<ModelResourceLocation>>) _locations.invoke(mesher))).map(map -> map.get(stack.getItem().delegate)).map(map -> map.get(mesher.getMetadata(stack))).orElse(null);
			}
			catch (Throwable e1)
			{
				e1.printStackTrace();
			}
			// Next, try mesh definitions
			if (modelResourceLocation == null)
			{
				ItemMeshDefinition itemMeshDefinition = mesher.shapers.get(stack.getItem());
				modelResourceLocation = Optional.ofNullable(itemMeshDefinition).map(mesh -> mesh.getModelLocation(stack)).orElse(null);
			}
			// Finally, this must be a missing/invalid model
			if (modelResourceLocation == null) { return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel(); }
			try
			{
				return itemcache.get(modelResourceLocation, () -> createModel(state, model, null, 0));
			}
			catch (ExecutionException e)
			{
				e.printStackTrace();
				return originalModel;
			}
		}
	}
	
	private static class State {
		
		private final IBlockState cleanState;
		private final TObjectLongMap<ConnectedTexturePair> serializedContext;
		private final IBakedModel parent;
		
		public State (IBlockState cleanState, TObjectLongMap<ConnectedTexturePair> serializedContext, IBakedModel parent) {
			this.cleanState = cleanState;
			this.serializedContext = serializedContext;
			this.parent = parent;
		}
		
		@Override
		public boolean equals (Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			State other = (State) obj;
			if (cleanState != other.cleanState) { return false; }
			if (parent != other.parent) { return false; }
			if (serializedContext == null)
			{
				if (other.serializedContext != null) { return false; }
			}
			else if (!serializedContext.equals(other.serializedContext)) { return false; }
			return true;
		}
		
		@Override
		public int hashCode () {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cleanState == null) ? 0 : cleanState.hashCode());
			result = prime * result + ((parent == null) ? 0 : parent.hashCode());
			result = prime * result + ((serializedContext == null) ? 0 : serializedContext.hashCode());
			return result;
		}
		
		@Override
		public String toString () {
			return "State [cleanState=" + cleanState + ", serializedContext=" + serializedContext + ", parent=" + parent + "]";
		}
	}
	
	private final @Nonnull ConnectedModel model;
	private final @Nonnull IBakedModel parent;
	private final @Nonnull Overrides overrides = new Overrides();
	protected final ListMultimap<BlockRenderLayer, BakedQuad> genQuads = MultimapBuilder.enumKeys(BlockRenderLayer.class).arrayListValues().build();
	protected final Table<BlockRenderLayer, EnumFacing, List<BakedQuad>> faceQuads = Tables.newCustomTable(Maps.newEnumMap(BlockRenderLayer.class), () -> Maps.newEnumMap(EnumFacing.class));
	protected final BlockModelShapes bakedModelStore;
	
	public ConnectedBakedModel (ConnectedModel model, IBakedModel parent) {
		this.model = model;
		this.parent = parent;
		this.bakedModelStore = StorehouseModelLoader.getModelManager().getBlockModelShapes();
	}
	
	public ConnectedModel getModel () {
		return model;
	}
	
	public IBakedModel getParent () {
		return parent;
	}
	
	public List<BakedQuad> getParentQuads (IBlockState state, EnumFacing side) {
		IBakedModel model = parent;
		while (model instanceof ConnectedBakedModel)
		{
			model = ((ConnectedBakedModel) model).getParent();
		}
		return model.getQuads(state, side, 0L);
	}
	
	@Override
	public List<BakedQuad> getQuads (@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		if (Arrays.asList(Thread.currentThread().getStackTrace()).parallelStream().anyMatch(stacktrace -> stacktrace.getMethodName().equals("getDamageModel")))
			return parent.getQuads(state, side, rand);
		IBakedModel parent = getParent(rand);
		ConnectedBakedModel baked = this;
		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
		ConnectionState connectionState = CTBlockRegistry.INSTANCE.getState();
		boolean isPossibleState = connectionState.state == state;
		if (Minecraft.getMinecraft().world != null && (state instanceof IExtendedBlockState || isPossibleState) && connectionState.state != null)
		{
			IBlockState cleanState = state instanceof IExtendedBlockState ? ((IExtendedBlockState) state).getClean() : state;
			if (!isPossibleState)
			{
				IExtendedBlockState ext = (IExtendedBlockState) state;
				connectionState = ext.getValue(RenderProperty.INSTANCE);
			}
			if (connectionState != null)
			{
				connectionState.buildCache(bakedModelStore, cleanState, model.getConnectedTextures());
				TObjectLongMap<ConnectedTexturePair> serialized = connectionState.serialized();
				try
				{
					final ConnectionState finalConnectionState = connectionState;
					baked = modelcache.get(new State(cleanState, serialized, parent), () -> createModel(state, model, finalConnectionState, rand));
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (state != null)
		{
			try
			{
				baked = modelcache.get(new State(state, null, parent), () -> createModel(state, model, null, rand));
			}
			catch (ExecutionException e)
			{
				e.printStackTrace();
			}
		}
		List<BakedQuad> ret;
		if (side != null && layer != null)
		{
			ret = baked.faceQuads.get(layer, side);
		}
		else if (side != null)
		{
			ret = baked.faceQuads.column(side).values().stream().flatMap(List::stream).collect(Collectors.toList());
		}
		else if (layer != null)
		{
			ret = baked.genQuads.get(layer);
		}
		else
		{
			ret = Lists.newArrayList(baked.genQuads.values());
		}
		return ret;
	}
	
	/**
	 * Random sensitive parent, will proxy to {@link WeightedBakedModel} if possible.
	 */
	public IBakedModel getParent (long rand) {
		if (getParent() instanceof WeightedBakedModel) { return ((WeightedBakedModel) parent).getRandomModel(rand); }
		return getParent();
	}
	
	@Override
	public @Nonnull ItemOverrideList getOverrides () {
		return overrides;
	}
	
	@Override
	public boolean isAmbientOcclusion () {
		return parent.isAmbientOcclusion();
	}
	
	@Override
	public boolean isGui3d () {
		return parent.isGui3d();
	}
	
	@Override
	public boolean isBuiltInRenderer () {
		return false;
	}
	
	@Override
	public @Nonnull TextureAtlasSprite getParticleTexture () {
		return Optional.ofNullable(getModel().getTexture(getParent().getParticleTexture().getIconName())).map(t -> t.normal).orElse(getParent().getParticleTexture());
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective (ItemCameraTransforms.TransformType cameraTransformType) {
		return Pair.of(this, parent.handlePerspective(cameraTransformType).getRight());
	}
	
	protected static final BlockRenderLayer[] LAYERS = BlockRenderLayer.values();
	private static final EnumFacing[] FACINGS = ObjectArrays.concat(EnumFacing.VALUES, (EnumFacing) null);
	
	protected ConnectedBakedModel createModel (@Nullable IBlockState state, ConnectedModel model, @Nullable ConnectionState connectedState,
			long rand) {
		IBakedModel parent = getParent(rand);
		while (parent instanceof ConnectedBakedModel)
		{
			parent = ((ConnectedBakedModel) parent).getParent(rand);
		}
		ConnectedBakedModel ret = new ConnectedBakedModel(model, parent);
		for (BlockRenderLayer layer : LAYERS)
		{
			for (EnumFacing facing : FACINGS)
			{
				List<BakedQuad> parentQuads = parent.getQuads(state, facing, rand);
				List<BakedQuad> quads;
				if (facing != null)
				{
					ret.faceQuads.put(layer, facing, quads = new ArrayList<>());
				}
				else
				{
					quads = ret.genQuads.get(layer);
				}
				// Linked to maintain the order of quads
				Map<BakedQuad, ConnectedTexturePair> texturemap = new LinkedHashMap<>();
				// Gather all quads and map them to their textures
				// All quads should have an associated ICTMTexture, so ignore any that do not
				for (BakedQuad q : parentQuads)
				{
					ConnectedTexturePair tex = getModel().getTexture(q.getSprite().getIconName());
					if (tex != null)
					{
						texturemap.put(q, tex);
					}
					else
					{
						quads.add(q);
					}
				}
				for (Entry<BakedQuad, ConnectedTexturePair> e : texturemap.entrySet())
				{
					ConnectedTexturePair tex = e.getValue();
					// If the layer is null, this is a wrapped vanilla texture, so passthrough the layer check to the block
					if (state.getBlock().canRenderInLayer(state, layer))
					{
						EnumMap<EnumFacing, int[]> textureIndices = connectedState == null ? null : connectedState.getTextureIndices(e.getValue());
						BakedQuad bq = e.getKey();
						Quad quad = Quad.from(bq);
						quad = quad.derotate();
						if (textureIndices == null)
						{
							quads.addAll(Collections.singletonList(quad.transformUVs(tex.normal).rebake()));
						}
						else
						{
							Quad[] tempquads = quad.subdivide(4);
							int[] indices = textureIndices.get(bq.getFace());
							for (int i = 0; i < tempquads.length; i++)
							{
								Quad q = tempquads[i];
								if (q != null)
								{
									int quadrant = q.getUvs().normalize().getQuadrant();
									tempquads[i] = q.grow().transformUVs(indices[quadrant] > 15 ? tex.normal : tex.connected, ConnectedLogic.uvs[indices[quadrant]]);
								}
							}
							quads.addAll(Arrays.stream(tempquads).filter(Objects::nonNull).map(q -> q.rebake()).collect(Collectors.toList()));
						}
					}
				}
			}
		}
		return ret;
	}
}
