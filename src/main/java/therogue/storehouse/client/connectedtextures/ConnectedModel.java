
package therogue.storehouse.client.connectedtextures;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import therogue.storehouse.client.connectedtextures.ConnectionState.ConnectedTexturePair;

public class ConnectedModel implements IModel {
	
	private IModel vanillamodel;
	private final Collection<ResourceLocation> textureDependencies;
	private Map<String, ConnectedTexturePair> textures = new HashMap<>();
	
	public ConnectedModel (IModel vanillamodel, ResourceLocation modellocation) {
		this.vanillamodel = vanillamodel;
		this.textureDependencies = new HashSet<>();
		this.textureDependencies.addAll(vanillamodel.getTextures());
		IConnectedTextureLogic logic = CTBlockRegistry.INSTANCE.getCTBlockFromModel(modellocation);
		this.textureDependencies.addAll(logic.getConnectionTextureMap().values());
		this.textureDependencies.removeIf(rl -> rl.getResourcePath().startsWith("#"));
	}
	
	public IModel getVanillaParent () {
		return vanillamodel;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies () {
		return Collections.emptySet();
	}
	
	@Override
	public Collection<ResourceLocation> getTextures () {
		return textureDependencies;
	}
	
	@Override
	public IBakedModel bake (IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		IBakedModel parent = vanillamodel.bake(state, format, rl -> {
			TextureAtlasSprite sprite = bakedTextureGetter.apply(rl);
			textures.computeIfAbsent(sprite.getIconName(), s -> {
				ConnectedTexturePair tex = CTBlockRegistry.INSTANCE.getConnectedTexturePair(sprite, bakedTextureGetter);
				return tex;
			});
			return sprite;
		});
		return new ConnectedBakedModel(this, parent);
	}
	
	@Override
	public IModelState getDefaultState () {
		return TRSRTransformation.identity();
	}
	
	public Collection<ConnectedTexturePair> getConnectedTextures () {
		return ImmutableList.<ConnectedTexturePair> builder().addAll(textures.values()).build();
	}
	
	public ConnectedTexturePair getTexture (String iconName) {
		return textures.get(iconName);
	}
	
	@Override
	public IModel retexture (ImmutableMap<String, String> textures) {
		this.vanillamodel = getVanillaParent().retexture(textures);
		return this;
	}
}
