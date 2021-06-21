package Hilligans.Data.Other;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Item.BlockItem;
import Hilligans.Item.Item;
import Hilligans.ModHandler.Content.ModContent;
import org.json.JSONObject;

public class ItemProperties {

    public boolean serverSide = false;
    public String block;

    public ItemProperties serverSide(boolean val) {
        serverSide = val;
        return this;
    }

    public ItemProperties block(String block) {
        this.block = block;
        return this;
    }

    public static ItemProperties loadProperties(JSONObject jsonObject) {
        ItemProperties itemProperties = new ItemProperties();
        if(jsonObject.has("properties")) {
            JSONObject properties = jsonObject.getJSONObject("properties");
            if(properties.has("block")) {
                itemProperties.block = properties.getString("block");
            }
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
            return new BlockItem(name, Blocks.MAPPED_BLOCKS.get(block),"ourcraft");
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
