package dev.Hilligans.ourcraft.Client.Rendering.MiniMap;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.World;

import java.awt.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class GridChunk {
    int x;
    int z;

    int[] colors = new int[65536];
    public int texture = -1;
    public World world;

    public GridChunk(int x, int z, World world) {
        this.x = x;
        this.z = z;
        this.world = world;
        update();
    }


    public int get(int x, int z) {
        return colors[x << 8 | z];
    }

    public void set(int x, int z, int color) {
        colors[x << 8 | z] = color;
    }

    public void set1(int x, int z, int colorVal) {
        Color color = new Color(colorVal);
        if (x % 16 == 0 || z % 16 == 0) {
            color = color.darker();
        }
        int height = distance(x, z);
        if (height > 0) {
            float factor = logFactor(height);
            color = change(color, factor, factor, Math.min(factor * 1.3f, 1.0f));
        } else if (height == -1) {
            color = change(color, 1.0f, 1.0f, 0.85f);
        }
        set(x, z, color.getRGB());
    }

    public int makeTexture1() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(256 * 256 * 4);
        for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                Color color = new Color(get(x, y));
                buffer.put((x + y * 256) * 4, (byte) color.getRed());
                buffer.put((x + y * 256) * 4 + 1, (byte) color.getGreen());
                buffer.put((x + y * 256) * 4 + 2, (byte) color.getBlue());
                buffer.put((x + y * 256) * 4 + 3, (byte) 255);
            }
        }
        if(texture != -1) {
            glDeleteTextures(texture);
        }
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 256, 256, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        return texture;
    }

    public Color change(Color color, float r, float g, float b) {
        return new Color(Math.min((int)(color.getRed()*r), 255), Math.min((int)(color.getGreen()*g), 255), Math.min((int)(color.getBlue()*b), 255));
    }

    public float logFactor(int distance) {
        return (float) (0.8f - (1 - Math.log(distance)) * 0.2f);
    }

    public Chunk getChunk(int x, int z) {
        return world.getChunk(this.x + x >> 4,this.z + z >> 4);
    }

    public int distance(int x, int y) {
        Chunk chunk = getChunk(x,y);
        if(chunk == null) {
            //  System.out.println("missing chunk");
            //System.out.println(x + ":" + y);
            return 0;
        } else {
           //  System.out.println("a");
        }
        int height = chunk.getHeightInt(x,y);
        System.out.println(x + ":");
        int height1 = chunk.getHeightInt(x + 1, y);
        int height2 = chunk.getHeightInt(x, y + 1);
        int height3 = chunk.getHeightInt(x + 1, y + 1);
        int max = Math.max(Math.max(height1 - height,height2 - height),height3 - height);
        int min = Math.min(Math.min(height1 - height,height2 - height),height3 - height);
        if(max <= 0) {
            return min;
        } else {
            return max;
        }
    }

    public void update() {
        if(world != null) {
            for (int X = 0; X < 16; X++) {
                for (int Z = 0; Z < 16; Z++) {
                    Chunk chunk = world.getChunk(this.x + X, this.z + Z);
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            if (chunk != null) {
                                //System.out.println("Chunk");
                                try {
                                    BlockPos blockPos = chunk.getHeight(x, z);
                                    System.out.println(X << 4 | x);
                                    set1(X << 4 | x, Z << 4 | z, chunk.getBlockState(blockPos).getBlock().blockProperties.blockTextureManager.colors[Block.UP]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                               // System.out.println("No Chunk");
                                if((X * 16 + x) % 16 == 0 || (Z * 16 + z) % 16 == 0) {
                                    set(X * 16 + x, Z * 16 + z, new Color(25, 15, 44).darker().getRGB());
                                } else {
                                    set(X * 16 + x, Z * 16 + z, new Color(25, 15, 44).getRGB());
                                }
                            }
                        }
                    } 
                }
            }
        }
    }

    public void update(int X, int Z) {
        Chunk chunk = world.getChunk(X, Z);
        X = X - this.x;
        Z = Z - this.z;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BlockPos blockPos = chunk.getHeight(x, z);
                set1(X * 16 + x, Z * 16 + z, chunk.getBlockState(blockPos).getBlock().blockProperties.blockTextureManager.colors[Block.UP]);
            }
        }
    }
}
