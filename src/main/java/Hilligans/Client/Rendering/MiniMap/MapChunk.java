package Hilligans.Client.Rendering.MiniMap;

import Hilligans.Block.Block;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Tag.CompoundTag;
import Hilligans.Tag.IntegerArrayTag;
import Hilligans.World.Chunk;
import Hilligans.WorldSave.ChunkLoader;
import Hilligans.WorldSave.WorldLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class MapChunk {

    int x;
    int z;

    int[] colors = new int[256];
    public int texture = -1;
    public Chunk chunk;

    public MapChunk(Chunk chunk) {
        this.chunk = chunk;
        this.x = chunk.x;
        this.z = chunk.z;
        update();
    }

    public MapChunk(int x, int z) {
        this.x = x;
        this.z = z;
        CompoundTag compoundTag = WorldLoader.loadTag("map_data/chunks/x" + x + "_z" + z + ".dat");
        IntegerArrayTag tag = (IntegerArrayTag) compoundTag.getTag("colors");
        colors = tag.val;
    }

    public MapChunk(int color) {
        Arrays.fill(colors, color);
        chunk = new Chunk(0,0,null);
    }

    public int get(int x, int z) {
        return colors[x << 4 | z];
    }

    public void set(int x, int z, int color) {
        colors[x << 4 | z] = color;
    }

    public int makeTexture() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(256 * 4);

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                Color color = new Color(get(x,y), true);
                if(x == 0 || y == 0) {
                    color = color.darker();
                }
                int height = distance(x,y);
                if(height > 0) {
                    float factor = logFactor(height);
                    color = change(color,factor,factor,Math.min(factor * 1.3f,1.0f));
                } else if(height == -1) {
                    color = change(color,1.0f,1.0f,0.85f);
                }
                buffer.put((x + y * 16) * 4, (byte) color.getRed());
                buffer.put((x + y * 16) * 4 + 1, (byte) color.getGreen());
                buffer.put((x + y * 16) * 4 + 2, (byte) color.getBlue());
                buffer.put((x + y * 16) * 4 + 3, (byte)255);
            }
        }
        if(texture != -1) {
            glDeleteBuffers(texture);
        }

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 16, 16, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        return texture;
    }




    public Color change(Color color, float r, float g, float b) {
        return new Color(Math.min((int)(color.getRed()*r), 255), Math.min((int)(color.getGreen()*g), 255), Math.min((int)(color.getBlue()*b), 255));
    }

    public float logFactor(int distance) {
        return (float) (0.8f - (1 - Math.log(distance)) * 0.2f);
    }

    public int distance(int x, int y) {
        int height = chunk.getHeightInt(x,y);
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
        if(chunk != null) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos blockPos = chunk.getHeight(x,z);
                    set(x,z,chunk.getBlockState(blockPos).getBlock().blockProperties.blockTextureManager.colors[Block.UP]);
                }
            }
            save();
        }
    }

    public void save() {
        CompoundTag compoundTag = new CompoundTag();
        IntegerArrayTag tag = new IntegerArrayTag(colors);
        compoundTag.putTag("colors",tag);
        WorldLoader.save(compoundTag,"map_data/chunks/x" + x + "_z" + z + ".dat");
    }



}
