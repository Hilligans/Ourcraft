package dev.hilligans.engine.client.graphics.util;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.data.Triplet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TextureAtlas {

    int width = 0;

    public static final int MAX_TEXTURE_SIZE = 256;
    public static final int MIN_TEXTURE_SIZE = 16;

    public int maxTextureSize;
    public int minTextureSize;
    public int ratio;

    public int height;

    public Int2ObjectOpenHashMap<TextureAtlas.ImageHolder> imageMap = new Int2ObjectOpenHashMap<>();

    public ArrayList<TextureAtlas.ImageHolder> imageHolders = new ArrayList<>();
    public ArrayList<Triplet<Integer, TextureSource, TextureLocation>> textures = new ArrayList<>();
    public GameInstance gameInstance;

    public int glTextureId = -1;

    public TextureAtlas(GameInstance gameInstance, int maxTextureSize, int minTextureSize) {
        this.gameInstance = gameInstance;
        this.maxTextureSize = maxTextureSize;
        this.minTextureSize = minTextureSize;
        this.ratio = MAX_TEXTURE_SIZE / MIN_TEXTURE_SIZE;
    }

    public TextureAtlas(GameInstance gameInstance, int maxTextureSize) {
        this(gameInstance, maxTextureSize,MIN_TEXTURE_SIZE);
    }

    public synchronized int addImage(BufferedImage img, int width) {
        int id;
        for(TextureAtlas.ImageHolder imageHolder : imageHolders) {
            if(imageHolder.imageSize == width && imageHolder.canAddImage()) {
                Triplet<Integer, Integer, Integer> val = imageHolder.getNextID();
                gameInstance.THREAD_PROVIDER.startVirtualThread(() -> imageHolder.addTexture(img, val.typeB, val.typeC), "abc");
                id = val.typeA;
                imageMap.put(id,imageHolder);
                return id;
            }
        }

        TextureAtlas.ImageHolder imageHolder = new TextureAtlas.ImageHolder(width,imageHolders.size(), this);
        Triplet<Integer, Integer, Integer> val = imageHolder.getNextID();
        gameInstance.THREAD_PROVIDER.execute(() -> imageHolder.addTexture(img, val.typeB, val.typeC));
        imageHolders.add(imageHolder);
        id = val.typeA;
        imageMap.put(id,imageHolder);
        this.width++;
        return id;
    }

    public dev.hilligans.engine.client.graphics.resource.Image toImage() {
        dev.hilligans.engine.client.graphics.resource.Image img;
        if(imageHolders.size() != 0) {
            img = imageHolders.get(0).image;
            for (int x = 1; x < imageHolders.size(); x++) {
                img = joinImage(img, imageHolders.get(x).image=null);
            }
            height = img.height;
            return img;
        }
        return null;
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

    public static dev.hilligans.engine.client.graphics.resource.Image joinImage(dev.hilligans.engine.client.graphics.resource.Image img1, dev.hilligans.engine.client.graphics.resource.Image img2) {
        int width = img1.getWidth() + img2.getWidth();
        int height = Math.max(img1.getHeight(),img2.getHeight());
        dev.hilligans.engine.client.graphics.resource.Image img = new dev.hilligans.engine.client.graphics.resource.Image(width,height,4);
        for(int x = 0; x < img1.getWidth(); x++) {
            for(int y = 0; y < img1.getHeight(); y++) {
                img.putPixel(x,y,img1.getPixel(x,y));
            }
        }
        for(int x = 0; x < img2.getWidth(); x++) {
            for(int y = 0; y < img2.getHeight(); y++) {
                img.putPixel(x + img1.getWidth(),y,img2.getPixel(x,y));
            }
        }
        return img;
    }

    public static class ImageHolder {

        public double imageSize;
        int count;

        public dev.hilligans.engine.client.graphics.resource.Image image;

        int width = 0;
        int height = 0;

        public int id;
        public int idHolder = 0;

        TextureAtlas textureAtlas;

        public ImageHolder(int imageSize, int id, TextureAtlas textureAtlas) {
            this.imageSize = imageSize;
            this.id = id;
            this.textureAtlas = textureAtlas;
            image = new Image(textureAtlas.maxTextureSize, textureAtlas.maxTextureSize, 4);
            //bufferedImage = new BufferedImage(textureAtlas.maxTextureSize,textureAtlas.maxTextureSize,BufferedImage.TYPE_INT_ARGB);
            int color = new Color(255,255,255,127).getRGB();
            for(int y = 0; y < image.getHeight(); y++) {
                for(int x = 0; x < image.getWidth(); x++) {
                    image.putPixel(x, y, color);
                    //bufferedImage.setRGB(x,y,);
                }
            }
            count = textureAtlas.maxTextureSize / imageSize;
        }

        public void addTexture(BufferedImage img, int width, int height) {
            for(int y = 0; y < Math.min(img.getHeight(),imageSize); y++) {
                for(int x = 0; x < Math.min(img.getWidth(),64); x++) {
                    //bufferedImage.setRGB(x + width * (int)imageSize, y + height * (int)imageSize, img.getRGB(x,y));
                    image.putPixel((int) (x + width * imageSize), (int) (y + height * imageSize), img.getRGB(x,y));
                }
            }
        }

        public synchronized boolean canAddImage() {
            return height <= imageSize;
        }

        public synchronized Triplet<Integer, Integer, Integer> getNextID() {
            int val = idHolder;
            idHolder++;

            int w = width;
            int h = height;

            width++;
            if(width >= count) {
                width = 0;
                height++;
            }

            return new Triplet<>(val + textureAtlas.ratio * id, w, h);
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
    }
}
