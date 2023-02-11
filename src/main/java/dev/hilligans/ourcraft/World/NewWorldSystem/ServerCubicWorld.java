package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BoundingBox;
import dev.hilligans.ourcraft.Server.MultiPlayerServer;
import dev.hilligans.ourcraft.Util.Noises.PerlinNoise;
import dev.hilligans.ourcraft.World.Features.TreeFeature;
import dev.hilligans.ourcraft.World.WorldGen.IWorldHeightBuilder;
import dev.hilligans.planets.world.PlanetFeaturePlacerHelper;
import org.joml.Random;

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

    PerlinNoise perlinNoise = new PerlinNoise(new Random().nextInt(Short.MAX_VALUE), 0.5, 0.03, 200, 3);

    @Override
    public void generateWorld() {
        int chunkGenerationRadius = 15;
        IWorldHeightBuilder.GenerationBoundingBox generationBoundingBox = worldHeightBuilder.getGenerationBoundingBox();
        for(int x = -chunkGenerationRadius; x < chunkGenerationRadius; x++) {
            for(int y = -chunkGenerationRadius; y < chunkGenerationRadius; y++) {
                for(int z = -chunkGenerationRadius; z < chunkGenerationRadius; z++) {
                    IChunk chunk = new CubicChunk(this, 32, x, y, z);
                    setChunk((long)x << widthBits, (long)y << widthBits, (long)z << widthBits, chunk);
                }
            }
        }

        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    setBlockState(x, y, z, Blocks.STONE.getDefaultState1());
                }
            }
        }


        for(int a = -radius; a <= radius; a++) {
            for(int b = -radius; b <= radius; b++) {
                int height = worldHeightBuilder.getWorldHeight(a, b, radius + 1);
                for(int c = 0; c < height; c++) {
                    setBlockState(a, b, radius + c, Blocks.STONE.getDefaultState1());
                }
                for(int c = height; c <= 0; c++) {
                    setBlockState(a, b, radius + c, Blocks.AIR.getDefaultState1());
                }
                if(a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a + ptr, b, radius + 1 + ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a + ptr, b, radius + c, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, b + ptr, radius + 1 + ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a, b + ptr, radius + c, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a - ptr, b, radius + 1 + ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a - ptr, b, radius + c, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, b - ptr, radius + 1 + ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a, b - ptr, radius + c, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, b + length, radius + 1 + Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, b + length, radius + c, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, b - length, radius + 1 + Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, b - length, radius + c, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, b + length, radius + 1 + Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, b + length, radius + c, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, b - length, radius + 1 + Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, b - length, radius + c, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }







                height = worldHeightBuilder.getWorldHeight(a, b, -radius - 1);
                for(int c = 0; c < height; c++) {
                    setBlockState(a, b, -radius - c, Blocks.STONE.getDefaultState1());
                }
                for(int c = height; c <= 0; c++) {
                    setBlockState(a, b, -radius - c , Blocks.AIR.getDefaultState1());
                }
                if(a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a + ptr, b, -radius - 1 - ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a + ptr, b, -radius - c, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, b + ptr, -radius - 1 - ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a, b + ptr, -radius - c, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a - ptr, b, -radius - 1 - ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a - ptr, b, -radius - c, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, b - ptr, -radius - 1 - ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a, b - ptr, -radius - c, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, b + length, -radius - 1 - Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, b + length, -radius - c, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, b - length, -radius - 1 - Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, b - length, -radius - c, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, b + length, -radius - 1 - Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, b + length, -radius - c, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, b - length, -radius - 1 - Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, b - length, -radius - c, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }





                height = worldHeightBuilder.getWorldHeight(a, radius + 1, b);
                for(int c = 0; c < height; c++) {
                    setBlockState(a,  radius + c, b, Blocks.STONE.getDefaultState1());
                }
                for(int c = height; c <= 0; c++) {
                    setBlockState(a,  radius + c, b, Blocks.AIR.getDefaultState1());
                }

                if(a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a + ptr, radius + 1 + ptr, b);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a + ptr,  radius + c, b, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, radius + 1 + ptr, b + ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a,  radius + c, b + ptr, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a - ptr, radius + 1 + ptr, b);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a - ptr,  radius + c, b, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, radius + 1 + ptr, b - ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a,  radius + c, b - ptr, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight( a + width, radius + 1 + Math.max(length, width),  b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, radius + c, b + length, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight( a + width, radius + 1 + Math.max(length, width),  b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, radius + c, b - length, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight( a - width, radius + 1 + Math.max(length, width),  b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, radius + c, b + length, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight( a - width, radius + 1 + Math.max(length, width),  b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, radius + c, b - length, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }






                height = worldHeightBuilder.getWorldHeight(a, -radius - 1, b);
                for(int c = 0; c < height; c++) {
                    setBlockState(a, -radius - c, b, Blocks.STONE.getDefaultState1());
                }
                for(int c = height; c <= 0; c++) {
                    setBlockState(a, -radius - c , b, Blocks.AIR.getDefaultState1());
                }
                if(a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a + ptr, -radius - 1 - ptr, b);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a + ptr, -radius - c, b, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, -radius - 1 - ptr, b + ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a, -radius - c, b + ptr, Blocks.STONE.getDefaultState1());
                        }

                    }
                }
                if(a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a - ptr, -radius - 1 - ptr, b);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a - ptr, -radius - c, b, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, -radius - 1 - ptr, b - ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(a, -radius - c, b - ptr, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width,  -radius - 1 - Math.max(length, width), b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, -radius - c, b + length , Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width,  -radius - 1 - Math.max(length, width), b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, -radius - c, b - length , Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width,  -radius - 1 - Math.max(length, width), b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, -radius - c, b + length , Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width,  -radius - 1 - Math.max(length, width), b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, -radius - c, b - length , Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }






                height = worldHeightBuilder.getWorldHeight(radius + 1, a, b);
                for(int c = 0; c < height; c++) {
                    setBlockState(radius + c, a, b, Blocks.STONE.getDefaultState1());
                }
                for(int c = height; c <= 0; c++) {
                    setBlockState(radius + c, a, b, Blocks.AIR.getDefaultState1());
                }
                if(a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(radius + 1 + ptr, a + ptr, b);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(radius + c, a + ptr, b, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(radius + 1 + ptr, a, b + ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(radius + c, a, b + ptr, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(radius + 1 + ptr, a - ptr, b);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(radius + c, a - ptr, b, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(radius + 1 + ptr, a, b - ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(radius + c, a, b - ptr, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight( radius + 1 + Math.max(length, width), a + width, b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(radius + c, a + width, b + length, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight( radius + 1 + Math.max(length, width), a + width, b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(radius + c, a + width, b - length, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight( radius + 1 + Math.max(length, width), a - width, b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(radius + c, a - width, b + length, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight( radius + 1 + Math.max(length, width), a - width, b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(radius + c, a - width, b - length, Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }





                height = worldHeightBuilder.getWorldHeight(-radius - 1, a, b);
                for(int c = 0; c < height; c++) {
                    setBlockState(-radius - c, a, b, Blocks.STONE.getDefaultState1());
                }
                for(int c = height; c <= 0; c++) {
                    setBlockState(-radius - c , a, b, Blocks.AIR.getDefaultState1());
                }

                if(a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(-radius - 1 - ptr, a + ptr, b);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(-radius - c, a + ptr, b, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(-radius - 1 - ptr, a, b + ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(-radius - c, a, b + ptr, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(-radius - 1 - ptr, a - ptr, b);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(-radius - c, a - ptr, b, Blocks.STONE.getDefaultState1());
                        }
                    }
                }
                if(b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b*-1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(-radius - 1 - ptr, a, b - ptr);
                        for(int c = ptr; c < h; c++) {
                            setBlockState(-radius - c, a, b - ptr, Blocks.STONE.getDefaultState1());
                        }
                    }
                }

                if(a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(-radius - 1 - Math.max(length, width), a + width, b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState( -radius - c, a + width, b + length , Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(-radius - 1 - Math.max(length, width), a + width, b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState( -radius - c, a + width, b - length , Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(-radius - 1 - Math.max(length, width), a - width, b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState( -radius - c, a - width, b + length , Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
                if(a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b*-1 + width) {
                        width++;
                        int length = 0;
                        while(h + radius > a*-1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(-radius - 1 - Math.max(length, width), a - width, b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState( -radius - c, a - width, b - length , Blocks.STONE.getDefaultState1());
                            }
                        }
                    }
                }
            }
        }


        /*
        PlanetFeaturePlacerHelper helper = new PlanetFeaturePlacerHelper(this);
        TreeFeature treeFeature = new TreeFeature();

        BlockPos pos = new BlockPos(0, 0, 0);
        Random random = new Random();
        for(int a = -radius; a < radius; a++) {
            for (int b = -radius; b < radius; b++) {
                if(random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(radius, a, b));
                    treeFeature.place(helper);
                }
                if(random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(a, radius, b));
                    treeFeature.place(helper);
                }
                if(random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(a, b, radius));
                    treeFeature.place(helper);
                }
                if(random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(-radius, a, b));
                    treeFeature.place(helper);
                }
                if(random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(a, -radius, b));
                    treeFeature.place(helper);
                }
                if(random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(a, b, -radius));
                    treeFeature.place(helper);
                }
            }
           }
         */
    }
}
