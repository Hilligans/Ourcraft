package Hilligans.Blocks;

import java.util.ArrayList;
import java.util.HashMap;

public class Blocks {

    public static final HashMap<String, Short> MAPPED_BLOCKS = new HashMap<>();
    public static final ArrayList<Block> BLOCKS = new ArrayList<>();

    public static final Block AIR = new Block("air").transparentTexture(true);
    public static final Block STONE = new Block("stone").withTexture("stone.png");
    public static final Block GRASS = new Block("grass").withTexture("grass_side.png").withSidedTexture("grass_block.png",Block.UP).withSidedTexture("dirt.png",Block.DOWN);
    public static final Block DIRT = new Block("dirt").withTexture("dirt.png");
    public static final Block BEDROCK = new Block("bedrock").withTexture("bedrock.png");
    public static final Block IRON_ORE = new Block("iron_ore").withTexture("swag_ore.png");
    public static final Block PHIL = new Block("phil").withTexture("phil.png");
    public static final Block LEAVES = new Block("leaves").withTexture("leaves.png").transparentTexture(true);
    public static final Block LOG = new Block("log").withTexture("log.png").withSidedTexture("log_top.png",Block.UP).withSidedTexture("log_top.png",Block.DOWN);


    static short id = 0;

    public static short getNextId() {
        short val = id;
        id++;
        return val;
    }

    public static Block getBlockWithID(int id) {
        return BLOCKS.get(id);
    }

    public static void generateTextures() {
        for(Block block : BLOCKS) {
            block.generateTextures();
        }
    }


}
