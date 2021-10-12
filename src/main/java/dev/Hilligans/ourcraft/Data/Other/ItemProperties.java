package dev.Hilligans.ourcraft.Data.Other;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.ItemModel;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.IModel;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ItemTextureManager;
import dev.Hilligans.ourcraft.Item.BlockItem;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Ourcraft;
import org.json.JSONObject;

public class ItemProperties {

    public boolean serverSide = false;
    public boolean dynamicModel = false;
    public String block;
    public IModel itemModel;
    public ItemTextureManager itemTextureManager;

    public ItemProperties serverSide(boolean val) {
        serverSide = val;
        return this;
    }

    public ItemProperties block(String block) {
        this.block = block;
        return this;
    }

    public ItemProperties addModel(String model) {
        itemModel = new ItemModel(model,"");
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
            return new BlockItem(name, Ourcraft.GAME_INSTANCE.MAPPED_BLOCKS.get(block),"ourcraft");
        } else {
            return new Item(name,this);
        }
    }

    ///TODO fix this, very jank
    public Item getItem(String name, ModContent modContent) {
        if(block != null) {

            return new BlockItem(name, modContent.blocks.stream().findFirst().filter(block1 -> block1.name.equals(name)).get(),modContent.modID);
        } else {
            return new Item(name,this);
        }
    }

}
