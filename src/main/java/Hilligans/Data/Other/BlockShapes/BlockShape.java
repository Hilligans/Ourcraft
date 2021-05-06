package Hilligans.Data.Other.BlockShapes;

import Hilligans.Client.Rendering.NewRenderer.BlockModel;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Data.Other.BlockState;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.CubeManager;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Util.Vector5f;
import Hilligans.World.World;
import org.joml.Vector3f;

public class BlockShape {

    public BlockModel data;

    public BlockShape() {
        data = BlockModel.create("/Models/Blocks/block.txt");
    }

    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return new BoundingBox(0,0,0,1,1,1);
    }

    public Vector5f[] getVertices(int side, BlockState blockState, BlockTextureManager blockTextureManager) {
        if(side != 6) {
            return getVertices(side, 1.0f, blockState, blockTextureManager);
        } else {
            return new Vector5f[]{};
        }
    }

    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockTextureManager blockTextureManager) {
        if(side != 6) {
            Vector5f[] vector5fs = CubeManager.getVertices1(blockTextureManager, side, size);
            applyColoring(vector5fs, side);
            return vector5fs;
        } else {
            return new Vector5f[]{};
        }
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockTextureManager blockTextureManager, Vector3f offset) {
        data.addData(primitiveBuilder,blockTextureManager,side,size,offset,0,0);
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockTextureManager blockTextureManager, Vector3f offset, float offsetX, float offsetY, float offsetZ) {
        data.addData(primitiveBuilder,blockTextureManager,side,size,offset,0,0,offsetX,offsetY,offsetZ);
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

    public Integer[] getIndices(int side, int spot) {
        if(side != 6) {
            return CubeManager.getIndices(side, spot);
        } else {
            return new Integer[]{};
        }
    }

    protected void applyColoring(Vector5f[] vector5fs, int side) {
        for(Vector5f vector5f : vector5fs) {
            if (side == 2 || side == 3) {
                vector5f.setColored(0.95f, 0.95f, 0.95f, 1.0f);
            } else if (side == 0 || side == 1) {
                vector5f.setColored(0.9f, 0.9f, 0.9f, 1.0f);
            } else if (side == 4) {
                vector5f.setColored(0.85f, 0.85f, 0.85f, 1.0f);
            } else {
                vector5f.setColored();
            }
        }
    }
}
