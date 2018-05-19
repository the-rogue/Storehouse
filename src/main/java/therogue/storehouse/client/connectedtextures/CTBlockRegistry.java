
package therogue.storehouse.client.connectedtextures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.LOG;
import therogue.storehouse.Storehouse;
import therogue.storehouse.client.connectedtextures.ConnectionState.ConnectedTexturePair;

public enum CTBlockRegistry {
	INSTANCE;
	
	private final Map<ResourceLocation, IConnectedTextureLogic> modellocations = new HashMap<ResourceLocation, IConnectedTextureLogic>();
	private final Map<ResourceLocation, IConnectedTextureLogic> textureLocations = new HashMap<ResourceLocation, IConnectedTextureLogic>();
	
	public void registerS (Block block, String... locations) {
		register(block, Storehouse.MOD_ID, locations);
	}
	
	public void register (Block block, String domain, String... locations) {
		register(new DefaultLogic(block, domain, locations));
	}
	
	public void register (Block block, ResourceLocation... locations) {
		register(new DefaultLogic(block, locations));
	}
	
	public void register (Block block, List<ResourceLocation> locations) {
		register(new DefaultLogic(block, locations));
	}
	
	public void register (IConnectedTextureLogic theBlock) {
		modellocations.put(theBlock.getModelLocation(), theBlock);
		textureLocations.putAll(theBlock.getTextures().stream().collect(Collectors.toMap(r -> r, r -> theBlock)));
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
	
	public IConnectedTextureLogic getCTBlockFromTexture (ResourceLocation location) {
		LOG.info(textureLocations + " location: " + location);
		if (!containsTexture(location)) throw new NoConnectedTextureRegistration(location.toString());
		return textureLocations.get(location);
	}
	
	public boolean containsTexture (ResourceLocation location) {
		return textureLocations.containsKey(location);
	}
	
	public ConnectedTexturePair getConnectedTexturePair (TextureAtlasSprite sprite,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		if (!containsTexture(new ResourceLocation(sprite.getIconName()))) return null;
		IConnectedTextureLogic block = getCTBlockFromTexture(new ResourceLocation(sprite.getIconName()));// TODO to texturelocations
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
	
	public static class DefaultLogic implements IConnectedTextureLogic {
		
		private final ResourceLocation blockModelLocation;
		private final List<ResourceLocation> textureLocations;
		
		public DefaultLogic (Block block, String domain, String... locations) {
			this(block, Lists.newArrayList(locations).stream().map(s -> new ResourceLocation(domain, s)).collect(Collectors.toList()));
		}
		
		public DefaultLogic (Block block, ResourceLocation... locations) {
			this(block, Lists.newArrayList(locations));
		}
		
		public DefaultLogic (Block block, List<ResourceLocation> locations) {
			blockModelLocation = block.getRegistryName();
			textureLocations = locations;
		}
		
		@Override
		public ResourceLocation getModelLocation () {
			return blockModelLocation;
		}
		
		@Override
		public List<ResourceLocation> getTextures () {
			return textureLocations;// "blocks/machine/generator/solar/advanced"
		}
	}
}
