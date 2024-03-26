package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.biome.Biome;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.BlockStateBuilder;
import dev.hilligans.ourcraft.block.blockstate.BlockStateTable;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.blockstate.IBlockStateTable;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.ourcraft.client.audio.SoundCategory;
import dev.hilligans.ourcraft.client.input.Input;
import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsElement;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.ourcraft.client.rendering.Texture;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;
import dev.hilligans.ourcraft.command.CommandHandler;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.data.descriptors.Tag;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.item.data.ToolLevel;
import dev.hilligans.ourcraft.item.data.ToolLevelList;
import dev.hilligans.ourcraft.item.Item;
import dev.hilligans.ourcraft.mod.handler.content.ContentPack;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.mod.handler.EventBus;
import dev.hilligans.ourcraft.mod.handler.content.ModList;
import dev.hilligans.ourcraft.mod.handler.events.common.RegistryClearEvent;
import dev.hilligans.ourcraft.mod.handler.ModLoader;
import dev.hilligans.ourcraft.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.recipe.helper.RecipeView;
import dev.hilligans.ourcraft.resource.dataloader.DataLoader;
import dev.hilligans.ourcraft.resource.IBufferAllocator;
import dev.hilligans.ourcraft.resource.registry.loaders.RegistryLoader;
import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.resource.ResourceLocation;
import dev.hilligans.ourcraft.resource.ResourceManager;
import dev.hilligans.ourcraft.resource.UniversalResourceLoader;
import dev.hilligans.ourcraft.settings.Setting;
import dev.hilligans.ourcraft.util.*;
import dev.hilligans.ourcraft.recipe.IRecipe;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.registry.Registry;
import dev.hilligans.ourcraft.world.Feature;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class GameInstance {
    public final EventBus EVENT_BUS = new EventBus();
    public final ModLoader MOD_LOADER = new ModLoader(this);
    public final Logger LOGGER = new Logger("", "");
    public final ResourceManager RESOURCE_MANAGER = new ResourceManager(this);
    public final ModContent OURCRAFT = new ModContent("ourcraft",this).addClassLoader(new URLClassLoader(new URL[]{Ourcraft.class.getProtectionDomain().getCodeSource().getLocation()})).addMainClass(Ourcraft.class);
    public final ContentPack CONTENT_PACK = new ContentPack(this);
    public final ModList MOD_LIST = new ModList(this);
    public final UniversalResourceLoader RESOURCE_LOADER = new UniversalResourceLoader();
    public final ArgumentContainer ARGUMENTS = new ArgumentContainer();
    public final DataLoader DATA_LOADER = new DataLoader();
    public final ThreadProvider THREAD_PROVIDER = new ThreadProvider(this);
    //public final
    public Side side;

    public final int gameInstanceUniversalID;

    public Semaphore builtSemaphore = new Semaphore(1);

    public InstanceLoaderPipeline<?> loaderPipeline;

    public final ToolLevelList MATERIAL_LIST = new ToolLevelList();

    public GameInstance() {
        gameInstanceUniversalID = getNewGameInstanceUniversalID();
    }

    public void loadContent() {
        //registerDefaultContent();
        //CONTENT_PACK.registerModContent(OURCRAFT);
        //CONTENT_PACK.mods.put("ourcraft",OURCRAFT);
        //MOD_LOADER.loadDefaultMods();
        //CONTENT_PACK.buildVital();
        //CONTENT_PACK.mods.forEach((s, modContent) -> modContent.invokeRegistryLoaders());
        //REBUILDING.set(true);
        //CONTENT_PACK.generateData();
        //buildBlockStates();
        //REBUILDING.set(false);
    }

    public void build(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            for(Object o : registry.ELEMENTS) {
                if(o instanceof IGraphicsElement graphicsElement) {
                    graphicsElement.load(this, graphicsEngine, graphicsContext);
                }
            }
        }
    }

    public void cleanupGraphics(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            for(Object o : registry.ELEMENTS) {
                if(o instanceof IGraphicsElement graphicsElement) {
                    graphicsElement.cleanup(this, graphicsEngine, graphicsContext);
                }
            }
        }
    }

    public String path = System.getProperty("user.dir");

    public final Registry<Registry<?>> REGISTRIES = new Registry<>(this, Registry.class, "registry");

    public Registry<Block> BLOCKS;
    public Registry<Item> ITEMS;
    public Registry<Biome> BIOMES;
    public Registry<Tag> TAGS;
    public Registry<IRecipe<?>> RECIPES;
    public Registry<RecipeView<?>> RECIPE_VIEWS;
    public Registry<IGraphicsEngine<?,?,?>> GRAPHICS_ENGINES;
    public Registry<CommandHandler> COMMANDS;
    public Registry<Protocol> PROTOCOLS;
    public Registry<Setting> SETTINGS;
    public Registry<ResourceLoader<?>> RESOURCE_LOADERS;
    public Registry<SoundBuffer> SOUNDS;
    public Registry<SoundCategory> SOUND_CATEGORIES;
    public Registry<ToolLevel> TOOL_MATERIALS;
    public Registry<RegistryLoader> DATA_LOADERS;
    public Registry<ScreenBuilder> SCREEN_BUILDERS;
    public Registry<Feature> FEATURES;
    public Registry<RenderTarget> RENDER_TARGETS;
    public Registry<RenderPipeline> RENDER_PIPELINES;
    public Registry<RenderTaskSource> RENDER_TASK;
    public Registry<Input> KEY_BINDS;
    public Registry<VertexFormat> VERTEX_FORMATS;
    public Registry<InputHandlerProvider> INPUT_HANDLER_PROVIDERS;
    public Registry<Texture> TEXTURES;
    public Registry<ShaderSource> SHADERS;
    public Registry<ILayoutEngine<?>> LAYOUT_ENGINES;

    public ArrayList<IBlockState> BLOCK_STATES;

    public void copyRegistries() {
        BLOCKS = (Registry<Block>) REGISTRIES.getExcept("ourcraft:block");
        ITEMS = (Registry<Item>) REGISTRIES.getExcept("ourcraft:item");
        BIOMES = (Registry<Biome>) REGISTRIES.getExcept("ourcraft:biome");
        TAGS = (Registry<Tag>) REGISTRIES.getExcept("ourcraft:tag");
        RECIPES = (Registry<IRecipe<?>>) REGISTRIES.getExcept("ourcraft:recipe");
        RECIPE_VIEWS = (Registry<RecipeView<?>>) REGISTRIES.getExcept("ourcraft:recipe_view");
        GRAPHICS_ENGINES = (Registry<IGraphicsEngine<?, ?, ?>>) REGISTRIES.getExcept("ourcraft:graphics_engine");
        COMMANDS = (Registry<CommandHandler>) REGISTRIES.getExcept("ourcraft:command");
        PROTOCOLS = (Registry<Protocol>) REGISTRIES.getExcept("ourcraft:protocol");
        SETTINGS = (Registry<Setting>) REGISTRIES.getExcept("ourcraft:setting");
        RESOURCE_LOADERS = (Registry<ResourceLoader<?>>) REGISTRIES.getExcept("ourcraft:resource_loader");
        SOUNDS = (Registry<SoundBuffer>) REGISTRIES.getExcept("ourcraft:sound");
        TOOL_MATERIALS = (Registry<ToolLevel>) REGISTRIES.getExcept("ourcraft:tool_level");
        DATA_LOADERS = (Registry<RegistryLoader>) REGISTRIES.getExcept("ourcraft:registry_loader");
        SCREEN_BUILDERS = (Registry<ScreenBuilder>) REGISTRIES.getExcept("ourcraft:screen");
        FEATURES = (Registry<Feature>) REGISTRIES.getExcept("ourcraft:feature");
        RENDER_TARGETS = (Registry<RenderTarget>) REGISTRIES.getExcept("ourcraft:render_target");
        RENDER_PIPELINES = (Registry<RenderPipeline>) REGISTRIES.getExcept("ourcraft:render_pipeline");
        RENDER_TASK = (Registry<RenderTaskSource>) REGISTRIES.getExcept("ourcraft:render_task");
        KEY_BINDS = (Registry<Input>) REGISTRIES.getExcept("ourcraft:key_bind");
        VERTEX_FORMATS = (Registry<VertexFormat>) REGISTRIES.getExcept("ourcraft:vertex_format");
        INPUT_HANDLER_PROVIDERS = (Registry<InputHandlerProvider>) REGISTRIES.getExcept("ourcraft:input");
        TEXTURES = (Registry<Texture>) REGISTRIES.getExcept("ourcraft:texture");
        SHADERS = (Registry<ShaderSource>) REGISTRIES.getExcept("ourcraft:shader");
        LAYOUT_ENGINES = (Registry<ILayoutEngine<?>>) REGISTRIES.getExcept("ourcraft:layout_engine");
        SOUND_CATEGORIES = (Registry<SoundCategory>) REGISTRIES.getExcept("ourcraft:sound_category");
    }

    public void finishBuild() {
        for (Registry<?> registry : REGISTRIES.ELEMENTS) {
            for (Object o : registry.ELEMENTS) {
                if (o instanceof IRegistryElement) {
                    ((IRegistryElement) o).load(this);
                }
            }
        }
        buildBlockStates();
    }

    public void buildBlockStates() {
        BLOCK_STATES = new ArrayList<>(BLOCKS.ELEMENTS.size());
        int offset = 0;
        for(Block block : getBlocks()) {
            BlockStateBuilder builder = new BlockStateBuilder();
            block.registerBlockStates(builder);
            IBlockStateTable table = new BlockStateTable(BLOCK_STATES,offset);
            block.setTable(table);
            builder.setBlock(block);
            for(int x = 0; x < builder.getSize(); x++) {
                builder.setBlock(block);
                BLOCK_STATES.add(builder.build(x).setBlockStateID(offset));
            }
            offset += builder.getSize();
        }
    }

    public boolean built() {
        return builtSemaphore.availablePermits() == 1;
    }

    public void clear() {
        BLOCKS.clear();
        ITEMS.clear();
        TAGS.clear();
        RECIPES.clear();
        //TODO fix
        PROTOCOLS.clear();
        EVENT_BUS.postEvent(new RegistryClearEvent(this));
    }

    public <T> T get(String name, Class<T> registryClass) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            if(registry.classType == registryClass) {
                return (T) registry.get(name);
            }
        }
        return null;
    }

    public <T> T getExcept(String name, Class<T> registryClass) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            if(registry.classType == registryClass) {
                return (T) registry.getExcept(name);
            }
        }
        throw new RuntimeException(STR."Unknown registry \{registryClass}");
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

    public void registerToolLevels(ToolLevel... toolLevels) {
        for(ToolLevel toolLevel : toolLevels) {
            TOOL_MATERIALS.put(toolLevel.name.getName(), toolLevel);
        }
    }

    public void register(IRegistryElement registryElement) {
        register(registryElement.getIdentifierName(), registryElement);
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

    public boolean replace(String registryName, String resource, IRegistryElement registryElement) {
        Registry<?> registry = REGISTRIES.get(registryName);
        if(registry == null) {
            return false;
        }
        return registry.replace(resource, registryElement);
    }

    public ByteBuffer getResource(ResourceLocation resourceLocation) {
        return DATA_LOADER.get(resourceLocation);
    }

    public ByteBuffer getResourceDirect(ResourceLocation resourceLocation) {
        return DATA_LOADER.getDirect(resourceLocation);
    }

    public ByteBuffer getResource(ResourceLocation resourceLocation, IBufferAllocator allocator) {
        return DATA_LOADER.get(resourceLocation, allocator);
    }

    public void registerDefaultContent() {
     //   Ourcraft.registerDefaultContent(OURCRAFT);
        //Container.register();
        //Entity.register();
    }

    public int getUniqueID() {
        return gameInstanceUniversalID;
    }

    public void handleArgs(String[] args) {
        ARGUMENTS.handle(args);
    }

    private static volatile int staticGameInstanceUniversalID = 0;

    public static synchronized int getNewGameInstanceUniversalID() {
        return staticGameInstanceUniversalID++;
    }
}
