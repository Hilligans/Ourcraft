package dev.Hilligans.ourcraft.ModHandler.Content;

import dev.Hilligans.ourcraft.Biome.Biome;
import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Client.Audio.SoundBuffer;
import dev.Hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.*;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.BlockModel;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.IModel;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Descriptors.Tag;
import dev.Hilligans.ourcraft.Data.Other.BlockProperties;
import dev.Hilligans.ourcraft.Data.Other.ItemProperties;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Item.BlockItem;
import dev.Hilligans.ourcraft.Item.Data.ToolLevel;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.ModHandler.Mod;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;
import dev.Hilligans.ourcraft.Network.Protocol;
import dev.Hilligans.ourcraft.Resource.RegistryLoaders.RegistryLoader;
import dev.Hilligans.ourcraft.Resource.Loaders.ResourceLoader;
import dev.Hilligans.ourcraft.Util.ByteArray;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.Util.Util;
import dev.Hilligans.ourcraft.World.Feature;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class ModContent {

    public String modID;
    public GameInstance gameInstance;

    public Mod mod;
    public Class<?> mainClass;
    public URLClassLoader classLoader;
    public ModDependency[] dependencies = new ModDependency[0];
    public int version = -1;
    public String description = "";
    public boolean isJar = true;
    public String[] authors = new String[0];
    public String path = "";

    public ArrayList<Block> blocks = new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Texture> textures = new ArrayList<>();
    public HashMap<String,BufferedImage> blockTextures = new HashMap<>();
    public ArrayList<SoundBuffer> sounds = new ArrayList<>();
    public ArrayList<IModel> models = new ArrayList<>();
    public ArrayList<Tag> tags = new ArrayList<>();
    public ArrayList<ResourceLoader<?>> resourceLoaders = new ArrayList<>();
    public ArrayList<Biome> biomes = new ArrayList<>();
    public ArrayList<ToolLevel> toolLevels = new ArrayList<>();
    public ArrayList<RegistryLoader> registryLoaders = new ArrayList<>();
    public ArrayList<ScreenBuilder> screenBuilders = new ArrayList<>();
    public ArrayList<Feature> features = new ArrayList<>();
    public ArrayList<IGraphicsEngine<?,?,?>> graphicsEngines = new ArrayList<>();
    public ArrayList<RenderTarget> renderTargets = new ArrayList<>();
    public ArrayList<RenderPipeline> renderPipelines = new ArrayList<>();
    public ArrayList<RenderTaskSource> renderTasks = new ArrayList<>();
    public ArrayList<VertexFormat> vertexFormats = new ArrayList<>();
    public ArrayList<InputHandlerProvider> inputHandlerProviders = new ArrayList<>();

    public HashMap<String,Protocol> protocols = new HashMap<>();

    public boolean loaded = false;
    public boolean shouldLoad = true;

    public ModContent(String modID, GameInstance gameInstance) {
        this.modID = modID;
        this.gameInstance = gameInstance;
    }

    public ModContent(ByteArray packetData, GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        readData(packetData);
        if(Settings.cacheDownloadedMods) {
            if(Settings.storeServerModsIndividually) {
                String ip = ClientMain.getClient().serverIP.replace(':','_');
                if(!ip.equals("")) {
                    WorldLoader.write("mod_cache/servers/" + ip + "/" + getModID() + "-" + version + ".dat",packetData.toByteBuffer());
                }
            } else {
                WorldLoader.write("mod_cache/mods/" + getModID() + "-" + version + ".dat",packetData.toByteBuffer());
            }
        }
    }

    public String getModID() {
        return modID;
    }

    public ModContent addClassLoader(URLClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public ModContent addMainClass(Class<?> classVal) {
        mainClass = classVal;
        shouldLoad = false;
        return this;
    }

    public static ModContent readLocal(String name,GameInstance gameInstance) {
        ByteBuffer buffer = WorldLoader.readBuffer("mod_cache/" + (Settings.storeServerModsIndividually ? "servers/" + ClientMain.getClient().serverIP.replace(':','_') + "/" : "mods/") + name + ".dat");
        if(buffer != null) {
            ModContent modContent = new ModContent("", gameInstance);
            modContent.readData(new PacketData(buffer,2));
            return modContent;
        }
        return null;
    }

    public void load() throws Exception {
        if(mainClass != null && shouldLoad) {
            mainClass.getConstructor(ModContent.class).newInstance(this);
        }
        loaded = true;
    }

    public void invokeRegistryLoaders() {
        for(RegistryLoader registryLoader : registryLoaders) {
            registryLoader.runInit();
        }
    }

    public void registerBlock(Block block) {
        block.setModContent(this);
        blocks.add(block);
       // blockTextures.putAll(block.blockProperties.blockTextureManager.getAllTextures());
        items.add(new BlockItem(block.name,block,modID).setModContent(this));
    }

    public void registerBlocks(Block... blocks) {
        for(Block block : blocks) {
            registerBlock(block);
        }
    }

    public void registerItem(Item item) {
        items.add(item);
        item.source = this;
    }

    public void registerItems(Item... items) {
        for(Item item : items) {
            registerItem(item);
        }
    }

    public void registerSound(SoundBuffer soundBuffer) {
        sounds.add(soundBuffer);
        soundBuffer.source = this;
    }

    public void registerSounds(SoundBuffer... soundBuffers) {
        for(SoundBuffer soundBuffer : soundBuffers) {
            registerSound(soundBuffer);
        }
    }

    public void registerTexture(Texture... textures) {
        for(Texture texture : textures) {
            texture.assignModContent(this);
            this.textures.add(texture);
        }
    }

    public void registerModel(IModel... models) {
        for(IModel iModel : models) {
            this.models.add(iModel);
        }
    }

    @SafeVarargs
    public final void registerPacket(Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            Protocol protocol = protocols.computeIfAbsent("Play", Protocol::new);
            protocol.register(packet);
        }
    }

    @SafeVarargs
    public final void registerPacket(String protocolName, Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            Protocol protocol = protocols.computeIfAbsent(protocolName, Protocol::new);
            protocol.register(packet);
        }
    }

    @SafeVarargs
    public final void registerPacket(String protocolName, int id, Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            Protocol protocol = protocols.computeIfAbsent(protocolName, Protocol::new);
            protocol.register(packet,id);
        }
    }

    public void registerResourceLoader(ResourceLoader<?>... resourceLoaders) {
        for(ResourceLoader<?> resourceLoader : resourceLoaders) {
            this.resourceLoaders.add(resourceLoader);
            resourceLoader.gameInstance = gameInstance;
        }
    }

    public void registerBiome(Biome... biomes) {
        for(Biome biome : biomes) {
            biome.source = this;
            this.biomes.add(biome);
        }
    }

    public void registerFeature(Feature... features) {
        for(Feature feature : features) {
            feature.setModContent(this);
            this.features.add(feature);
        }
    }

    public void registerToolLevel(ToolLevel... toolLevels) {
        this.toolLevels.addAll(List.of(toolLevels));
    }

    public void registerRegistryLoader(RegistryLoader... registryLoaders) {
        for(RegistryLoader registryLoader : registryLoaders) {
            registryLoader.gameInstance = gameInstance;
            this.registryLoaders.add(registryLoader);
        }
    }

    public void registerScreenBuilder(ScreenBuilder... screenBuilders) {
        for(ScreenBuilder screenBuilder : screenBuilders) {
            screenBuilder.assignModContent(this);
            this.screenBuilders.add(screenBuilder);
        }
    }

    public void registerGraphicsEngine(IGraphicsEngine<?,?, ?>... graphicsEngines) {
        for(IGraphicsEngine<?,?, ?> graphicsEngine : graphicsEngines) {
            this.graphicsEngines.add(graphicsEngine);
        }
    }

    public void registerRenderTarget(RenderTarget... renderTargets) {
        for(RenderTarget renderTarget : renderTargets) {
            renderTarget.assignModContent(this);
        }
        this.renderTargets.addAll(Arrays.asList(renderTargets));
    }

    public void registerRenderPipelines(RenderPipeline... renderPipelines) {
        for(RenderPipeline renderPipeline : renderPipelines) {
            renderPipeline.assignModContent(this);
        }
        this.renderPipelines.addAll(Arrays.asList(renderPipelines));
    }

    public void registerRenderTask(RenderTaskSource... renderTasks) {
        for(RenderTaskSource renderTask : renderTasks) {
            renderTask.assignModContent(this);
        }
        this.renderTasks.addAll(Arrays.asList(renderTasks));
    }

    public void registerVertexFormat(VertexFormat... vertexFormats) {
        for(VertexFormat vertexFormat : vertexFormats) {
            vertexFormat.assignModContent(this);
        }
        this.vertexFormats.addAll(Arrays.asList(vertexFormats));
    }

    public void registerInputHandlerProviders(InputHandlerProvider... providers) {
        for(InputHandlerProvider provider : providers) {
            provider.assignModContent(this);
        }
        this.inputHandlerProviders.addAll(Arrays.asList(providers));
    }

    public void putData(ByteArray byteArray) {
        byteArray.writeInt(version);
        byteArray.writeString(getModID());
        byteArray.writeString(description);
        byteArray.writeString(Util.toString(authors));
        byteArray.writeString(Util.toString(getDependencies()));
        byteArray.writeInt(models.size());
        for(IModel iModel : models) {
            byteArray.writeString(iModel.getPath());
            byteArray.writeString(iModel.getModel());
        }
        byteArray.writeInt(textures.size());
        for(Texture texture : textures) {
            byteArray.writeString(texture.path);
            byteArray.writeImage(texture.texture);
        }
        byteArray.writeInt(blockTextures.size());
        for(String string : blockTextures.keySet()) {
            byteArray.writeString(string);
            byteArray.writeTexture(blockTextures.get(string));
        }
        byteArray.writeInt(sounds.size());
        for(SoundBuffer soundBuffer : sounds) {
            byteArray.writeString(soundBuffer.file);
            byteArray.writeBytes(soundBuffer.data.array());
        }
        byteArray.writeInt(models.size());
        for(IModel model : models) {
            byteArray.writeString(model.getPath());
            byteArray.writeString(model.getModel());
        }
        byteArray.writeInt(blocks.size());
        for(Block block : blocks) {
            byteArray.writeString(block.name);
            byteArray.writeString(block.blockProperties.write().toString());
            byteArray.writeStrings(block.blockProperties.blockTextureManager.getTextures());
        }
        byteArray.writeInt(items.size());
        for(Item item : items) {
            byteArray.writeString(item.name);
            byteArray.writeString(item.itemProperties.getJsonObject().toString());
        }
    }

    public void readData(ByteArray byteArray) {
        version = byteArray.readInt();
        modID = byteArray.readString();
        description = byteArray.readString();
        byteArray.readString();
        byteArray.readString();
        int size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            byteArray.readString();
            byteArray.readString();
        }
        size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            textures.add(new Texture(byteArray.readString(), byteArray.readImage()));
        }
        size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            blockTextures.put(byteArray.readString(),byteArray.readTexture());
        }
        size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            sounds.add(new SoundBuffer(byteArray.readString(),byteArray.readBytes()));
        }
        size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            String path = byteArray.readString();
            BlockModel blockModel = new BlockModel(byteArray.readString());
            blockModel.path = path;
            models.add(blockModel);
        }
        size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            Block block = new Block(byteArray.readString(),BlockProperties.loadProperties(new JSONObject(byteArray.readString())), modID);
            block.blockProperties.blockTextureManager.addStrings(byteArray.readStrings());
            blocks.add(block);
        }
        size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            String name = byteArray.readString();
            items.add(ItemProperties.loadProperties(new JSONObject(byteArray.readString())).getItem(name,this));
        }
    }

    public String[] getDependencies() {
        String[] strings = new String[dependencies.length];
        int x = 0;
        for(ModDependency modDependency : dependencies) {
            strings[x] = modDependency.modID;
            x++;
        }
        return strings;
    }

    public void readData(JSONObject jsonObject) {
        if(jsonObject.has("dependencies")) {
            JSONArray dependencies = jsonObject.getJSONArray("dependencies");
            this.dependencies = new ModDependency[dependencies.length()];
            for(int x = 0; x < dependencies.length(); x++) {
                JSONObject dependency = dependencies.getJSONObject(x);
                String modID = dependency.getString("mod");
                int minVersion = dependency.has("minVersion") ? dependency.getInt("minVersion") : -1;
                int maxVersion = dependency.has("maxVersion") ? dependency.getInt("maxVersion") : -1;
                this.dependencies[x] =  new ModDependency(modID,minVersion,maxVersion);
            }
        }
        version = jsonObject.has("version") ? jsonObject.getInt("version") : -1;
        description = jsonObject.has("description") ? jsonObject.getString("description") : "";

    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    @Override
    public String toString() {
        return "ModContent{" +
                "modID='" + modID + '\'' +
                ", mod=" + mod +
                ", mainClass=" + mainClass +
                ", classLoader=" + classLoader +
                ", dependencies=" + Arrays.toString(dependencies) +
                ", version=" + version +
                ", description='" + description + '\'' +
                ", authors=" + Arrays.toString(authors) +
                ", blocks=" + blocks +
                ", items=" + items +
                ", textures=" + textures +
                ", blockTextures=" + blockTextures +
                ", sounds=" + sounds +
                ", models=" + models +
                ", loaded=" + loaded +
                '}';
    }
}
