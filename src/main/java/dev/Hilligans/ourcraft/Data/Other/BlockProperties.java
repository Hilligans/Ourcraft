package dev.Hilligans.ourcraft.Data.Other;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.BlockModel;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.BlockTextureManager;
import dev.Hilligans.ourcraft.Data.Descriptors.Tag;
import dev.Hilligans.ourcraft.Data.Descriptors.TagCollection;
import dev.Hilligans.ourcraft.Data.Other.BlockShapes.BlockShape;
import dev.Hilligans.ourcraft.Item.Data.ToolLevel;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;
import org.json.JSONArray;
import org.json.JSONObject;

public class BlockProperties {
    
    public static BlockShape defaultShape = new BlockShape();


    public boolean serverSide = false;
    public boolean transparent = false;
    public boolean translucent = false;
    public boolean alwaysRender = false;
    public boolean canWalkThrough = false;
    public boolean airBlock = false;
    public boolean flammable = false;
    public boolean dynamicItemModel = false;

    public int mapColor = 0;
    public String placementMode = "default";
    public String textureFolder = "Blocks/";
    public int blockStateSize = 0;
    public String texture = "";
    public BlockTextureManager blockTextureManager = new BlockTextureManager();
    public BlockShape blockShape = defaultShape;
    public String toolLevel;
    public String source;
    public String drops;
    public String name;

    public String path;
    public JSONObject overrides;
    public boolean fromFile = false;
    public TagCollection tags = new TagCollection();

    public BlockProperties() {
    }

    public BlockProperties setName(String name) {
        this.drops = name;
        this.name = name;
        return this;
    }

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

    public BlockProperties addTexture(String texture, int side, int count) {
        if(count == 1) {
            withTexture(texture);
        } else {
            withSidedTexture(texture,side);
        }
        return this;
    }

    public BlockProperties textureSource(String source) {
        blockTextureManager.textureSource = source;
        return this;
    }

    public BlockProperties mapColor(int color) {
        this.mapColor = color;
        return this;
    }

    public JSONObject write() {
        JSONObject jsonObject = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONObject model = new JSONObject();
        JSONArray boundingBox = new JSONArray ();
        JSONObject blockStates = new JSONObject();

        jsonObject.put("properties",properties);
        jsonObject.put("model",model);

        model.put("boundingBox",boundingBox);
        model.put("blockStates",blockStates);

        properties.put("canWalkThrough",canWalkThrough);
        properties.put("airBlock",airBlock);
        properties.put("transparent",transparent);
        properties.put("translucent",translucent);
        properties.put("placementMode",placementMode);
        properties.put("blockStateByteCount",blockStateSize);
        properties.put("mapColor",mapColor);
        properties.put("flammable",flammable);
        properties.put("dynamicItemModel",dynamicItemModel);
        properties.put("alwaysRender",alwaysRender);
        properties.put("toolLevel", toolLevel);

        model.put("modelName",blockShape.path);

        return jsonObject;
    }

