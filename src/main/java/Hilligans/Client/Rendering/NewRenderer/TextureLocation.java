package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.Triplet;
import Hilligans.Ourcraft;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TextureLocation {

    public static HashMap<String,BufferedImage> cachedImages = new HashMap<>();


    public String name;
    public String path;

    public int width;
    public int height;


    public TextureLocation(String name, String path) {

    }

    public BufferedImage getImage() {


        return null;
    }

    public BufferedImage reloadGetImage() {

        return null;
    }

    public void reload() {

    }







    public static BufferedImage loadImage(String path) {
        BufferedImage img = cachedImages.get(path);
        if(img != null) {
            return img;
        }
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
        return getDefaultImage();
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

    public static BufferedImage getDefaultImage() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, Color.GREEN.getRGB());
        img.setRGB(1, 0, Color.ORANGE.getRGB());
        img.setRGB(0, 1, Color.MAGENTA.getRGB());
        img.setRGB(1, 1, Color.BLUE.getRGB());
        return img;
    }

}
