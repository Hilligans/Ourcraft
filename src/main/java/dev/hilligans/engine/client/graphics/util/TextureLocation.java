package dev.hilligans.engine.client.graphics.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TextureLocation {


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
