
package therogue.storehouse.client.connectedtextures;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Maps;

import gnu.trove.impl.Constants;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.custom_hash.TObjectByteCustomHashMap;
import gnu.trove.map.custom_hash.TObjectLongCustomHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.property.IUnlistedProperty;
import therogue.storehouse.Storehouse;
import therogue.storehouse.client.connectedtextures.IConnectedTextureLogic.IConnectionTest;

/**
 * List of IBlockRenderContext's
 */
@ParametersAreNonnullByDefault
public class ConnectionState {
	
	private Map<ConnectedTexturePair, EnumMap<EnumFacing, int[]>> textureIndices;
	private TObjectLongMap<ConnectedTexturePair> serialized;
	private IBlockAccess world;
	private BlockPos pos;
	public final Map<ConnectedTexturePair, Map<CacheKey, TObjectByteMap<IBlockState>>> textureConnectionCache = new HashMap<>();
	
	public ConnectionState (IBlockAccess world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}
	
	public void buildCache (IBlockState state, Collection<ConnectedTexturePair> textures) {
		if (textureIndices != null) return;
		world = new WorldCache(world);
		textureIndices = Maps.newIdentityHashMap();
		serialized = new TObjectLongCustomHashMap<>(new IdentityHashingStrategy<>());
		for (ConnectedTexturePair tex : textures)
		{
			EnumMap<EnumFacing, int[]> ctmData = new EnumMap<>(EnumFacing.class);
			long data = 0;
			Map<CacheKey, TObjectByteMap<IBlockState>> connectionCache = textureConnectionCache.computeIfAbsent(tex, (key) -> new HashMap<>());
			for (EnumFacing face : EnumFacing.VALUES)
			{
				ConnectedLogic logic = new ConnectedLogic(world, pos, face, tex.connectionLogic, (boolean ignoreStates, IBlockState from,
						IBlockState to, EnumFacing dir) -> {
					synchronized (connectionCache)
					{
						TObjectByteMap<IBlockState> sidecache = connectionCache.computeIfAbsent(new CacheKey(from, dir), k -> new TObjectByteCustomHashMap<>(new IdentityHashingStrategy<>(), Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, (byte) -1));
						byte cached = sidecache.get(to);
						if (cached == -1)
						{
							sidecache.put(to, cached = (byte) ((tex.connectionLogic.canConnect() == null ? IConnectionTest.DEFAULT.connects(ignoreStates, from, to, dir) : tex.connectionLogic.canConnect().connects(ignoreStates, from, to, dir)) ? 1 : 0));
						}
						return cached == 1;
					}
				});
				ctmData.put(face, logic.submapCache);
				data |= logic.serialized << (face.ordinal() * 10);
			}
			textureIndices.put(tex, ctmData);
			serialized.put(tex, data);
		}
	}
	
	public @Nullable EnumMap<EnumFacing, int[]> getTextureIndices (ConnectedTexturePair tex) {
		return this.textureIndices.get(tex);
	}
	
	public TObjectLongMap<ConnectedTexturePair> serialized () {
		return serialized;
	}
	
	public static final class ConnectedTexturePair {
		
		public final TextureAtlasSprite normal;
		public final TextureAtlasSprite connected;
		public final IConnectedTextureLogic connectionLogic;
		
		public ConnectedTexturePair (TextureAtlasSprite normal, TextureAtlasSprite connected, IConnectedTextureLogic connectionLogic) {
			this.normal = normal;
			this.connected = connected;
			this.connectionLogic = connectionLogic;
		}
	}
	
	public static final class CacheKey {
		
		private final IBlockState from;
		private final EnumFacing dir;
		
		public CacheKey (IBlockState from, EnumFacing dir) {
			this.from = from;
			this.dir = dir;
		}
		
		@Override
		public int hashCode () {
			final int prime = 31;
			int result = 1;
			result = prime * result + dir.hashCode();
			result = prime * result + from.hashCode();
			return result;
		}
		
		@Override
		public boolean equals (@Nullable Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey other = (CacheKey) obj;
			if (dir != other.dir)
				return false;
			if (from != other.from)
				return false;
			return true;
		}
	}
	
	/**
	 * Used by render state creation to avoid unnecessary block lookups through the world.
	 */
	@ParametersAreNonnullByDefault
	public class WorldCache implements IBlockAccess {
		
		private final IBlockAccess wrapped;
		private final Map<BlockPos, IBlockState> stateCache = new HashMap<>();
		
		public WorldCache (IBlockAccess passthrough) {
			this.wrapped = passthrough;
		}
		
		@Override
		@Nullable
		public TileEntity getTileEntity (BlockPos pos) {
			return wrapped.getTileEntity(pos);
		}
		
		@Override
		public int getCombinedLight (BlockPos pos, int lightValue) {
			return wrapped.getCombinedLight(pos, lightValue);
		}
		
		@Override
		public IBlockState getBlockState (BlockPos pos) {
			return stateCache.computeIfAbsent(pos, wrapped::getBlockState);
		}
		
		@Override
		public boolean isAirBlock (BlockPos pos) {
			IBlockState state = getBlockState(pos);
			return state.getBlock().isAir(state, this, pos);
		}
		
		@Override
		public Biome getBiome (BlockPos pos) {
			return wrapped.getBiome(pos);
		}
		
		@Override
		public int getStrongPower (BlockPos pos, EnumFacing direction) {
			return wrapped.getStrongPower(pos, direction);
		}
		
		@Override
		public WorldType getWorldType () {
			return wrapped.getWorldType();
		}
		
		@Override
		public boolean isSideSolid (BlockPos pos, EnumFacing side, boolean _default) {
			return wrapped.isSideSolid(pos, side, _default);
		}
	}
	
	public static enum RenderProperty implements IUnlistedProperty<ConnectionState> {
		INSTANCE;
		
		@Override
		public String getName () {
			return Storehouse.RESOURCENAMEPREFIX + "render_property";
		}
		
		@Override
		public boolean isValid (ConnectionState value) {
			return true;
		}
		
		@Override
		public Class<ConnectionState> getType () {
			return ConnectionState.class;
		}
		
		@Override
		public String valueToString (ConnectionState value) {
			return value.serialized().toString();
		}
	}
}
