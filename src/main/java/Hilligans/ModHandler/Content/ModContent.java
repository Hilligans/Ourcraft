package Hilligans.ModHandler.Content;

import Hilligans.Block.Block;
import Hilligans.Client.Audio.SoundBuffer;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Item.BlockItem;
import Hilligans.Item.Item;
import Hilligans.ModHandler.Mod;
import Hilligans.Network.PacketData;
import Hilligans.Util.Util;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ModContent {

    public String modID;

    public Mod mod;
    public Class<?> mainClass;
    public ModDependency[] dependencies = new ModDependency[0];
    public int version = -1;
    public String description = "";
    public String[] authors;

    public ArrayList<Block> blocks = new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Texture> textures = new ArrayList<>();
    public ArrayList<SoundBuffer> sounds = new ArrayList<>();

    public ModContent(String modID) {
        this.modID = modID;
    }

    public ModContent(PacketData packetData) {
        readData(packetData);
    }

    public void load() throws Exception {
        if(mainClass != null) {
            mainClass.getConstructor(ModContent.class).newInstance(this);
        }
    }

    public void registerBlock(Block block) {
        blocks.add(block);
        items.add(new BlockItem(block.name,block));
    }

    public void registerBlocks(Block... blocks) {
        for(Block block : blocks) {
            registerBlock(block);
        }
    }

    public void registerItem(Item item) {
        items.add(item);
    }

    public void registerSound(SoundBuffer soundBuffer) {
        sounds.add(soundBuffer);
    }

    public void registerTexture(Texture texture) {
        textures.add(texture);
    }

    public void putData(PacketData packetData) {
        packetData.writeInt(version);
        packetData.writeString(modID);
        packetData.writeString(description);
        packetData.writeString(Util.toString(authors));
        packetData.writeString(Util.toString(getDependencies()));
        packetData.writeInt(blocks.size());
        for(Block block : blocks) {
            packetData.writeString(block.name);
            packetData.writeString(block.blockProperties.getJsonObject().toString());
        }
    }

    public void readData(PacketData packetData) {
        version = packetData.readInt();
        modID = packetData.readString();
        description = packetData.readString();
        packetData.readString();
        packetData.readString();
        for(int x = 0; x < packetData.readInt(); x++) {
            Block block = new Block(packetData.readString(),BlockProperties.loadProperties(new JSONObject(packetData.readString())));
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
    

}
