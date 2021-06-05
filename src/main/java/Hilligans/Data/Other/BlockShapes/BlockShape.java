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

    public BlockShape(String path) {
        data = BlockModel.create("/Models/Blocks/" + path);
    }

    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return new BoundingBox(0,0,0,1,1,1);
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockTextureManager blockTextureManager, Vector3f offset) {
        data.addData(primitiveBuilder,blockTextureManager,side,size,offset,0,0);
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockTextureManager blockTextureManager, Vector3f offset, float offsetX, float offsetY, float offsetZ) {
        data.addData(primitiveBuilder,blockTextureManager,side,size,offset,0,0,offsetX,offsetY,offsetZ);
    }

    public int getSide(BlockState blockState, int side) {
        return side;
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
