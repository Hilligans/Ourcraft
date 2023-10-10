package dev.hilligans.ourcraft.Client.Rendering.NewRenderer;

import dev.hilligans.ourcraft.Client.Rendering.World.Managers.WorldTextureManager;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Data.Primitives.Triplet;
import dev.hilligans.ourcraft.Ourcraft;

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

    public static BufferedImage getDefaultImage() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, Color.GREEN.getRGB());
        img.setRGB(1, 0, Color.ORANGE.getRGB());
        img.setRGB(0, 1, Color.MAGENTA.getRGB());
        img.setRGB(1, 1, Color.BLUE.getRGB());
        return img;
    }

}
