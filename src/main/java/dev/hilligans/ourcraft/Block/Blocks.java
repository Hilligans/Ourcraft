package dev.hilligans.ourcraft.Block;

import dev.hilligans.ourcraft.Block.BlockTypes.*;
import dev.hilligans.ourcraft.Data.Other.BlockProperties;
import dev.hilligans.ourcraft.Ourcraft;

public class Blocks {

    public static final Block AIR = new Block("air", new BlockProperties().transparent().airBlock().canWalkThrough());
    public static final Block STONE = new Block("stone",new BlockProperties().withTexture("stone.png"));

    public static final Block RED = new Block("red", new BlockProperties().withTexture("red.png").translucent().transparent());
    public static final Block DIRT = new Block("dirt",new BlockProperties().withTexture("dirt.png"));
    public static final Block GRASS = new Block("grass", new BlockProperties().withTexture("grass_side.png").withSidedTexture("grass_block.png",Block.UP).withSidedTexture("dirt.png",Block.DOWN)).setBlockDrop(Blocks.DIRT);
    public static final Block BEDROCK = new Block( "bedrock",new BlockProperties().withTexture("bedrock.png"));
    public static final Block IRON_ORE = new Block("iron_ore",new BlockProperties().withTexture("swag_ore.png"));
    public static final Block LEAVES = new Block("leaves",new BlockProperties().withTexture("leaves.png").transparent()).setBlockDrop(Blocks.AIR);
    public static final Block LOG = new Block("log", new BlockProperties().withTexture("log.png").withSidedTexture("log_top.png",Block.UP).withSidedTexture("log_top.png",Block.DOWN));
    public static final Block SAND = new Block("sand",new BlockProperties().withTexture("sand.png"));
    public static final Block CACTUS = new Block("cactus",new BlockProperties().withTexture("cactus.png"));

    public static final Block CHEST = new ChestBlock("chest",new BlockProperties().withTexture("flex_tape.png"));

    public static final Block COLOR_BLOCK = new ColorBlock("color_block", new BlockProperties().withTexture("white.png"));

    public static final Block STAIR_BLOCK = new StairBlock("stair", new BlockProperties().withTexture("dirt.png"));


    public static final Block GRASS_PLANT = new PlantBlock("grass_plant", new BlockProperties().withTexture("grass_plant.png"));
    public static final Block WEEPING_VINE = new WeepingVineBlock("weeping_vine" ,new BlockProperties().withTexture("weeping_vine1.png"));

    // Tree types
    public static final Block MAPLE_LOG = new Block("maple_log", new BlockProperties());
    public static final Block MAPLE_PLANKS = new Block("maple_planks", new BlockProperties());

    public static final Block PINE_LOG = new Block("pine_log", new BlockProperties());
    public static final Block PINE_PLANKS = new Block("pine_planks", new BlockProperties());

    public static final Block SPRUCE_LOG = new Block("spruce_log", new BlockProperties());
    public static final Block SPRUCE_PLANKS = new Block("spruce_planks", new BlockProperties());

    public static final Block BIRCH_LOG = new Block("birch_log", new BlockProperties());
    public static final Block BIRCH_PLANKS = new Block("birch_planks", new BlockProperties());

    public static final Block OAK_LOG = new Block("oak_log", new BlockProperties());
    public static final Block OAK_PLANKS = new Block("oak_planks", new BlockProperties());

    public static final Block WILLOW_LOG = new Block("willow_log", new BlockProperties());
    public static final Block WILLOW_PLANKS = new Block("willow_planks", new BlockProperties());

    public static final Block ACACIA_LOG = new Block("acacia_log", new BlockProperties());
    public static final Block ACACIA_PLANKS = new Block("acacia_planks", new BlockProperties());

    public static final Block POPLAR_LOG = new Block("poplar_log", new BlockProperties());
    public static final Block POPLAR_PLANKS = new Block("poplar_planks", new BlockProperties());

    public static final Block ELM_LOG = new Block("elm_log", new BlockProperties());
    public static final Block ELM_WOOD = new Block("elm_wood", new BlockProperties());

    public static final Block PALM_LOG = new Block("palm_log", new BlockProperties());
    public static final Block PALM_WOOD = new Block("palm_wood", new BlockProperties());

    public static final Block REDWOOD_LOG = new Block("redwood_log", new BlockProperties());
    public static final Block REDWOOD_WOOD = new Block("redwood_wood", new BlockProperties());

    public static final Block SAPLING = new OakSapling("oak_sapling",new BlockProperties());

    public static final Block WATER = new Block("water", new BlockProperties().withTexture("water.png").translucent());

    public static Block getBlockWithID(int id) {
        return Ourcraft.GAME_INSTANCE.BLOCKS.get(id);
    }

}
