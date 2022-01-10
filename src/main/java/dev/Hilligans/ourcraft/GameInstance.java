package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Biome.Biome;
import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Client.Audio.Sounds;
import dev.Hilligans.ourcraft.Client.Rendering.ClientUtil;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.Widget;
import dev.Hilligans.ourcraft.Command.CommandHandler;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Data.Descriptors.Tag;
import dev.Hilligans.ourcraft.Entity.Entity;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.Item.Items;
import dev.Hilligans.ourcraft.ModHandler.Content.ContentPack;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.EventBus;
import dev.Hilligans.ourcraft.ModHandler.Events.Common.ProgramArgumentEvent;
import dev.Hilligans.ourcraft.ModHandler.Events.Common.RegistryClearEvent;
import dev.Hilligans.ourcraft.ModHandler.ModLoader;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.Protocol;
import dev.Hilligans.ourcraft.Recipe.RecipeHelper.RecipeView;
import dev.Hilligans.ourcraft.Resource.ResourceLoader;
import dev.Hilligans.ourcraft.Resource.ResourceManager;
import dev.Hilligans.ourcraft.Resource.UniversalResourceLoader;
import dev.Hilligans.ourcraft.Settings.Setting;
import dev.Hilligans.ourcraft.Tag.NBTTag;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import dev.Hilligans.ourcraft.Recipe.IRecipe;
import dev.Hilligans.ourcraft.Util.Registry.Registry;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class GameInstance {

    public final EventBus EVENT_BUS = new EventBus();
    public final ModLoader MOD_LOADER = new ModLoader(this);
    public final Logger LOGGER = Logger.getGlobal();
    public final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2,new NamedThreadFactory("random_executor"));
    public final ResourceManager RESOURCE_MANAGER = new ResourceManager();
    public final ModContent OURCRAFT = new ModContent("ourcraft",this).addClassLoader(new URLClassLoader(new URL[]{Ourcraft.class.getProtectionDomain().getCodeSource().getLocation()})).addMainClass(Ourcraft.class);
    public final ContentPack CONTENT_PACK = new ContentPack(this);
    public final AtomicBoolean REBUILDING = new AtomicBoolean(false);
    public final UniversalResourceLoader RESOURCE_LOADER = new UniversalResourceLoader();


    public GameInstance() {
        REGISTRIES.put("ouracrft:blocks", BLOCKS);
        REGISTRIES.put("ourcraft:items", ITEMS);
        REGISTRIES.put("ourcraft:biomes", BIOMES);
        REGISTRIES.put("ourcraft:tags", TAGS);
        REGISTRIES.put("ourcraft:recipes", RECIPES);
        REGISTRIES.put("ourcraft:recipe_views", RECIPE_VIEWS);
        REGISTRIES.put("ourcraft:graphics_engines", GRAPHICS_ENGINES);
        REGISTRIES.put("ourcraft:settings", SETTINGS);
        REGISTRIES.put("ourcraft:protocols", PROTOCOLS);
        REGISTRIES.put("ourcraft:commands", COMMANDS);
        REGISTRIES.put("ourcraft:resource_loaders", RESOURCE_LOADERS);

    }

    public void loadContent() {
        registerDefaultContent();
        CONTENT_PACK.mods.put("ourcraft",OURCRAFT);
        MOD_LOADER.loadDefaultMods();
    }

    public String path = System.getProperty("user.dir");

    public final Registry<Registry<?>> REGISTRIES = new Registry<>(this);

    public final Registry<Block> BLOCKS = new Registry<>(this, Block.class);
    public final Registry<Item> ITEMS = new Registry<>(this, Item.class);
    public final Registry<Biome> BIOMES = new Registry<>(this, Biome.class);
    public final Registry<Tag> TAGS = new Registry<>(this, Tag.class);
    public final Registry<IRecipe<?>> RECIPES = new Registry<>(this, IRecipe.class);
    public final Registry<RecipeView<?>> RECIPE_VIEWS = new Registry<>(this, RecipeView.class);
    public final Registry<IGraphicsEngine<?,?>> GRAPHICS_ENGINES = new Registry<>(this, IGraphicsEngine.class);
    public final Registry<CommandHandler> COMMANDS = new Registry<>(this, CommandHandler.class);
    public final Registry<Protocol> PROTOCOLS = new Registry<>(this, Protocol.class);
    public final Registry<Setting> SETTINGS = new Registry<>(this, Setting.class);
    public final Registry<ResourceLoader<?>> RESOURCE_LOADERS = new Registry<>(this, ResourceLoader.class);

    public void clear() {
        BLOCKS.clear();
        ITEMS.clear();
        TAGS.clear();
        RECIPES.clear();
        EVENT_BUS.postEvent(new RegistryClearEvent(this));
    }

    public Item getItem(int id) {
        if(ITEMS.ELEMENTS.size() > id) {
            return ITEMS.get(id);
        }
        return null;
    }

    public Item getItem(String name) {
        return ITEMS.MAPPED_ELEMENTS.get(name);
    }

    public Block getBlockWithID(int id) {
        return BLOCKS.get(id);
    }

    public Block getBlock(String id) {
        return BLOCKS.MAPPED_ELEMENTS.get(id);
    }

    public ArrayList<Block> getBlocks() {
        return BLOCKS.ELEMENTS;
    }


    public void registerBlock(Block block) {
        BLOCKS.put(block.getName(),block);
    }

    public void registerBlock(Block... blocks) {
        for(Block block : blocks) {
            registerBlock(block);
        }
    }

    public void registerItem(Item item) {
        ITEMS.put(item.name,item);
    }

    public void registerItems(Item... items) {
        for(Item item : items) {
            registerItem(item);
        }
    }

    public void registerBiome(Biome biome) {
        BIOMES.put(biome.name, biome);
    }

    public void registerBiomes(Biome... biomes) {
        for(Biome biome : biomes) {
            registerBiome(biome);
        }
    }

    public void registerTag(Tag tag) {
        TAGS.put(tag.type + ":" + tag.tagName,tag);
    }

    public void registerRags(Tag... tags) {
        for(Tag tag : tags) {
            registerTag(tag);
        }
    }

    public void registerCommand(CommandHandler commandHandler) {
        COMMANDS.put(commandHandler.getRegistryName(),commandHandler);
    }

    public void registerCommands(CommandHandler... commands) {
        for(CommandHandler commandHandler : commands) {
            registerCommand(commandHandler);
        }
    }

    public void register(String name, Object o) {
        boolean put = false;
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            if(registry.canPut(o)) {
                registry.putUnchecked(name,o);
                put = true;
            }
        }
        if(!put) {
            throw new RuntimeException("failed to put");
        }
    }

    public void register(List<String> names, List<Object> objects) {
        for(int x = 0; x < names.size(); x++) {
            register(names.get(x), objects.get(x));
        }
    }

    public void register(Collection<String> names, Collection<Object> objects) {
        register(names.stream().toList(), objects.stream().toList());
    }

    public void register(String[] names, Object[] objects) {
        for(int x = 0; x < names.length; x++) {
            register(names[x], objects[x]);
        }
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
        Ourcraft.registerDefaultContent(OURCRAFT);
        PacketBase.register();
        ClientUtil.register();
        Container.register();
        NBTTag.register();
        Widget.register();
        Entity.register();
        Blocks.register();
        Sounds.SOUNDS.size();
        Items.register();
    }

    public void handleArgs(String[] args) {
        for(String string : args) {
            EVENT_BUS.postEvent(new ProgramArgumentEvent(string));
        }
    }

}
