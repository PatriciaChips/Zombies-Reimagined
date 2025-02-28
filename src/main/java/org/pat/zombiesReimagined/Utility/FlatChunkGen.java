package org.pat.zombiesReimagined.Utility;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class FlatChunkGen extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome){

        ChunkData chunk = createChunkData(world);

        for(int x = 0; x <= 15; x++){
            for(int z = 0; z <= 15; z++){
                chunk.setBlock(x, -5, z, Material.BEDROCK);
                chunk.setBlock(x, 11, z, Material.GRASS_BLOCK);
                for(int y = -4; y <= 10; y++) {
                    if (y <= 6) {
                        chunk.setBlock(x, y, z, Material.STONE);
                    } else {
                        chunk.setBlock(x, y, z, Material.DIRT);
                    }
                }
            }
        }

        return chunk;

    }
}
