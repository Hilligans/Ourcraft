package Hilligans.Resource;

import Hilligans.Client.Lang.Language;
import Hilligans.Client.Lang.Languages;
import Hilligans.Client.Rendering.ClientUtil;
import Hilligans.Client.Rendering.NewRenderer.IModel;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.Triplet;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.Ourcraft;
import Hilligans.Util.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourceManager {

    public HashMap<String, ArrayList<LanguageFile>> languageFiles = new HashMap<>();
    public HashMap<String, ArrayList<Image>> textures = new HashMap<>();
    public HashMap<String, ArrayList<Sound>> sounds = new HashMap<>();
    public HashMap<String, ArrayList<IModel>> models = new HashMap<>();

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
            return images.get(0).bufferedImage;
        }
        return null;
    }

    public int getColor(String name) {
        ArrayList<Image> images = textures.get(name);
        if(images != null) {
            return images.get(0).color;
        }
        return 0;
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
        }
        return stream;
    }

    public synchronized InputStream getResource(String path, String mod) {

        try {
            Triplet<Class<?>, String, Boolean> type = Ourcraft.MOD_LOADER.mainClasses.get(mod);
            String filePath = type.getTypeB();
            if (type.getTypeC()) {
                if (filePath != null) {
                    try {
                        ZipFile zipFile = new ZipFile(filePath);
                        ZipEntry zipEntry = zipFile.getEntry(path);
                        if (zipEntry != null) {
                            return zipFile.getInputStream(zipEntry);
                        }
                    } catch (Exception ignored) {
                    }
                }
            } else {
                File file = new File("target/classes/" + path);
                if (file.exists()) {
                    try {
                        return file.toURI().toURL().openStream();
                    } catch (Exception ignored) {
                    }
                }
            }

            ModContent modContent = Ourcraft.CONTENT_PACK.mods.get(mod);
            try {
                if (modContent != null) {
                    String jarPath = modContent.classLoader.getURLs()[0].getPath();
                    for (Iterator<URL> it = ResourceManager.class.getClassLoader().getResources(path).asIterator(); it.hasNext(); ) {
                        URL url = it.next();
                        String urlPath = url.getPath();
                        if ((urlPath.startsWith(jarPath)) || (urlPath.startsWith("file:") && urlPath.substring(5).startsWith(jarPath)) || (jarPath.startsWith(urlPath.substring(0, urlPath.length() - path.length())))) {
                            return url.openStream();
                        }
                    }
                } else {
                    System.err.println("no mod found with id " + mod);
                }
            } catch (Exception ignored) {
            }
        } catch (Exception ignored) {}
        return ResourceManager.class.getResourceAsStream("/" + path);
    }

    public void clearData() {
        textures.clear();
        models.clear();
    }

    static class Image {

        public String name;
        public String source;
        public BufferedImage bufferedImage;
        public int color = -1;

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
            ZipEntry zipEntry = zipFile.getEntry("images/" + path);
            if (zipEntry != null) {
                return ImageIO.read(zipFile.getInputStream(zipEntry));
            }
        } catch (Exception ignored) {}

        InputStream url = ClientMain.class.getResourceAsStream("/images/" + path);
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
        Triplet<Class<?>,String,Boolean> type = Ourcraft.MOD_LOADER.mainClasses.get(source);
        String filePath = type.getTypeB();
        if(type.getTypeC()) {
            if (filePath != null) {
                try {
                    ZipFile zipFile = new ZipFile(filePath);
                    ZipEntry zipEntry = zipFile.getEntry("images/" + path);
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
