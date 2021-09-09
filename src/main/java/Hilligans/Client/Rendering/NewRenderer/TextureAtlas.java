package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Client.Rendering.*;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Data.Primitives.TripleTypeWrapper;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureAtlas {

    int width = 0;
    int height = 1;

    public static final int MAX_TEXTURE_SIZE = 512;
    public static final int MIN_TEXTURE_SIZE = 16;
    public static final int RATIO = MAX_TEXTURE_SIZE / MIN_TEXTURE_SIZE;

    public int maxTextureSize;
    public int minTextureSize;
    public int ratio;

    public Int2ObjectOpenHashMap<TextureAtlas.ImageHolder> imageMap = new Int2ObjectOpenHashMap<>();

    public ArrayList<TextureAtlas.ImageHolder> imageHolders = new ArrayList<>();
    public ArrayList<TripleTypeWrapper<Integer,TextureSource,TextureLocation>> textures = new ArrayList<>();

    public static WorldTextureManager instance = new WorldTextureManager();

    public int glTextureId = -1;

    public int textureId = 0;

    public TextureAtlas(int maxTextureSize, int minTextureSize) {
        this.maxTextureSize = maxTextureSize;
        this.minTextureSize = minTextureSize;
        this.ratio = MAX_TEXTURE_SIZE / MIN_TEXTURE_SIZE;
    }

    public TextureAtlas() {
        this(MAX_TEXTURE_SIZE,MIN_TEXTURE_SIZE);
    }

    public TextureAtlas(int maxTextureSize) {
        this(maxTextureSize,MIN_TEXTURE_SIZE);
    }

    public TextureSource registerTexture(String location) {
        return registerTexture(new TextureLocation(location,""));
    }

    public TextureSource registerTexture(String location, String source) {
        return registerTexture(new TextureLocation(location,source));
    }

    public TextureSource registerTexture(TextureLocation textureLocation) {
        BufferedImage img = textureLocation.getImage();
        int id = addImage(img);
        TextureSource textureSource = new TextureSource(this,getNextSourceId());
        textures.add(new TripleTypeWrapper<>(id,textureSource,textureLocation));
        return textureSource;
    }

    public void bindTexture() {
        GL30.glBindTexture(GL_TEXTURE_2D,glTextureId);
    }

    public int getNextSourceId() {
        int id = textureId;
        textureId++;
        return id;
    }

    public int addImage(BufferedImage img) {
        int id;
        for(TextureAtlas.ImageHolder imageHolder : imageHolders) {
            if(imageHolder.imageSize == img.getWidth() && imageHolder.canAddImage()) {
                imageHolder.addTexture(img);
                id = imageHolder.getNextID();
                imageMap.put(id,imageHolder);
                return id;
            }
        }
        TextureAtlas.ImageHolder imageHolder = new TextureAtlas.ImageHolder(img.getWidth(),imageHolders.size(), this);
        imageHolder.addTexture(img);
        imageHolders.add(imageHolder);
        id = imageHolder.getNextID();
        imageMap.put(id,imageHolder);
        width++;
        return id;
    }

    public int addImage(BufferedImage img, int width) {
        int id;
        for(TextureAtlas.ImageHolder imageHolder : imageHolders) {
            if(imageHolder.imageSize == width && imageHolder.canAddImage()) {
                imageHolder.addTexture(img);
                id = imageHolder.getNextID();
                imageMap.put(id,imageHolder);
                return id;
            }
        }
        TextureAtlas.ImageHolder imageHolder = new TextureAtlas.ImageHolder(width,imageHolders.size(), this);
        imageHolder.addTexture(img);
        imageHolders.add(imageHolder);
        id = imageHolder.getNextID();
        imageMap.put(id,imageHolder);
        this.width++;
        return id;
    }

    public int buildAtlas() {
        BufferedImage img;
        if(imageHolders.size() != 0) {
            img = imageHolders.get(0).bufferedImage;

            for (int x = 1; x < imageHolders.size(); x++) {
                img = joinImage(img, imageHolders.get(x).bufferedImage);
            }

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);
            allocatePixels(byteBuffer, img);
            int texture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            glTextureId = texture;
            return texture;
        }
        return -1;
    }

    public void reload() {
        width = 0;
        height = 1;
        imageHolders.clear();
        imageMap.clear();
        for(TripleTypeWrapper<Integer,TextureSource,TextureLocation> texture : textures) {
            BufferedImage img = texture.getTypeC().reloadGetImage();
            texture.typeA = addImage(img);
        }
        buildAtlas();
    }

    public float minX(TextureSource textureSource) {
        int id = textures.get(textureSource.id).typeA;
        return imageMap.get(id).minX(id);
    }

    public float minY(TextureSource textureSource) {
        int id = textures.get(textureSource.id).typeA;
        return imageMap.get(id).minY(id);
    }

    public float maxX(TextureSource textureSource) {
        int id = textures.get(textureSource.id).typeA;
        return imageMap.get(id).maxX(id);
    }

    public float maxY(TextureSource textureSource) {
        int id = textures.get(textureSource.id).typeA;
        return imageMap.get(id).maxY(id);
    }

    public float minX(int id) {
        return imageMap.get(id).minX(id);
    }

    public float minY(int id) {
        return imageMap.get(id).minY(id);
    }

    public float maxX(int id) {
        return imageMap.get(id).maxX(id);
    }

    public float maxY(int id) {
        return imageMap.get(id).maxY(id);
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

    public static class ImageHolder {

        public double imageSize;
        int count;

        public BufferedImage bufferedImage;

        int width = 0;
        int height = 0;

        public int id;
        public int idHolder = 0;

        TextureAtlas textureAtlas;

        public ImageHolder(int imageSize, int id, TextureAtlas textureAtlas) {
            this.imageSize = imageSize;
            this.id = id;
            this.textureAtlas = textureAtlas;
            bufferedImage = new BufferedImage(textureAtlas.maxTextureSize,textureAtlas.maxTextureSize,BufferedImage.TYPE_INT_ARGB);
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                for(int x = 0; x < bufferedImage.getWidth(); x++) {
                    bufferedImage.setRGB(x,y,new Color(255,255,255,127).getRGB());
                }
            }
            count = textureAtlas.maxTextureSize / imageSize;
        }

        public void addTexture(BufferedImage img) {
            for(int y = 0; y < Math.min(img.getHeight(),imageSize); y++) {
                for(int x = 0; x < Math.min(img.getWidth(),64); x++) {
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
            return val + textureAtlas.ratio * id;
        }

        public float minX(int id) {
            id -= textureAtlas.ratio * this.id;
            int y = (int)(id / count);
            int x = (int)(id - (y * count));
            return ((float)1 / count) * x + (float)(this.id) / textureAtlas.imageHolders.size() * textureAtlas.imageHolders.size();
        }
        public float maxX(int id) {
            id -= textureAtlas.ratio * this.id;
            int y = (int)(id / count);
            int x = (int)(id - (y * count));
            return (float)1 / count * (x + 1) + (float)(this.id) / textureAtlas.imageHolders.size() * textureAtlas.imageHolders.size();
        }
        public float minY(int id) {
            id -= textureAtlas.ratio * this.id;
            int y = (int)(id / count);
            return (float)1 / count * y;
        }
        public float maxY(int id) {
            id -= textureAtlas.ratio * this.id;
            int y = (int)(id / count);
            return ((float)1 / count) * (y + 1);
        }

        public float imageSize() {
            return (float) (imageSize / textureAtlas.maxTextureSize);
        }

    }




}
