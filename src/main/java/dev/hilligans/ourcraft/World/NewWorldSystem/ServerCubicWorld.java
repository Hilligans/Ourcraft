package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BoundingBox;
import dev.hilligans.ourcraft.Server.MultiPlayerServer;
import dev.hilligans.ourcraft.World.WorldGen.IWorldHeightBuilder;

public class ServerCubicWorld extends CubicWorld implements IServerWorld {

    public MultiPlayerServer multiPlayerServer;
    public IWorldHeightBuilder worldHeightBuilder;
    public final int widthBits = 5;

    public ServerCubicWorld(int id, String worldName, int radius, IWorldHeightBuilder worldHeightBuilder) {
        super(id, worldName, radius);
        this.worldHeightBuilder = worldHeightBuilder;
    }

    @Override
    public BlockPos getWorldSpawn(BoundingBox boundingBox) {
        return new BlockPos(0,64,0);
    }

    @Override
    public void setServer(MultiPlayerServer server) {
        this.multiPlayerServer = server;
    }

    @Override
    public MultiPlayerServer getServer() {
        return multiPlayerServer;
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        IChunk chunk = getChunk(blockX, blockY, blockZ);
        if(chunk == null) {
            chunk = new CubicChunk(this, 1 << widthBits, (int) (blockX >> widthBits), (int) (blockY >> widthBits), (int) (blockZ >> widthBits));
        }
        return chunk;
    }

    @Override
    public void generateWorld() {
        int chunkGenerationRadius = 10;
        IWorldHeightBuilder.GenerationBoundingBox generationBoundingBox = worldHeightBuilder.getGenerationBoundingBox();
        for(int x = -chunkGenerationRadius; x < chunkGenerationRadius; x++) {
            for(int y = -chunkGenerationRadius; y < chunkGenerationRadius; y++) {
                for(int z = -chunkGenerationRadius; z < chunkGenerationRadius; z++) {
                    IChunk chunk = new CubicChunk(this, 32, x, y, z);
                    setChunk((long)x << widthBits, (long)y << widthBits, (long)z << widthBits, chunk);
                }
            }
        }

        for(int x = -radius; x < radius; x++) {
            for(int y = -radius; y < radius; y++) {
                for(int z = -radius; z < radius; z++) {
                    setBlockState(x, y, z, Blocks.STONE.getDefaultState1());
                }
            }
        }


        for(int a = 0; a < radius; a++) {
            for(int b = 0; b < radius; b++) {
                int height = worldHeightBuilder.getWorldHeight(a, b, radius);
                for(int c = 0; c < height; c++) {
                    setBlockState(a, b, radius + c, Blocks.STONE.getDefaultState1());
                }
                height = worldHeightBuilder.getWorldHeight(a, b, -radius);
                for(int c = 0; c < height; c++) {
                    setBlockState(a, b, radius - c, Blocks.STONE.getDefaultState1());
                }
                height = worldHeightBuilder.getWorldHeight(a, radius, b);
                for(int c = 0; c < height; c++) {
                    setBlockState(a,  radius + c, b, Blocks.STONE.getDefaultState1());
                }
                height = worldHeightBuilder.getWorldHeight(a, -radius, b);
                for(int c = 0; c < height; c++) {
                    setBlockState(a, radius - c, b, Blocks.STONE.getDefaultState1());
                }
                height = worldHeightBuilder.getWorldHeight(radius, a, b);
                for(int c = 0; c < height; c++) {
                    setBlockState(radius + c, a, b, Blocks.STONE.getDefaultState1());
                }
                height = worldHeightBuilder.getWorldHeight(-radius, a, b);
                for(int c = 0; c < height; c++) {
                    setBlockState(radius - c, a, b, Blocks.STONE.getDefaultState1());
                }
            }
        }
    }
}
