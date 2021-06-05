package Hilligans.Block;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Data.Other.*;
import Hilligans.Data.Other.BlockShapes.BlockShape;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Data.Primitives.FloatList;
import Hilligans.Data.Primitives.IntList;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Item.BlockItem;
import Hilligans.Item.ItemStack;
import Hilligans.ModHandler.ModLoader;
import Hilligans.Ourcraft;
import Hilligans.Util.Vector5f;
import Hilligans.World.DataProvider;
import Hilligans.World.World;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

public class Block {

    public String name;
    public String modId;
    public short id;
    public BlockProperties blockProperties;
    private Block droppedBlock;

    public BlockShape blockShape = new BlockShape();

    public Block(String name, BlockProperties blockProperties) {
        this.name = name;
        this.modId = Ourcraft.MOD_LOADER.mod;
        id = Blocks.getNextId();
        this.blockProperties = blockProperties;
        droppedBlock = this;
        if(!blockProperties.serverSide) {
            Blocks.BLOCKS.add(this);
            Blocks.MAPPED_BLOCKS.put(name, this);
        } else {
            ServerSidedData.getInstance().putBlock(name,this);
        }
        new BlockItem(name,this);
    }

    public Block setBlockDrop(Block blockDrop) {
        this.droppedBlock = blockDrop;
        return this;
    }

    public String getName() {
        return "block." + modId + "." + name;
    }

    public boolean activateBlock(World world, PlayerEntity playerEntity, BlockPos pos) {
        return false;
    }

    public void onPlace(World world, BlockPos blockPos) {}

    public void onBreak(World world, BlockPos blockPos) {}

    public void onUpdate(World world, BlockPos blockPos) {}

    public void tickBlock(World world, BlockPos blockPos) {}

    public void tick(World world, BlockPos pos) {}

    public void randomTick(World world, BlockPos pos) {}

    public BlockState getDefaultState() {
        return new BlockState(this);
    }

    public boolean hasBlockState() {
        return false;
    }

    public int blockStateByteCount() {
        return blockProperties.blockStateSize;
    }

    public BlockState getStateWithData(short data) {
        return new BlockState(this);
    }

    public BlockState getStateForPlacement(Vector3d playerPos, RayResult rayResult) {
        return getDefaultState();
    }

    public Block getDroppedBlock() {
        return droppedBlock;
    }

    public boolean getAllowedMovement(Vector3d motion, Vector3d pos, BlockPos blockPos, BoundingBox boundingBox, World world) {
        return blockProperties.canWalkThrough || !getBoundingBox(world, blockPos).intersectsBox(boundingBox, blockPos.get3d(), pos, motion.x, motion.y, motion.z);
    }

    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return blockShape.getBoundingBox(world,pos);
    }

    public void generateTextures() {
        blockProperties.blockTextureManager.generate();
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockPos blockPos, int x, int z) {
       blockShape.addVertices(primitiveBuilder,side,size,blockState,blockProperties.blockTextureManager, new BlockPos(x,blockPos.y,z).get3f());
    }

    public int getSide(BlockState blockState, int side) {
        return blockShape.getSide(blockState,side);
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

    public void renderItem(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        Renderer.renderBlockItem(matrixStack,x,y,size,this);
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
