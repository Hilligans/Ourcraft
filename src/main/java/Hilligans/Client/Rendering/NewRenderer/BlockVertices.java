package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Util.Vector5f;
import Hilligans.WorldSave.WorldLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class BlockVertices {

    float[][] vertices = new float[7][];
    int[][] indices = new int[7][];

    public BlockVertices(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        for(int x = 0; x < 7; x++) {
            JSONObject jsonObject1;
            JSONArray vertices;
            JSONArray indices;
            try {
                jsonObject1 = jsonObject.getJSONObject(x + "");
                vertices = jsonObject1.getJSONArray("vertices");
                indices = jsonObject1.getJSONArray("indices");
            } catch (Exception ignored) {
                continue;
            }
            float[] verticesList = new float[vertices.length() * 9];
            AtomicInteger size = new AtomicInteger();
            vertices.forEach(T -> {
                ((JSONArray)T).forEach(Q -> verticesList[size.getAndIncrement()] = ((Number)Q).floatValue());
                System.arraycopy(new float[]{2,2,2,2},0,verticesList,size.getAndAdd(4),4);
            });
            size.set(0);
            int[] indicesList = new int[indices.length()];
            indices.forEach(Q -> indicesList[size.getAndIncrement()] = (int)Q);
            this.vertices[x] = verticesList;
            this.indices[x] = indicesList;
        }
    }

    public void addData(PrimitiveBuilder primitiveBuilder, BlockTextureManager blockTextureManager, int side, float size, BlockPos offset) {
        if(vertices[side] != null) {
            float color = getSideColor(side);
            float[] vals = new float[vertices[side].length];
            int id = blockTextureManager.textures[side];

            float startX = WorldTextureManager.getMinX(id);
            float startY = WorldTextureManager.getMinY(id);
            float offsetX = WorldTextureManager.getMaxX(id) - startX;
            float offsetY = WorldTextureManager.getMaxY(id) - startY;

            for(int x = 0; x < vals.length; x+= 9) {
                vals[x] = vertices[side][x] * size + offset.x;
                vals[x + 1] = vertices[side][x + 1] * size + offset.y;
                vals[x + 2] = vertices[side][x + 2] * size + offset.z;
                vals[x + 7] = vertices[side][x + 3] * offsetX + startX;
                vals[x + 8] = vertices[side][x + 4] * offsetY + startY;
                vals[x + 3] = color;
                vals[x + 4] = color;
                vals[x + 5] = color;
                vals[x + 6] = 1.0f;
            }
            int[] ints = new int[indices[side].length];
            smallArrayCopy(indices[side],0,ints,0,indices[side].length, primitiveBuilder.getVerticesCount());
            primitiveBuilder.add(vals,ints);
        }
    }

    public static void smallArrayCopy(int[] source, int startPos, int[] dest, int destPos, int length, int toAdd) {
        for(int x = startPos; x < Math.min(source.length,startPos + length); x++) {
            dest[destPos + x] = source[x] + toAdd;
        }
    }

    private float getSideColor(int side) {
        if (side == 2 || side == 3) {
            return 0.95f;
        } else if (side == 0 || side == 1) {
            return 0.9f;
        } else if (side == 4) {
            return 0.85f;
        } else {
            return 1;
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

    public static BlockVertices create(String path) {
        String val = WorldLoader.readString(path);
        try {
            return new BlockVertices(val);
        } catch (Exception ignored) {
            return null;
        }
    }
}
