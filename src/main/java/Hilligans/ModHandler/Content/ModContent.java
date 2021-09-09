package Hilligans.ModHandler.Content;

import Hilligans.Block.Block;
import Hilligans.Client.Audio.SoundBuffer;
import Hilligans.Client.Rendering.NewRenderer.BlockModel;
import Hilligans.Client.Rendering.NewRenderer.IModel;
import Hilligans.Client.Rendering.Texture;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.ItemProperties;
import Hilligans.Item.BlockItem;
import Hilligans.Item.Item;
import Hilligans.ModHandler.Mod;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.Protocol;
import Hilligans.Ourcraft;
import Hilligans.Util.ByteArray;
import Hilligans.Util.Settings;
import Hilligans.Util.Util;
import Hilligans.WorldSave.WorldLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ModContent {

    public String modID;

    public Mod mod;
    public Class<?> mainClass;
    public URLClassLoader classLoader;
    public ModDependency[] dependencies = new ModDependency[0];
    public int version = -1;
    public String description = "";
    public String[] authors = new String[0];

    public ArrayList<Block> blocks = new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Texture> textures = new ArrayList<>();
    public HashMap<String,BufferedImage> blockTextures = new HashMap<>();
    public ArrayList<SoundBuffer> sounds = new ArrayList<>();
    public ArrayList<IModel> models = new ArrayList<>();

    public BiFunction<JSONObject,String,Block> blockParser = (blockData, string) -> {
        Block block = new Block(string, "/Data/" + blockData.getString("data"),modID);
        JSONArray textures = blockData.getJSONArray("textures");
        for(int x = 0; x < textures.length(); x++) {
            block.blockProperties.addTexture(textures.getString(x),x);
        }
        return block;
    };


    public HashMap<String,Protocol> protocols = new HashMap<>();

    public boolean loaded = false;

    public ModContent(String modID) {
        this.modID = modID;
    }

    public ModContent(ByteArray packetData) {
        readData(packetData);
        if(Settings.cacheDownloadedMods) {
            if(Settings.storeServerModsIndividually) {
                String ip = ClientMain.getClient().serverIP.replace(':','_');
                if(!ip.equals("")) {
                    WorldLoader.write("mod_cache/servers/" + ip + "/" + modID + "-" + version + ".dat",packetData.toByteBuffer());
                }
            } else {
                WorldLoader.write("mod_cache/mods/" + modID + "-" + version + ".dat",packetData.toByteBuffer());
            }
        }
    }

    public ModContent addClassLoader(URLClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public static ModContent readLocal(String name) {
        ByteBuffer buffer = WorldLoader.readBuffer("mod_cache/" + (Settings.storeServerModsIndividually ? "servers/" + ClientMain.getClient().serverIP.replace(':','_') + "/" : "mods/") + name + ".dat");
        if(buffer != null) {
            ModContent modContent = new ModContent("");
            modContent.readData(new PacketData(buffer,2));
            return modContent;
        }
        return null;
    }

    public void load() throws Exception {
        if(mainClass != null) {
            mainClass.getConstructor(ModContent.class).newInstance(this);
        }
        loaded = true;
        readInitializers();
    }

    public void registerBlock(Block block) {
        block.modId = modID;
        blocks.add(block);
        blockTextures.putAll(block.blockProperties.blockTextureManager.getAllTextures());
        items.add(new BlockItem(block.name,block,modID));
    }

    public void registerBlocks(Block... blocks) {
        for(Block block : blocks) {
            registerBlock(block);
        }
    }

    public void registerItem(Item item) {
        items.add(item);
    }

    public void registerItems(Item... items) {
        for(Item item : items) {
            registerItem(item);
        }
    }

    public void registerSound(SoundBuffer soundBuffer) {
        sounds.add(soundBuffer);
    }

    public void registerSounds(SoundBuffer... soundBuffers) {
        for(SoundBuffer soundBuffer : soundBuffers) {
            registerSound(soundBuffer);
        }
    }

    public void registerTexture(Texture texture) {
        textures.add(texture);
    }

    public void registerTextures(Texture... textures) {
        for(Texture texture : textures) {
            registerTexture(texture);
        }
    }

    public void registerModel(IModel model) {
        models.add(model);
    }

    public void registerModels(IModel... models) {
        for(IModel iModel : models) {
            registerModel(iModel);
        }
    }

    public void registerPacket(Supplier<PacketBase> packet) {
        Protocol protocol = protocols.computeIfAbsent("Play", Protocol::new);
        protocol.register(packet);
    }

    @SafeVarargs
    public final void registerPackets(Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            registerPacket(packet);
        }
    }

    public void registerPacket(String protocolName, Supplier<PacketBase> packet) {
        Protocol protocol = protocols.computeIfAbsent(protocolName, Protocol::new);
        protocol.register(packet);
    }

    @SafeVarargs
    public final void registerPackets(String protocolName, Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            registerPacket(protocolName,packet);
        }
    }

    public void registerPacket(String protocolName, int id, Supplier<PacketBase> packet) {
        Protocol protocol = protocols.computeIfAbsent(protocolName, Protocol::new);
        protocol.register(packet,id);
    }

    @SafeVarargs
    public final void registerPackets(String protocolName, int id, Supplier<PacketBase>... packets) {
        for(Supplier<PacketBase> packet : packets) {
            registerPacket(protocolName,id,packet);
        }
    }

    public void readInitializers() {
        try {
            JSONObject blocks = new JSONObject(WorldLoader.readString(Ourcraft.RESOURCE_MANAGER.getResource("Data/Blocks.json", modID)));
            for(String string : blocks.keySet()) {
                JSONObject blockData = blocks.getJSONObject(string);
                Block block = blockParser.apply(blockData,string);
                registerBlock(block);
            }
        } catch (Exception ignored) {}
    }


    public void putData(ByteArray byteArray) {
        byteArray.writeInt(version);
        byteArray.writeString(modID);
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
            byteArray.writeTexture(texture.texture);
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
            textures.add(new Texture(byteArray.readString(), byteArray.readTexture()));
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
