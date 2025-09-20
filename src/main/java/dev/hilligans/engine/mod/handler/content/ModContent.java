package dev.hilligans.engine.mod.handler.content;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.mod.handler.Mod;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.engine.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLClassLoader;
import java.util.ArrayList;

public class ModContent {

    public String modID;
    public GameInstance gameInstance;

    public Mod mod;
    public Class<?> mainClass;
    public URLClassLoader classLoader;
    public ModDependency[] dependencies = new ModDependency[0];
    public int version = -1;
    public String description = "";
    public String[] authors = new String[0];
    public String path = "";

    public ArrayList<ShaderSource> shaders = new ArrayList<>();
    public boolean loaded = false;
    public boolean shouldLoad = true;

    public ModContent(String modID, GameInstance gameInstance) {
        this.modID = modID;
        this.gameInstance = gameInstance;
    }

    public ModContent(IByteArray packetData, GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        readData(packetData);
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

    public void load() throws Exception {
        if(mainClass != null && shouldLoad) {
            mainClass.getConstructor(ModContent.class).newInstance(this);
        }
        loaded = true;
    }

    public void putData(IByteArray byteArray) {
        byteArray.writeInt(version);
        byteArray.writeUTF16(getModID());
        byteArray.writeUTF16(description);
        byteArray.writeUTF16(Util.toString(authors));
        byteArray.writeUTF16(Util.toString(getDependencies()));
       // byteArray.writeInt(models.size());
        /*
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

         */
    }

    public void readData(IByteArray byteArray) {
        version = byteArray.readInt();
        modID = byteArray.readUTF16();
        description = byteArray.readUTF16();
        byteArray.readUTF16();
        byteArray.readUTF16();
        int size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            byteArray.readUTF16();
            byteArray.readUTF16();
        }
        /*
        size = byteArray.readInt();
        for(int x = 0; x < size; x++) {
            textures.add(new Texture(byteArray.readUTF16(), byteArray.readImage()));
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

         */
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
}
