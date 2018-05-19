
package therogue.storehouse.client.connectedtextures;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.base.Preconditions;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.client.FMLClientHandler;

@ParametersAreNonnullByDefault
public class Quad {
	
	public static final class Vertex {
		
		private final Vector3f pos;
		private final Vector2f uvs;
		
		public Vertex (Vector3f pos, Vector2f uvs) {
			this.pos = pos;
			this.uvs = uvs;
		}
		
		public Vector3f getPos () {
			return pos;
		}
		
		public Vector2f getUvs () {
			return uvs;
		}
		
		@Override
		public int hashCode () {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((pos == null) ? 0 : pos.hashCode());
			result = prime * result + ((uvs == null) ? 0 : uvs.hashCode());
			return result;
		}
		
		@Override
		public boolean equals (Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			Vertex other = (Vertex) obj;
			if (pos == null)
			{
				if (other.pos != null) return false;
			}
			else if (!pos.equals(other.pos)) return false;
			if (uvs == null)
			{
				if (other.uvs != null) return false;
			}
			else if (!uvs.equals(other.uvs)) return false;
			return true;
		}
		
		@Override
		public String toString () {
			return "Vertex [pos=" + pos + ", uvs=" + uvs + "]";
		}
	}
	
	private static final TextureAtlasSprite BASE = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TextureMap.LOCATION_MISSING_TEXTURE.toString());
	
	public class UVs {
		
		private float minU, minV, maxU, maxV;
		private final TextureAtlasSprite sprite;
		private final Vector2f[] data;
		
		private UVs (Vector2f... data) {
			this(BASE, data);
		}
		
		private UVs (TextureAtlasSprite sprite, Vector2f... data) {
			this.data = data;
			this.sprite = sprite;
			float minU = Float.MAX_VALUE;
			float minV = Float.MAX_VALUE;
			float maxU = 0, maxV = 0;
			for (Vector2f v : data)
			{
				minU = Math.min(minU, v.x);
				minV = Math.min(minV, v.y);
				maxU = Math.max(maxU, v.x);
				maxV = Math.max(maxV, v.y);
			}
			this.minU = minU;
			this.minV = minV;
			this.maxU = maxU;
			this.maxV = maxV;
		}
		
		public UVs (float minU, float minV, float maxU, float maxV, TextureAtlasSprite sprite) {
			this.minU = minU;
			this.minV = minV;
			this.maxU = maxU;
			this.maxV = maxV;
			this.sprite = sprite;
			this.data = vectorize();
		}
		
		public float getMinU () {
			return minU;
		}
		
		public float getMinV () {
			return minV;
		}
		
		public float getMaxU () {
			return maxU;
		}
		
		public float getMaxV () {
			return maxV;
		}
		
		public TextureAtlasSprite getSprite () {
			return sprite;
		}
		
		public UVs transform (TextureAtlasSprite other, float[] submap) {
			UVs normal = normalize();
			float width = normal.maxU - normal.minU;
			float height = normal.maxV - normal.minV;
			float minU = submap[2];
			float minV = submap[3];
			minU += normal.minU * submap[0];
			minV += normal.minV * submap[1];
			float maxU = minU + (width * submap[0]);
			float maxV = minV + (height * submap[1]);
			Vector2f vec0U = new Vector2f(data[0].x == this.minU ? minU : maxU, data[0].y == this.minV ? minV : maxV);
			Vector2f vec1U = new Vector2f(data[1].x == this.minU ? minU : maxU, data[1].y == this.minV ? minV : maxV);
			Vector2f vec2U = new Vector2f(data[2].x == this.minU ? minU : maxU, data[2].y == this.minV ? minV : maxV);
			Vector2f vec3U = new Vector2f(data[3].x == this.minU ? minU : maxU, data[3].y == this.minV ? minV : maxV);
			return new UVs(other, vec0U, vec1U, vec2U, vec3U).relativize();
		}
		
		UVs normalizeQuadrant () {
			UVs normal = normalize();
			int quadrant = normal.getQuadrant();
			float minUInterp = quadrant == 1 || quadrant == 2 ? 0.5f : 0;
			float minVInterp = quadrant < 2 ? 0.5f : 0;
			float maxUInterp = quadrant == 0 || quadrant == 3 ? 0.5f : 1;
			float maxVInterp = quadrant > 1 ? 0.5f : 1;
			normal = new UVs(sprite, normalize(new Vector2f(minUInterp, minVInterp), new Vector2f(maxUInterp, maxVInterp), normal.vectorize()));
			return normal.relativize();
		}
		
		public UVs normalize () {
			Vector2f min = new Vector2f(sprite.getMinU(), sprite.getMinV());
			Vector2f max = new Vector2f(sprite.getMaxU(), sprite.getMaxV());
			return new UVs(sprite, normalize(min, max, data));
		}
		
		public UVs relativize () {
			return relativize(sprite);
		}
		
		public UVs relativize (TextureAtlasSprite sprite) {
			Vector2f min = new Vector2f(sprite.getMinU(), sprite.getMinV());
			Vector2f max = new Vector2f(sprite.getMaxU(), sprite.getMaxV());
			return new UVs(sprite, lerp(min, max, data));
		}
		
		public Vector2f[] vectorize () {
			return data == null ? new Vector2f[] { new Vector2f(minU, minV), new Vector2f(minU, maxV), new Vector2f(maxU, maxV), new Vector2f(maxU, minV) } : data;
		}
		
		private Vector2f[] normalize (Vector2f min, Vector2f max, @Nonnull Vector2f... vecs) {
			Vector2f[] ret = new Vector2f[vecs.length];
			for (int i = 0; i < ret.length; i++)
			{
				ret[i] = normalize(min, max, vecs[i]);
			}
			return ret;
		}
		
		private Vector2f normalize (Vector2f min, Vector2f max, Vector2f vec) {
			return new Vector2f(Quad.normalize(min.x, max.x, vec.x), Quad.normalize(min.y, max.y, vec.y));
		}
		
		private Vector2f[] lerp (Vector2f min, Vector2f max, @Nonnull Vector2f... vecs) {
			Vector2f[] ret = new Vector2f[vecs.length];
			for (int i = 0; i < ret.length; i++)
			{
				ret[i] = lerp(min, max, vecs[i]);
			}
			return ret;
		}
		
		private Vector2f lerp (Vector2f min, Vector2f max, Vector2f vec) {
			return new Vector2f(Quad.lerp(min.x, max.x, vec.x), Quad.lerp(min.y, max.y, vec.y));
		}
		
		public int getQuadrant () {
			if (maxU <= 0.5f)
			{
				if (maxV <= 0.5f)
				{
					return 3;
				}
				else
				{
					return 0;
				}
			}
			else
			{
				if (maxV <= 0.5f)
				{
					return 2;
				}
				else
				{
					return 1;
				}
			}
		}
		
		@Override
		public String toString () {
			return "UVs [minU=" + minU
					+ ", minV="
					+ minV
					+ ", maxU="
					+ maxU
					+ ", maxV="
					+ maxV
					+ ", sprite="
					+ sprite
					+ ", data="
					+ Arrays.toString(data)
					+ "]";
		}
	}
	
	private final Vector3f[] vertPos;
	private final Vector2f[] vertUv;
	// Technically nonfinal, but treated as such except in constructor
	private UVs uvs;
	private final Builder builder;
	private final int blocklight, skylight;
	
	private Quad (Vector3f[] verts, Vector2f[] uvs, Builder builder, TextureAtlasSprite sprite) {
		this(verts, uvs, builder, sprite, 0, 0);
	}
	
	private Quad (Vector3f[] verts, Vector2f[] uvs, Builder builder, TextureAtlasSprite sprite, int blocklight, int skylight) {
		this.vertPos = verts;
		this.vertUv = uvs;
		this.builder = builder;
		this.uvs = new UVs(sprite, uvs);
		this.blocklight = blocklight;
		this.skylight = skylight;
	}
	
	private Quad (Vector3f[] verts, UVs uvs, Builder builder) {
		this(verts, uvs.vectorize(), builder, uvs.getSprite());
	}
	
	private Quad (Vector3f[] verts, UVs uvs, Builder builder, int blocklight, int skylight) {
		this(verts, uvs.vectorize(), builder, uvs.getSprite(), blocklight, skylight);
	}
	
	public UVs getUvs () {
		return uvs;
	}
	
	public Vector3f getVert (int index) {
		return new Vector3f(vertPos[index % 4]);
	}
	
	public Quad withVert (int index, Vector3f vert) {
		Preconditions.checkElementIndex(index, 4, "Vertex index out of range!");
		Vector3f[] newverts = new Vector3f[4];
		System.arraycopy(vertPos, 0, newverts, 0, newverts.length);
		newverts[index] = vert;
		return new Quad(newverts, getUvs(), builder);
	}
	
	public Vector2f getUv (int index) {
		return new Vector2f(vertUv[index % 4]);
	}
	
	public Quad withUv (int index, Vector2f uv) {
		Preconditions.checkElementIndex(index, 4, "UV index out of range!");
		Vector2f[] newuvs = new Vector2f[4];
		System.arraycopy(getUvs().vectorize(), 0, newuvs, 0, newuvs.length);
		newuvs[index] = uv;
		return new Quad(vertPos, new UVs(newuvs), builder);
	}
	
	public Quad[] subdivide (int count) {
		if (count == 1)
		{
			return new Quad[] { this };
		}
		else if (count != 4) { throw new UnsupportedOperationException(); }
		List<Quad> rects = Lists.newArrayList();
		Pair<Quad, Quad> firstDivide = divide(false);
		Pair<Quad, Quad> secondDivide = firstDivide.getLeft().divide(true);
		rects.add(secondDivide.getLeft());
		if (firstDivide.getRight() != null)
		{
			Pair<Quad, Quad> thirdDivide = firstDivide.getRight().divide(true);
			rects.add(thirdDivide.getLeft());
			rects.add(thirdDivide.getRight());
		}
		else
		{
			rects.add(null);
			rects.add(null);
		}
		rects.add(secondDivide.getRight());
		return rects.toArray(new Quad[rects.size()]);
	}
	
	private Pair<Quad, Quad> divide (boolean vertical) {
		float min, max;
		UVs uvs = getUvs().normalize();
		if (vertical)
		{
			min = uvs.minV;
			max = uvs.maxV;
		}
		else
		{
			min = uvs.minU;
			max = uvs.maxU;
		}
		if (min < 0.5 && max > 0.5)
		{
			UVs first = new UVs(vertical ? uvs.minU : 0.5f, vertical ? 0.5f : uvs.minV, uvs.maxU, uvs.maxV, uvs.getSprite());
			UVs second = new UVs(uvs.minU, uvs.minV, vertical ? uvs.maxU : 0.5f, vertical ? 0.5f : uvs.maxV, uvs.getSprite());
			int firstIndex = 0;
			for (int i = 0; i < vertUv.length; i++)
			{
				if (vertUv[i].y == getUvs().minV && vertUv[i].x == getUvs().minU)
				{
					firstIndex = i;
					break;
				}
			}
			float f = (0.5f - min) / (max - min);
			Vector3f[] firstQuad = new Vector3f[4];
			Vector3f[] secondQuad = new Vector3f[4];
			for (int i = 0; i < 4; i++)
			{
				int idx = (firstIndex + i) % 4;
				firstQuad[i] = new Vector3f(vertPos[idx]);
				secondQuad[i] = new Vector3f(vertPos[idx]);
			}
			int i1 = 0;
			int i2 = vertical ? 1 : 3;
			int j1 = vertical ? 3 : 1;
			int j2 = 2;
			firstQuad[i1].x = lerp(firstQuad[i1].x, firstQuad[i2].x, f);
			firstQuad[i1].y = lerp(firstQuad[i1].y, firstQuad[i2].y, f);
			firstQuad[i1].z = lerp(firstQuad[i1].z, firstQuad[i2].z, f);
			firstQuad[j1].x = lerp(firstQuad[j1].x, firstQuad[j2].x, f);
			firstQuad[j1].y = lerp(firstQuad[j1].y, firstQuad[j2].y, f);
			firstQuad[j1].z = lerp(firstQuad[j1].z, firstQuad[j2].z, f);
			secondQuad[i2].x = lerp(secondQuad[i1].x, secondQuad[i2].x, f);
			secondQuad[i2].y = lerp(secondQuad[i1].y, secondQuad[i2].y, f);
			secondQuad[i2].z = lerp(secondQuad[i1].z, secondQuad[i2].z, f);
			secondQuad[j2].x = lerp(secondQuad[j1].x, secondQuad[j2].x, f);
			secondQuad[j2].y = lerp(secondQuad[j1].y, secondQuad[j2].y, f);
			secondQuad[j2].z = lerp(secondQuad[j1].z, secondQuad[j2].z, f);
			Quad q1 = new Quad(firstQuad, first.relativize(), builder, blocklight, skylight);
			Quad q2 = new Quad(secondQuad, second.relativize(), builder, blocklight, skylight);
			return Pair.of(q1, q2);
		}
		else
		{
			return Pair.of(this, null);
		}
	}
	
	public static float lerp (float a, float b, float f) {
		float ret = (a * (1 - f)) + (b * f);
		return ret;
	}
	
	public static float normalize (float min, float max, float x) {
		float ret = (x - min) / (max - min);
		return ret;
	}
	
	public Quad rotate (int amount) {
		Vector2f[] uvs = new Vector2f[4];
		TextureAtlasSprite s = getUvs().getSprite();
		for (int i = 0; i < 4; i++)
		{
			Vector2f normalized = new Vector2f(normalize(s.getMinU(), s.getMaxU(), vertUv[i].x), normalize(s.getMinV(), s.getMaxV(), vertUv[i].y));
			Vector2f uv;
			switch (amount)
			{
				case 1:
					uv = new Vector2f(normalized.y, 1 - normalized.x);
					break;
				case 2:
					uv = new Vector2f(1 - normalized.x, 1 - normalized.y);
					break;
				case 3:
					uv = new Vector2f(1 - normalized.y, normalized.x);
					break;
				default:
					uv = new Vector2f(normalized.x, normalized.y);
					break;
			}
			uvs[i] = uv;
		}
		for (int i = 0; i < uvs.length; i++)
		{
			uvs[i] = new Vector2f(lerp(s.getMinU(), s.getMaxU(), uvs[i].x), lerp(s.getMinV(), s.getMaxV(), uvs[i].y));
		}
		Quad ret = new Quad(vertPos, uvs, builder, getUvs().getSprite(), blocklight, skylight);
		return ret;
	}
	
	public Quad derotate () {
		int start = 0;
		for (int i = 0; i < 4; i++)
		{
			if (vertUv[i].x <= getUvs().minU && vertUv[i].y <= getUvs().minV)
			{
				start = i;
				break;
			}
		}
		Vector2f[] uvs = new Vector2f[4];
		for (int i = 0; i < 4; i++)
		{
			uvs[i] = vertUv[(i + start) % 4];
		}
		return new Quad(vertPos, uvs, builder, getUvs().getSprite(), blocklight, skylight);
	}
	
	public BakedQuad rebake () {
		@Nonnull
		VertexFormat format = this.builder.vertexFormat;
		// Sorry OF users
		boolean hasLightmap = (this.blocklight > 0 || this.skylight > 0) && !FMLClientHandler.instance().hasOptifine();
		if (hasLightmap)
		{
			// TODO waiting on https://github.com/MinecraftForge/MinecraftForge/pull/3896
			// if (format == DefaultVertexFormats.ITEM) { // ITEM is convertable to BLOCK (replace normal+padding with lmap)
			// format = DefaultVertexFormats.BLOCK;
			// } else
			if (!format.getElements().contains(DefaultVertexFormats.TEX_2S))
			{ // Otherwise, this format is unknown, add TEX_2S if it does not exist
				format = new VertexFormat(format).addElement(DefaultVertexFormats.TEX_2S);
			}
		}
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		builder.setQuadOrientation(this.builder.quadOrientation);
		builder.setQuadTint(this.builder.quadTint);
		builder.setApplyDiffuseLighting(this.builder.applyDiffuseLighting);
		builder.setTexture(this.uvs.getSprite());
		for (int v = 0; v < 4; v++)
		{
			for (int i = 0; i < format.getElementCount(); i++)
			{
				VertexFormatElement ele = format.getElement(i);
				switch (ele.getUsage())
				{
					case UV:
						if (ele.getIndex() == 1)
						{
							// Stuff for fullbright
							builder.put(i, ((float) blocklight * 0x20) / 0xFFFF, ((float) skylight * 0x20) / 0xFFFF);
						}
						else if (ele.getIndex() == 0)
						{
							Vector2f uv = vertUv[v];
							builder.put(i, uv.x, uv.y, 0, 1);
						}
						break;
					case POSITION:
						Vector3f p = vertPos[v];
						builder.put(i, p.x, p.y, p.z, 1);
						break;
					/*
					 * case COLOR:
					 * builder.put(i, 35, 162, 204); Pretty things
					 * break;
					 */
					default:
						builder.put(i, this.builder.data.get(ele.getUsage()).get(v));
				}
			}
		}
		return builder.build();
	}
	
	public Quad transformUVs (TextureAtlasSprite sprite) {
		return transformUVs(sprite, ConnectedLogic.FULL_TEXTURE);
	}
	
	public Quad transformUVs (TextureAtlasSprite sprite, float[] submap) {
		return new Quad(vertPos, getUvs().transform(sprite, submap), builder, blocklight, skylight);
	}
	
	public Quad grow () {
		return new Quad(vertPos, getUvs().normalizeQuadrant(), builder, blocklight, skylight);
	}
	
	public static Quad from (BakedQuad baked) {
		Builder b = new Builder(baked.getFormat(), baked.getSprite());
		baked.pipe(b);
		return b.build();
	}
	
	@Override
	public String toString () {
		return "Quad [vertPos=" + Arrays.toString(vertPos) + ", vertUv=" + Arrays.toString(vertUv) + "]";
	}
	
	public static class Builder implements IVertexConsumer {
		
		private final VertexFormat vertexFormat;
		private final TextureAtlasSprite sprite;
		private int quadTint = -1;
		private EnumFacing quadOrientation;
		private boolean applyDiffuseLighting;
		private ListMultimap<EnumUsage, float[]> data = MultimapBuilder.enumKeys(EnumUsage.class).arrayListValues().build();
		
		public Builder (VertexFormat vertexFormat, TextureAtlasSprite sprite) {
			this.vertexFormat = vertexFormat;
			this.sprite = sprite;
		}
		
		public VertexFormat getVertexFormat () {
			return vertexFormat;
		}
		
		public TextureAtlasSprite getSprite () {
			return sprite;
		}
		
		public void setQuadTint (int quadTint) {
			this.quadTint = quadTint;
		}
		
		public void setQuadOrientation (EnumFacing quadOrientation) {
			this.quadOrientation = quadOrientation;
		}
		
		public void setApplyDiffuseLighting (boolean applyDiffuseLighting) {
			this.applyDiffuseLighting = applyDiffuseLighting;
		}
		
		@Override
		public void put (int element, @Nullable float... data) {
			if (data == null) return;
			float[] copy = new float[data.length];
			System.arraycopy(data, 0, copy, 0, data.length);
			VertexFormatElement ele = vertexFormat.getElement(element);
			this.data.put(ele.getUsage(), copy);
		}
		
		public Quad build () {
			Vector3f[] verts = fromData(data.get(EnumUsage.POSITION), 3);
			Vector2f[] uvs = fromData(data.get(EnumUsage.UV), 2);
			return new Quad(verts, uvs, this, getSprite());
		}
		
		@SuppressWarnings ("unchecked")
		private <T extends Vector> T[] fromData (List<float[]> data, int size) {
			Vector[] ret = size == 2 ? new Vector2f[data.size()] : new Vector3f[data.size()];
			for (int i = 0; i < data.size(); i++)
			{
				ret[i] = size == 2 ? new Vector2f(data.get(i)[0], data.get(i)[1]) : new Vector3f(data.get(i)[0], data.get(i)[1], data.get(i)[2]);
			}
			return (T[]) ret;
		}
		
		@Override
		public void setTexture (@Nullable TextureAtlasSprite texture) {
		}
	}
}
