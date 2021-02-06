package Hilligans.Block;

import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Util.Vector5f;
import Hilligans.Client.Rendering.World.BlockTextureManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.World.BlockState;
import org.joml.Vector3f;

public class Block {

    public String name;
    public short id;
    public boolean transparentTexture = false;
    private Block droppedBlock;

    public BlockTextureManager blockTextureManager = new BlockTextureManager();

    public Block(String name) {
        this.name = name;
        id = Blocks.getNextId();
        droppedBlock = this;
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

    public Block transparentTexture(boolean val) {
        transparentTexture = val;
        return this;
    }

    public Block setBlockDrop(Block blockDrop) {
        this.droppedBlock = blockDrop;
        return this;
    }


    public BlockState getDefaultState() {
        return new BlockState(this);
    }

    public Block getDroppedBlock() {
        return droppedBlock;
    }

    public Vector3f getAllowedMovement(Vector3f motion, Vector3f pos, BlockPos blockPos, BoundingBox boundingBox) {
        if(!getBoundingBox().intersectsBox(boundingBox,blockPos.get3f(),pos,motion.x,motion.y,motion.z)) {
            return motion;
        }
        return new Vector3f(0f,0f,0f);
    }

    public boolean getAllowedMovement1(Vector3f motion, Vector3f pos, BlockPos blockPos, BoundingBox boundingBox) {
        return !getBoundingBox().intersectsBox(boundingBox, blockPos.get3f(), pos, motion.x, motion.y, motion.z);
    }

    private BoundingBox getBoundingBox() {
        //return new BoundingBox(-1,-1,-1,0,0,0);
        return new BoundingBox(0,0,0,1,1,1);
    }

    public void generateTextures() {
        blockTextureManager.generate();
    }

    public Vector5f[] getVertices(int side) {
       // return CubeManager.getVertices(blockTextureManager,side,0.5f);

       /* Vector5f[] vertices = CubeManager.getVertices1(blockTextureManager,side,1f);
        if(side == 4 || side == 5) {
            for(Vector5f vector5f : vertices) {
                vector5f.setColored();
            }
            return vertices;
        }
        for(Vector5f vector5f : vertices) {
            vector5f.setColored(0,0,0,0.5f);
        }
        return vertices;
        re
        */
       return CubeManager.getVertices1(blockTextureManager,side,1f);
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
