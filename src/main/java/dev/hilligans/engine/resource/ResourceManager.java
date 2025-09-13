package dev.hilligans.engine.resource;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.client.lang.Language;
import dev.hilligans.ourcraft.client.lang.Languages;
import dev.hilligans.ourcraft.client.rendering.newrenderer.IModel;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

public class ResourceManager {

    public HashMap<String, ArrayList<LanguageFile>> languageFiles = new HashMap<>();
    public HashMap<String, ArrayList<Image>> textures = new HashMap<>();
    public HashMap<String, ArrayList<Sound>> sounds = new HashMap<>();
    public HashMap<String, ArrayList<IModel>> models = new HashMap<>();

    public ArrayList<URLClassLoader> classLoaders = new ArrayList<>();
    public GameInstance gameInstance;


    public ResourceManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void setLanguageFile(String languageFile) {
        Languages.switchingLanguage.set(true);
        Future<Language> languageFuture = gameInstance.THREAD_PROVIDER.submit(() -> {
            Language language = new Language(languageFile + ".txt");
            ArrayList<LanguageFile> languageData = languageFiles.get(languageFile);
            if(languageData != null) {
                for(LanguageFile languageFile1 : languageData) {
                    language.parseLines(languageFile1.data.split("\n"));
                }
            }
            Languages.CURRENT_LANGUAGE = language;
            Languages.switchingLanguage.set(false);
            return language;
        });
    }

    public int getColor(String name) {
        ArrayList<Image> images = textures.get(name);
        if(images != null) {
            return images.get(0).color;
        }
        return 0;
    }

    /*
    public void putImage(String name, BufferedImage bufferedImage) {
        textures.computeIfAbsent(name, k -> new ArrayList<>());
        textures.get(name).add(new Image(name,bufferedImage));
    }

     */

    public void putModel(String name, IModel model) {
        models.computeIfAbsent(name, k -> new ArrayList<>());
        models.get(name).add(model);
    }

    public IModel getModel(String name) {
        ArrayList<IModel> models = this.models.get(name);
        if(models != null) {
            return models.get(0);
        }
        return null;
    }

    public InputStream getResource(String path) {
        InputStream stream = ResourceManager.class.getResourceAsStream("/" + path);
        if(stream == null) {
            for(URLClassLoader classLoader : classLoaders) {
                stream = classLoader.getResourceAsStream(path);
                if(stream != null) {
                    break;
                }
            }
        } else {
            return stream;
        }
        return ResourceManager.class.getResourceAsStream(path);
    }

    /*
    public static void reload() {
        for(String string : Ourcraft.GAME_INSTANCE.CONTENT_PACK.mods.keySet()) {
            if(!Ourcraft.GAME_INSTANCE.CONTENT_PACK.mods.get(string).isJar) {
                try {
                    FileUtils.copyDirectory(new File("src/main/resources/" + string + "/Data/"), new File("target/classes/" + string + "/Data/"),false);
                    FileUtils.copyDirectory(new File("src/main/resources/Models"), new File("target/classes/Models"),false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

     */

    public void clearData() {
        textures.clear();
        models.clear();
    }

    static class Image {

        public String name;
        public String source;
        public BufferedImage bufferedImage;
        public int color = -1;

        /*
        public Image(String source, BufferedImage bufferedImage) {
            this.source = source;
            this.bufferedImage = bufferedImage;
            if(!Settings.isServer) {
                ClientUtil.randomExecutor.submit(() -> {
                    long r = 0;
                    long g = 0;
                    long b = 0;
                    int size = 0;
                    for (int x = 0; x < bufferedImage.getWidth(); x++) {
                        for (int y = 0; y < bufferedImage.getHeight(); y++) {
                            int color = bufferedImage.getRGB(x, y);
                            if(((color >> 24) & 0xFF) != 0) {
                                r += (color >> 16) & 0xFF;
                                g += (color >> 8) & 0xFF;
                                b += (color) & 0xFF;
                                size++;
                            }
                        }
                    }
                    color = new Color((int)(r / size), (int)(g / size), (int)(b / size)).getRGB();
                });
            }
        }
         */
    }

    static class Sound {

        public ByteBuffer data;
        public String fileName;

        public Sound(String fileName, ByteBuffer data) {
            this.fileName = fileName;
            this.data = data;
        }
    }

    static class LanguageFile {

        public String data;
        public String modID;


    }
}
