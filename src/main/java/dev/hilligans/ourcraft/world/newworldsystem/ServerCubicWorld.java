package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.server.concurrent.ChunkLocker;
import dev.hilligans.ourcraft.server.MultiPlayerServer;
import dev.hilligans.ourcraft.util.noises.PerlinNoise;
import dev.hilligans.ourcraft.world.features.TreeFeature;
import dev.hilligans.ourcraft.world.gen.IWorldHeightBuilder;
import dev.hilligans.planets.world.PlanetFeaturePlacerHelper;
import org.joml.Random;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class ServerCubicWorld extends CubicWorld implements IServerWorldBase {

    public MultiPlayerServer multiPlayerServer;
    public IWorldHeightBuilder worldHeightBuilder;
    public IWorldGenerator worldGenerator;
    public final int widthBits = 5;
    public ChunkLocker chunkLocker = new ChunkLocker();


    public ConcurrentLinkedQueue<Consumer<IServerWorld>> postTickFutures = new ConcurrentLinkedQueue<>();


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
        for (int x = -chunkGenerationRadius; x < chunkGenerationRadius; x++) {
            for (int y = -chunkGenerationRadius; y < chunkGenerationRadius; y++) {
                for (int z = -chunkGenerationRadius; z < chunkGenerationRadius; z++) {
                    IChunk chunk = new CubicChunk(this, 32, x, y, z);
                    setChunk((long) x << widthBits, (long) y << widthBits, (long) z << widthBits, chunk);
                }
            }
        }

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    setBlockState(x, y, z, Blocks.STONE.getDefaultState());
                }
            }
        }

        BoundingBox boundingBox = new BoundingBox(-radius, -radius, -radius, radius, radius, radius);


        for (int a = -radius; a <= radius; a++) {
            for (int b = -radius; b <= radius; b++) {
                int height = worldHeightBuilder.getWorldHeight(a, b, radius + 1);
                for (int c = 0; c < height; c++) {
                    setBlockState(a, b, radius + c, Blocks.STONE.getDefaultState());
                }
                for (int c = height; c <= 0; c++) {
                    setBlockState(a, b, radius + c, Blocks.AIR.getDefaultState());
                }
                if (a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a + ptr, b, radius + 1 + ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a + ptr, b, radius + c, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a + ptr, b, radius + h - 1);
                    }
                }
                if (b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, b + ptr, radius + 1 + ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a, b + ptr, radius + c, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a, b + ptr, radius + h - 1);
                    }
                }
                if (a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a - ptr, b, radius + 1 + ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a - ptr, b, radius + c, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a - ptr, b, radius + h - 1);
                    }
                }
                if (b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, b - ptr, radius + 1 + ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a, b - ptr, radius + c, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a, b - ptr, radius + h - 1);
                    }
                }
                if (a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, b + length, radius + 1 + Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, b + length, radius + c, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, b + length, radius + h - 1);
                        }
                    }
                }
                if (a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, b - length, radius + 1 + Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, b - length, radius + c, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, b - length, radius + h - 1);
                        }
                    }
                }
                if (a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, b + length, radius + 1 + Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, b + length, radius + c, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a - width, b + length, radius + h - 1);
                        }
                    }
                }
                if (a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, b - length, radius + 1 + Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, b - length, radius + c, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a - width, b - length, radius + h - 1);
                        }
                    }
                }


                height = worldHeightBuilder.getWorldHeight(a, b, -radius - 1);
                for (int c = 0; c < height; c++) {
                    setBlockState(a, b, -radius - c, Blocks.STONE.getDefaultState());
                }
                for (int c = height; c <= 0; c++) {
                    setBlockState(a, b, -radius - c, Blocks.AIR.getDefaultState());
                }
                if (a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a + ptr, b, -radius - 1 - ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a + ptr, b, -radius - c, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a + ptr, b, -radius - h + 1);
                    }
                }
                if (b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, b + ptr, -radius - 1 - ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a, b + ptr, -radius - c, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a, b + ptr, -radius - h + 1);
                    }
                }
                if (a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a - ptr, b, -radius - 1 - ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a - ptr, b, -radius - c, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a - ptr, b, -radius - h + 1);
                    }
                }
                if (b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, b - ptr, -radius - 1 - ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a, b - ptr, -radius - c, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a, b - ptr, -radius - h + 1);
                    }
                }
                if (a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, b + length, -radius - 1 - Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, b + length, -radius - c, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, b + length, -radius - h + 1);
                        }
                    }
                }
                if (a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, b - length, -radius - 1 - Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, b - length, -radius - c, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, b - length, -radius - h + 1);
                        }
                    }
                }
                if (a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, b + length, -radius - 1 - Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, b + length, -radius - c, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a - width, b + length, -radius - h + 1);
                        }
                    }
                }
                if (a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, b - length, -radius - 1 - Math.max(length, width));
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, b - length, -radius - c, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a - width, b - length, -radius - h + 1);
                        }
                    }
                }


                height = worldHeightBuilder.getWorldHeight(a, radius + 1, b);
                for (int c = 0; c < height; c++) {
                    setBlockState(a, radius + c, b, Blocks.STONE.getDefaultState());
                }
                for (int c = height; c <= 0; c++) {
                    setBlockState(a, radius + c, b, Blocks.AIR.getDefaultState());
                }

                if (a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a + ptr, radius + 1 + ptr, b);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a + ptr, radius + c, b, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a + ptr, radius + h - 1, b);
                    }
                }
                if (b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, radius + 1 + ptr, b + ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a, radius + c, b + ptr, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a, radius + h - 1, b + ptr);
                    }
                }
                if (a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a - ptr, radius + 1 + ptr, b);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a - ptr, radius + c, b, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a - ptr, radius + h - 1, b);
                    }
                }
                if (b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, radius + 1 + ptr, b - ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a, radius + c, b - ptr, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a, radius + h - 1, b - ptr);
                    }
                }
                if (a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, radius + 1 + Math.max(length, width), b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, radius + c, b + length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, radius + h - 1, b + length);
                        }
                    }
                }
                if (a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, radius + 1 + Math.max(length, width), b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, radius + c, b - length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, radius + h - 1, b - length);
                        }
                    }
                }
                if (a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, radius + 1 + Math.max(length, width), b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, radius + c, b + length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a - width, radius + h - 1, b + length);
                        }
                    }
                }
                if (a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, radius + 1 + Math.max(length, width), b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, radius + c, b - length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a - width, radius + h - 1, b - length);
                        }
                    }
                }


                height = worldHeightBuilder.getWorldHeight(a, -radius - 1, b);
                for (int c = 0; c < height; c++) {
                    setBlockState(a, -radius - c, b, Blocks.STONE.getDefaultState());
                }
                for (int c = height; c <= 0; c++) {
                    setBlockState(a, -radius - c, b, Blocks.AIR.getDefaultState());
                }
                if (a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a + ptr, -radius - 1 - ptr, b);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a + ptr, -radius - c, b, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a + ptr, -radius - h + 1, b);
                    }
                }
                if (b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, -radius - 1 - ptr, b + ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a, -radius - c, b + ptr, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a, -radius - h + 1, b + ptr);
                    }
                }
                if (a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a - ptr, -radius - 1 - ptr, b);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a - ptr, -radius - c, b, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a - ptr, -radius - h + 1, b);
                    }
                }
                if (b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(a, -radius - 1 - ptr, b - ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(a, -radius - c, b - ptr, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(a, -radius - h + 1, b - ptr);
                    }
                }
                if (a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, -radius - 1 - Math.max(length, width), b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, -radius - c, b + length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, -radius - h + 1, b + length);
                        }
                    }
                }
                if (a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a + width, -radius - 1 - Math.max(length, width), b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a + width, -radius - c, b - length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, -radius - h + 1, b - length);
                        }
                    }
                }
                if (a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, -radius - 1 - Math.max(length, width), b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, -radius - c, b + length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a - width, -radius - h + 1, b + length);
                        }
                    }
                }
                if (a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(a - width, -radius - 1 - Math.max(length, width), b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(a - width, -radius - c, b - length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(a + width, -radius - h + 1, b - length);
                        }
                    }
                }


                height = worldHeightBuilder.getWorldHeight(radius + 1, a, b);
                for (int c = 0; c < height; c++) {
                    setBlockState(radius + c, a, b, Blocks.STONE.getDefaultState());
                }
                for (int c = height; c <= 0; c++) {
                    setBlockState(radius + c, a, b, Blocks.AIR.getDefaultState());
                }
                if (a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(radius + 1 + ptr, a + ptr, b);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(radius + c, a + ptr, b, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(radius + h - 1, a + ptr, b);
                    }
                }
                if (b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(radius + 1 + ptr, a, b + ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(radius + c, a, b + ptr, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(radius + h - 1, a, b + ptr);
                    }
                }
                if (a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(radius + 1 + ptr, a - ptr, b);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(radius + c, a - ptr, b, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(radius + h - 1, a - ptr, b);
                    }
                }
                if (b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(radius + 1 + ptr, a, b - ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(radius + c, a, b - ptr, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(radius + h - 1, a, b - ptr);
                    }
                }
                if (a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(radius + 1 + Math.max(length, width), a + width, b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(radius + c, a + width, b + length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(radius + h - 1, a + width, b + length);
                        }
                    }
                }
                if (a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(radius + 1 + Math.max(length, width), a + width, b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(radius + c, a + width, b - length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(radius + h - 1, a + width, b - length);
                        }
                    }
                }
                if (a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(radius + 1 + Math.max(length, width), a - width, b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(radius + c, a - width, b + length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(radius + h - 1, a - width, b + length);
                        }
                    }
                }
                if (a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(radius + 1 + Math.max(length, width), a - width, b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(radius + c, a - width, b - length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(radius + h - 1, a - width, b - length);
                        }
                    }
                }


                height = worldHeightBuilder.getWorldHeight(-radius - 1, a, b);
                for (int c = 0; c < height; c++) {
                    setBlockState(-radius - c, a, b, Blocks.STONE.getDefaultState());
                }
                for (int c = height; c <= 0; c++) {
                    setBlockState(-radius - c, a, b, Blocks.AIR.getDefaultState());
                }

                if (a == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(-radius - 1 - ptr, a + ptr, b);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(-radius - c, a + ptr, b, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(-radius - h + 1, a + ptr, b);
                    }
                }
                if (b == radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(-radius - 1 - ptr, a, b + ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(-radius - c, a, b + ptr, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(-radius - h + 1, a, b + ptr);
                    }
                }
                if (a == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > a * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(-radius - 1 - ptr, a - ptr, b);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(-radius - c, a - ptr, b, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(-radius - h + 1, a - ptr, b);
                    }
                }
                if (b == -radius) {
                    int h = height;
                    int ptr = 0;
                    while (h + radius > b * -1 + ptr) {
                        ptr++;
                        h = worldHeightBuilder.getWorldHeight(-radius - 1 - ptr, a, b - ptr);
                        for (int c = ptr; c < h; c++) {
                            setBlockState(-radius - c, a, b - ptr, Blocks.STONE.getDefaultState());
                        }
                        boundingBox.resizeForPoint(-radius - h + 1, a, b - ptr);
                    }
                }

                if (a == radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(-radius - 1 - Math.max(length, width), a + width, b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(-radius - c, a + width, b + length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(-radius - h + 1, a + width, b + length);
                        }
                    }
                }
                if (a == radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(-radius - 1 - Math.max(length, width), a + width, b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(-radius - c, a + width, b - length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(-radius - h + 1, a + width, b - length);
                        }
                    }
                }
                if (a == -radius && b == radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(-radius - 1 - Math.max(length, width), a - width, b + length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(-radius - c, a - width, b + length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(-radius - h + 1, a - width, b + length);
                        }
                    }
                }
                if (a == -radius && b == -radius) {
                    int h = height;
                    int width = 0;
                    while (h + radius > b * -1 + width) {
                        width++;
                        int length = 0;
                        while (h + radius > a * -1 + length) {
                            length++;
                            h = worldHeightBuilder.getWorldHeight(-radius - 1 - Math.max(length, width), a - width, b - length);
                            for (int c = Math.max(length, width); c < h; c++) {
                                setBlockState(-radius - c, a - width, b - length, Blocks.STONE.getDefaultState());
                            }
                            boundingBox.resizeForPoint(-radius - h + 1, a - width, b - length);
                        }
                    }
                }
            }
        }

        int water_radius = radius - 4;
        System.out.println(boundingBox);
        for(int x = (int) boundingBox.minX; x <= boundingBox.maxX; x++) {
            for(int y = (int) boundingBox.minY; y <= boundingBox.maxY; y++) {
                for(int z = (int) boundingBox.minZ; z <= boundingBox.maxZ; z++) {
                    boolean xRad = x < water_radius && x > -water_radius;
                    boolean yRad = y < water_radius && y > -water_radius;
                    boolean zRad = z < water_radius && z > -water_radius;
                    if((zRad && yRad && xRad)) {
                        if (getBlockState(x, y, z).getBlock() == Blocks.AIR) {
                            setBlockState(x, y, z, Blocks.WATER.getDefaultState());
                        }
                    }
                }
            }
        }


        PlanetFeaturePlacerHelper helper = new PlanetFeaturePlacerHelper(this);
        TreeFeature treeFeature = new TreeFeature();

        BlockPos pos = new BlockPos(0, 0, 0);
        Random random = new Random();
        for (int a = -radius * 2; a <= radius * 2; a++) {
            for (int b = -radius * 2; b <= radius * 2; b++) {
                if (random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(radius + worldHeightBuilder.getWorldHeight(radius + 1, a, b), a, b));
                    if(helper.getBlockstateBelow().getBlock() == Blocks.STONE) {
                        treeFeature.place(helper);
                    }
                }
                if (random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(a, radius + worldHeightBuilder.getWorldHeight(a, radius + 1, b), b));
                    if(helper.getBlockstateBelow().getBlock() == Blocks.STONE) {
                        treeFeature.place(helper);
                    }
                }
                if (random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(a, b, radius + worldHeightBuilder.getWorldHeight(a, b, radius + 1)));
                    if(!helper.getBlockstateBelow().getBlock().blockProperties.airBlock) {
                        treeFeature.place(helper);
                    }
                }
                if (random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(-radius - worldHeightBuilder.getWorldHeight(-radius - 1, a, b), a, b));
                    if(helper.getBlockstateBelow().getBlock() == Blocks.STONE) {
                        treeFeature.place(helper);
                    }
                }
                if (random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(a, -radius - worldHeightBuilder.getWorldHeight(a , -radius - 1, b), b));
                    if(helper.getBlockstateBelow().getBlock() == Blocks.STONE) {
                        treeFeature.place(helper);
                    }
                }
                if (random.nextInt(64) == 0) {
                    helper.setPlacementPosition(pos.set(a, b, -radius - worldHeightBuilder.getWorldHeight(a, b, -radius - 1)));
                    if(helper.getBlockstateBelow().getBlock() == Blocks.STONE) {
                        treeFeature.place(helper);
                    }
                }
            }
        }
    }

    @Override
    public IWorldGenerator getWorldGenerator() {
        return worldGenerator;
    }

    @Override
    public ChunkLocker getChunkLocker() {
        return chunkLocker;
    }

    @Override
    public ConcurrentLinkedQueue<Consumer<IServerWorld>> getPostTickQueue() {
        return postTickFutures;
    }
}
