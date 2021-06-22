package Hilligans.Resource;

import Hilligans.Client.Lang.Language;
import Hilligans.Client.Lang.Languages;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.TripleTypeWrapper;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.Ourcraft;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourceManager {

    public HashMap<String, ArrayList<LanguageFile>> languageFiles = new HashMap<>();
    public HashMap<String, ArrayList<Image>> textures = new HashMap<>();
    public HashMap<String, ArrayList<Sound>> sounds = new HashMap<>();
    public HashMap<String, ArrayList<Model>> models = new HashMap<>();

    public ArrayList<URLClassLoader> classLoaders = new ArrayList<>();

    public void setLanguageFile(String languageFile) {
        Languages.switchingLanguage.set(true);
        Future<Language> languageFuture = Ourcraft.EXECUTOR.submit(() -> {
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

    public BufferedImage getImage(String name) {
        ArrayList<Image> images = textures.get(name);
        if(images != null) {
            //System.out.println(name);
            return images.get(0).bufferedImage;
        }
        return null;
    }

    public void loadTexture(String path, String source,  String name) {
        BufferedImage image = createFlipped(loadImage(path,source));;
        ArrayList<Image> images = textures.computeIfAbsent(name,(T) -> new ArrayList<Image>());
        images.add(new Image(source,image));
    }

    public void putImage(String name, BufferedImage bufferedImage) {
        textures.computeIfAbsent(name, k -> new ArrayList<>());
        textures.get(name).add(new Image(name,bufferedImage));
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
        }
        return stream;
    }

    public void clearData() {
        textures.clear();
    }

    static class Image {

        public String name;
        public String source;
        public BufferedImage bufferedImage;

        public Image(String source, BufferedImage bufferedImage) {
            this.source = source;
            this.bufferedImage = bufferedImage;
        }
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

    static class Model {



    }

    public static BufferedImage createFlipped(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        return createTransformed(image, at);
    }

    private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage loadImage(String path) {
        try {
            File file = new File(WorldTextureManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            ZipFile zipFile = new ZipFile(file);
            ZipEntry zipEntry = zipFile.getEntry("Images/" + path);
            if (zipEntry != null) {
                return ImageIO.read(zipFile.getInputStream(zipEntry));
            }
        } catch (Exception ignored) {}

        InputStream url = ClientMain.class.getResourceAsStream("/Images/" + path);
        if(url != null) {
            try {
                return ImageIO.read(url);
            } catch (IOException ignored) {}
        }
        return WorldTextureManager.DefaultImage();
    }

    public static BufferedImage loadImage(String path, String source) {
        if(source.equals("")) {
            return loadImage(path);
        }
        TripleTypeWrapper<Class<?>,String,Boolean> type = Ourcraft.MOD_LOADER.mainClasses.get(source);
        String filePath = type.getTypeB();
        if(type.getTypeC()) {
            if (filePath != null) {
                try {
                    ZipFile zipFile = new ZipFile(filePath);
                    ZipEntry zipEntry = zipFile.getEntry("Images/" + path);
                    if (zipEntry != null) {
                        return ImageIO.read(zipFile.getInputStream(zipEntry));
                    }
                } catch (Exception ignored) {}
            }
        } else {
            File file = new File("target/classes/Images/" + path);
            if(file.exists()) {
                try {
                    return ImageIO.read(file);
                } catch (Exception ignored) {}
            }
        }
        return loadImage(path);
    }

}
