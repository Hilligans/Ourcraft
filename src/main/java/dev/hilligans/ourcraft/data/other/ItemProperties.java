package dev.hilligans.ourcraft.data.other;

import dev.hilligans.ourcraft.client.rendering.world.managers.ItemTextureManager;
import dev.hilligans.ourcraft.data.descriptors.TagCollection;
import dev.hilligans.ourcraft.item.Item;
import dev.hilligans.engine.mod.handler.content.ModContent;
import org.json.JSONObject;

public class ItemProperties {

    public boolean serverSide = false;
    public boolean dynamicModel = false;
    public String block;
    //public IModel itemModel;
    public ItemTextureManager itemTextureManager;
    public TagCollection tags = new TagCollection();

    public ItemProperties serverSide(boolean val) {
        serverSide = val;
        return this;
    }

    public ItemProperties block(String block) {
        this.block = block;
        return this;
    }

    public ItemProperties addModel(String model) {
        //itemModel = new ItemModel(model,"");
        itemTextureManager = new ItemTextureManager(model);
        return this;
    }

    public ItemProperties dynamicModel() {
        dynamicModel = true;
        return this;
    }

    public static ItemProperties loadProperties(JSONObject jsonObject) {
        ItemProperties itemProperties = new ItemProperties();
        if(jsonObject.has("properties")) {
            JSONObject properties = jsonObject.getJSONObject("properties");
            if(properties.has("block")) {
                itemProperties.block = properties.getString("block");
            }
            itemProperties.dynamicModel = properties.has("dynamicModel") && properties.getBoolean("dynamicModel");
        }
        return itemProperties;
    }

    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        JSONObject properties = new JSONObject();

        jsonObject.put("properties",properties);
        if(block != null) {
            properties.put("block",block);
        }
        return jsonObject;
    }

    public Item getItem(String name) {
        if(block != null) {
            return null;
            // return new BlockItem(name, Ourcraft.GAME_INSTANCE.getBlock(block),"ourcraft");
        } else {
            return new Item(name,this);
        }
    }

    ///TODO fix this, very jank
    public Item getItem(String name, ModContent modContent) {
        if(block != null) {

            return null;
           // return new BlockItem(name, modContent.blocks.stream().findFirst().filter(block1 -> block1.name.equals(name)).get(),modContent.getModID());
        } else {
            return new Item(name,this);
        }
    }

}
