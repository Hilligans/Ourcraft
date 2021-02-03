package Hilligans.Blocks;

import Hilligans.Client.Rendering.World.TextureManager;
import Hilligans.Util.Vector5f;
import Hilligans.Client.Rendering.World.BlockTextureManager;
import Hilligans.World.BlockPos;

public class Block {

    public String name;
    public short id;

    BlockTextureManager blockTextureManager = new BlockTextureManager();

    public Block(String name) {
        this.name = name;
        id = Blocks.getNextId();
        Blocks.BLOCKS.add(this);
        Blocks.MAPPED_BLOCKS.put(name,id);
    }

    public Block withTexture(String texture) {
        blockTextureManager.addString(texture);
        return this;
    }

    public Block withSidedTexture(String texture, int side) {
        blockTextureManager.addString(texture,side);
        return this;
    }

    public void generateTextures() {
        blockTextureManager.generate();
    }

    public Vector5f[] getVertices(int side) {

        int id = blockTextureManager.textures[side];

        float minX = TextureManager.getMinX(id);
        float maxX = TextureManager.getMaxX(id);
        float minY = TextureManager.getMinY(id);
        float maxY = TextureManager.getMaxY(id);

        switch (side)  {
            case 0:
                return new Vector5f[] { new Vector5f(0.5f,0.5f,-0.5f,maxX,maxY),
                                        new Vector5f(0.5f, -0.5f, -0.5f, maxX,minY),
                                        new Vector5f(-0.5f,-0.5f,-0.5f,minX,minY),
                                        new Vector5f(-0.5f,0.5f,-0.5f,minX,maxY)};
            case 1:
                return new Vector5f[] { new Vector5f(0.5f,0.5f,0.5f,maxX,maxY),
                                        new Vector5f(0.5f, -0.5f, 0.5f, maxX,minY),
                                        new Vector5f(-0.5f,-0.5f,0.5f,minX,minY),
                                        new Vector5f(-0.5f,0.5f,0.5f,minX,maxY)};

            case 2:
                return new Vector5f[] { new Vector5f(-0.5f,0.5f,0.5f,maxX,maxY),
                                        new Vector5f(-0.5f,-0.5f,0.5f,maxX,minY),
                                        new Vector5f(-0.5f,-0.5f,-0.5f,minX,minY),
                                        new Vector5f(-0.5f,0.5f,-0.5f,minX,maxY)};
            case 3:
                return new Vector5f[] { new Vector5f(0.5f,0.5f,0.5f,maxX,maxY),
                                        new Vector5f(0.5f,-0.5f,0.5f,maxX,minY),
                                        new Vector5f(0.5f,-0.5f,-0.5f,minX,minY),
                                        new Vector5f(0.5f,0.5f,-0.5f,minX,maxY)};
            case 5:
                return new Vector5f[] { new Vector5f(0.5f,0.5f,0.5f,minX,minY),
                                        new Vector5f(0.5f,0.5f,-0.5f,maxX,minY),
                                        new Vector5f(-0.5f,0.5f,-0.5f,maxX,maxY),
                                        new Vector5f(-0.5f,0.5f,0.5f,minX,maxY)};
            default:
                return new Vector5f[] { new Vector5f(0.5f,-0.5f,0.5f,minX,minY),
                                        new Vector5f(0.5f,-0.5f,-0.5f,maxX,minY),
                                        new Vector5f(-0.5f,-0.5f,-0.5f,maxX,maxY),
                                        new Vector5f(-0.5f,-0.5f,0.5f,minX,maxY)};
        }
    }

    public Integer[] getIndices(int side, int spot) {
        switch (side) {
            case 0:
            case 5:
            case 3:
                return new Integer[] {spot,spot + 1,spot + 2,spot,spot + 2,spot + 3};
            default:
                return new Integer[]{spot,spot + 2, spot + 1, spot, spot + 3, spot + 2};
        }
    }

    public static BlockPos getBlockPos(int side) {
        switch (side) {
            case 0:
                return new BlockPos(0,0,-1);
            case 1:
                return new BlockPos(0,0,1);
            case 2:
                return new BlockPos(-1,0,0);
            case 3:
                return new BlockPos(1,0,0);
            case 4:
                return new BlockPos(0,-1,0);
            default:
                return new BlockPos(0,1,0);
        }
    }

    public static final int UP = 5;
    public static final int DOWN = 4;

}
