package dev.hilligans.engine.client.graphics.util;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.resource.ResourceLocation;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class WorldTextureManager {

    public static Image loadImage1(String path, String source, GameInstance gameInstance) {
        return (Image) gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation("Images/" + path, source));
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

    public static BufferedImage charToBufferedImage(char s) {
        //First, we have to calculate the string's width and height
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();

        Font font = new Font("SansSerif", Font.PLAIN, 48);
        //Set the font to be used when drawing the string
        g.setFont(font);

        //Get the string visual bounds
        FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
        Rectangle2D rect = font.getStringBounds(s + "", frc);
        //Release resources
        g.dispose();

        //Then, we have to draw the string on the final image
        if(Math.ceil(rect.getWidth()) == 0) {
            //System.out.println(s);
            return null;
        }
        //Create a new image where to print the character

        img = new BufferedImage((int) Math.ceil(rect.getWidth()), (int) Math.ceil(rect.getHeight()), BufferedImage.TYPE_INT_ARGB);
        g = img.getGraphics();
        g.setColor(Color.white);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int x = 0;
        int y = fm.getAscent();
        g.drawString(s + "", x, y);
        g.dispose();

        return img;
    }
}
