
package therogue.storehouse.client.connectedtextures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import therogue.storehouse.client.connectedtextures.ConnectionState.ConnectedTexturePair;

public enum CTBlockRegistry {
	INSTANCE;
	
	private final Map<String, GroupLogic> groupings = new HashMap<>();
	private final Map<ResourceLocation, IConnectedTextureLogic> modellocations = new HashMap<>();
	private ConnectionState currentState = new ConnectionState(null, null, null);
	
	public IBlockState extendedState (IBlockState state, IBlockAccess world, BlockPos pos) {
		currentState = new ConnectionState(state, world, pos);
		return state;
	}
	
	public ConnectionState getState () {
		return currentState;
	}
	
	public void registerGroup (String group, Block toRegister) {
		if (groupings.containsKey(group))
		{
			groupings.get(group).addBlock(toRegister);
			modellocations.put(toRegister.getRegistryName(), groupings.get(group));
		}
		else
		{
			GroupLogic gl = new GroupLogic(Lists.newArrayList(toRegister));
			register(gl);
			groupings.put(group, gl);
		}
	}
	
	public void register (Block block) {
		register(new DefaultLogic(block));
	}
	
	public void register (IConnectedTextureLogic theBlock) {
		modellocations.putAll(theBlock.getModelLocation().stream().collect(Collectors.toMap(r -> r, r -> theBlock)));
	}
	
	public IConnectedTextureLogic getCTBlockFromModel (ResourceLocation location) {
		if (!containsModel(location)) throw new NoConnectedTextureRegistration(location.toString());
		location = toRegistry(location);
		return modellocations.get(location);
	}
	
	public boolean containsModel (ResourceLocation location) {
		location = toRegistry(location);
		return modellocations.containsKey(location);
	}
	
	public ConnectedTexturePair getConnectedTexturePair (ResourceLocation modelLocation, TextureAtlasSprite sprite,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		if (!containsModel(modelLocation)) return null;
		IConnectedTextureLogic block = getCTBlockFromModel(modelLocation);
		TextureAtlasSprite connectedsprite = bakedTextureGetter.apply(new ResourceLocation(sprite.getIconName().concat("-connected")));
		return new ConnectedTexturePair(sprite, connectedsprite, block);
	}
	
	private ResourceLocation toRegistry (ResourceLocation from) {
		String domain = from.getResourceDomain();
		String path = from.getResourcePath();
		int lastIndx = path.lastIndexOf("/");
		if (lastIndx != -1)
		{
			path = path.substring(lastIndx, path.length());
		}
		path = path.replace(".json", "");
		return new ResourceLocation(domain, path);
	}
	
	public class NoConnectedTextureRegistration extends NullPointerException {
		
		private static final long serialVersionUID = -8842875361459026189L;
		
		public NoConnectedTextureRegistration (String texture) {
			super(texture);
		}
	}
	
	public interface IConnectedTextureLogic {
		
		/**
		 * @return a function to determine whether this block can connect to the blockstate in the given direction
		 */
		public default boolean connects (ConnectedTexturePair texture, IBlockState from, IBakedModel toModel, IBlockState to, EnumFacing dir) {
			return from.getBlock() == to.getBlock();
		}
		
		public List<ResourceLocation> getModelLocation ();
		
		public default List<ResourceLocation> transformToConnectedTextures (Collection<ResourceLocation> textures) {
			List<ResourceLocation> connectedList = new ArrayList<>();
			for (ResourceLocation unconnected : textures)
			{
				connectedList.add(new ResourceLocation(unconnected.getResourceDomain(), unconnected.getResourcePath() + "-connected"));
			}
			return connectedList;
		}
	}
	
	public static class GroupLogic implements IConnectedTextureLogic {
		
		private final List<ResourceLocation> blockModelLocations = new ArrayList<>();
		private final List<Block> blockGroup;
		
		public GroupLogic (List<Block> group) {
			this.blockGroup = group;
			group.forEach(block -> blockModelLocations.add(block.getRegistryName()));
		}
		
		public void addBlock (Block block) {
			this.blockGroup.add(block);
			this.blockModelLocations.add(block.getRegistryName());
		}
		
		@Override
		public List<ResourceLocation> getModelLocation () {
			return blockModelLocations;
		}
		
		@Override
		public boolean connects (ConnectedTexturePair texture, IBlockState from, IBakedModel toModel, IBlockState to, EnumFacing dir) {
			if (toModel instanceof ConnectedBakedModel)
			{
				ConnectedBakedModel connectedModel = ((ConnectedBakedModel) toModel);
				List<BakedQuad> quads = connectedModel.getParentQuads(to, dir);
				if (quads != null && quads.stream().map(bakedQuad -> bakedQuad.getSprite()).collect(Collectors.toList()).contains(texture.normal)) return true;
				if (blockGroup.contains(to.getBlock())) return true;
			}
			return false;
		}
	}
	
	public static class DefaultLogic implements IConnectedTextureLogic {
		
		private final ResourceLocation blockModelLocation;
		
		public DefaultLogic (Block block) {
			blockModelLocation = block.getRegistryName();
		}
		
		@Override
		public List<ResourceLocation> getModelLocation () {
			return Lists.newArrayList(blockModelLocation);
		}
		
		@Override
		public boolean connects (ConnectedTexturePair texture, IBlockState from, IBakedModel toModel, IBlockState to, EnumFacing dir) {
			if (toModel instanceof ConnectedBakedModel)
			{
				ConnectedBakedModel connectedModel = ((ConnectedBakedModel) toModel);
				List<BakedQuad> quads = connectedModel.getParentQuads(to, dir);
				if (quads != null && quads.stream().map(bakedQuad -> bakedQuad.getSprite()).collect(Collectors.toList()).contains(texture.normal)) return true;
				if (from.getBlock() == to.getBlock()) return true;
			}
			return false;
		}
	}
}
