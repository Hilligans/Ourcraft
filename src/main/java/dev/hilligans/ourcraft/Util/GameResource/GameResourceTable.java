package dev.hilligans.ourcraft.Util.GameResource;


import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Instance.TagCache;
import dev.hilligans.ourcraft.Item.Item;
import dev.hilligans.ourcraft.World.Feature;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used by jei and bots.
 */
public class GameResourceTable {

    public Long2ObjectOpenHashMap<ArrayList<ResourceRecipe>> RECIPE_MAP = new Long2ObjectOpenHashMap<>();
    public HashMap<String, GameResource> DATA = new HashMap<>();
    public GameInstance gameInstance;
    public TagCache tagCache;
    public long id = 0;

    public void createMap(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.tagCache = new TagCache(gameInstance);
        tagCache.build();

        for(Item item : gameInstance.ITEMS.ELEMENTS) {
            DATA.put(item.getUniqueName(), new ItemGameResource(item).setUniqueID(getId()));
        }

        for(Block block : gameInstance.BLOCKS.ELEMENTS) {
            GameResource blockGameResource = new BlockGameResource(block).setUniqueID(getId());
            DATA.put(block.getUniqueName(),blockGameResource);
            if(block.droppedBlock != Blocks.AIR) {
                ItemGameResource itemGameResource = (ItemGameResource) DATA.get("item" + block.droppedBlock.getUniqueName().substring(5));
                ResourceRecipe build = new ResourceRecipe("build").addInput(itemGameResource).addOutput(blockGameResource);

                ResourceRecipe mine = new ResourceRecipe("mine").addInput(blockGameResource).addOutput(itemGameResource);
                if(block.blockProperties.toolLevel != null && !block.blockProperties.toolLevel.equals("")) {
                    System.out.println(block.getName());
                    mine.addRequirement(new TagGameResource("#ourcraft:tool_level." + block.blockProperties.toolLevel));
                }
                RECIPE_MAP.computeIfAbsent(blockGameResource.uniqueID,a -> new ArrayList<>()).add(build);
                RECIPE_MAP.computeIfAbsent(itemGameResource.uniqueID,a -> new ArrayList<>()).add(mine);
            }
        }

        for(Feature feature : gameInstance.FEATURES.ELEMENTS) {
            DATA.put(feature.getUniqueName(), new FeatureGameResource(feature).setUniqueID(getId()));
            ArrayList<Block> blocks = feature.getBlockList();
            for(Block block : blocks) {
                RECIPE_MAP.computeIfAbsent(DATA.get(block.getUniqueName()).uniqueID, a -> new ArrayList<>()).add(new ResourceRecipe("place_feature").addOutput(new BlockGameResource(block)));
            }
        }
    }

    public long getId() {
        return id++;
    }

    static class ResourceRecipe {

        public ArrayList<GameResource> input = new ArrayList<>();
        public ArrayList<GameResource> requires = new ArrayList<>();

        public ArrayList<GameResource> output = new ArrayList<>();
        public String action;

        public ResourceRecipe(String action) {
            this.action = action;
        }

        public ResourceRecipe addInput(GameResource gameResource) {
            input.add(gameResource);
            return this;
        }

        public ResourceRecipe addRequirement(GameResource gameResource) {
            requires.add(gameResource);
            return this;
        }

        public ResourceRecipe addOutput(GameResource gameResource) {
            output.add(gameResource);
            return this;
        }


        @Override
        public String toString() {
            return "ResourceRecipe{" +
                    "input=" + input +
                    ", requires=" + requires +
                    ", output=" + output +
                    ", action='" + action + '\'' +
                    '}';
        }
    }
}
