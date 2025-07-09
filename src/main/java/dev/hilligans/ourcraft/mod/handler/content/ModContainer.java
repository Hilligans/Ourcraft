package dev.hilligans.ourcraft.mod.handler.content;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.biome.Biome;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.ourcraft.client.input.Input;
import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.ourcraft.client.rendering.Texture;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;
import dev.hilligans.ourcraft.client.rendering.newrenderer.IModel;
import dev.hilligans.ourcraft.command.CommandHandler;
import dev.hilligans.ourcraft.data.descriptors.Tag;
import dev.hilligans.ourcraft.entity.EntityType;
import dev.hilligans.ourcraft.item.Item;
import dev.hilligans.ourcraft.item.data.ToolLevel;
import dev.hilligans.ourcraft.mod.handler.ModClass;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.network.engine.INetworkEngine;
import dev.hilligans.ourcraft.network.packet.PacketType;
import dev.hilligans.ourcraft.recipe.IRecipe;
import dev.hilligans.ourcraft.recipe.helper.RecipeView;
import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.resource.registry.loaders.RegistryLoader;
import dev.hilligans.ourcraft.settings.Setting;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.registry.Registry;
import dev.hilligans.ourcraft.world.Feature;

import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.function.Supplier;

public class ModContainer {

    public final ModClass modClass;
    public Registry<Registry<?>> registries;
    public GameInstance gameInstance;
    public URLClassLoader classLoader;
    public Path path;



    public Registry<Block> blockRegistry;
    public Registry<Item> itemRegistry;
    public Registry<Biome> biomeRegistry;
    public Registry<Tag> tagRegistry;
    public Registry<IRecipe<?>> recipeRegistry;
    public Registry<RecipeView<?>> recipeViewRegistry;
    public Registry<IGraphicsEngine<?,?,?>> graphicsEngineRegistry;
    public Registry<CommandHandler> commandHandlerRegistry;
    public Registry<Protocol> protocolRegistry;
    public Registry<Setting> settingRegistry;
    public Registry<ResourceLoader<?>> resourceLoaderRegistry;
    public Registry<SoundBuffer> soundBufferRegistry;
    public Registry<ToolLevel> toolLevelRegistry;
    public Registry<RegistryLoader> registryLoaderRegistry;
    public Registry<ScreenBuilder> screenBuilderRegistry;
    public Registry<Feature> featureRegistry;
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
        itemRegistry = (Registry<Item>) registries.getExcept("ourcraft:item");
        biomeRegistry = (Registry<Biome>) registries.getExcept("ourcraft:biome");
        tagRegistry = (Registry<Tag>) registries.getExcept("ourcraft:tag");
        recipeRegistry = (Registry<IRecipe<?>>) registries.getExcept("ourcraft:recipe");
        recipeViewRegistry = (Registry<RecipeView<?>>) registries.getExcept("ourcraft:recipe_view");
        graphicsEngineRegistry = (Registry<IGraphicsEngine<?, ?, ?>>) registries.getExcept("ourcraft:graphics_engine");
        commandHandlerRegistry = (Registry<CommandHandler>) registries.getExcept("ourcraft:command");
        protocolRegistry = (Registry<Protocol>) registries.getExcept("ourcraft:protocol");
        settingRegistry = (Registry<Setting>) registries.getExcept("ourcraft:setting");
        resourceLoaderRegistry = (Registry<ResourceLoader<?>>) registries.getExcept("ourcraft:resource_loader");
        soundBufferRegistry = (Registry<SoundBuffer>) registries.getExcept("ourcraft:sound");
        toolLevelRegistry = (Registry<ToolLevel>) registries.getExcept("ourcraft:tool_level");
        registryLoaderRegistry = (Registry<RegistryLoader>) registries.getExcept("ourcraft:registry_loader");
        screenBuilderRegistry = (Registry<ScreenBuilder>) registries.getExcept("ourcraft:screen");
        featureRegistry = (Registry<Feature>) registries.getExcept("ourcraft:feature");
        renderTargetRegistry = (Registry<RenderTarget>) registries.getExcept("ourcraft:render_target");
        renderPipelineRegistry = (Registry<RenderPipeline>) registries.getExcept("ourcraft:render_pipeline");
        renderTaskRegistry = (Registry<RenderTaskSource>) registries.getExcept("ourcraft:render_task");
        inputRegistry = (Registry<Input>) registries.getExcept("ourcraft:key_bind");
        vertexFormatRegistry = (Registry<VertexFormat>) registries.getExcept("ourcraft:vertex_format");
        inputHandlerProviderRegistry = (Registry<InputHandlerProvider>) registries.getExcept("ourcraft:input");
        textureRegistry = (Registry<Texture>) registries.getExcept("ourcraft:texture");
        shaderSourceRegistry = (Registry<ShaderSource>) registries.getExcept("ourcraft:shader");
        layoutEngineRegistry = (Registry<ILayoutEngine<?>>) registries.getExcept("ourcraft:layout_engine");
        entityTypeRegistry = (Registry<EntityType>) registries.getExcept("ourcraft:entity_type");
        networkEngineRegistry = (Registry<INetworkEngine<?,?>>) registries.getExcept("ourcraft:network_engine");
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

