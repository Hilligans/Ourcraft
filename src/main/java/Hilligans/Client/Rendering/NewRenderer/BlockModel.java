package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Block.Block;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockModel implements IModel {

    float[][] vertices = new float[6][];
    int[][] indices = new int[6][];
    float[][] color = new float[6][3];


    public String jsonString;
    public String path;

    public BlockModel(String jsonString) {
        for(int x = 0; x < 6; x++) {
            color[x][0] = 1.0f;
            color[x][1] = 1.0f;
            color[x][2] = 1.0f;
        }
        this.jsonString = jsonString;
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
            this.vertices[x] = verticesList;
            this.indices[x] = indicesList;
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
                float color = getSideColor(side,rotX,rotY);
                float[] vals = new float[vertices.length];
                int id = ((BlockTextureManager) textureManager).textures[side];

                float startX = TextAtlas.getMinX(id);
                float startY = TextAtlas.getMinY(id);
                float offsetX = TextAtlas.getMaxX(id) - startX;
                float offsetY = TextAtlas.getMaxY(id) - startY;
                for (int x = 0; x < vals.length; x += 9) {
                    vals[x] = vertices[x] * size;
                    vals[x + 1] = vertices[x + 1] * size;
                    vals[x + 2] = vertices[x + 2] * size;
                    vals[x + 7] = vertices[x + 3] * offsetX + startX;
                    vals[x + 8] = vertices[x + 4] * offsetY + startY;
                    vals[x + 3] = color * getRed(side);
                    vals[x + 4] = color * getGreen(side);
                    vals[x + 5] = color * getBlue(side);
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

    public float getRed(int side) {
        return color[side][0];
    }

    public float getGreen(int side) {
        return color[side][1];
    }

    public float getBlue(int side) {
        return color[side][2];
    }

    @Override
    public String getModel() {
        return jsonString;
    }

    @Override
    public String getPath() {
        return path;
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

    private float getSideColor(int side, int rotX, int rotY) {
        int value = side | rotX << 3 | rotY << 5;
        return getSideColor(Block.rotationSides[value]);
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

    public static final boolean[] doRations = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};

    public void applyRotation(float[] vertices, int rotX, int rotY, float size, Vector3f offset) {
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
                    vertices[x] = abs(vertices[x]) + offset.x;
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
                    vertices[x] = abs(vertices[x]) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 2]) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }


            case 4 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = vertices[x];
                    vertices[x] = abs(vertices[x + 2] - size) + offset.x;
                    vertices[x + 1] += + offset.y;
                    vertices[x + 2] = abs(temp) + offset.z;
                }
            }
            case 5 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x]);
                    vertices[x] = abs(vertices[x + 1] - size) + offset.x;
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
                    float temp = abs(vertices[x] - size);
                    vertices[x] = abs(vertices[x + 1]) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 2] - size) + offset.y;
                    vertices[x + 2] = temp + offset.z;

                }
            }


            case 8 -> {
                for(int x = 0; x < vertices.length; x+= 9) {
                    vertices[x] = abs(vertices[x] - size) + offset.x;
                    vertices[x + 1] += offset.y;
                    vertices[x + 2] = abs(vertices[x + 2] - size) + offset.z;
                }
            }
            case 9 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x + 1] - size);
                    vertices[x] = abs(vertices[x] - size) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 2] - size) + offset.y;
                    vertices[x + 2] = temp + offset.z;

                }
            }

            case 10 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    vertices[x] = abs(vertices[x] - size) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 1] - size) + offset.y;
                    vertices[x + 2] = vertices[x + 2] + offset.z;
                }
            }

            case 11 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x + 1]);
                    vertices[x] = abs(vertices[x] - size) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 2]) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }



            case 12 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = vertices[x];
                    vertices[x] = abs(vertices[x + 2]) + offset.x;
                    vertices[x + 1] += + offset.y;
                    vertices[x + 2] = abs(temp - size) + offset.z;
                }
            }

            case 13 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x] - size);
                    vertices[x] = abs(vertices[x + 1]) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 2] - size) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }

            case 14 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x] - size);
                    vertices[x] = abs(vertices[x + 2] - size) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 1] - size) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }

            case 15 -> {
                for (int x = 0; x < vertices.length; x += 9) {
                    float temp = abs(vertices[x] - size);
                    vertices[x] = abs(vertices[x + 1] - size) + offset.x;
                    vertices[x + 1] = abs(vertices[x + 2]) + offset.y;
                    vertices[x + 2] = temp + offset.z;
                }
            }
        }
    }

    public float abs(float val) {
        return val < 0 ? -val : val;
    }

    @Override
    public int[] getIndices(int side, int rotX, int rotY) {
        boolean rotate = doRations[rotX | rotY << 2];
        if(rotate) {
            int[] indices = this.indices[side];
            int[] newIndices = new int[indices.length];
            for(int x = 0; x < indices.length; x += 3) {
                newIndices[x] = indices[x];
                newIndices[x + 1] = indices[x + 2];
                newIndices[x + 2] = indices[x + 1];
            }
            return newIndices;
        }
        return indices[side];
    }

    public BlockModel withColor(JSONArray color) {
        if(color != null) {
            for (int x = 0; x < 6; x++) {
                this.color[x][0] = color.getNumber(x * 3).floatValue();
                this.color[x][1] = color.getNumber(x * 3 + 1).floatValue();
                this.color[x][2] = color.getNumber(x * 3 + 2).floatValue();
            }
        }
        return this;
    }

    public static BlockModel create(String path) {
        IModel model = Ourcraft.GAME_INSTANCE.RESOURCE_MANAGER.getModel(path);
        if(model instanceof BlockModel) {
            return (BlockModel) model;
        }
        String val = WorldLoader.readString(Ourcraft.GAME_INSTANCE.RESOURCE_MANAGER.getResource(path));
        try {
            BlockModel blockModel = new BlockModel(val);
            blockModel.path = path;
            return blockModel;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            System.err.println(val);
            return null;
        }
    }
}
