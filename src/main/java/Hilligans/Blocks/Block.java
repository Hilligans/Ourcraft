package Hilligans.Blocks;

import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Client.Rendering.World.TextureManager;
import Hilligans.Util.Vector5f;
import Hilligans.Client.Rendering.World.BlockTextureManager;
import Hilligans.World.BlockPos;

public class Block {

    public String name;
    public short id;

    public BlockTextureManager blockTextureManager = new BlockTextureManager();

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
        return CubeManager.getVertices(blockTextureManager,side,0.5f);
    }

    public Integer[] getIndices(int side, int spot) {
        return CubeManager.getIndices(side,spot);
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
