package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Biome.Biome;
import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Client.Audio.SoundBuffer;
import dev.Hilligans.ourcraft.Client.Input.Input;
import dev.Hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.*;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.Widget;
import dev.Hilligans.ourcraft.Command.CommandHandler;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Data.Descriptors.Tag;
import dev.Hilligans.ourcraft.Entity.Entity;
import dev.Hilligans.ourcraft.Entity.EntityFetcher;
import dev.Hilligans.ourcraft.Item.Data.ToolLevel;
import dev.Hilligans.ourcraft.Item.Data.ToolLevelList;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.ModHandler.Content.ContentPack;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.EventBus;
import dev.Hilligans.ourcraft.ModHandler.Events.Common.RegistryClearEvent;
import dev.Hilligans.ourcraft.ModHandler.ModLoader;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.Protocol;
import dev.Hilligans.ourcraft.Recipe.RecipeHelper.RecipeView;
import dev.Hilligans.ourcraft.Resource.DataLoader.DataLoader;
import dev.Hilligans.ourcraft.Resource.RegistryLoaders.RegistryLoader;
import dev.Hilligans.ourcraft.Resource.Loaders.ResourceLoader;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Resource.ResourceManager;
import dev.Hilligans.ourcraft.Resource.UniversalResourceLoader;
import dev.Hilligans.ourcraft.Settings.Setting;
import dev.Hilligans.ourcraft.Util.ArgumentContainer;
import dev.Hilligans.ourcraft.Util.Logger;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import dev.Hilligans.ourcraft.Recipe.IRecipe;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.Util.Registry.Registry;
import dev.Hilligans.ourcraft.Util.Side;
import dev.Hilligans.ourcraft.World.Feature;

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
    //public final
    public Side side;

    public final ToolLevelList MATERIAL_LIST = new ToolLevelList();

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
        REGISTRIES.put("ourcraft:sounds", SOUNDS);
        REGISTRIES.put("ourcraft:entities", ENTITIES);
        REGISTRIES.put("ourcraft:tool_materials", TOOL_MATERIALS);
        REGISTRIES.put("ourcraft:screen_builders", SCREEN_BUILDERS);
        REGISTRIES.put("ourcraft:features", FEATURES);
        REGISTRIES.put("ourcraft:render_targets", RENDER_TARGETS);
        REGISTRIES.put("ourcraft:render_pipeline", RENDER_PIPELINES);
        REGISTRIES.put("ourcraft:render_task", RENDER_TASK);
        REGISTRIES.put("ourcraft:key_binds", KEY_BINDS);
        REGISTRIES.put("ourcraft:vertex_formats", VERTEX_FORMATS);
        REGISTRIES.put("ourcraft:input_handler_providers", INPUT_HANDLER_PROVIDERS);
        REGISTRIES.put("ourcraft:textures", TEXTURES);
        REGISTRIES.put("ourcraft:shaders", SHADERS);
    }

    public void loadContent() {
        registerDefaultContent();
        CONTENT_PACK.registerModContent(OURCRAFT);
        //CONTENT_PACK.mods.put("ourcraft",OURCRAFT);
        MOD_LOADER.loadDefaultMods();
        CONTENT_PACK.buildVital();
        CONTENT_PACK.mods.forEach((s, modContent) -> modContent.invokeRegistryLoaders());
        CONTENT_PACK.generateData();
    }

    public void build(IGraphicsEngine<?,?> graphicsEngine) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            for(Object o : registry.ELEMENTS) {
                if(o instanceof IRegistryElement) {
                    ((IRegistryElement) o).loadGraphics(graphicsEngine);
                }
            }
        }
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
    public final Registry<SoundBuffer> SOUNDS = new Registry<>(this, SoundBuffer.class);
    public final Registry<EntityFetcher> ENTITIES = new Registry<>(this, EntityFetcher.class);
    public final Registry<ToolLevel> TOOL_MATERIALS = new Registry<>(this, ToolLevel.class);
    public final Registry<RegistryLoader> DATA_LOADERS = new Registry<>(this, RegistryLoader.class);
    public final Registry<ScreenBuilder> SCREEN_BUILDERS = new Registry<>(this, ScreenBuilder.class);
    public final Registry<Feature> FEATURES = new Registry<>(this, Feature.class);
    public final Registry<RenderTarget> RENDER_TARGETS = new Registry<>(this, RenderTarget.class);
    public final Registry<RenderPipeline> RENDER_PIPELINES = new Registry<>(this, RenderPipeline.class);
    public final Registry<RenderTaskSource> RENDER_TASK = new Registry<>(this, RenderTaskSource.class);
    public final Registry<Input> KEY_BINDS = new Registry<>(this, Input.class);
    public final Registry<VertexFormat> VERTEX_FORMATS = new Registry<>(this, VertexFormat.class);
    public final Registry<InputHandlerProvider> INPUT_HANDLER_PROVIDERS = new Registry<>(this, InputHandlerProvider.class);
    public final Registry<Texture> TEXTURES = new Registry<>(this, Texture.class);
    public final Registry<ShaderSource> SHADERS = new Registry<>(this, ShaderSource.class);

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

    public void registerEntity(EntityFetcher entityFetcher) {
        //ENTITIES.put(entityFetcher.);
    }

    public void registerEntities(EntityFetcher... entityFetchers) {
        for(EntityFetcher entityFetcher : entityFetchers) {
            registerEntity(entityFetcher);
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

    public void registerGraphicsEngine(IGraphicsEngine<?,?>... graphicsEngines) {
        for(IGraphicsEngine<?,?> graphicsEngine : graphicsEngines) {
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

    public ByteBuffer getResource(ResourceLocation resourceLocation) {
        return DATA_LOADER.get(resourceLocation);
    }

    public ByteBuffer getResourceDirect(ResourceLocation resourceLocation) {
        return DATA_LOADER.getDirect(resourceLocation);
    }

    public void registerDefaultContent() {
        Ourcraft.registerDefaultContent(OURCRAFT);
        PacketBase.register();
        Container.register();
        Widget.register();
        Entity.register();
    }

    public void handleArgs(String[] args) {
        ARGUMENTS.handle(args);
    }
}
