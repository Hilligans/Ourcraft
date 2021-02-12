package Hilligans.Client.Rendering.World;

import Hilligans.Data.Other.Texture;
import Hilligans.Util.Vector5f;

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

    public static Vector5f[] getHorizontalSlabVertices(BlockTextureManager blockTextureManager, int side, float size, float offsetY) {

        int id = blockTextureManager.textures[side];
        float sub = TextureManager.getTextureSize(id) / 2f;

        float minX = TextureManager.getMinX(id);
        float maxX = TextureManager.getMaxX(id);
        float minY = TextureManager.getMinY(id);
        float maxY = TextureManager.getMaxY(id);

        switch (side) {
            case 0:
                return new Vector5f[]{new Vector5f(size, offsetY + size / 2, 0, maxX, maxY - sub),
                        new Vector5f(size, offsetY, 0, maxX, minY),
                        new Vector5f(0, offsetY, 0, minX, minY),
                        new Vector5f(0, offsetY + size / 2, 0, minX, maxY - sub)};
            case 1:
                return new Vector5f[]{new Vector5f(size, offsetY + size / 2, size, maxX, maxY - sub),
                        new Vector5f(size, offsetY, size, maxX, minY),
                        new Vector5f(0, offsetY, size, minX, minY),
                        new Vector5f(0, offsetY + size / 2, size, minX, maxY - sub)};

            case 2:
                return new Vector5f[]{new Vector5f(0, offsetY + size / 2, size, maxX, maxY - sub),
                        new Vector5f(0, offsetY, size, maxX, minY),
                        new Vector5f(0, offsetY, 0, minX, minY),
                        new Vector5f(0, offsetY + size / 2, 0, minX, maxY - sub)};
            case 3:
                return new Vector5f[]{new Vector5f(size, offsetY + size / 2, size, maxX, maxY - sub),
                        new Vector5f(size, offsetY, size, maxX, minY),
                        new Vector5f(size, offsetY, 0, minX, minY),
                        new Vector5f(size, offsetY + size / 2, 0, minX, maxY - sub)};
            case 5:
                return new Vector5f[]{new Vector5f(size, offsetY + size / 2, size, minX, minY),
                        new Vector5f(size, offsetY + size / 2, 0, maxX, minY),
                        new Vector5f(0, offsetY + size / 2, 0, maxX, maxY),
                        new Vector5f(0, offsetY + size / 2, size, minX, maxY)};
            default:
                return new Vector5f[]{new Vector5f(size, offsetY, size, minX, minY),
                        new Vector5f(size, offsetY, 0, maxX, minY),
                        new Vector5f(0, offsetY, 0, maxX, maxY),
                        new Vector5f(0, offsetY, size, minX, maxY)};
        }
    }

    public static Vector5f[] getVerticalSlabVertices(BlockTextureManager blockTextureManager, int side, float size, float offset, boolean rotated) {

        int id = blockTextureManager.textures[side];

        float minX = TextureManager.getMinX(id);
        float maxX = TextureManager.getMaxX(id);
        float minY = TextureManager.getMinY(id);
        float maxY = TextureManager.getMaxY(id);
        float sub = (maxX - minX) / 2f;
        float sub1 = (maxY - minY) / 2f;

        if(rotated) {
            switch (side) {
                case 0:
                    return new Vector5f[]{new Vector5f(size, size, offset, maxX, maxY),
                            new Vector5f(size, 0, offset, maxX, minY),
                            new Vector5f(0, 0, offset, minX, minY),
                            new Vector5f(0, size, offset, minX, maxY)};
                case 1:
                    return new Vector5f[]{new Vector5f(size, size, offset + size / 2, maxX, maxY),
                            new Vector5f(size, 0, offset + size / 2, maxX, minY),
                            new Vector5f(0, 0, offset + size / 2, minX, minY),
                            new Vector5f(0, size, offset + size / 2, minX, maxY)};

                case 2:
                    return new Vector5f[]{new Vector5f(0, size, offset + size / 2, maxX - sub, maxY),
                            new Vector5f(0, 0, offset + size / 2, maxX - sub, minY),
                            new Vector5f(0, 0, offset, minX, minY),
                            new Vector5f(0, size, offset, minX, maxY)};
                case 3:
                    return new Vector5f[]{new Vector5f(size, size, offset + size / 2, maxX - sub, maxY),
                            new Vector5f(size, 0, offset + size / 2, maxX - sub, minY),
                            new Vector5f(size, 0, offset, minX, minY),
                            new Vector5f(size, size, offset, minX, maxY)};
                case 5:
                    return new Vector5f[]{new Vector5f(size, size, offset + size / 2, minX, minY),
                            new Vector5f(size, size, offset, maxX - sub, minY),
                            new Vector5f(0, size, offset, maxX - sub, maxY),
                            new Vector5f(0, size, offset + size / 2, minX, maxY)};
                default:
                    return new Vector5f[]{new Vector5f(size, 0, offset + size / 2, minX, minY),
                            new Vector5f(size, 0, offset, maxX - sub, minY),
                            new Vector5f(0, 0, offset, maxX - sub, maxY),
                            new Vector5f(0, 0, offset + size / 2, minX, maxY)};
            }
        } else {

            switch (side) {
                case 0:
                    return new Vector5f[]{new Vector5f(offset + size / 2, size, 0, maxX - sub, maxY),
                            new Vector5f(offset + size / 2, 0, 0, maxX - sub, minY),
                            new Vector5f(offset, 0, 0, minX, minY),
                            new Vector5f(offset, size, 0, minX, maxY)};
                case 1:
                    return new Vector5f[]{new Vector5f(offset + size / 2, size, size, maxX - sub, maxY),
                            new Vector5f(offset + size / 2, 0, size, maxX - sub, minY),
                            new Vector5f(offset, 0, size, minX, minY),
                            new Vector5f(offset, size, size, minX, maxY)};

                case 2:
                    return new Vector5f[]{new Vector5f(offset, size, size, maxX, maxY),
                            new Vector5f(offset, 0, size, maxX, minY),
                            new Vector5f(offset, 0, 0, minX, minY),
                            new Vector5f(offset, size, 0, minX, maxY)};
                case 3:
                    return new Vector5f[]{new Vector5f(offset + size / 2, size, size, maxX, maxY),
                            new Vector5f(offset + size / 2, 0, size, maxX, minY),
                            new Vector5f(offset + size / 2, 0, 0, minX, minY),
                            new Vector5f(offset + size / 2, size, 0, minX, maxY)};
                case 5:
                    return new Vector5f[]{new Vector5f(offset + size / 2, size, size, minX, minY),
                            new Vector5f(offset + size / 2, size, 0, maxX, minY),
                            new Vector5f(offset, size, 0, maxX, maxY - sub1),
                            new Vector5f(offset, size, size, minX, maxY - sub1)};
                default:
                    return new Vector5f[]{new Vector5f(offset + size / 2, 0, size, minX, minY),
                            new Vector5f(offset + size / 2, 0, 0, maxX, minY),
                            new Vector5f(offset, 0, 0, maxX, maxY - sub1),
                            new Vector5f(offset, 0, size, minX, maxY - sub1)};
            }
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
