package Hilligans.World;

import Hilligans.Blocks.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Util.Settings;
import org.joml.Matrix4f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Chunk {

    ArrayList<SubChunk> chunks = new ArrayList<>();

    public World world;

   // public static final int chunkHeight = 16;

    public int x;
    public int z;

    public static int terrain = 64;

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

                int offset = getHeight(getBlockHeight(x,z),getBiome(x,z));

                offset = interpolate(offset,getHeight(getBlockHeight(x + 1, z + 1),getBiome(x + 1, z + 1)));



                for(int y = 0; y < Settings.chunkHeight * 16; y++) {
                    if(y + 5 < offset) {
                        setBlockState(x,y,z,new BlockState(Blocks.STONE));
                    } else if(y < offset) {
                        setBlockState(x,y,z, new BlockState(Blocks.DIRT));
                    }  else if(y == offset) {
                        setBlockState(x,y,z, new BlockState(Blocks.GRASS));
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
    }

    public double getBlockHeight(int x, int z) {
        return world.noise.noise((int)((x + this.x * 16)/2 + Integer.MAX_VALUE / 2),(int)((z + this.z * 16)/2 + Integer.MAX_VALUE / 2));
    }

    public int getHeight(double height, double biome) {
        biome = biome * 20 + 10;



        return (int) (height * biome - biome / 2 + terrain);
    }

    public int interpolate(int height, int xHeight) {
        return Math.round(((float)height + xHeight) / 2);
    }

    public int interpolate(int height, int xHeight, int zHeight) {
        return Math.round(((float)height + xHeight + zHeight) / 3);
    }

    public double getBiome(int x, int z) {
        return world.biomes.noise((int)((x + this.x * 16)/8 + Integer.MAX_VALUE / 2),(int)((z + this.z * 16)/8 + Integer.MAX_VALUE / 2));
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
