package Hilligans.World;

import Hilligans.Biome.Biome;
import Hilligans.Block.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Util.Noises.SimplexNoise;
import Hilligans.Util.Settings;
import Hilligans.World.Builders.WorldBuilder;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Chunk {

    ArrayList<SubChunk> chunks = new ArrayList<>();

    public World world;

   // public static final int chunkHeight = 16;

    public int  x;
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

    public void render(MatrixStack matrixStack) {
        matrixStack.translate(x * 16, 0, z * 16);
        for(SubChunk subChunk : chunks) {
            subChunk.renderMesh(matrixStack);
        }
    }

    public void generate() {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {

                //int offset = getHeight(getBlockHeight(x + this.x * 16,z + this.z * 16));

                int offset = getBlockHeight(x + this.x * 16,z + this.z * 16);

                offset = interpolate(offset,getBlockHeight(x + 1 + this.x * 16, z + 1 + this.z * 16));

                Biome biome = getBiome1(x + this.x * 16,z + this.z * 16);
               // System.out.println("x:" + (x + this.x * 16) + " z:" + (z + this.z * 16) + " " + biome.name);


                for(int y = 0; y < Settings.chunkHeight * 16; y++) {
                    if(y + 5 < offset) {
                        setBlockState(x,y,z,new BlockState(Blocks.STONE));
                    } else if(y < offset) {
                        setBlockState(x,y,z, biome.underBlock.getDefaultState());
                    }  else if(y == offset) {
                        setBlockState(x,y,z, biome.surfaceBlock.getDefaultState());
                    }
                    if(y == 0) {
                        setBlockState(x,0,z,new BlockState(Blocks.BEDROCK));
                    }
                }
            }
        }

        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < Settings.chunkHeight * 16; y++) {
                for(int z = 0; z < 16; z++) {
                   double val = world.noise.smoothNoise(0.1 * (x + this.x * 16),0.1 * y,0.1 * (z + this.z * 16));
                   if(val > 0.2 && y != 0) {
                       setBlockState(x,y,z,new BlockState(Blocks.AIR));
                       if(getBlockState(x,y - 1,z).block == Blocks.DIRT) {
                           setBlockState(x,y - 1,z,new BlockState(Blocks.AIR));
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

        Biome biome = getBiome1(x,z);

        double val = world.simplexNoise.getHeight(x,z,biome.terrainHeights);
        //System.out.println(val);
        return (int) (val + terrain);
        //return world.heightMap.getHeight(x / 64f,z / 64f);
        //return world.customNoise.getNoise(x,z,10,6);
        //return world.noise.noise((int)((x + this.x * 16)/2 + Integer.MAX_VALUE / 2),(int)((z + this.z * 16)/2 + Integer.MAX_VALUE / 2));
    }

  //  public int getHeight(double height, double biome) {
       // biome = biome * 20 + 5;



        //return (int) (height * biome - biome / 2 + terrain);
  //  }

    public int interpolate(int height, int xHeight) {
        return Math.round(((float)height + xHeight) / 2);
    }

    public int interpolate(int height, int xHeight, int zHeight) {
        return Math.round(((float)height + xHeight + zHeight) / 3);
    }

    private final double size = 400;

    public Biome getBiome1(int x, int z) {

        //System.out.println(height + " height");

        //System.out.println("noise " + noise);

        return world.biomeMap.getBiome(x,z);
    }

    public BlockState getBlockState(int x, int y, int z) {
        if(y >= Settings.chunkHeight * 16 || y < 0) {
            return new BlockState(Blocks.AIR);
        }
        int pos = y >> 4;
        SubChunk subChunk = chunks.get(pos);
        return subChunk.getBlock(x & 15, y & 15, z & 15);
    }

    public void setBlockState(int x, int y, int z, BlockState blockState) {
        if(y > Settings.chunkHeight * 16 || y < 0) {
           return;
        }
        int pos = y >> 4;
        chunks.get(pos).setBlockState(x,y,z,blockState);
    }


}
