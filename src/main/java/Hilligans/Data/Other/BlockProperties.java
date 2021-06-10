package Hilligans.Data.Other;

import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Data.Other.BlockShapes.BlockShape;
import Hilligans.WorldSave.WorldLoader;
import org.json.JSONArray;
import org.json.JSONObject;

public class BlockProperties {

    public static BlockShape defaultShape = new BlockShape();


    public boolean serverSide = false;
    public boolean transparent = false;
    public boolean canWalkThrough = false;
    public boolean airBlock = false;
    public String placementMode = "default";
    public int blockStateSize = 0;
    public BlockTextureManager blockTextureManager = new BlockTextureManager();
    public BlockShape blockShape = defaultShape;

    public BlockProperties serverSide() {
        serverSide = true;
        return this;
    }

    public BlockProperties transparent() {
        transparent = true;
        return this;
    }

    public BlockProperties transparent(boolean transparent) {
        this.transparent = transparent;
        return this;
    }

    public BlockProperties airBlock() {
        airBlock = true;
        return this;
    }

    public BlockProperties airBlock(boolean airBlock) {
        this.airBlock = airBlock;
        return this;
    }


    public BlockProperties canWalkThrough() {
        canWalkThrough = true;
        return this;
    }

    public BlockProperties canWalkThrough(boolean canWalkThrough) {
        this.canWalkThrough = canWalkThrough;
        return this;
    }

    public BlockProperties withTexture(String texture) {
        blockTextureManager.addString(texture);
        return this;
    }

    public BlockProperties withSidedTexture(String texture, int side) {
        blockTextureManager.addString(texture,side);
        return this;
    }

    public BlockProperties textureSource(String source) {
        blockTextureManager.textureSource = source;
        return this;
    }

    public static BlockProperties loadProperties(String path) {
        BlockProperties blockProperties = new BlockProperties();
        String val = WorldLoader.readString(path);
        if(!val.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(val);

                if(jsonObject.has("properties")) {
                    JSONObject properties = jsonObject.getJSONObject("properties");
                    blockProperties.canWalkThrough(getBoolean(properties,"canWalkThrough",false));
                    blockProperties.airBlock(getBoolean(properties,"airBlock",false));
                    blockProperties.transparent(getBoolean(properties,"transparent",false));
                    blockProperties.placementMode = properties.has("placementMode") ? properties.getString("placementMode") : "default";
                    blockProperties.blockStateSize = properties.has("blockStateByteCount") ? properties.getInt("blockStateByteCount") : 0;

                }
                if(jsonObject.has("model")) {
                    JSONObject model = jsonObject.getJSONObject("model");
                    if(model.has("modelName")) {
                        blockProperties.blockShape = new BlockShape(model.getString("modelName"));
                    }
                    BoundingBox boundingBox;
                    if(model.has("boundingBox")) {
                        JSONArray boundingBoxArray = model.getJSONArray("boundingBox");
                        if(boundingBoxArray.length() > 6) {
                            float[] vals = {0,0,0,1,1,1};
                            for(int x = 0; x < 6; x++) {
                                vals[x] = boundingBoxArray.getNumber(x).floatValue();
                            }
                            boundingBox = new JoinedBoundingBox(vals);
                            for(int x = 6; x < boundingBoxArray.length(); x+=6) {
                                ((JoinedBoundingBox) boundingBox).addBox(boundingBoxArray.getNumber(x).floatValue(),boundingBoxArray.getNumber(x + 1).floatValue(),boundingBoxArray.getNumber(x + 2).floatValue(),boundingBoxArray.getNumber(x + 3).floatValue(),boundingBoxArray.getNumber(x + 4).floatValue(),boundingBoxArray.getNumber(x + 5).floatValue());
                            }
                        } else {
                            float[] vals = {0,0,0,1,1,1};
                            for(int x = 0; x < boundingBoxArray.length(); x++) {
                                vals[x] = boundingBoxArray.getNumber(x).floatValue();
                            }
                            boundingBox = new BoundingBox(vals);
                        }
                    } else {
                        boundingBox = new BoundingBox(0,0,0,1,1,1);
                    }
                    blockProperties.blockShape.defaultBoundingBox = boundingBox;

                    if(model.has("blockStates")) {
                        JSONObject blockStates = model.getJSONObject("blockStates");
                        for(String string : blockStates.keySet()) {
                            try {
                                int block = Integer.parseInt(string);
                                JSONObject jsonObject1 = blockStates.getJSONObject(string);
                                int rotX = jsonObject1.getInt("rotX");
                                int rotY = jsonObject1.getInt("rotY");
                                blockProperties.blockShape.putRotation(block,rotX,rotY);
                            } catch (Exception ignored) {}
                        }
                    }

                }
            } catch (Exception ignored) {}
        }
        return blockProperties;
    }

    public static boolean getBoolean(JSONObject jsonObject, String key, boolean defaultValue) {
        if(jsonObject.has(key)) {
            return jsonObject.getBoolean(key);
        }
        return defaultValue;
    }



}
