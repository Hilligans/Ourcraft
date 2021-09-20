package Hilligans.World;

import Hilligans.Biome.Biome;
import Hilligans.Client.Rendering.NewRenderer.GLRenderer;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.NewRenderer.Shader;
import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.Data.Other.BlockStates.BlockState;
import Hilligans.Block.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Entity.Entity;
import Hilligans.Util.Settings;
import Hilligans.World.Builders.WorldBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2IntOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import org.lwjgl.opengl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.glGetBufferSubData;
import static org.lwjgl.opengl.GL31C.GL_COPY_READ_BUFFER;
import static org.lwjgl.opengl.GL31C.GL_COPY_WRITE_BUFFER;

public class Chunk {

    public ArrayList<SubChunk> chunks = new ArrayList<>();

    public World world;

    public Short2ObjectOpenHashMap<DataProvider> dataProviders = new Short2ObjectOpenHashMap<>();

    public Int2ObjectOpenHashMap<Entity> entities = new Int2ObjectOpenHashMap<>();
    public Long2ObjectOpenHashMap<ArrayList<BlockPos>> blockTicks = new Long2ObjectOpenHashMap<>();
    public Short2IntOpenHashMap heightMap = new Short2IntOpenHashMap();


    public boolean needsSaving = false;


    public int x;
    public int z;

    public static int terrain = 64;

    public boolean populated = false;

    public Chunk(int x, int z, World world) {
        this.world = world;
        this.x = x;
        this.z = z;

        for(int a = 0; a < Settings.chunkHeight; a++) {
            SubChunk subChunk = new SubChunk(world,this.x * 16,a * 16, this.z * 16);
            chunks.add(subChunk);
        }
    }

    public void tick() {
        if(world.isServer()) {
            ArrayList<BlockPos> list = blockTicks.get(((ServerWorld) world).server.getTime());
            if(list != null) {
                blockTicks.remove(((ServerWorld) world).server.getTime());
                for(BlockPos pos : list) {
                    world.getBlockState(pos).getBlock().tickBlock(world,pos);
                }
            }
        }
    }

    public int getTotalVertices() {
        int vertices = 0;
        for(SubChunk subChunk : chunks) {
            vertices += subChunk.verticesCount;
        }
        return vertices;
    }

    public void scheduleTick(BlockPos pos, int time) {
        if(world.isServer()) {
            long futureTime = ((ServerWorld)world).server.getTime() + time;
            ArrayList<BlockPos> list = blockTicks.computeIfAbsent(futureTime, k -> new ArrayList<>());
            list.add(pos);
        }
    }

    public void setWorld(World world) {
        this.world = world;
        for(SubChunk subChunk : chunks) {
            subChunk.world = world;
        }
    }

    public void render(MatrixStack matrixStack) {
        if(id == -1 || !Settings.optimizeMesh) {
            boolean val = true;
            for (SubChunk subChunk : chunks) {
                subChunk.renderMesh(matrixStack);
                val = val && subChunk.id != -2 && subChunk.id != -3 && subChunk.id != -1;
            }
            if(val && Settings.optimizeMesh) {
                buildMesh1();
            }
        } else {
            if (sizeVal != 0) {
                GL30.glBindVertexArray(id);
                matrixStack.push();
                matrixStack.applyTransformation(ShaderManager.worldShader.shader);
                GLRenderer.glDrawElements(GL_TRIANGLES, sizeVal, GL_UNSIGNED_INT, 0);
                matrixStack.pop();
            }
        }

    }

    public void destroy() {
        for(SubChunk subChunk : chunks) {
            subChunk.destroy();
        }
        blockTicks.clear();
        entities.clear();
        dataProviders.clear();
    }

