package Hilligans.Client.Rendering.World.Managers.VertexManagers;

import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Util.Vector5f;

public class CubeManager {

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
}
