package dev.hilligans.ourcraft.data.other.blockshapes;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.client.rendering.newrenderer.BlockModel;
import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.client.rendering.world.managers.BlockTextureManager;
import dev.hilligans.ourcraft.client.rendering.world.managers.VAOManager;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.world.World;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ShortOpenHashMap;
import org.joml.Vector3f;

public class BlockShape {

    public BlockModel data;
    public GameInstance gameInstance;

    public Int2ShortOpenHashMap rotations = new Int2ShortOpenHashMap();
    public Int2ObjectOpenHashMap<BoundingBox> boundingBoxes = new Int2ObjectOpenHashMap<>();
    public Int2ObjectOpenHashMap<BlockModel> models = new Int2ObjectOpenHashMap<>();
    public Int2ObjectOpenHashMap<BlockTextureManager> textures = new Int2ObjectOpenHashMap<>();
    public BoundingBox defaultBoundingBox = new BoundingBox(0,0,0,1,1,1);

    public BlockShape() {
        data = BlockModel.create("Models/Blocks/block.txt");
    }

    public String path;

    public BlockShape(String path) {
        this.path = path;
        data = BlockModel.create("Models/Blocks/" + path);
    }

    public BlockShape(BlockModel model) {
        this.data = model;
    }

    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        int val = world.getBlockState(pos).readData();
        return boundingBoxes.getOrDefault(val,defaultBoundingBox).duplicate();
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockTextureManager blockTextureManager, Vector3f offset) {
        int rot = getRotation(blockState);
        if (rot != -1) {
            getModel(blockState).addData(primitiveBuilder,blockTextureManager,side,size,offset,rot & 3,(rot & 12) >> 2);
        } else {
            getModel(blockState).addData(primitiveBuilder, blockTextureManager, side, size, offset, 0, 0);
        }
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockTextureManager blockTextureManager, Vector3f offset, float offsetX, float offsetY, float offsetZ) {
        int rot = getRotation(blockState);
        if (rot != -1) {
            getModel(blockState).addData(primitiveBuilder,blockTextureManager,side,size,offset.add(offsetX,offsetY,offsetZ),rot & 3,(rot & 12) >> 2);
        } else {
            getModel(blockState).addData(primitiveBuilder, blockTextureManager, side, size, offset.add(offsetX, offsetY, offsetZ), 0, 0);
        }
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, IBlockState blockState, BlockTextureManager blockTextureManager, Vector3f offset, float offsetX, float offsetY, float offsetZ) {
        int rot = blockState.getBlock().getRotation(blockState);
        if (rot != -1) {
            getModel(blockState).addData(primitiveBuilder,blockTextureManager,side,size,offset.add(offsetX,offsetY,offsetZ),rot & 3,(rot & 12) >> 2);
        } else {
            getModel(blockState).addData(primitiveBuilder, blockTextureManager, side, size, offset.add(offsetX, offsetY, offsetZ), 0, 0);
        }
    }

    public void putRotation(int blockState, int rotX, int rotY) {
        boundingBoxes.put(blockState,defaultBoundingBox.rotateX(rotX,1.0f).rotateY(-rotY,1.0f));
        rotations.put(blockState, (short) (rotX | rotY << 2));
    }

    public void putModel(int blockState,String path) {
        if(path != null && !path.equals("")) {
            System.err.println("created model " + path);
            models.put(blockState, BlockModel.create("Models/Blocks/" + path));
        }
    }


    public int getSide(BlockState blockState, int side) {
        int val = getRotation(blockState);
        if(val != -1) {
            return Block.rotationSides[val << 3 | side];
        }
        return side;
    }

    public int getSide(IBlockState blockState, int side) {
        int val = blockState.getBlock().getRotation(blockState);
        if(val != -1) {
            return Block.rotationSides[val << 3 | side];
        }
        return side;
    }

    public BlockModel getModel(BlockState blockState) {
        return models.getOrDefault(blockState.blockId,data);
    }

    public BlockModel getModel(IBlockState state) {
        return models.getOrDefault(state.getBlockID(), data);
    }

    public short getRotation(BlockState blockState) {
        int sideVal = blockState.readData();
        if(sideVal != -1 && rotations.containsKey(sideVal)) {
            return rotations.get(sideVal);
        }
        return -1;
    }

    public int generateOutline(World world, BlockPos pos) {
        BoundingBox boundingBox = getBoundingBox(world, pos);
        boundingBox.minX -= 0.001f;
        boundingBox.minY -= 0.001f;
        boundingBox.minZ -= 0.001f;
        boundingBox.maxX += 0.001f;
        boundingBox.maxY += 0.001f;
        boundingBox.maxZ += 0.001f;
        float[] vertices = new float[]{boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY, boundingBox.maxZ, boundingBox.minX, boundingBox.minY, boundingBox.maxZ, boundingBox.minX, boundingBox.maxY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, boundingBox.minX, boundingBox.maxY, boundingBox.maxZ,};
        int[] indices = new int[]{0,1,1,2,2,3,3,0,0,4,1,5,2,6,3,7,4,5,5,6,6,7,7,4};
        return VAOManager.createLine(vertices,indices);
    }

}
