package Hilligans.Block;

import Hilligans.Block.BlockTypes.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Blocks {

    public static final HashMap<String, Block> MAPPED_BLOCKS = new HashMap<>();
    public static final ArrayList<Block> BLOCKS = new ArrayList<>();

    public static final Block AIR = new Block("air").transparentTexture(true);
    public static final Block STONE = new Block("stone").withTexture("stone.png");

    public static final Block DIRT = new Block("dirt").withTexture("dirt.png");
    public static final Block GRASS = new Block("grass").withTexture("grass_side.png").withSidedTexture("grass_block.png",Block.UP).withSidedTexture("dirt.png",Block.DOWN).setBlockDrop(Blocks.DIRT);
    public static final Block BEDROCK = new Block("bedrock").withTexture("bedrock.png");
    public static final Block IRON_ORE = new Block("iron_ore").withTexture("swag_ore.png");
    public static final Block LEAVES = new Block("leaves").withTexture("leaves.png").transparentTexture(true).setBlockDrop(Blocks.AIR);
    public static final Block LOG = new Block("log").withTexture("log.png").withSidedTexture("log_top.png",Block.UP).withSidedTexture("log_top.png",Block.DOWN);
    public static final Block SAND = new Block("sand").withTexture("sand.png");
    public static final Block CACTUS = new Block("cactus").withTexture("cactus.png");

    public static final Block SLAB = new SlabBlock("slab").withTexture("dirt.png");

    //public static final Block TAPE = new Block("tape").withTexture("flex_tape.png");

    public static final Block CHEST = new ChestBlock("chest").withTexture("flex_tape.png");

    public static final Block COLOR_BLOCK = new ColorBlock("color_block").withTexture("white.png");

    public static final Block STAIR_BLOCK = new StairBlock("stair").withTexture("dirt.png");


    public static final Block GRASS_PLANT = new PlantBlock("grass_plant").withTexture("grass_plant.png");
    public static final Block WEEPING_VINE = new WeepingVineBlock("weeping_vine").withTexture("weeping_vine1.png");



    // Tree types
    public static final Block MAPLE_LOG = new Block("maple_log");
    public static final Block MAPLE_PLANKS = new Block("maple_planks");

    public static final Block PINE_LOG = new Block("pine_log");
    public static final Block PINE_PLANKS = new Block("pine_planks");

    public static final Block SPRUCE_LOG = new Block("spruce_log");
    public static final Block SPRUCE_PLANKS = new Block("spruce_planks");

    public static final Block BIRCH_LOG = new Block("birch_log");
    public static final Block BIRCH_PLANKS = new Block("birch_planks");

    public static final Block OAK_LOG = new Block("oak_log");
    public static final Block OAK_PLANKS = new Block("oak_planks");

    public static final Block WILLOW_LOG = new Block("willow_log");
    public static final Block WILLOW_PLANKS = new Block("willow_planks");

    public static final Block ACACIA_LOG = new Block("acacia_log");
    public static final Block ACACIA_PLANKS = new Block("acacia_planks");

    public static final Block POPLAR_LOG = new Block("poplar_log");
    public static final Block POPLAR_PLANKS = new Block("poplar_planks");

    public static final Block ELM_LOG = new Block("elm_log");
    public static final Block ELM_WOOD = new Block("elm_wood");

    public static final Block PALM_LOG = new Block("palm_log");
    public static final Block PALM_WOOD = new Block("palm_wood");

    public static final Block REDWOOD_LOG = new Block("redwood_log");
    public static final Block REDWOOD_WOOD = new Block("redwood_wood");



    //public static final Block RED = new Block("red").withTexture("red.png").transparentTexture(true);
    //public static final Block YELLOW = new Block("yellow").withTexture("yellow.png").transparentTexture(true);


    public static ArrayList<Block> serverBlocks = new ArrayList<>();
    public static HashMap<String, Block> mappedServerBlocks = new HashMap<>();

    public static void addBlock(String name, String texture) {
        Block block = new Block(name).withTexture(texture);
        serverBlocks.add(block);
        mappedServerBlocks.put(name,block);
    }

    public static void addBlock(String name, String texture, String[] sidedTextures, int[] sides) {
        Block block = new Block(name).withTexture(texture);
        for(int x = 0; x < sidedTextures.length; x++) {
            block.withSidedTexture(sidedTextures[x],sides[x]);
        }
        serverBlocks.add(block);
        mappedServerBlocks.put(name,block);
    }

    public static void addBlock(String name, String[] sidedTextures, int[] sides) {
        Block block = new Block(name);
        for(int x = 0; x < sidedTextures.length; x++) {
            block.withSidedTexture(sidedTextures[x],sides[x]);
        }
        serverBlocks.add(block);
        mappedServerBlocks.put(name,block);
    }

    public static void clear() {
        serverBlocks = new ArrayList<>();
        mappedServerBlocks = new HashMap<>();
    }


    static short id = 0;

    public static short getNextId() {
        short val = id;
        id++;
        return val;
    }

    public static Block getBlockWithID(int id) {
        return BLOCKS.get(id);
    }

    public static Block getBlock(String id) {
        return MAPPED_BLOCKS.get(id);
    }

    public static void generateTextures() {
        for(Block block : BLOCKS) {
            block.generateTextures();
        }
    }


}