    public void generate() {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                int offset = getBlockHeight(x + this.x * 16,z + this.z * 16);
                offset = interpolate(offset,getBlockHeight(x + 1 + this.x * 16, z + 1 + this.z * 16));
                Biome biome = getBiome1(x + this.x * 16,z + this.z * 16);

                for(int y = 0; y < Settings.chunkHeight * 16; y++) {
                    if(y + 5 < offset) {
                        setBlockState(x,y,z,Blocks.STONE.getDefaultState());
                    } else if(y < offset) {
                        setBlockState(x,y,z, biome.underBlock.getDefaultState());
                    }  else if(y == offset) {
                        setBlockState(x,y,z, biome.surfaceBlock.getDefaultState());
                    }
                    if(y == 0) {
                        setBlockState(x,0,z,Blocks.BEDROCK.getDefaultState());
                    }
                }
            }
        }

        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < Settings.chunkHeight * 16; y++) {
                for(int z = 0; z < 16; z++) {
                   double val = world.noise.smoothNoise(0.1 * (x + this.x * 16),0.1 * y,0.1 * (z + this.z * 16));
                   if(val > 0.2 && y != 0) {
                       setBlockState(x,y,z,Blocks.AIR.getDefaultState());
                       if(getBlockState(x,y - 1,z).getBlock() == Blocks.DIRT) {
                           setBlockState(x,y - 1,z,Blocks.AIR.getDefaultState());
                       }
                   }
                }
            }
        }
        Chunk[] chunks = world.getChunksAround(x,z,0);
        for(Chunk chunk : chunks) {
            if(chunk != null) {
                chunk.populate();
            }
        }
    }

    public void populate() {
        if(!populated) {
            Chunk[] chunks = world.getChunksAround(x,z,0);
            for (Chunk chunk : chunks) {
                if (chunk == null) {
                    return;
                }
            }
            populated = true;

            for(WorldBuilder worldBuilder : world.worldBuilders) {
                worldBuilder.build(this);
            }

            for(WorldBuilder worldBuilder : getBiome1(world.random.nextInt(16) + x * 16,world.random.nextInt(16) + z * 16).worldBuilders) {
                    worldBuilder.build(this);
            }
        }
    }

    public int getBlockHeight(int x, int z) {
        Biome biome = getBiome1(x, z);
        double val = world.simplexNoise.getHeight(x, z, biome.terrainHeights);
        return (int) (val + terrain);
    }

    public int interpolate(int height, int xHeight) {
        return Math.round(((float)height + xHeight) / 2);
    }

    public int interpolate(int height, int xHeight, int zHeight) {
        return Math.round(((float)height + xHeight + zHeight) / 3);
    }

    private final double size = 400;

    public Biome getBiome1(int x, int z) {
        return world.biomeMap.getBiome(x,z);
    }

    public BlockState getBlockState(int x, int y, int z) {
        if(y >= Settings.chunkHeight * 16 || y < 0) {
            return Blocks.AIR.getDefaultState();
        }
        int pos = y >> 4;
        SubChunk subChunk = chunks.get(pos);
        return subChunk.getBlock(x & 15, y & 15, z & 15);
    }

    public BlockState getBlockState(BlockPos blockPos) {
        return getBlockState(blockPos.x,blockPos.y,blockPos.z);
    }

    public DataProvider getDataProvider(BlockPos pos) {
        needsSaving = true;
        //int height = pos.y >> 4;
        //SubChunk subChunk = chunks.get(height);
        return dataProviders.get((short)(pos.x & 15 | (pos.y & 255) << 4 | (pos.z & 15) << 12));
        //return subChunk.getDataProvider(pos.x & 15, pos.y & 15, pos.z & 15);
    }

    public void setDataProvider(BlockPos pos, DataProvider dataProvider) {
        needsSaving = true;
        //int height = pos.y >> 4;
        //SubChunk subChunk = chunks.get(height);
        dataProviders.put((short)(pos.x & 15 | (pos.y & 255) << 4 | (pos.z & 15) << 12),dataProvider);
        //subChunk.setDataProvider(pos.x & 15, pos.y & 15, pos.z & 15, dataProvider);
    }

    public void setBlockState(int x, int y, int z, BlockState blockState) {
        needsSaving = true;
        if(y > Settings.chunkHeight * 16 || y < 0) {
           return;
        }

        if(populated) {
            short height = (short) (x << 4 | z);
            if (blockState.getBlock().blockProperties.airBlock) {
                if (heightMap.get(height) == y) {
                    for (int i = y; i > 0; i--) {
                        if (!getBlockState(x, i, z).getBlock().blockProperties.airBlock) {
                            heightMap.put(height, i);
                        }
                    }
                }
            } else {
                if (heightMap.get(height) < y) {
                    heightMap.put(height, y);
                }
            }
        }
        int pos = y >> 4;
        chunks.get(pos).setBlockState(x,y,z,blockState);
    }

    public BlockPos getHeight(int x, int z) {
        return new BlockPos(x,heightMap.get((short)((x << 4) | z)),z);
    }

    public int getHeightInt(int x, int z) {
        return heightMap.get((short)(((x & 0xF ) << 4) | (z  /*& 0xF */)));
    }

    public void updateBlock(BlockPos pos) {
        if(pos.y > Settings.chunkHeight * 16 || pos.y < 0) {
            return;
        }
        int pos1 = pos.y >> 4;
        chunks.get(pos1).updateBlock(pos);
    }

    public ArrayList<DoubleTypeWrapper<BlockState,Integer>> getBlockChainedList() {
        BlockState currentState = null;
        ArrayList<DoubleTypeWrapper<BlockState,Integer>> values = new ArrayList<>();
        int count = 0;
        for(int i = 0; i < 16 * 16 * Settings.chunkHeight * 16; i++) {
            int x = i & 15;
            int y = i >> 4 & 255;
            int z = i >> 12 & 15;
            BlockState newState = getBlockState(x, y, z);
            if (!newState.equals(currentState)) {
                if (currentState != null) {
                    values.add(new DoubleTypeWrapper<>(currentState, count));
                    count = 0;
                }
                currentState = newState;
            }
            count++;
        }
        return values;
    }

    public void setFromChainedList(ArrayList<DoubleTypeWrapper<BlockState,Integer>> values) {
        populated = true;
        int offset = 0;
        for(DoubleTypeWrapper<BlockState, Integer> block : values) {
            for(int i = 0; i < block.getTypeB(); i++) {
                int x = offset & 15;
                int y = offset >> 4 & 255;
                int z = offset >> 12 & 15;
                setBlockState(x,y,z,block.getTypeA().duplicate());
                offset++;
            }
        }
    }

    int id = -1;
    int sizeVal = -1;

    public void buildMesh1() {
        glGetError();
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.worldShader);
        int pointer = 0;

        int size = 0;
        for(SubChunk subChunk : chunks) {
            if (subChunk.id == -1 || subChunk.id == -2 || subChunk.verticesCount == -1) {
                continue;
            }
            int vertexID = VAOManager.buffers.get(subChunk.id).typeA;
            glBindBuffer           (GL_COPY_READ_BUFFER, vertexID);
            size += GL30.glGetBufferParameteri(GL_COPY_READ_BUFFER,GL_BUFFER_SIZE);
        }
        int VBO = glGenBuffers();
        glBindBuffer(GL_COPY_WRITE_BUFFER, VBO);
        glBufferData(GL_COPY_WRITE_BUFFER,size,GL_STATIC_DRAW);

        for(SubChunk subChunk : chunks) {
            if (subChunk.id == -1 || subChunk.id == -2 || subChunk.verticesCount == -1 || subChunk.verticesCount == 0) {
                continue;
            }
            int vertexID = VAOManager.buffers.get(subChunk.id).typeA;
            int indexID = VAOManager.buffers.get(subChunk.id).typeB;
            glBindBuffer           (GL_COPY_READ_BUFFER, vertexID);
            int sizeVal = GL30.glGetBufferParameteri(GL_COPY_READ_BUFFER,GL_BUFFER_SIZE);
            glBindBuffer           (GL_COPY_WRITE_BUFFER, VBO);
            GL31.glCopyBufferSubData(GL_COPY_READ_BUFFER,GL_COPY_WRITE_BUFFER, 0, pointer, sizeVal);
            int[] indices = new int[subChunk.verticesCount];
            glBindBuffer(GL_COPY_READ_BUFFER,indexID);
            glGetBufferSubData(GL_COPY_READ_BUFFER, 0, indices);

            primitiveBuilder.add(indices);
            primitiveBuilder.sizeVal += sizeVal / 4 / primitiveBuilder.shader.shaderElementCount;
            pointer += sizeVal;
        }

        int VAO = glGenVertexArrays();
        int EBO = glGenBuffers();
        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, primitiveBuilder.indices.getElementData(), GL_STATIC_DRAW);
        int x = 0;
        pointer = 0;
        for(Shader.ShaderElement shaderElement : primitiveBuilder.shader.shaderElements) {
            glVertexAttribPointer(x,shaderElement.count,shaderElement.type,shaderElement.normalised,primitiveBuilder.shader.shaderElementCount * 4,pointer * 4);
            glEnableVertexAttribArray(x);
            x++;
            pointer += shaderElement.count;
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        VAOManager.buffers.put((VAO),new DoubleTypeWrapper<>(VBO,EBO));
        sizeVal = primitiveBuilder.indices.size();
        id = VAO;
    }

}