    public <T extends IRegistryElement> void registerCore(String type, T... data) {
        for(T val : data) {
            val.assignOwner(this);
        }
        gameInstance.REGISTRIES.getExcept(type).putAllGen(data);
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

    public void registerItem(Item item) {
        //items.add(item);
        //item.source = this;
        item.assignOwner(this);
        itemRegistry.put(item);
    }

    public void registerItems(Item... items) {
        for(Item item : items) {
            registerItem(item);
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

    public void registerModel(IModel... models) {
        for(IModel iModel : models) {
        //    this.models.add(iModel);
        }
    }

    @SafeVarargs
    public final void registerPacket(PacketType... packets) {
        ModContainer self = this;
        for(PacketType packetType : packets) {
            Protocol protocol = protocolRegistry.computeIfAbsent("ourcraft:Play", (s -> new Protocol(s.split(":")[1]).setSource(self)));
            protocol.register(packetType);
        }
    }

    @SafeVarargs
    public final void registerPacket(String protocolName, PacketType... packets) {
        ModContainer self = this;
        for(PacketType packetType : packets) {
            Protocol protocol = protocolRegistry.computeIfAbsent(protocolName, (s -> new Protocol(s.split(":")[1]).setSource(self)));
            protocol.register(packetType);
        }
    }

    @SafeVarargs
    public final void registerPacket(String protocolName, int id, Supplier<PacketBase<?>>... packets) {
        ModContainer self = this;
        for(Supplier<PacketBase<?>> packet : packets) {
            Protocol protocol = protocolRegistry.computeIfAbsent(protocolName, (s -> new Protocol(s.split(":")[1]).setSource(self)));
         //   protocol.register(packet,id);
        }
    }

    public void registerResourceLoader(ResourceLoader<?>... resourceLoaders) {
        for(ResourceLoader<?> resourceLoader : resourceLoaders) {
            resourceLoader.gameInstance = gameInstance;
            resourceLoader.assignOwner(this);
        }
        resourceLoaderRegistry.putAll(resourceLoaders);
    }

    public void registerBiome(Biome... biomes) {
        for(Biome biome : biomes) {
            biome.assignOwner(this);
        }
        biomeRegistry.putAll(biomes);
    }

    public void registerFeature(Feature... features) {
        for(Feature feature : features) {
            feature.assignOwner(this);
        }
        featureRegistry.putAll(features);
    }

    public void registerToolLevel(ToolLevel... toolLevels) {
        for(ToolLevel toolLevel : toolLevels) {
            toolLevel.assignOwner(this);
        }
        toolLevelRegistry.putAll(toolLevels);
        //this.toolLevels.addAll(List.of(toolLevels));
    }

    public void registerRegistryLoader(RegistryLoader... registryLoaders) {
        for(RegistryLoader registryLoader : registryLoaders) {
            registryLoader.gameInstance = gameInstance;
            //this.registryLoaders.add(registryLoader);
        }
        registryLoaderRegistry.putAll(registryLoaders);
    }

    public void registerScreenBuilder(ScreenBuilder... screenBuilders) {
        for(ScreenBuilder screenBuilder : screenBuilders) {
            screenBuilder.assignOwner(this);
          //  this.screenBuilders.add(screenBuilder);
        }
        screenBuilderRegistry.putAll(screenBuilders);
    }

    public void registerGraphicsEngine(IGraphicsEngine<?,?,?>... graphicsEngines) {
        for(IGraphicsEngine<?,?,?> graphicsEngine : graphicsEngines) {
            graphicsEngine.assignOwner(this);
        }
        graphicsEngineRegistry.putAll(graphicsEngines);
    }

    public void registerRenderTarget(RenderTarget... renderTargets) {
        for(RenderTarget renderTarget : renderTargets) {
            renderTarget.assignOwner(this);
        }
        renderTargetRegistry.putAll(renderTargets);
        //this.renderTargets.addAll(Arrays.asList(renderTargets));
    }

    public void registerRenderPipelines(RenderPipeline... renderPipelines) {
        for(RenderPipeline renderPipeline : renderPipelines) {
            renderPipeline.assignOwner(this);
        }
        renderPipelineRegistry.putAll(renderPipelines);
        //this.renderPipelines.addAll(Arrays.asList(renderPipelines));
    }

    public void registerRenderTask(RenderTaskSource... renderTasks) {
        for(RenderTaskSource renderTask : renderTasks) {
            renderTask.assignOwner(this);
        }
        renderTaskRegistry.putAll(renderTasks);
        //this.renderTasks.addAll(Arrays.asList(renderTasks));
    }

    public void registerVertexFormat(VertexFormat... vertexFormats) {
        for(VertexFormat vertexFormat : vertexFormats) {
            vertexFormat.assignOwner(this);
        }
        vertexFormatRegistry.putAll(vertexFormats);
        //this.vertexFormats.addAll(Arrays.asList(vertexFormats));
    }

    public void registerInputHandlerProviders(InputHandlerProvider... providers) {
        for(InputHandlerProvider provider : providers) {
            provider.assignOwner(this);
        }
        inputHandlerProviderRegistry.putAll(providers);
        //this.inputHandlerProviders.addAll(Arrays.asList(providers));
    }

    public void registerKeybinds(Input... inputs) {
        for(Input input : inputs) {
            input.assignOwner(this);
        }
        inputRegistry.putAll(inputs);
        //this.keybinds.addAll(Arrays.asList(inputs));
    }

    public void registerShader(ShaderSource... shaderSources) {
        for(ShaderSource shaderSource : shaderSources) {
            shaderSource.assignOwner(this);
        }
        shaderSourceRegistry.putAll(shaderSources);
        //this.shaders.addAll(Arrays.asList(shaderSources));
    }

    public void registerLayoutEngine(ILayoutEngine<?>... layoutEngines) {
        for(ILayoutEngine<?> layoutEngine : layoutEngines) {
            layoutEngine.assignOwner(this);
        }
        layoutEngineRegistry.putAll(layoutEngines);
        //this.layoutEngines.addAll(Arrays.asList(layoutEngines));
    }

    public void registerEntityType(EntityType... entityTypes) {
        for(EntityType entityType : entityTypes) {
            entityType.assignOwner(this);
        }
        entityTypeRegistry.putAll(entityTypes);
    }

    public void registerNetworkEngine(INetworkEngine<?, ?>... networkEngines) {
        for(INetworkEngine<?, ?> networkEngine : networkEngines) {
            networkEngine.assignOwner(this);
        }
        networkEngineRegistry.putAll(networkEngines);
    }

    @Override
    public int hashCode() {
        return registries.hashCode();
    }
}
