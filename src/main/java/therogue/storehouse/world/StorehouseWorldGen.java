/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.util.LOG;

public class StorehouseWorldGen implements IWorldGenerator {
	
	public static final StorehouseWorldGen INSTANCE = new StorehouseWorldGen();
	private WorldGenerator azurite_ore_gen; // Generates Azurite Ore
	private WorldGenerator copper_ore_gen;
	private WorldGenerator tin_ore_gen;
	
	public StorehouseWorldGen () {
		azurite_ore_gen = new WorldGenMinable(ModBlocks.ore_blocks.getStateFromMeta(ModBlocks.azurite_ore_itemstack.getMetadata()), 3);
		copper_ore_gen = new WorldGenMinable(ModBlocks.ore_blocks.getStateFromMeta(ModBlocks.copper_ore_itemstack.getMetadata()), 8);
		tin_ore_gen = new WorldGenMinable(ModBlocks.ore_blocks.getStateFromMeta(ModBlocks.tin_ore_itemstack.getMetadata()), 8);
	}
	
	@Override
	public void generate (Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.isSurfaceWorld() && world.provider.getAverageGroundLevel() > 55 && world.provider.getAverageGroundLevel() < 70 && world.provider.getDimension() != 1)
		{
			this.runGenerator(this.azurite_ore_gen, world, random, chunkX, chunkZ, 40, 2, 52);
			this.runGenerator(this.copper_ore_gen, world, random, chunkX, chunkZ, 8, 40, 75);
			this.runGenerator(this.tin_ore_gen, world, random, chunkX, chunkZ, 7, 20, 55);
		}
	}
	
	private void runGenerator (WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
		while (minHeight < 0)
		{
			++minHeight;
		}
		while (maxHeight > 256)
		{
			--minHeight;
		}
		if (minHeight > maxHeight)
		{
			int temp = minHeight;
			minHeight = maxHeight;
			maxHeight = temp;
		}
		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight) throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator"); // Should never be thrown
		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chancesToSpawn; i++)
		{
			int x = chunk_X * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z * 16 + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}
	
	public static void init () {
		LOG.log("trace", "Registering World Generator");
		GameRegistry.registerWorldGenerator(INSTANCE, 0);
	}
}