    public void read(JSONObject jsonObject) {
        if (jsonObject.has("properties")) {
            JSONObject properties = jsonObject.getJSONObject("properties");
            canWalkThrough(getBoolean(properties, "canWalkThrough", false));
            airBlock(getBoolean(properties, "airBlock", false));
            transparent(getBoolean(properties, "transparent", false));
            translucent = properties.optBoolean("translucent");
            flammable = getBoolean(properties,"flammable",false);
            placementMode = properties.has("placementMode") ? properties.getString("placementMode") : "default";
            blockStateSize = properties.has("blockStateByteCount") ? properties.getInt("blockStateByteCount") : 0;
            mapColor = properties.has("mapColor") ? properties.getInt("mapColor") : 0;
            dynamicItemModel = getBoolean(properties,"dynamicItemModel",false);
            alwaysRender = properties.optBoolean("alwaysRender");
            toolLevel = properties.optString("toolLevel");
            drops = properties.optString("drops", drops);
        }
        if (jsonObject.has("model")) {
            JSONObject model = jsonObject.getJSONObject("model");
            JSONArray colors = model.optJSONArray("colors");
            if (model.has("modelName")) {
                if(model.getString("modelName").endsWith(".obj")) {
                    blockShape = new BlockShape();
                    try {
                        blockShape.data = new BlockModel(new ObjFile(WorldLoader.readString("/Models/Blocks/" + model.getString("modelName"))).toBlockModel().toString());
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                } else {
                    blockShape = new BlockShape(model.getString("modelName"));
                }
                blockShape.data.withColor(colors);
            }
            BoundingBox boundingBox;
            if (model.has("boundingBox")) {
                JSONArray boundingBoxArray = model.getJSONArray("boundingBox");
                if (boundingBoxArray.length() > 6) {
                    float[] vals = {0, 0, 0, 1, 1, 1};
                    for (int x = 0; x < 6; x++) {
                        vals[x] = boundingBoxArray.getNumber(x).floatValue();
                    }
                    boundingBox = new JoinedBoundingBox(vals);
                    for (int x = 6; x < boundingBoxArray.length(); x += 6) {
                        ((JoinedBoundingBox) boundingBox).addBox(boundingBoxArray.getNumber(x).floatValue(), boundingBoxArray.getNumber(x + 1).floatValue(), boundingBoxArray.getNumber(x + 2).floatValue(), boundingBoxArray.getNumber(x + 3).floatValue(), boundingBoxArray.getNumber(x + 4).floatValue(), boundingBoxArray.getNumber(x + 5).floatValue());
                    }
                } else {
                    float[] vals = {0, 0, 0, 1, 1, 1};
                    for (int x = 0; x < boundingBoxArray.length(); x++) {
                        vals[x] = boundingBoxArray.getNumber(x).floatValue();
                    }
                    boundingBox = new BoundingBox(vals);
                }
            } else {
                boundingBox = new BoundingBox(0, 0, 0, 1, 1, 1);
            }
            blockShape.defaultBoundingBox = boundingBox;

            if (model.has("blockStates")) {
                JSONObject blockStates = model.getJSONObject("blockStates");
                for (String string : blockStates.keySet()) {
                    try {
                        if(string.equals("default")) {
                            continue;
                        }
                        int block = Integer.parseInt(string);
                        JSONObject jsonObject1 = blockStates.getJSONObject(string);
                        int rotX = jsonObject1.getInt("rotX");
                        int rotY = jsonObject1.getInt("rotY");
                        blockShape.putRotation(block, rotX, rotY);
                        blockShape.putModel(block,jsonObject1.optString("modelName"));
                        BlockModel blockModel = blockShape.models.get(block);
                        if(blockModel != null) {
                            blockModel.withColor(colors);
                        }
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            }
        }
        if(toolLevel != null && !toolLevel.equals("")) {
            tags.put(new Tag("mine_level", toolLevel, "ourcraft"));
        }
    }

    public static BlockProperties loadProperties(String path, JSONObject overrides) {
        BlockProperties blockProperties = new BlockProperties();
        if (path.equals("Data/null")) {
            return blockProperties;
        }
        try {
            blockProperties.path = path;
            blockProperties.overrides = overrides;
            blockProperties.fromFile = true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return blockProperties;
    }

    public static void recursivelyOverride(JSONObject jsonObject, JSONObject override) {
        for (String string : override.keySet()) {
            Object object = override.get(string);
            if(object instanceof JSONObject) {
                recursivelyOverride(jsonObject.getJSONObject(string),override.getJSONObject(string));
            } else {
                jsonObject.put(string, object);
            }
        }
    }

    public static BlockProperties loadProperties(JSONObject jsonObject) {
        BlockProperties blockProperties = new BlockProperties();
        blockProperties.read(jsonObject);
        return blockProperties;
    }

    public static boolean getBoolean(JSONObject jsonObject, String key, boolean defaultValue) {
        if(jsonObject.has(key)) {
            return jsonObject.getBoolean(key);
        }
        return defaultValue;
    }

    @Override
    public String toString() {
        return "BlockProperties{" +
                "serverSide=" + serverSide +
                ", transparent=" + transparent +
                ", canWalkThrough=" + canWalkThrough +
                ", airBlock=" + airBlock +
                ", placementMode='" + placementMode + '\'' +
                ", blockStateSize=" + blockStateSize +
                ", blockTextureManager=" + blockTextureManager +
                ", blockShape=" + blockShape +
                '}';
    }
}
