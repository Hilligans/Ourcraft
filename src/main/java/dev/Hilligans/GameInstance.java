package dev.Hilligans;

import dev.Hilligans.Biome.Biome;
import dev.Hilligans.Block.Block;
import dev.Hilligans.Block.Blocks;
import dev.Hilligans.Client.Audio.Sounds;
import dev.Hilligans.Client.Rendering.ClientUtil;
import dev.Hilligans.Client.Rendering.Widgets.Widget;
import dev.Hilligans.Container.Container;
import dev.Hilligans.Entity.Entity;
import dev.Hilligans.Item.Item;
import dev.Hilligans.Item.Items;
import dev.Hilligans.ModHandler.Content.ContentPack;
import dev.Hilligans.ModHandler.Content.ModContent;
import dev.Hilligans.ModHandler.EventBus;
import dev.Hilligans.ModHandler.ModLoader;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Resource.ResourceManager;
import dev.Hilligans.Tag.Tag;
import dev.Hilligans.Util.NamedThreadFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class GameInstance {

    public final EventBus EVENT_BUS = new EventBus();
    public final ModLoader MOD_LOADER = new ModLoader(this);
    public final Logger LOGGER = Logger.getGlobal();
    public final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2,new NamedThreadFactory("random_executor"));
    public final ResourceManager RESOURCE_MANAGER = new ResourceManager();
    public final ModContent OURCRAFT = new ModContent("ourcraft",this).addClassLoader(new URLClassLoader(new URL[]{Ourcraft.class.getProtectionDomain().getCodeSource().getLocation()})).addMainClass(Ourcraft.class);
    public final ContentPack CONTENT_PACK = new ContentPack(this);
    public final AtomicBoolean REBUILDING = new AtomicBoolean(false);


    public GameInstance() {
    }

    public void loadContent() {
        registerDefaultContent();
        CONTENT_PACK.mods.put("ourcraft",OURCRAFT);
        MOD_LOADER.loadDefaultMods();

    }

    public String path = System.getProperty("user.dir");

    public final ArrayList<Block> BLOCKS = new ArrayList<>();
    public final HashMap<String, Block> MAPPED_BLOCKS = new HashMap<>();
    public final ArrayList<Item> ITEMS = new ArrayList<>();
    public final HashMap<String, Item> MAPPED_ITEMS = new HashMap<>();
    public final ArrayList<Biome> BIOMES = new ArrayList<>();
    public final HashMap<String, Biome> MAPPED_BIOMES = new HashMap<>();


    public void clear() {
        BLOCKS.clear();
        MAPPED_BLOCKS.clear();
        ITEMS.clear();
        MAPPED_ITEMS.clear();
    }

    public Item getItem(int id) {
        if(ITEMS.size() > id) {
            return ITEMS.get(id);
        }
        return null;
    }
    public Block getBlockWithID(int id) {
        return BLOCKS.get(id);
    }

    public Block getBlock(String id) {
        return MAPPED_BLOCKS.get(id);
    }


    public void registerBlock(Block block) {
        MAPPED_BLOCKS.put(block.getName(),block);
        BLOCKS.add(block);
    }
    public void registerBlock(Block... blocks) {
        for(Block block : blocks) {
            registerBlock(block);
        }
    }
    public void registerItem(Item item) {
        ITEMS.add(item);
        MAPPED_ITEMS.put(item.name, item);
    }
    public void registerBiome(Biome biome) {
        BIOMES.add(biome);
        MAPPED_BIOMES.put(biome.name, biome);
    }


    static short itemId = 0;
    public short blockId = 0;

    public static short getNextItemId() {
        short val = itemId;
        itemId++;
        return val;
    }
    public short getNextBlockID() {
        short val = blockId;
        blockId++;
        return val;
    }

    public void registerDefaultContent() {
        PacketBase.register();
        ClientUtil.register();
        Container.register();
        Tag.register();
        Widget.register();
        Entity.register();
        Blocks.register();
        Sounds.SOUNDS.size();
        Items.register();
    }

}
