package dev.hilligans.engine;

import dev.hilligans.engine.application.IApplication;
import dev.hilligans.engine.client.graphics.*;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.api.ITextureConverter;
import dev.hilligans.engine.gametype.IGameType;
import dev.hilligans.engine.schema.Schema;
import dev.hilligans.engine.template.ITemplate;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.BlockStateBuilder;
import dev.hilligans.ourcraft.block.blockstate.BlockStateTable;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.blockstate.IBlockStateTable;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.ourcraft.client.audio.SoundCategory;
import dev.hilligans.engine.client.input.Input;
import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.engine.client.graphics.util.Texture;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayoutEngine;
import dev.hilligans.engine.command.ICommand;
import dev.hilligans.ourcraft.data.descriptors.Tag;
import dev.hilligans.engine.entity.EntityType;
import dev.hilligans.ourcraft.item.Item;
import dev.hilligans.ourcraft.item.data.ToolLevelList;
import dev.hilligans.engine.mod.handler.EventBus;
import dev.hilligans.engine.mod.handler.content.ContentPack;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.ModList;
import dev.hilligans.engine.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.INetworkEngine;
import dev.hilligans.engine.resource.IBufferAllocator;
import dev.hilligans.engine.resource.ResourceLocation;
import dev.hilligans.engine.resource.ResourceManager;
import dev.hilligans.engine.resource.UniversalResourceLoader;
import dev.hilligans.engine.resource.dataloader.DataLoader;
import dev.hilligans.engine.resource.loaders.ResourceLoader;
import dev.hilligans.engine.authentication.IAuthenticationScheme;
import dev.hilligans.ourcraft.settings.Setting;
import dev.hilligans.engine.test.ITest;
import dev.hilligans.engine.util.Logger;
import dev.hilligans.engine.util.Side;
import dev.hilligans.engine.util.ThreadProvider;
import dev.hilligans.engine.util.argument.ArgumentContainer;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GameInstance {
    public final EventBus EVENT_BUS = new EventBus();
    public final Logger LOGGER = new Logger("", "");
    public final ResourceManager RESOURCE_MANAGER = new ResourceManager(this);
    public final ContentPack CONTENT_PACK = new ContentPack(this);
    public final ModList MOD_LIST = new ModList(this);
    public final UniversalResourceLoader RESOURCE_LOADER = new UniversalResourceLoader();
    public final ArgumentContainer ARGUMENTS;
    public final DataLoader DATA_LOADER = new DataLoader(this);
    public final ThreadProvider THREAD_PROVIDER = new ThreadProvider(this);
    public Side side;

    public final int gameInstanceUniversalID;

    public Semaphore builtSemaphore = new Semaphore(1);

    public InstanceLoaderPipeline<?> loaderPipeline;

    public final ToolLevelList MATERIAL_LIST = new ToolLevelList();

    public GameInstance() {
        this(new ArgumentContainer());
    }

    public GameInstance(ArgumentContainer argumentContainer) {
        this.ARGUMENTS = argumentContainer;
        this.gameInstanceUniversalID = getNewGameInstanceUniversalID();
        this.REGISTRIES = new Registry<>(this, Registry.class, "registry");
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

    public void cleanup() {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            for(IRegistryElement element : registry.ELEMENTS) {
                element.cleanup();
            }
        }
    }

    public String path = System.getProperty("user.dir");

    public final Registry<Registry<?>> REGISTRIES;

    public Registry<Block> BLOCKS;
    public Registry<Tag> TAGS;
    public Registry<IGraphicsEngine<?,?,?>> GRAPHICS_ENGINES;
    public Registry<Protocol> PROTOCOLS;
    public Registry<Setting> SETTINGS;
    public Registry<ResourceLoader<?>> RESOURCE_LOADERS;
    public Registry<SoundBuffer> SOUNDS;
    public Registry<SoundCategory> SOUND_CATEGORIES;
    public Registry<RenderTarget> RENDER_TARGETS;
    public Registry<RenderPipeline> RENDER_PIPELINES;
    public Registry<RenderTaskSource> RENDER_TASK;
    public Registry<Input> KEY_BINDS;
    public Registry<VertexFormat> VERTEX_FORMATS;
    public Registry<InputHandlerProvider> INPUT_HANDLER_PROVIDERS;
    public Registry<Texture> TEXTURES;
    public Registry<ShaderSource> SHADERS;
    public Registry<ILayoutEngine<?>> LAYOUT_ENGINES;
    public Registry<EntityType> ENTITY_TYPES;
    public Registry<INetworkEngine<?, ?>> NETWORK_ENGINES;
    public Registry<ICommand> COMMANDS;
    public Registry<IAuthenticationScheme<?>> AUTHENTICATION_SCHEMES;
    public Registry<ITest> TESTS;
    public Registry<IApplication> APPLICATIONS;
    public Registry<ITextureConverter> TEXTURE_CONVERTERS;
    public Registry<Schema<?>> SCHEMAS;
    public Registry<ITemplate<?>> TEMPLATES;
    public Registry<IGameType> GAME_TYPES;

    public ArrayList<IBlockState> BLOCK_STATES;

    public void copyRegistries() {
        BLOCKS = (Registry<Block>) REGISTRIES.getExcept("ourcraft:block");
        TAGS = (Registry<Tag>) REGISTRIES.getExcept("ourcraft:tag");
        GRAPHICS_ENGINES = (Registry<IGraphicsEngine<?, ?, ?>>) REGISTRIES.getExcept(Engine.name("graphics_engine"));
        PROTOCOLS = (Registry<Protocol>) REGISTRIES.getExcept(Engine.name("protocol"));
        SETTINGS = (Registry<Setting>) REGISTRIES.getExcept("ourcraft:setting");
        RESOURCE_LOADERS = (Registry<ResourceLoader<?>>) REGISTRIES.getExcept(Engine.name("resource_loader"));
        SOUNDS = (Registry<SoundBuffer>) REGISTRIES.getExcept("ourcraft:sound");
        RENDER_TARGETS = (Registry<RenderTarget>) REGISTRIES.getExcept(Engine.name("render_target"));
        RENDER_PIPELINES = (Registry<RenderPipeline>) REGISTRIES.getExcept(Engine.name("render_pipeline"));
        RENDER_TASK = (Registry<RenderTaskSource>) REGISTRIES.getExcept(Engine.name("render_task"));
        KEY_BINDS = (Registry<Input>) REGISTRIES.getExcept(Engine.name("key_bind"));
        VERTEX_FORMATS = (Registry<VertexFormat>) REGISTRIES.getExcept(Engine.name("vertex_format"));
        INPUT_HANDLER_PROVIDERS = (Registry<InputHandlerProvider>) REGISTRIES.getExcept(Engine.name("input"));
        TEXTURES = (Registry<Texture>) REGISTRIES.getExcept(Engine.name("texture"));
        SHADERS = (Registry<ShaderSource>) REGISTRIES.getExcept(Engine.name("shader"));
        LAYOUT_ENGINES = (Registry<ILayoutEngine<?>>) REGISTRIES.getExcept(Engine.name("layout_engine"));
        SOUND_CATEGORIES = (Registry<SoundCategory>) REGISTRIES.getExcept("ourcraft:sound_category");
        ENTITY_TYPES = (Registry<EntityType>) REGISTRIES.getExcept("ourcraft:entity_type");
        NETWORK_ENGINES = (Registry<INetworkEngine<?,?>>) REGISTRIES.getExcept(Engine.name("network_engine"));
        COMMANDS = (Registry<ICommand>) REGISTRIES.getExcept(Engine.name("command"));
        AUTHENTICATION_SCHEMES = (Registry<IAuthenticationScheme<?>>) REGISTRIES.getExcept(Engine.name("authentication_scheme"));
        TESTS = (Registry<ITest>) REGISTRIES.getExcept(Engine.name("test"));
        APPLICATIONS = (Registry<IApplication>) REGISTRIES.getExcept(Engine.name("application"));
        TEXTURE_CONVERTERS = (Registry<ITextureConverter>) REGISTRIES.getExcept(Engine.name("texture_converter"));
        SCHEMAS = (Registry<Schema<?>>) REGISTRIES.getExcept(Engine.name("schema"));
        TEMPLATES = (Registry<ITemplate<?>>) REGISTRIES.getExcept(Engine.name("template"));
        GAME_TYPES = (Registry<IGameType>) REGISTRIES.getExcept(Engine.name("game_type"));
    }

    public void finishBuild() {
        for (Registry<?> registry : REGISTRIES.ELEMENTS) {
            for (Object o : registry.ELEMENTS) {
                if (o instanceof IRegistryElement element) {
                    //TODO figure out why this is needed
                    element.preLoad(this);
                    element.load(this);
                }
            }
        }
        buildBlockStates();
    }

    public Side getSide() {
        return side;
    }

    public ArgumentContainer getArgumentContainer() {
        return ARGUMENTS;
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

    public <T> T get(String name, Class<T> registryClass) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            if(registry.classType == registryClass) {
                return (T) registry.get(name);
            }
        }
        return null;
    }

    public <T> T getResource(String resource, Class<T> clazz) {
        return RESOURCE_LOADER.getResource(resource, clazz);
    }

    public <T> T getResource(ResourceLocation resourceLocation, Class<T> clazz) {
        return RESOURCE_LOADER.getResource(resourceLocation, clazz);
    }

    public <T> T getExcept(String name, Class<T> registryClass) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            if(registry.classType == registryClass) {
                return (T) registry.getExcept(name);
            }
        }
        throw new RuntimeException("Unknown registry " + registryClass);
        //throw new RuntimeException(STR."Unknown registry \{registryClass}");
    }

    public <T extends IRegistryElement> Registry<T> getRegistry(String registryKey, Class<T> registryClass) {
        return (Registry<T>) REGISTRIES.get(registryKey);
    }

    public <T extends IRegistryElement> Registry<T> getRegistry(Class<T> registryClass) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            if(registry.classType == registryClass) {
                return (Registry<T>) registry;
            }
        }
        return null;
    }

    public Item getItem(String name) {
        Registry<Item> ITEMS = getRegistry(Engine.name("item"), Item.class);
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

    public Schema<?> getSchema(Object object) {
        for (Schema<?> schema : SCHEMAS.ELEMENTS) {
            if (schema.getSchemaClass() == object.getClass()) {
                return schema;
            }
        }

        return null;
    }

    public int getUniqueID() {
        return gameInstanceUniversalID;
    }

    public ModContainer getMod(String modID) {
        return MOD_LIST.getMod(modID);
    }

    private static volatile int staticGameInstanceUniversalID = 0;

    public static synchronized int getNewGameInstanceUniversalID() {
        return staticGameInstanceUniversalID++;
    }
}
