package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.biome.Biome;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.BlockStateBuilder;
import dev.hilligans.ourcraft.block.blockstate.BlockStateTable;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.blockstate.IBlockStateTable;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.ourcraft.client.input.Input;
import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.ourcraft.client.rendering.Texture;
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
import dev.hilligans.ourcraft.mod.handler.events.common.RegistryClearEvent;
import dev.hilligans.ourcraft.mod.handler.ModLoader;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class GameInstance {

    public final EventBus EVENT_BUS = new EventBus();
    public final ModLoader MOD_LOADER = new ModLoader(this);
    public final Logger LOGGER = new Logger("", "");
    public final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2,new NamedThreadFactory("random_executor"));
    public final ResourceManager RESOURCE_MANAGER = new ResourceManager();
    public final ModContent OURCRAFT = new ModContent("ourcraft",this).addClassLoader(new URLClassLoader(new URL[]{Ourcraft.class.getProtectionDomain().getCodeSource().getLocation()})).addMainClass(Ourcraft.class);
    public final ContentPack CONTENT_PACK = new ContentPack(this);
    public final AtomicBoolean REBUILDING = new AtomicBoolean(false);
    public final UniversalResourceLoader RESOURCE_LOADER = new UniversalResourceLoader();
    public final ArgumentContainer ARGUMENTS = new ArgumentContainer();
    public final DataLoader DATA_LOADER = new DataLoader();
    public final ThreadProvider THREAD_PROVIDER = new ThreadProvider(this);
    //public final
    public Side side;

    public int gameInstanceUniversalID = -1;

    public final ToolLevelList MATERIAL_LIST = new ToolLevelList();

    public GameInstance() {
        gameInstanceUniversalID = getNewGameInstanceUniversalID();

        REGISTRIES.put(BLOCKS);
        REGISTRIES.put(ITEMS);
        REGISTRIES.put(BIOMES);
        REGISTRIES.put(TAGS);
        REGISTRIES.put(RECIPES);
        REGISTRIES.put(RECIPE_VIEWS);
        REGISTRIES.put(GRAPHICS_ENGINES);
        REGISTRIES.put(SETTINGS);
        REGISTRIES.put(PROTOCOLS);
        REGISTRIES.put(COMMANDS);
        REGISTRIES.put(RESOURCE_LOADERS);
        REGISTRIES.put(SOUNDS);
        REGISTRIES.put(TOOL_MATERIALS);
        REGISTRIES.put(SCREEN_BUILDERS);
        REGISTRIES.put(FEATURES);
        REGISTRIES.put(RENDER_TARGETS);
        REGISTRIES.put(RENDER_PIPELINES);
        REGISTRIES.put(RENDER_TASK);
        REGISTRIES.put(KEY_BINDS);
        REGISTRIES.put(VERTEX_FORMATS);
        REGISTRIES.put(INPUT_HANDLER_PROVIDERS);
        REGISTRIES.put(TEXTURES);
        REGISTRIES.put(SHADERS);
    }

    public void loadContent() {
        registerDefaultContent();
        CONTENT_PACK.registerModContent(OURCRAFT);
        //CONTENT_PACK.mods.put("ourcraft",OURCRAFT);
        MOD_LOADER.loadDefaultMods();
        CONTENT_PACK.buildVital();
        CONTENT_PACK.mods.forEach((s, modContent) -> modContent.invokeRegistryLoaders());
        REBUILDING.set(true);
        CONTENT_PACK.generateData();
        buildBlockStates();
        REBUILDING.set(false);
    }

    public void build(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            for(Object o : registry.ELEMENTS) {
                if(o instanceof IRegistryElement registryElement) {
                    registryElement.loadGraphics(graphicsEngine, graphicsContext);
                }
            }
        }
        for(RenderPipeline renderPipeline : RENDER_PIPELINES.ELEMENTS) {
            renderPipeline.buildTargets(graphicsEngine);
        }
    }

    public void cleanupGraphics(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            for(Object o : registry.ELEMENTS) {
                if(o instanceof IRegistryElement registryElement) {
                    registryElement.cleanupGraphics(graphicsEngine, graphicsContext);
                }
            }
        }
    }

    public String path = System.getProperty("user.dir");

    public final Registry<Registry<?>> REGISTRIES = new Registry<>(this, Registry.class, "registry");

    public final Registry<Block> BLOCKS = new Registry<>(this, Block.class, "block");
    public final Registry<Item> ITEMS = new Registry<>(this, Item.class, "item");
    public final Registry<Biome> BIOMES = new Registry<>(this, Biome.class, "biome");
    public final Registry<Tag> TAGS = new Registry<>(this, Tag.class, "tag");
    public final Registry<IRecipe<?>> RECIPES = new Registry<>(this, IRecipe.class, "recipe");
    public final Registry<RecipeView<?>> RECIPE_VIEWS = new Registry<>(this, RecipeView.class, "recipe_view");
    public final Registry<IGraphicsEngine<?,?,?>> GRAPHICS_ENGINES = new Registry<>(this, IGraphicsEngine.class, "graphics_engine");
    public final Registry<CommandHandler> COMMANDS = new Registry<>(this, CommandHandler.class, "command");
    public final Registry<Protocol> PROTOCOLS = new Registry<>(this, Protocol.class, "protocol");
    public final Registry<Setting> SETTINGS = new Registry<>(this, Setting.class, "setting");
    public final Registry<ResourceLoader<?>> RESOURCE_LOADERS = new Registry<>(this, ResourceLoader.class, "resource_loader");
    public final Registry<SoundBuffer> SOUNDS = new Registry<>(this, SoundBuffer.class, "sound");
    public final Registry<ToolLevel> TOOL_MATERIALS = new Registry<>(this, ToolLevel.class, "tool_level");
    public final Registry<RegistryLoader> DATA_LOADERS = new Registry<>(this, RegistryLoader.class, "registry_loader");
    public final Registry<ScreenBuilder> SCREEN_BUILDERS = new Registry<>(this, ScreenBuilder.class, "screen");
    public final Registry<Feature> FEATURES = new Registry<>(this, Feature.class, "feature");
    public final Registry<RenderTarget> RENDER_TARGETS = new Registry<>(this, RenderTarget.class, "render_target");
    public final Registry<RenderPipeline> RENDER_PIPELINES = new Registry<>(this, RenderPipeline.class, "render_pipeline");
    public final Registry<RenderTaskSource> RENDER_TASK = new Registry<>(this, RenderTaskSource.class, "render_task");
    public final Registry<Input> KEY_BINDS = new Registry<>(this, Input.class, "key_bind");
    public final Registry<VertexFormat> VERTEX_FORMATS = new Registry<>(this, VertexFormat.class, "vertex_format");
    public final Registry<InputHandlerProvider> INPUT_HANDLER_PROVIDERS = new Registry<>(this, InputHandlerProvider.class, "input");
    public final Registry<Texture> TEXTURES = new Registry<>(this, Texture.class, "texture");
    public final Registry<ShaderSource> SHADERS = new Registry<>(this, ShaderSource.class, "shader");
    public ArrayList<IBlockState> BLOCK_STATES;

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

    public void clear() {
        BLOCKS.clear();
        ITEMS.clear();
        TAGS.clear();
        RECIPES.clear();
        //TODO fix
        PROTOCOLS.clear();
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

    public void registerBlock(Block... blocks) {
        for(Block block : blocks) {
            BLOCKS.put(block.getName(),block);
        }
    }

    public void registerItem(Item... items) {
        for(Item item : items) {
            ITEMS.put(item.name,item);
        }
    }

    public void registerBiome(Biome... biomes) {
        for(Biome biome : biomes) {
            BIOMES.put(biome.name, biome);
        }
    }

    public void registerTag(Tag... tags) {
        for(Tag tag : tags) {
            TAGS.put(tag.type + ":" + tag.tagName,tag);
        }
    }

    public void registerCommand(CommandHandler... commands) {
        for(CommandHandler commandHandler : commands) {
            COMMANDS.put(commandHandler.getIdentifierName(),commandHandler);
        }
    }

    public void registerSound(SoundBuffer... soundBuffers) {
        for(SoundBuffer soundBuffer : soundBuffers) {
            SOUNDS.put(soundBuffer.file, soundBuffer);
        }
    }

    public void registerToolLevels(ToolLevel... toolLevels) {
        for(ToolLevel toolLevel : toolLevels) {
            TOOL_MATERIALS.put(toolLevel.name.getName(), toolLevel);
        }
    }

    public void registerRegistryLoader(RegistryLoader... loaders) {
        for(RegistryLoader loader : loaders) {
            DATA_LOADERS.put(loader.name.getName(),loader);
        }
    }

    public void registerResourceLoader(ResourceLoader<?>... resourceLoaders) {
        for(ResourceLoader<?> resourceLoader : resourceLoaders) {
            RESOURCE_LOADERS.put(resourceLoader.name,resourceLoader);
        }
    }

    public void registerProtocol(Protocol... protocols) {
        for(Protocol protocol : protocols) {
            PROTOCOLS.put(protocol.protocolName,protocol);
        }
    }

    public void registerScreenBuilder(ScreenBuilder... screenBuilders) {
        for(ScreenBuilder screenBuilder : screenBuilders) {
            SCREEN_BUILDERS.put(screenBuilder.getResourceLocation(screenBuilder.modContent).toIdentifier(),screenBuilder);
        }
    }

    public void registerFeature(Feature... features) {
        for(Feature feature : features) {
            FEATURES.put(feature.getIdentifierName(), feature);
        }
    }

    public void registerGraphicsEngine(IGraphicsEngine<?,?,?>... graphicsEngines) {
        for(IGraphicsEngine<?,?,?> graphicsEngine : graphicsEngines) {
            GRAPHICS_ENGINES.put(graphicsEngine.getIdentifierName(), graphicsEngine);
        }
    }

    public void registerRenderTarget(RenderTarget... renderTargets) {
        RENDER_TARGETS.putAll(renderTargets);
    }

    public void registerRenderPipeline(RenderPipeline... renderPipelines) {
        RENDER_PIPELINES.putAll(renderPipelines);
    }

    public void registerRenderTask(RenderTaskSource... renderTaskSources) {
        RENDER_TASK.putAll(renderTaskSources);
    }

    public void registerVertexFormat(VertexFormat... vertexFormats) {
        VERTEX_FORMATS.putAll(vertexFormats);
    }

    public void registerInputHandlerProviders(InputHandlerProvider... providers) {
        INPUT_HANDLER_PROVIDERS.putAll(providers);
    }

    public void registerTextures(Texture... textures) {
        TEXTURES.putAll(textures);
    }

    public void registerKeybind(Input... inputs) {
        KEY_BINDS.putAll(inputs);
    }

    public void registerShader(ShaderSource... shaderSources) {
        SHADERS.putAll(shaderSources);
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
        Ourcraft.registerDefaultContent(OURCRAFT);
        Container.register();
        Entity.register();
    }

    public void handleArgs(String[] args) {
        ARGUMENTS.handle(args);
    }

    private static int staticGameInstanceUniversalID = 0;

    public static synchronized int getNewGameInstanceUniversalID() {
        return staticGameInstanceUniversalID++;
    }
}
