package Hilligans.ModHandler.Content;

import Hilligans.Block.Block;
import Hilligans.Client.Audio.SoundBuffer;
import Hilligans.Client.Rendering.Model;
import Hilligans.Client.Rendering.NewRenderer.IModel;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.ItemProperties;
import Hilligans.Item.BlockItem;
import Hilligans.Item.Item;
import Hilligans.ModHandler.Mod;
import Hilligans.Network.PacketData;
import Hilligans.Util.ByteArray;
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
    public String[] authors = new String[0];

    public ArrayList<Block> blocks = new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Texture> textures = new ArrayList<>();
    public ArrayList<SoundBuffer> sounds = new ArrayList<>();
    public ArrayList<IModel> models = new ArrayList<>();

    public ModContent(String modID) {
        this.modID = modID;
    }

    public ModContent(ByteArray packetData) {
        readData(packetData);
    }

    public void load() throws Exception {
        if(mainClass != null) {
            mainClass.getConstructor(ModContent.class).newInstance(this);
        }
    }

    public void registerBlock(Block block) {
        block.modId = modID;
        blocks.add(block);
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

    public void registerSound(SoundBuffer soundBuffer) {
        sounds.add(soundBuffer);
    }

    public void registerTexture(Texture texture) {
        textures.add(texture);
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
        byteArray.writeInt(blocks.size());
        for(Block block : blocks) {
            byteArray.writeString(block.name);
            byteArray.writeString(block.blockProperties.getJsonObject().toString());
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
            blocks.add(new Block(byteArray.readString(),BlockProperties.loadProperties(new JSONObject(byteArray.readString())), modID));
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
    

}
