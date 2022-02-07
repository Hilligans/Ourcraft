package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Biome.Biomes;
import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Client.Audio.Sounds;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngine;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Item.Data.ToolLevel;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Identifier;
import dev.Hilligans.ourcraft.Network.Protocols;
import dev.Hilligans.ourcraft.Resource.RegistryLoaders.JsonRegistryLoader;
import dev.Hilligans.ourcraft.Resource.Loaders.ImageLoader;
import dev.Hilligans.ourcraft.Resource.Loaders.JsonLoader;
import dev.Hilligans.ourcraft.Resource.ResourceManager;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.Hilligans.ourcraft.Util.Side;
import org.json.JSONArray;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.Hilligans.ourcraft.Block.Blocks.*;

public class Ourcraft {

    public static final GameInstance GAME_INSTANCE = new GameInstance();
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2,new NamedThreadFactory("random_executor"));

    public static String path = System.getProperty("user.dir");


    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }

    public static synchronized ResourceManager getResourceManager() {
        return GAME_INSTANCE.RESOURCE_MANAGER;
    }

    public static File getFile(String path) {
        return new File(path + "/" + path);
    }

    public static void registerDefaultContent(ModContent modContent) {
        modContent.registerResourceLoader(new JsonLoader(), new ImageLoader());

        modContent.registerBlocks(AIR,STONE,DIRT,GRASS,BEDROCK,IRON_ORE,LEAVES,LOG,SAND,CACTUS,CHEST,COLOR_BLOCK,STAIR_BLOCK,GRASS_PLANT,WEEPING_VINE,MAPLE_LOG,MAPLE_PLANKS,PINE_LOG,PINE_PLANKS,SPRUCE_LOG,SPRUCE_PLANKS,BIRCH_LOG,BIRCH_PLANKS,OAK_LOG,OAK_PLANKS,WILLOW_LOG,WILLOW_PLANKS,ACACIA_LOG,ACACIA_PLANKS,POPLAR_LOG,POPLAR_PLANKS,ELM_LOG,ELM_WOOD,PALM_LOG,PALM_WOOD,REDWOOD_LOG,REDWOOD_WOOD,SAPLING);
        modContent.registerBiome(Biomes.PLAINS,Biomes.SANDY_HILLS,Biomes.DESERT,Biomes.FOREST);
        Sounds.reg();
        modContent.registerSounds(Sounds.BLOCK_BREAK, Sounds.MUSIC);
        modContent.registerTexture(Textures.TEXTURES.toArray(new Texture[0]));
        Protocols.register(modContent);

        modContent.registerRegistryLoader(new JsonRegistryLoader(new Identifier("tool_tiers", "ourcraft"), "Data/ToolTiers.json", (modContent1, jsonObject, string) -> {
            JSONArray elements = jsonObject.getJSONArray("material");
            ToolLevel[] levels = new ToolLevel[elements.length()];
            for(int x = 0; x < elements.length(); x++) {
                levels[x] = new ToolLevel(new Identifier(elements.getString(x), modContent1.modID));
            }
            String[] parts = jsonObject.getString("location").split(" ");
            switch (parts[0]) {
                case "start" -> modContent1.gameInstance.MATERIAL_LIST.addToStart(levels);
                case "end" -> modContent1.gameInstance.MATERIAL_LIST.addToEnd(levels);
                case "before" -> modContent1.gameInstance.MATERIAL_LIST.insertBefore(parts[1], levels);
                case "after" -> modContent1.gameInstance.MATERIAL_LIST.insertAfter(parts[1], levels);
            }
            modContent.gameInstance.registerToolLevels(levels);
        }).rerunOnInstanceClear());

        modContent.registerRegistryLoader(new JsonRegistryLoader(new Identifier("blocks", "ourcraft"), "Data/Blocks.json", (modContent12, jsonObject, key) -> {
            try {
                Block block = new Block(key, "Data/" + jsonObject.optString("data"), jsonObject.optJSONObject("overrides"));
                JSONArray textures = jsonObject.getJSONArray("textures");
                for (int x = 0; x < textures.length(); x++) {
                    block.blockProperties.addTexture(textures.getString(x), x, textures.length());
                }
                modContent12.registerBlock(block);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));


        modContent.registerRegistryLoader(new JsonRegistryLoader(new Identifier("screens", "ourcraft"), "Data/Screens.json", (modContent12, jsonObject, key) -> {
            modContent12.registerScreenBuilder(new ScreenBuilder(key, jsonObject));
        }).rerunOnInstanceClear());

        if(modContent.gameInstance.side.equals(Side.CLIENT)) {
            modContent.registerGraphicsEngine(new VulkanEngine());
            System.out.println(modContent.graphicsEngines.get(0).getUniqueName());
        }

        Ourcraft.getResourceManager().gameInstance = modContent.gameInstance;
    }


}
