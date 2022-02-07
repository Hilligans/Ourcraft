package dev.Hilligans.ourcraft.Instance;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Data.Descriptors.Tag;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

import java.util.ArrayList;
import java.util.HashMap;

public class TagCache {

    public HashMap<String, ArrayList<IRegistryElement>> cache = new HashMap<>();

    public GameInstance gameInstance;

    public TagCache(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void build() {
        for(Tag tag : gameInstance.TAGS.ELEMENTS) {
            ArrayList<IRegistryElement> registryElements = new ArrayList<>();
            cache.put(tag.asString(), registryElements);
            for(Item item : gameInstance.ITEMS.ELEMENTS) {
                if(item.getTagCollection().contains(tag)) {
                    registryElements.add(item);
                }
            }
            for(Block block : gameInstance.BLOCKS.ELEMENTS) {
                if(block.getTagCollection().contains(tag)) {
                    registryElements.add(block);
                }
            }
        }
    }



}
