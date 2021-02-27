package Hilligans.Block;

import Hilligans.Data.Other.BlockShapes.BlockShape;
import Hilligans.Data.Other.BlockState;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Data.Other.RayResult;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Item.BlockItem;
import Hilligans.Util.Vector5f;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.World.DataProvider;
import Hilligans.World.World;
import org.joml.Vector3f;

public class  Block {

    public String name;
    public short id;
    public boolean transparentTexture = false;
    public boolean canWalkThrough = false;
    private Block droppedBlock;
    public BlockShape blockShape = new BlockShape();
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

    public boolean hasBlockState() {
        return false;
    }

    public BlockState getStateWithData(short data) {
        return new BlockState(this);
    }

    public BlockState getStateForPlacement(Vector3f playerPos, RayResult rayResult) {
        return getDefaultState();
    }

    public Block getDroppedBlock() {
        return droppedBlock;
    }

    public boolean getAllowedMovement(Vector3f motion, Vector3f pos, BlockPos blockPos, BoundingBox boundingBox, World world) {
        return canWalkThrough || !getBoundingBox(world, blockPos).intersectsBox(boundingBox, blockPos.get3f(), pos, motion.x, motion.y, motion.z);
    }

    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return blockShape.getBoundingBox(world,pos);
    }

    public void generateTextures() {
        blockTextureManager.generate();
    }

    public Vector5f[] getVertices(int side, BlockState blockState, BlockPos blockPos) {
       return blockShape.getVertices(side,blockState, blockTextureManager);
    }

    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockPos blockPos) {
        return blockShape.getVertices(side,size,blockState,blockTextureManager);
    }

    public Integer[] getIndices(int side, int spot) {
        return blockShape.getIndices(side,spot);
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
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    public static final int EAST = 3;

}
