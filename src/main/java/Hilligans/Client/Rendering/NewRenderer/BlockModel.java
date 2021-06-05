package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.TextureManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Ourcraft;
import Hilligans.Util.Vector5f;
import Hilligans.WorldSave.WorldLoader;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockModel implements IModel {

    float[][] vertices = new float[6][];
    int[][] indices = new int[6][];

    public BlockModel(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        for(int x = 0; x < 6; x++) {
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
           // for(int y = 0; y < 16; y++) {
                this.vertices[x] = verticesList;
                this.indices[x] = indicesList;
            //}
        }
    }

    private void createAndAddData(float[][] vertices, int[][] indices, int rot) {
        switch (rot) {
            case 0:
                break;
            case 1:
                modifyAll(vertices,1,false,false,false,1.0f);
                break;
            case 2:
                modifyAll(vertices,0,true,false,false,1.0f);
                break;
            case 3:
               // modifyAll(vertices,1,);


        }
    }

    private void modifyAll(float[][] data, int swapMode, boolean negateX, boolean negateY, boolean negateZ, float size) {
        for(int x = 0; x < 6; x++) {
            modifyData(data[x],swapMode,negateX,negateY,negateZ,size);
        }
    }

    private void modifyData(float[] data, int swapMode, boolean negateX, boolean negateY, boolean negateZ, float size) {
        for(int x = 0; x < data.length; x += 9) {
            switch (swapMode) {
                case  0:
                    break;
                case 1:
                    float tempVal = data[x];
                    data[x] = data[x + 2];
                    data[x + 2] = tempVal;
                    break;
                case 2:
                    tempVal = data[x];
                    data[x] = data[x + 1];
                    data[x + 1] = tempVal;
                case 3:
                    tempVal = data[x + 1];
                    data[x + 1] = data[x + 2];
                    data[x + 2] = tempVal;
                case 4:
                    tempVal = data[x];
                    float tempVal1 = data[x + 1];
                    data[x + 1] = tempVal;
                    data[x] = data[x + 2];
                    data[x + 2] = tempVal1;
                case 5:
                    tempVal = data[x + 2];
                    tempVal1 = data[x + 1];
                    data[x + 1] = tempVal;
                    data[x + 2] = data[x];
                    data[x] = tempVal1;
            }
            if(negateX) {
                data[x] = size - data[x];
            }
            if(negateY) {
                data[x + 1] = size - data[x + 1];
            }
            if(negateZ) {
                data[x + 2] = size - data[x + 2];
            }
        }
    }

    @Override
    public void addData(PrimitiveBuilder primitiveBuilder, TextureManager textureManager, int side, float size, Vector3f offset, int rotX, int rotY) {
        float[] vertices = getVertices(side,rotX,rotY);
        if(vertices != null) {
            if(textureManager instanceof BlockTextureManager) {
                float color = getSideColor(side);
                float[] vals = new float[vertices.length];
                int id = ((BlockTextureManager) textureManager).textures[side];

                float startX = WorldTextureManager.getMinX(id);
                float startY = WorldTextureManager.getMinY(id);
                float offsetX = WorldTextureManager.getMaxX(id) - startX;
                float offsetY = WorldTextureManager.getMaxY(id) - startY;
                for (int x = 0; x < vals.length; x += 9) {
                    vals[x] = vertices[x] * size;
                    vals[x + 1] = vertices[x + 1] * size;
                    vals[x + 2] = vertices[x + 2] * size;
                    vals[x + 7] = vertices[x + 3] * offsetX + startX;
                    vals[x + 8] = vertices[x + 4] * offsetY + startY;
                    vals[x + 3] = color;
                    vals[x + 4] = color;
                    vals[x + 5] = color;
                    vals[x + 6] = 1.0f;
                }
                int[] indices = getIndices(side,rotX,rotY);
                int[] ints = new int[indices.length];
                smallArrayCopy(indices, 0, ints, 0, indices.length, primitiveBuilder.getVerticesCount());
                applyRotation(vals,rotX,rotY,size,offset);
                primitiveBuilder.add(vals, ints);
            }
        }
    }

    @Override
    public void addData(PrimitiveBuilder primitiveBuilder, TextureManager textureManager, int side, float size, Vector3f offset, int rotX, int rotY, float offsetX, float offsetY, float offsetZ) {
        float[] vertices = getVertices(side,rotX,rotY);
        if(vertices != null) {
            if(textureManager instanceof BlockTextureManager) {
                float color = getSideColor(side);
                float[] vals = new float[vertices.length];
                int id = ((BlockTextureManager) textureManager).textures[side];
                float startX = WorldTextureManager.getMinX(id);
                float startY = WorldTextureManager.getMinY(id);
                float texOffsetX = WorldTextureManager.getMaxX(id) - startX;
                float texOffsetY = WorldTextureManager.getMaxY(id) - startY;
                for (int x = 0; x < vals.length; x += 9) {
                    vals[x] = vertices[x] * size + offset.x + offsetX;
                    vals[x + 1] = vertices[x + 1] * size + offset.y + offsetY;
                    vals[x + 2] = vertices[x + 2] * size + offset.z + offsetZ;
                    vals[x + 7] = vertices[x + 3] * texOffsetX + startX;
                    vals[x + 8] = vertices[x + 4] * texOffsetY + startY;
                    vals[x + 3] = color;
                    vals[x + 4] = color;
                    vals[x + 5] = color;
                    vals[x + 6] = 1.0f;
                }
                int[] indices = getIndices(side,rotX,rotY);
                int[] ints = new int[indices.length];
                smallArrayCopy(indices, 0, ints, 0, indices.length, primitiveBuilder.getVerticesCount());
                //applyRotation(vals,rotX,rotY,size);
                primitiveBuilder.add(vals, ints);
            }
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

    @Override
    public float[] getVertices(int side) {
        return vertices[side];
    }

    @Override
    public int[] getIndices(int side) {
        return indices[side];
    }

    @Override
    public float[] getVertices(int side, int rotX, int rotY) {
        return vertices[side];
    }

    public void applyRotation(float[] vertices, int rotX, int rotY, float size, Vector3f offset) {
       /* switch (rotY) {
            case 0:
                break;
            case 1:
                for(int x = 0; x < vertices.length; x+= 9) {
                    float temp =  vertices[x];
                    vertices[x] = vertices[x + 2];
                    vertices[x + 2] = abs(temp - size);;
                }
                break;
            case 2:
                for(int x = 0; x < vertices.length; x+= 9) {
                    vertices[x] = abs(vertices[x] - size);
                    vertices[x + 2] = abs(vertices[x + 2] - size);
                }
                break;
            case 3:
                for(int x = 0; x < vertices.length; x+= 9) {
                    float temp = abs(vertices[x] - size);
                    vertices[x] = abs(vertices[x + 2]);
                    vertices[x + 2] = temp;
                }
                break;
        }

        */
        switch (rotX | rotY << 2) {
            case 0 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    vertices[x] += offset.x;
                    vertices[x + 1] += offset.y;
                    vertices[x + 2] += offset.z;
                }
            }
            case 1 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x + 1]);
                    vertices[x] += offset.x;
                    vertices[x + 1] = abs(vertices[x + 2] - size) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }
            case 2 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    vertices[x] += offset.x;
                    vertices[x + 1] = abs(vertices[x + 1] - size) + offset.y;
                    vertices[x + 2] = abs(vertices[x + 2] - size) + offset.z;
                }
            }
            case 3 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x + 1] - size);
                    vertices[x] += offset.x;
                    vertices[x + 1] = abs(vertices[x + 2]) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }


            case 4 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = vertices[x];
                    vertices[x] = vertices[x + 2];
                    vertices[x + 2] = abs(temp - size);
                }
            }
            case 5 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x] - size);
                    vertices[x] = vertices[x + 1] + offset.x;
                    vertices[x + 1] = abs(vertices[x + 2] - size) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }
            case 6 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = vertices[x];
                    vertices[x] = vertices[x + 2] + offset.x;
                    vertices[x + 1] = abs(vertices[x + 1] - size) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }
            case 7 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x]);
                    vertices[x] = abs(vertices[x + 1] - size) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 2] - size) + offset.y;
                    vertices[x + 2] = temp + offset.z;

                }
            }


            case 8 -> {
                for(int x = 0; x < vertices.length; x+= 9) {
                    vertices[x] = abs(vertices[x] - size);
                    vertices[x + 2] = abs(vertices[x + 2] - size);
                }
            }
            case 9 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    vertices[x] = abs(vertices[x] - size) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 1] - size) + offset.y;
                    vertices[x + 2] += offset.z;
                }
            }

        }
    }

    public float abs(float val) {
        return val < 0 ? -val : val;
    }

    @Override
    public int[] getIndices(int side, int rotX, int rotY) {
        return indices[side];
    }

    public static BlockModel create(String path) {
        String val = WorldLoader.readString(path);
        try {
            return new BlockModel(val);
        } catch (Exception ignored) {
            return null;
        }
    }
}
