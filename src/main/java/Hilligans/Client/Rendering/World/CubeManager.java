package Hilligans.Client.Rendering.World;

import Hilligans.Util.Vector5f;
import Hilligans.World.BlockPos;

public class CubeManager {
    public static Vector5f[] getVertices(BlockTextureManager blockTextureManager, int side, float size) {

        int id = blockTextureManager.textures[side];

        float minX = TextureManager.getMinX(id);
        float maxX = TextureManager.getMaxX(id);
        float minY = TextureManager.getMinY(id);
        float maxY = TextureManager.getMaxY(id);

        switch (side)  {
            case 0:
                return new Vector5f[] { new Vector5f(size,size,-size,maxX,maxY),
                        new Vector5f(size, -size, -size, maxX,minY),
                        new Vector5f(-size,-size,-size,minX,minY),
                        new Vector5f(-size,size,-size,minX,maxY)};
            case 1:
                return new Vector5f[] { new Vector5f(size,size,size,maxX,maxY),
                        new Vector5f(size, -size, size, maxX,minY),
                        new Vector5f(-size,-size,size,minX,minY),
                        new Vector5f(-size,size,size,minX,maxY)};

            case 2:
                return new Vector5f[] { new Vector5f(-size,size,size,maxX,maxY),
                        new Vector5f(-size,-size,size,maxX,minY),
                        new Vector5f(-size,-size,-size,minX,minY),
                        new Vector5f(-size,size,-size,minX,maxY)};
            case 3:
                return new Vector5f[] { new Vector5f(size,size,size,maxX,maxY),
                        new Vector5f(size,-size,size,maxX,minY),
                        new Vector5f(size,-size,-size,minX,minY),
                        new Vector5f(size,size,-size,minX,maxY)};
            case 5:
                return new Vector5f[] { new Vector5f(size,size,size,minX,minY),
                        new Vector5f(size,size,-size,maxX,minY),
                        new Vector5f(-size,size,-size,maxX,maxY),
                        new Vector5f(-size,size,size,minX,maxY)};
            default:
                return new Vector5f[] { new Vector5f(size,-size,size,minX,minY),
                        new Vector5f(size,-size,-size,maxX,minY),
                        new Vector5f(-size,-size,-size,maxX,maxY),
                        new Vector5f(-size,-size,size,minX,maxY)};
        }
    }

    public static Vector5f[] getVertices1(BlockTextureManager blockTextureManager, int side, float size) {

        int id = blockTextureManager.textures[side];

        float minX = TextureManager.getMinX(id);
        float maxX = TextureManager.getMaxX(id);
        float minY = TextureManager.getMinY(id);
        float maxY = TextureManager.getMaxY(id);

        switch (side) {
            case 0:
                return new Vector5f[]{new Vector5f(size, size, 0, maxX, maxY),
                        new Vector5f(size, 0, 0, maxX, minY),
                        new Vector5f(0, 0, 0, minX, minY),
                        new Vector5f(0, size, 0, minX, maxY)};
            case 1:
                return new Vector5f[]{new Vector5f(size, size, size, maxX, maxY),
                        new Vector5f(size, 0, size, maxX, minY),
                        new Vector5f(0, 0, size, minX, minY),
                        new Vector5f(0, size, size, minX, maxY)};

            case 2:
                return new Vector5f[]{new Vector5f(0, size, size, maxX, maxY),
                        new Vector5f(0, 0, size, maxX, minY),
                        new Vector5f(0, 0, 0, minX, minY),
                        new Vector5f(0, size, 0, minX, maxY)};
            case 3:
                return new Vector5f[]{new Vector5f(size, size, size, maxX, maxY),
                        new Vector5f(size, 0, size, maxX, minY),
                        new Vector5f(size, 0, 0, minX, minY),
                        new Vector5f(size, size, 0, minX, maxY)};
            case 5:
                return new Vector5f[]{new Vector5f(size, size, size, minX, minY),
                        new Vector5f(size, size, 0, maxX, minY),
                        new Vector5f(0, size, 0, maxX, maxY),
                        new Vector5f(0, size, size, minX, maxY)};
            default:
                return new Vector5f[]{new Vector5f(size, 0, size, minX, minY),
                        new Vector5f(size, 0, 0, maxX, minY),
                        new Vector5f(0, 0, 0, maxX, maxY),
                        new Vector5f(0, 0, size, minX, maxY)};
        }
    }

    public static Integer[] getIndices(int side, int spot) {
        switch (side) {
            case 0:
            case 5:
            case 3:
                return new Integer[] {spot,spot + 1,spot + 2,spot,spot + 2,spot + 3};
            default:
                return new Integer[]{spot,spot + 2, spot + 1, spot, spot + 3, spot + 2};
        }
    }
}
