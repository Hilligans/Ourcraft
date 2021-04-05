package Hilligans.Client.Rendering.World.Managers.VertexManagers;

import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Util.Vector5f;

public class PlantManager {

    public static Vector5f[] getXBlockVertices(float offsetX, float offsetZ, BlockTextureManager blockTextureManager, int side, float size) {
        int id = blockTextureManager.textures[side];

        float texMinX = WorldTextureManager.getMinX(id);
        float texMaxX = WorldTextureManager.getMaxX(id);
        float texMinY = WorldTextureManager.getMinY(id);
        float texMaxY = WorldTextureManager.getMaxY(id);

        float minX = -size + offsetX;
        float maxX = size + offsetX;
        float minZ = -size + offsetZ;
        float maxZ = size + offsetZ;
        float minY = 0;
        float maxY = size * 2;

        if(side == 0 || side == 2) {
            minX += 0.0001;
            minZ += 0.0001;
            maxX += 0.0001;
            maxZ += 0.0001;
        }
        switch (side) {
            case 0:
            case 1:
                return new Vector5f[]{
                        new Vector5f(minX,minY,minZ,texMinX,texMinY), new Vector5f(minX,maxY,minZ,texMinX,texMaxY), new Vector5f(maxX,maxY,maxZ,texMaxX,texMaxY), new Vector5f(maxX,minY,maxZ,texMaxX,texMinY)
                };
            case 2:
            case 3:
                return new Vector5f[]{
                        new Vector5f(maxX,minY,minZ,texMinX,texMinY), new Vector5f(maxX,maxY,minZ,texMinX,texMaxY), new Vector5f(minX,maxY,maxZ,texMaxX,texMaxY), new Vector5f(minX,minY,maxZ,texMaxX,texMinY)
                };
        }
        return new Vector5f[]{};
    }

}
