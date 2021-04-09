package Hilligans.Client.Rendering.World.Managers;

import Hilligans.ClientMain;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class WorldTextureManager {

    int width = 0;
    int height = 1;


    public static final int MAX_TEXTURE_SIZE = 256;
    public static final int MIN_TEXTURE_SIZE = 16;
    public static final int RATIO = MAX_TEXTURE_SIZE / MIN_TEXTURE_SIZE;

    public ArrayList<ImageHolder> imageHolders = new ArrayList<>();

    public Int2ObjectOpenHashMap<ImageHolder> imageMap = new Int2ObjectOpenHashMap<>();

    public HashMap<String, BufferedImage> bufferedImageHashMap = new HashMap<>();
    public HashMap<String, Integer> idHashMap = new HashMap<>();
    public HashMap<String, BufferedImage> moddedImageMap = new HashMap<>();


    public static WorldTextureManager instance = new WorldTextureManager();

    public BufferedImage img;

    public void clear() {
        width = 0;
        height = 1;
        idHashMap.clear();
        imageHolders.clear();
        imageMap.clear();
    }

    public int loadTextureId(String path, String textureName) {
        Integer id = idHashMap.get(textureName);
        if(id == null) {
            BufferedImage image = createFlipped(loadImage(path));
            bufferedImageHashMap.put(textureName,image);
            int id1 = addImage(image);
            idHashMap.put(textureName,id1);
            return id1;
        } else {
            return id;
        }
    }

    public void registerBlockTexture(BufferedImage bufferedImage, String textureName) {
        moddedImageMap.put(textureName,bufferedImage);
        idHashMap.put(textureName,addImage(bufferedImage));
    }

    public int registerTexture() {

        if(imageHolders.size() != 0) {
            img = imageHolders.get(0).bufferedImage;

            for (int x = 1; x < imageHolders.size(); x++) {
                img = joinImage(img, imageHolders.get(x).bufferedImage);
            }


            int texture;
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);
            allocatePixels(byteBuffer, img);
            texture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
            glGenerateMipmap(GL_TEXTURE_2D);

            return texture;
        }
        return 0;
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
   // int id = 0;
    public int addImage(BufferedImage img) {
        int id;
        for(ImageHolder imageHolder : imageHolders) {
            //System.out.println((imageHolder.imageSize == img.getWidth()) + " " + imageHolder.canAddImage());
            if(imageHolder.imageSize == img.getWidth() && imageHolder.canAddImage()) {
                imageHolder.addTexture(img);
          //      System.out.println("image id " + (imageHolder.idHolder + MAX_TEXTURE_SIZE / MIN_TEXTURE_SIZE * imageHolder.id));
                id = imageHolder.getNextID();
                imageMap.put(id,imageHolder);

                return id;
            }
        }
        ImageHolder imageHolder = new ImageHolder(img.getWidth(),imageHolders.size(), this);
        imageHolder.addTexture(img);
        imageHolders.add(imageHolder);
      //  System.out.println("new image id " + (imageHolder.idHolder + MAX_TEXTURE_SIZE / MIN_TEXTURE_SIZE * imageHolder.id));
        id = imageHolder.getNextID();
        imageMap.put(id,imageHolder);
        width++;
        return id;
    }

    static HashMap<String, BufferedImage> cachedImages = new HashMap<>();

    public static BufferedImage loadImage(String path) {
        BufferedImage img = cachedImages.get(path);
        if(img != null) {
            return img;
        }
        if(path != null) {
            BufferedImage image;
            try {
            InputStream url = ClientMain.class.getResourceAsStream("/Images/" + path);
            if(url == null) {
                return DefaultImage();
            }
            image = ImageIO.read(url);
            } catch (IOException e) {
                return DefaultImage();
            }
            cachedImages.put(path,image);
            return image;
        }
        return DefaultImage();
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

    public static BufferedImage joinImage(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth() + img2.getWidth();
        int height = Math.max(img1.getHeight(),img2.getHeight());

        BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

        for(int x = 0; x < img1.getWidth(); x++) {
            for(int y = 0; y < img1.getHeight(); y++) {
                img.setRGB(x,y,img1.getRGB(x,y));
            }
        }
        for(int x = 0; x < img2.getWidth(); x++) {
            for(int y = 0; y < img2.getHeight(); y++) {
                img.setRGB(x + img1.getWidth(),y,img2.getRGB(x,y));
            }
        }
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
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
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
    public static float getMinX(int id) {
        return instance.imageMap.get(id).minX(id) / instance.width;
    }
    public static float getMaxX(int id) {
        return instance.imageMap.get(id).maxX(id) / instance.width;
    }
    public static float getMinY(int id) {
        return instance.imageMap.get(id).minY(id);
    }
    public static float getMaxY(int id) {
        return instance.imageMap.get(id).maxY(id);
    }
    public static float getTextureSize(int id) {
        return instance.imageMap.get(id).imageSize();
    }


    public static int loadAndRegisterTexture(String path) {
        BufferedImage bufferedImage = createFlipped(loadImage(path));
        return registerTexture1(bufferedImage);
    }

    public static int loadAndRegisterUnflippedTexture(String path) {
        BufferedImage bufferedImage = loadImage(path);
        return registerTexture1(bufferedImage);
    }

    private static int registerTexture1(BufferedImage bufferedImage) {
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

    public static void removeTexture(int id) {
        glDeleteTextures(id);
    }

    public static BufferedImage stringToBufferedImage(String s) {
        //First, we have to calculate the string's width and height

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();



        Font font = new Font("Tahoma", Font.PLAIN, 48);
        //Set the font to be used when drawing the string
        g.setFont(font);

        //Get the string visual bounds
        FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
        Rectangle2D rect = font.getStringBounds(s, frc);
        //Release resources
        g.dispose();

        //Then, we have to draw the string on the final image

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

    public static class ImageHolder {

        public double imageSize;
        int count;

        public BufferedImage bufferedImage;

        int width = 0;
        int height = 0;

        public int id;
        public int idHolder = 0;

        WorldTextureManager worldTextureManager;

        public ImageHolder(int imageSize, int id, WorldTextureManager worldTextureManager) {
            this.imageSize = imageSize;
            this.id = id;
            this.worldTextureManager = worldTextureManager;
            bufferedImage = new BufferedImage(MAX_TEXTURE_SIZE,MAX_TEXTURE_SIZE,BufferedImage.TYPE_INT_ARGB);
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                for(int x = 0; x < bufferedImage.getWidth(); x++) {
                    bufferedImage.setRGB(x,y,new Color(255,255,255,127).getRGB());
                }
            }
            count = MAX_TEXTURE_SIZE / imageSize;
            System.out.println(" count " + count);
        }

        public void addTexture(BufferedImage img) {
            for(int y = 0; y < imageSize; y++) {
                for(int x = 0; x < imageSize; x++) {
                    bufferedImage.setRGB(x + width * (int)imageSize, y + height * (int)imageSize, img.getRGB(x,y));
                }
            }
            width++;
            if(width >= count) {
                width = 0;
                height++;
            }
        }

        public boolean canAddImage() {
            return height <= imageSize;
        }

        public int getNextID() {
            int val = idHolder;
            idHolder ++;
            return val + RATIO * id;
        }

        public float minX(int id) {
            boolean val = false;
            if(id == 16) {
                val = true;
            }
            id -= RATIO * this.id;
            int y = (int)(id / count);
            int x = (int)(id - (y * count));
            if(val) {
               // System.out.println("x " + x);
            }
            return ((float)1 / count) * x + (float)(this.id) / worldTextureManager.imageHolders.size() * worldTextureManager.imageHolders.size();
        }
        public float maxX(int id) {
            id -= RATIO * this.id;
            int y = (int)(id / count);
            int x = (int)(id - (y * count));
            return (float)1 / count * (x + 1) + (float)(this.id) / worldTextureManager.imageHolders.size() * worldTextureManager.imageHolders.size();
        }
        public float minY(int id) {
            id -= RATIO * this.id;
            int y = (int)(id / count);
            return (float)1 / count * y;
        }
        public float maxY(int id) {
            id -= RATIO * this.id;
            int y = (int)(id / count);
            return ((float)1 / count) * (y + 1);
        }

        public float imageSize() {
            return (float) (imageSize / MAX_TEXTURE_SIZE);
        }

    }

}
