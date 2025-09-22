package dev.hilligans.ourcraft.recipe.helper;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.util.registry.Registry;
import dev.hilligans.ourcraft.data.descriptors.Tag;
import dev.hilligans.ourcraft.data.descriptors.TagCollection;
import dev.hilligans.ourcraft.item.Item;

import java.util.ArrayList;

public class RecipeHelper  {


    public GameInstance gameInstance;
    public ArrayList<Item> itemCache;


    public RecipeHelper(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void updateCache(String filterKey) {
        itemCache = getItems(filterKey);
    }

    public ArrayList<Item> getItems(String filterKey) {
        DescriptorList[] descriptors = createDescriptors(filterKey);
        ArrayList<Item> items = new ArrayList<>();
        Registry<Item> ITEMS = gameInstance.getRegistry("ourcraft:item", Item.class);
        for(Item item : ITEMS.ELEMENTS) {
            for(DescriptorList descriptor : descriptors) {
                if(descriptor.matches(item)) {
                    items.add(item);
                }
            }
        }
        return items;
    }


    public DescriptorList[] createDescriptors(String filterKey) {
        String[] descriptorParts = filterKey.split("\\|");
        DescriptorList[] descriptors = new DescriptorList[descriptorParts.length];

        int x = 0;
        for(String stringDescriptor : descriptorParts) {
            String[] parts = stringDescriptor.split(" ");
            DescriptorList descriptor = new DescriptorList();
            descriptors[x++] = descriptor;
            for(String part : parts) {
                if(part.length() > 0) {
                    char startChar = part.charAt(0);
                    switch (startChar) {
                        case '@':
                            descriptor.modID.add(part.substring(1));;
                            break;
                        case '#':
                            break;
                        case '$':
                            descriptor.tagCollection.put(new Tag("item",part.substring(1), "ourcraft"));
                            break;
                        default:
                            descriptor.names.add(part);
                            break;
                    }
                }
            }
        }
        return descriptors;
    }

    static class DescriptorList {
        public ArrayList<String> names = new ArrayList<>();
        public ArrayList<String> modID = new ArrayList<>();
        public TagCollection tagCollection = new TagCollection();

        public boolean matches(Item item) {
            for(String name : names) {
                if(!item.name.contains(name)) {
                    return false;
                }
            }
            for(String modID : modID) {
                if(!item.modID.contains(modID)) {
                    return false;
                }
            }
            if(!item.itemProperties.tags.compare(tagCollection)) {
                return false;
            }
            return true;
        }
    }
}
