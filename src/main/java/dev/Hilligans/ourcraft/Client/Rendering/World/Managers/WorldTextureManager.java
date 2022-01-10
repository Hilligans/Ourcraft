package dev.Hilligans.ourcraft.Client.Rendering.World.Managers;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Primitives.Triplet;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Ourcraft;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class WorldTextureManager {

    int width = 0;
    int height = 1;

    public HashMap<String, Integer> idHashMap = new HashMap<>();


   // public static WorldTextureManager instance = new WorldTextureManager();


    public void clear() {
        width = 0;
        height = 1;
        idHashMap.clear();
    }


    public static int registerTexture(BufferedImage bufferedImage) {
        int texture;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);
        allocatePixels(byteBuffer, bufferedImage);
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        return texture;

    }

    public static int registerTexture(Image image) {
        int texture;
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        return texture;

    }

    static HashMap<String, BufferedImage> cachedImages = new HashMap<>();

    public static BufferedImage loadImage(String path) {
        BufferedImage img = cachedImages.get(path);
        if(img != null) {
            return img;
        }
        img = Ourcraft.getResourceManager().getImage(path);
        if(img != null) {
            return img;
        }
        InputStream url = Ourcraft.getResourceManager().getResource("Images/" + path);
        if(url != null) {
            try {
                return ImageIO.read(url);
            } catch (IOException ignored) {}
        }
        return DefaultImage();
    }

    public static Image loadImage1(String path) {
        return (Image) ClientMain.getClient().gameInstance.RESOURCE_LOADER.getResource("Images/" + path);
    }

    public static BufferedImage loadImage(String path, String source) {
        if(source.equals("")) {
            return loadImage(path);
        }
        Triplet<Class<?>,String,Boolean> type = Ourcraft.GAME_INSTANCE.MOD_LOADER.mainClasses.get(source);
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


    public static void allocatePixels(ByteBuffer byteBuffer, BufferedImage img) {
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                Color color = new Color(img.getRGB(x,y),true);
                byteBuffer.put((x + y * img.getWidth()) * 4,(byte)color.getRed());
                byteBuffer.put((x + y * img.getWidth()) * 4 + 1,(byte)color.getGreen());
                byteBuffer.put((x + y * img.getWidth()) * 4 + 2,(byte)color.getBlue());
                byteBuffer.put((x + y * img.getWidth()) * 4 + 3,(byte)color.getAlpha());
            }
        }
    }

    public static BufferedImage DefaultImage() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, Color.GREEN.getRGB());
        img.setRGB(1, 0, Color.ORANGE.getRGB());
        img.setRGB(0, 1, Color.MAGENTA.getRGB());
        img.setRGB(1, 1, Color.BLUE.getRGB());
        return img;
    }

    public static BufferedImage createFlipped(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        return createTransformed(image, at);
    }

    private static BufferedImage createRotated(BufferedImage image) {
        AffineTransform at = AffineTransform.getRotateInstance(
                Math.PI, image.getWidth()/2, image.getHeight()/2.0);
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

    public float minX(int id) {
        int y = id / width;
        int x = id - (y * width);
        return ((float)1 / width) * x;
    }
    public float maxX(int id) {
        int y = id / width;
        int x = id - (y * width);
        return ((float)1 / width) * (x + 1);
    }
    public float minY(int id) {
        int y = id / width;
        return (float)(1 / height) * y;
    }
    public float maxY(int id) {
        int y = id / width;
        return (float)(1 / height) * (y + 1);
    }

    public static int loadAndRegisterTexture(String path) {
        BufferedImage bufferedImage = createFlipped(loadImage(path));
        return registerTexture1(bufferedImage);
    }
    private static int registerTexture1(BufferedImage bufferedImage) {
        int texture;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);
        allocatePixels(byteBuffer, bufferedImage);
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
       // glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 4, GL_RGB, bufferedImage.getWidth(), bufferedImage.getHeight(), true);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        return texture;
    }

    public static BufferedImage stringToBufferedImage(String s) {
        //First, we have to calculate the string's width and height

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();



        //Font font = new Font("Tahoma", Font.PLAIN, 48);
        Font font = new Font("SansSerif", Font.PLAIN, 48);
        //Set the font to be used when drawing the string
        g.setFont(font);

        //Get the string visual bounds
        FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
        Rectangle2D rect = font.getStringBounds(s, frc);
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
        g.setColor(Color.white); //Otherwise the text would be white
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int x = 0;
        int y = fm.getAscent();
        g.drawString(s, x, y);
        g.dispose();

        //Return the image
        return img;
    }

    public static Font getFont(String name) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, ClientMain.class.getResourceAsStream(name));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveImage(BufferedImage bufferedImage, String name) {
        try {
            File outputfile = new File("characters/" + name + ".png");

            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
