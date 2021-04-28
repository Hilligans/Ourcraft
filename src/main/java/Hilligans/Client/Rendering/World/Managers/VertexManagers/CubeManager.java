package Hilligans.Client.Rendering.World.Managers.VertexManagers;

import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Util.Vector5f;

public class CubeManager {
    public static Vector5f[] getVertices(BlockTextureManager blockTextureManager, int side, float size) {

        int id = blockTextureManager.textures[side];

        float minX = WorldTextureManager.getMinX(id);
        float maxX = WorldTextureManager.getMaxX(id);
        float minY = WorldTextureManager.getMinY(id);
        float maxY = WorldTextureManager.getMaxY(id);

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
        float minX;
        float maxX;
        float minY;
        float maxY;
        if(blockTextureManager != null) {

            int id = blockTextureManager.textures[side];

            minX = WorldTextureManager.getMinX(id);
            maxX = WorldTextureManager.getMaxX(id);
            minY = WorldTextureManager.getMinY(id);
            maxY = WorldTextureManager.getMaxY(id);
        } else {
            minX = 0;
            minY = 0;
            maxX = 1;
            maxY = 1;
        }

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

    public static void addVertices(PrimitiveBuilder primitiveBuilder, BlockTextureManager blockTextureManager, int side, float size, float offsetY) {

    }

    public static Vector5f[] getHorizontalSlabVertices(BlockTextureManager blockTextureManager, int side, float size, float offsetY) {

        float minX;
        float maxX;
        float minY;
        float maxY;
        float sub = 0.5f;
        if(blockTextureManager != null) {

            int id = blockTextureManager.textures[side];

            minX = WorldTextureManager.getMinX(id);
            maxX = WorldTextureManager.getMaxX(id);
            minY = WorldTextureManager.getMinY(id);
            maxY = WorldTextureManager.getMaxY(id);
        } else {

            minX = 0;
            minY = 0;
            maxX = 1;
            maxY = 1;
        }
        //int id = blockTextureManager.textures[side];
        ////float sub = WorldTextureManager.getTextureSize(id) / 2f;

        //float minX = WorldTextureManager.getMinX(id);
        //float maxX = WorldTextureManager.getMaxX(id);
        //float minY = WorldTextureManager.getMinY(id);
        //float maxY = WorldTextureManager.getMaxY(id);

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

        float minX = WorldTextureManager.getMinX(id);
        float maxX = WorldTextureManager.getMaxX(id);
        float minY = WorldTextureManager.getMinY(id);
        float maxY = WorldTextureManager.getMaxY(id);
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

    public static Vector5f[] getStairVertices(BlockTextureManager blockTextureManager, int side, float size, int shape, int rotation) {
        int id = blockTextureManager.textures[side];

        float texMinX = WorldTextureManager.getMinX(id);
        float texMaxX = WorldTextureManager.getMaxX(id);
        float texMinY = WorldTextureManager.getMinY(id);
        float texMaxY = WorldTextureManager.getMaxY(id);

        float minX = 0,minY = 0,minZ = 0;
        float maxX = 1,maxY = 1,maxZ = 1;

        float halfY = Math.abs((maxY - minY) / 2);
        float halfZ = Math.abs((maxZ - minZ) / 2);

        switch (side) {
            case 0:
                switch (shape) {
                    case 0:
                        return new Vector5f[]{new Vector5f(maxX, halfY, minZ, texMaxX, texMaxY / 2),
                                new Vector5f(maxX, minY, minZ, texMaxX, texMinY),
                                new Vector5f(minX, minY, minZ, texMinX, texMinY),
                                new Vector5f(minX, halfY, minZ, texMinX, texMaxY / 2)};
                }
            case 1:
                return new Vector5f[]{new Vector5f(maxX, maxY, maxZ, texMaxX, texMaxY),
                        new Vector5f(maxX, minY, maxZ, texMaxX, texMinY),
                        new Vector5f(minX, minY, maxZ, texMinX, texMinY),
                        new Vector5f(minX, maxY, maxZ, texMinX, texMaxY)};

            case 2:
                return new Vector5f[]{new Vector5f(minX, halfY, maxZ, texMaxX, texMaxY / 2),
                        new Vector5f(minX, minY, maxZ, texMaxX, texMinY),
                        new Vector5f(minX, minY, minZ, texMinX, texMinY),
                        new Vector5f(minX, halfY, minZ, texMinX, texMaxY / 2),

                        new Vector5f(minX, maxY, maxZ, texMaxX, texMaxY),
                        new Vector5f(minX, halfY, maxZ, texMaxX, texMaxY / 2),
                        new Vector5f(minX, halfY, halfZ, texMaxX / 2, texMaxY / 2),
                        new Vector5f(minX, maxY, halfZ, texMaxX / 2,  texMaxY)};

            case 6:
                return new Vector5f[]{new Vector5f(maxX, maxY, halfZ, texMaxX, texMaxY),
                        new Vector5f(maxX, halfY, halfZ, texMaxX, texMaxY / 2),
                        new Vector5f(minX, halfY, halfZ, texMinX, texMaxY / 2),
                        new Vector5f(minX, maxY, halfZ, texMinX, texMaxY) };
        }



        return new Vector5f[]{};
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

    public static int[] getIndices2(int side, int spot) {
        switch (side) {
            case 0:
            case 5:
            case 3:
                return new int[] {spot,spot + 1,spot + 2,spot,spot + 2,spot + 3};
            default:
                return new int[]{spot,spot + 2, spot + 1, spot, spot + 3, spot + 2};
        }
    }

    public static Integer[] getInvertedIndices(int side, int spot) {
        switch (side) {
            case 1:
            case 2:
            case 4:
                return new Integer[] {spot,spot + 1,spot + 2,spot,spot + 2,spot + 3};
            default:
                return new Integer[]{spot,spot + 2, spot + 1, spot, spot + 3, spot + 2};
        }
    }




    public static Integer[] getIndices1(int side, int spot) {
        switch (side) {
            case 0:
            case 5:
            case 3:
                return new Integer[] {spot,spot + 1,spot + 1,spot + 2,spot + 2, spot, spot,spot + 2,spot + 2, spot + 3,spot + 3,spot};
            default:
                return new Integer[]{spot,spot + 2,spot + 2, spot + 1,spot + 1,spot, spot, spot + 3,spot + 3, spot + 2,spot + 2, spot};
        }
    }
}
