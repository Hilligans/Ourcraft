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
import dev.hilligans.ourcraft.item.Item;
import dev.hilligans.ourcraft.item.data.ToolLevel;
import dev.hilligans.ourcraft.mod.handler.ModClass;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.recipe.IRecipe;
import dev.hilligans.ourcraft.recipe.helper.RecipeView;
import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.resource.registry.loaders.RegistryLoader;
import dev.hilligans.ourcraft.settings.Setting;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.registry.Registry;
import dev.hilligans.ourcraft.world.Feature;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModContainer {

    public final ModClass modClass;
    public Registry<Registry<?>> registries;
    public GameInstance gameInstance;

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

    public ModContainer(ModClass modClass) {
        this.modClass = modClass;
    }

    public void setGameInstance(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.registries = gameInstance.REGISTRIES.duplicate();

        blockRegistry = (Registry<Block>) registries.getExcept("block");
        itemRegistry = (Registry<Item>) registries.getExcept("item");
        biomeRegistry = (Registry<Biome>) registries.getExcept("biome");
        tagRegistry = (Registry<Tag>) registries.getExcept("tag");
        recipeRegistry = (Registry<IRecipe<?>>) registries.getExcept("recipe");
        recipeViewRegistry = (Registry<RecipeView<?>>) registries.getExcept("recipe_view");
        graphicsEngineRegistry = (Registry<IGraphicsEngine<?, ?, ?>>) registries.getExcept("graphics_engine");
        commandHandlerRegistry = (Registry<CommandHandler>) registries.getExcept("command");
        protocolRegistry = (Registry<Protocol>) registries.getExcept("protocol");
        settingRegistry = (Registry<Setting>) registries.getExcept("setting");
        resourceLoaderRegistry = (Registry<ResourceLoader<?>>) registries.getExcept("resource_loader");
        soundBufferRegistry = (Registry<SoundBuffer>) registries.getExcept("sound");
        toolLevelRegistry = (Registry<ToolLevel>) registries.getExcept("tool_level");
        registryLoaderRegistry = (Registry<RegistryLoader>) registries.getExcept("resource_loader");
        screenBuilderRegistry = (Registry<ScreenBuilder>) registries.getExcept("screen");
        featureRegistry = (Registry<Feature>) registries.getExcept("feature");
        renderTargetRegistry = (Registry<RenderTarget>) registries.getExcept("render_target");
        renderPipelineRegistry = (Registry<RenderPipeline>) registries.getExcept("render_pipeline");
        renderTaskRegistry = (Registry<RenderTaskSource>) registries.getExcept("render_task");
        inputRegistry = (Registry<Input>) registries.getExcept("key_bind");
        vertexFormatRegistry = (Registry<VertexFormat>) registries.getExcept("vertex_format");
        inputRegistry = (Registry<Input>) registries.getExcept("input");
        textureRegistry = (Registry<Texture>) registries.getExcept("texture");
        shaderSourceRegistry = (Registry<ShaderSource>) registries.getExcept("shader");
        layoutEngineRegistry = (Registry<ILayoutEngine<?>>) registries.getExcept("layout_engine");
    }

    public String getModID() {
        return modClass.getModID();
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public <T extends IRegistryElement> void register(String type, T... data) {
        registries.get(type).putAllGen(data);
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
        ((Registry<SoundBuffer>)registries.get("sound")).put(soundBuffer);
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
    public final void registerPacket(Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            Protocol protocol = protocolRegistry.computeIfAbsent("Play", Protocol::new);
            protocol.register(packet);
        }
    }

    @SafeVarargs
    public final void registerPacket(String protocolName, Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            Protocol protocol = protocolRegistry.computeIfAbsent(protocolName, Protocol::new);
            protocol.register(packet);
        }
    }

    @SafeVarargs
    public final void registerPacket(String protocolName, int id, Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            Protocol protocol = protocolRegistry.computeIfAbsent(protocolName, Protocol::new);
            protocol.register(packet,id);
        }
    }

    public void registerResourceLoader(ResourceLoader<?>... resourceLoaders) {
        for(ResourceLoader<?> resourceLoader : resourceLoaders) {
            resourceLoader.gameInstance = gameInstance;
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
}
