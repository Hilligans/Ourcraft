package Hilligans.Block;

import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Client.Rendering.World.VAOManager;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Item.BlockItem;
import Hilligans.Util.Vector5f;
import Hilligans.Client.Rendering.World.BlockTextureManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.World.DataProvider;
import Hilligans.World.World;
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
        Blocks.MAPPED_BLOCKS.put(name,this);
        new BlockItem(name,this);
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

    public boolean activateBlock(World world, PlayerEntity playerEntity, BlockPos pos) {
        return false;
    }

    public void onPlace(World world, BlockPos blockPos) {}

    public void onBreak(World world, BlockPos blockPos) {}


    public BlockState getDefaultState() {
        return new BlockState(this);
    }

    public BlockState getStateWithData(short data) {
        return new BlockState(this);
    }

    public BlockState getStateForPlacement(Vector3f pos, Vector3f playerPos) {
        return new BlockState(this);
    }

    public Block getDroppedBlock() {
        return droppedBlock;
    }


    public boolean getAllowedMovement(Vector3f motion, Vector3f pos, BlockPos blockPos, BoundingBox boundingBox, World world) {
        return !getBoundingBox(world.getBlockState(blockPos)).intersectsBox(boundingBox, blockPos.get3f(), pos, motion.x, motion.y, motion.z);
    }

    public BoundingBox getBoundingBox(BlockState blockState) {
        return new BoundingBox(0,0,0,1,1,1);
    }

    public void generateTextures() {
        blockTextureManager.generate();
    }

    public Vector5f[] getVertices(int side, BlockState blockState) {
        Vector5f[] vector5fs = getVertices(side,1.0f,blockState);
        /*for(Vector5f vector5f : vector5fs) {
            if(side == 2 || side == 3) {
                vector5f.setColored(0.95f,0.95f,0.95f,1.0f);
            } else if(side == 0 || side == 1) {
                vector5f.setColored(0.9f,0.9f,0.9f,1.0f);
            } else if(side == 4) {
                vector5f.setColored(0.85f,0.85f,0.85f,1.0f);
            } else {
                vector5f.setColored();
            }
        }

         */
        return vector5fs;
    }

    public Vector5f[] getVertices(int side, float size, BlockState blockState) {
        Vector5f[] vector5fs = CubeManager.getVertices1(blockTextureManager,side,size);
        for(Vector5f vector5f : vector5fs) {
            if(side == 2 || side == 3) {
                vector5f.setColored(0.95f,0.95f,0.95f,1.0f);
            } else if(side == 0 || side == 1) {
                vector5f.setColored(0.9f,0.9f,0.9f,1.0f);
            } else if(side == 4) {
                vector5f.setColored(0.85f,0.85f,0.85f,1.0f);
            } else {
                vector5f.setColored();
            }
        }


        return vector5fs;
    }

    public int generateOutline(BlockState state) {
        BoundingBox boundingBox = getBoundingBox(state);
        boundingBox.minX -= 0.001f;
        boundingBox.minY -= 0.001f;
        boundingBox.minZ -= 0.001f;

        boundingBox.maxX += 0.001f;
        boundingBox.maxY += 0.001f;
        boundingBox.maxZ += 0.001f;

        float[] vertices = new float[24];
        vertices[0] = boundingBox.minX;
        vertices[1] = boundingBox.minY;
        vertices[2] = boundingBox.minZ;
        vertices[3] = boundingBox.maxX;
        vertices[4] = boundingBox.minY;
        vertices[5] = boundingBox.minZ;
        vertices[6] = boundingBox.maxX;
        vertices[7] = boundingBox.minY;
        vertices[8] = boundingBox.maxZ;
        vertices[9] = boundingBox.minX;
        vertices[10] = boundingBox.minY;
        vertices[11] = boundingBox.maxZ;

        vertices[12] = boundingBox.minX;
        vertices[13] = boundingBox.maxY;
        vertices[14] = boundingBox.minZ;
        vertices[15] = boundingBox.maxX;
        vertices[16] = boundingBox.maxY;
        vertices[17] = boundingBox.minZ;
        vertices[18] = boundingBox.maxX;
        vertices[19] = boundingBox.maxY;
        vertices[20] = boundingBox.maxZ;
        vertices[21] = boundingBox.minX;
        vertices[22] = boundingBox.maxY;
        vertices[23] = boundingBox.maxZ;

        int[] indices = new int[]{0,1,1,2,2,3,3,0,0,4,1,5,2,6,3,7,4,5,5,6,6,7,7,4};

        return VAOManager.createLine(vertices,indices);
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

    public DataProvider getDataProvider() {
        return null;
    }

    public static final int UP = 5;
    public static final int DOWN = 4;

}
