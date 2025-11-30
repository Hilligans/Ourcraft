package dev.hilligans.engine.mod.handler.content;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IApplication;
import dev.hilligans.engine.client.graphics.*;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.gametype.GameType;
import dev.hilligans.ourcraft.biome.Biome;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.engine.client.input.Input;
import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.engine.client.graphics.util.Texture;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayoutEngine;
import dev.hilligans.engine.client.graphics.api.IModel;
import dev.hilligans.engine.command.ICommand;
import dev.hilligans.ourcraft.data.descriptors.Tag;
import dev.hilligans.engine.entity.EntityType;
import dev.hilligans.engine.mod.handler.ModClass;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.INetworkEngine;
import dev.hilligans.engine.network.packet.PacketType;
import dev.hilligans.ourcraft.recipe.IRecipe;
import dev.hilligans.ourcraft.recipe.helper.RecipeView;
import dev.hilligans.engine.resource.loaders.ResourceLoader;
import dev.hilligans.engine.authentication.IAuthenticationScheme;
import dev.hilligans.ourcraft.settings.Setting;
import dev.hilligans.engine.test.ITest;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;

import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class ModContainer {

    public final ModClass modClass;
    public Registry<Registry<?>> registries;
    public GameInstance gameInstance;
    public URLClassLoader classLoader;
    public Path path;


    public Registry<Block> blockRegistry;
    public Registry<Biome> biomeRegistry;
    public Registry<Tag> tagRegistry;
    public Registry<IRecipe<?>> recipeRegistry;
    public Registry<RecipeView<?>> recipeViewRegistry;
    public Registry<IGraphicsEngine<?,?,?>> graphicsEngineRegistry;
    public Registry<Protocol> protocolRegistry;
    public Registry<Setting> settingRegistry;
    public Registry<ResourceLoader<?>> resourceLoaderRegistry;
    public Registry<SoundBuffer> soundBufferRegistry;
    public Registry<ScreenBuilder> screenBuilderRegistry;
    public Registry<RenderTarget> renderTargetRegistry;
    public Registry<RenderPipeline> renderPipelineRegistry;
    public Registry<RenderTaskSource> renderTaskRegistry;
    public Registry<Input> inputRegistry;
    public Registry<VertexFormat> vertexFormatRegistry;
    public Registry<InputHandlerProvider> inputHandlerProviderRegistry;
    public Registry<Texture> textureRegistry;
    public Registry<ShaderSource> shaderSourceRegistry;
    public Registry<ILayoutEngine<?>> layoutEngineRegistry;
    public Registry<EntityType> entityTypeRegistry;
    public Registry<INetworkEngine<?, ?>> networkEngineRegistry;
    public Registry<ICommand> commandRegistry;
    public Registry<IAuthenticationScheme<?>> authenticationSchemeRegistry;
    public Registry<ITest> testRegistry;
    public Registry<IApplication> applicationRegistry;
    public Registry<GameType> gameTypeRegistry;

    public ModContainer(Class<? extends ModClass> clazz, URLClassLoader classLoader, Path path) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        this.modClass = clazz.getConstructor().newInstance();
        this.classLoader = classLoader;
        this.path = path;
    }

    public ModContainer(ModClass modClass) {
        this.modClass = modClass;
    }

    public void setGameInstance(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.registries = gameInstance.REGISTRIES.duplicate();

        for(Registry<?> registry : registries.ELEMENTS) {
            registry.mapping = false;
        }

        blockRegistry = (Registry<Block>) registries.getExcept("ourcraft:block");
        biomeRegistry = (Registry<Biome>) registries.getExcept("ourcraft:biome");
        tagRegistry = (Registry<Tag>) registries.getExcept("ourcraft:tag");
        recipeRegistry = (Registry<IRecipe<?>>) registries.getExcept("ourcraft:recipe");
        recipeViewRegistry = (Registry<RecipeView<?>>) registries.getExcept("ourcraft:recipe_view");
        graphicsEngineRegistry = (Registry<IGraphicsEngine<?, ?, ?>>) registries.getExcept(Engine.name("graphics_engine"));
        protocolRegistry = (Registry<Protocol>) registries.getExcept(Engine.name("protocol"));
        settingRegistry = (Registry<Setting>) registries.getExcept("ourcraft:setting");
        resourceLoaderRegistry = (Registry<ResourceLoader<?>>) registries.getExcept(Engine.name("resource_loader"));
        soundBufferRegistry = (Registry<SoundBuffer>) registries.getExcept("ourcraft:sound");
        screenBuilderRegistry = (Registry<ScreenBuilder>) registries.getExcept("ourcraft:screen");
        renderTargetRegistry = (Registry<RenderTarget>) registries.getExcept(Engine.name("render_target"));
        renderPipelineRegistry = (Registry<RenderPipeline>) registries.getExcept(Engine.name("render_pipeline"));
        renderTaskRegistry = (Registry<RenderTaskSource>) registries.getExcept(Engine.name("render_task"));
        inputRegistry = (Registry<Input>) registries.getExcept(Engine.name("key_bind"));
        vertexFormatRegistry = (Registry<VertexFormat>) registries.getExcept(Engine.name("vertex_format"));
        inputHandlerProviderRegistry = (Registry<InputHandlerProvider>) registries.getExcept(Engine.name("input"));
        textureRegistry = (Registry<Texture>) registries.getExcept(Engine.name("texture"));
        shaderSourceRegistry = (Registry<ShaderSource>) registries.getExcept(Engine.name("shader"));
        layoutEngineRegistry = (Registry<ILayoutEngine<?>>) registries.getExcept(Engine.name("layout_engine"));
        entityTypeRegistry = (Registry<EntityType>) registries.getExcept("ourcraft:entity_type");
        networkEngineRegistry = (Registry<INetworkEngine<?,?>>) registries.getExcept(Engine.name("network_engine"));
        commandRegistry = (Registry<ICommand>) registries.getExcept(Engine.name("command"));
        authenticationSchemeRegistry = (Registry<IAuthenticationScheme<?>>) registries.getExcept(Engine.name("authentication_scheme"));
        testRegistry = (Registry<ITest>) registries.getExcept(Engine.name("test"));
        applicationRegistry = (Registry<IApplication>) registries.getExcept(Engine.name("application"));
        gameTypeRegistry = (Registry<GameType>) registries.getExcept(Engine.name("game_type"));
    }

    public String getModID() {
        return modClass.getModID();
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public <T extends IRegistryElement> void register(String type, T... data) {
        for(T val : data) {
            val.assignOwner(this);
        }
        registries.getExcept(type).putAllGen(data);
    }

    @SafeVarargs
    private final <T extends IRegistryElement> void register(Registry<T> registry, T... elements) {
        for(T val : elements) {
            val.assignOwner(this);
        }

        registry.putAll(elements);
    }

    public void registerGen(IRegistryElement... data) {
        Class<? extends IRegistryElement> clazz = null;
        Registry<?> registry = null;

        for(IRegistryElement val : data) {
            val.assignOwner(this);

            if(clazz == null || clazz != val.getClass()) {
                clazz = val.getClass();
                registry = Registry.find(registries, clazz);
            }

            if(registry == null) {
                throw new RuntimeException("Unknown registry for registry element:" + val.getUniqueName());
            }

            registry.putGen(val);
        }
    }

    public void registerGen(Iterable<IRegistryElement> data) {
        Class<? extends IRegistryElement> clazz = null;
        Registry<?> registry = null;

        for(IRegistryElement val : data) {
            val.assignOwner(this);

            if(clazz == null || clazz != val.getClass()) {
                clazz = val.getClass();
                registry = Registry.find(registries, clazz);
            }

            if(registry == null) {
                throw new RuntimeException("Unknown registry for registry element:" + val.getUniqueName());
            }

            registry.putGen(val);
        }
    }

    public <T extends IRegistryElement> void registerCore(String type, T... data) {
        for(T val : data) {
            val.assignOwner(this);
        }
        Registry<?> registry = gameInstance.REGISTRIES.getExcept(type);
        registry.setCoreRegistry();
        registry.putAllGen(data);
    }

    public void registerBlock(Block block) {
        block.assignOwner(this);
        blockRegistry.put(block);
        //blocks.add(block);
        // blockTextures.putAll(block.blockProperties.blockTextureManager.getAllTextures());
        //items.add(new BlockItem(block.name,block,modID).setModContent(this));
    }

    public void registerBlocks(Block... blocks) {
        for(Block block : blocks) {
            registerBlock(block);
        }
    }

    public void registerSound(SoundBuffer soundBuffer) {
        soundBuffer.assignOwner(this);
        soundBufferRegistry.put(soundBuffer);
        //sounds.add(soundBuffer);
        //soundBuffer.source = this;
    }

    public void registerSounds(SoundBuffer... soundBuffers) {
        for(SoundBuffer soundBuffer : soundBuffers) {
            registerSound(soundBuffer);
        }
    }

    public void registerTexture(Texture... textures) {
        for(Texture texture : textures) {
            texture.assignOwner(this);
          //  this.textures.add(texture);
        }
        textureRegistry.putAll(textures);
    }

    @SafeVarargs
    public final void registerPacket(PacketType<?>... packets) {
        ModContainer self = this;
        for(PacketType<?> packetType : packets) {
            Protocol protocol = protocolRegistry.computeIfAbsent(Engine.name("Play"), (s -> new Protocol(s.split(":")[1]).setSource(self)));
            protocol.register(packetType);
        }
    }

    @SafeVarargs
    public final void registerPacket(String protocolName, PacketType<?>... packets) {
        ModContainer self = this;
        for(PacketType<?> packetType : packets) {
            Protocol protocol = protocolRegistry.computeIfAbsent(protocolName, (s -> new Protocol(s.split(":")[1]).setSource(self)));
            protocol.register(packetType);
        }
    }

    public final void registerPacket(String protocolName, int id, PacketType<?> packet) {
        ModContainer self = this;
        Protocol protocol = protocolRegistry.computeIfAbsent(protocolName, (s -> new Protocol(s.split(":")[1]).setSource(self)));
        protocol.register(packet,id);
    }

    public void registerResourceLoader(ResourceLoader<?>... resourceLoaders) {
        register(resourceLoaderRegistry, resourceLoaders);
    }

    public void registerBiome(Biome... biomes) {
        for(Biome biome : biomes) {
            biome.assignOwner(this);
        }
        biomeRegistry.putAll(biomes);
    }

    public void registerScreenBuilder(ScreenBuilder... screenBuilders) {
        register(screenBuilderRegistry, screenBuilders);
    }

    public void registerGraphicsEngine(IGraphicsEngine<?,?,?>... graphicsEngines) {
        register(graphicsEngineRegistry, graphicsEngines);
    }

    public void registerRenderTarget(RenderTarget... renderTargets) {
        register(renderTargetRegistry, renderTargets);
    }

    public void registerRenderPipelines(RenderPipeline... renderPipelines) {
        register(renderPipelineRegistry, renderPipelines);
    }

    public void registerRenderTask(RenderTaskSource... renderTasks) {
        register(renderTaskRegistry, renderTasks);
    }

    public void registerVertexFormat(VertexFormat... vertexFormats) {
        register(vertexFormatRegistry, vertexFormats);
    }

    public void registerInputHandlerProviders(InputHandlerProvider... providers) {
        register(inputHandlerProviderRegistry, providers);
    }

    public void registerKeybinds(Input... inputs) {
        register(inputRegistry, inputs);
    }

    public void registerShader(ShaderSource... shaderSources) {
        register(shaderSourceRegistry, shaderSources);
    }

    public void registerLayoutEngine(ILayoutEngine<?>... layoutEngines) {
        register(layoutEngineRegistry, layoutEngines);
    }

    public void registerEntityType(EntityType... entityTypes) {
        register(entityTypeRegistry, entityTypes);
    }

    public void registerNetworkEngine(INetworkEngine<?, ?>... networkEngines) {
        register(networkEngineRegistry, networkEngines);
    }

    public void registerCommands(ICommand... commands) {
        register(commandRegistry, commands);
    }

    public void registerAuthenticationScheme(IAuthenticationScheme<?>... schemes) {
        register(authenticationSchemeRegistry, schemes);
    }

    public void registerTest(ITest... tests) {
        register(testRegistry, tests);
    }

    public void registerApplication(IApplication... applications) {
        register(applicationRegistry, applications);
    }

    public void registerGameType(GameType... gameTypes) {
        register(gameTypeRegistry, gameTypes);
    }

    @Override
    public int hashCode() {
        return registries.hashCode();
    }
}
