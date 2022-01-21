package dev.Hilligans.ourcraft.Client.Rendering.NewRenderer;

import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextAtlas {

    public Image image;
    public int size = 8192 * 1;
    public int minWidth = 16;
    public ExecutorService executorService;

    public Long2BooleanOpenHashMap map = new Long2BooleanOpenHashMap();
    public ArrayList<ImageLocation> images = new ArrayList<>();
    public Int2LongOpenHashMap ids = new Int2LongOpenHashMap();
    public HashMap<String, Integer> cache = new HashMap<>();

    long[][] openSpots;
    int arrSize;

    public int texture;
    int id = 0;

    public void start() {
        arrSize = (int) (Math.log((float)size / minWidth) / Math.log(2)) + 1;
        openSpots = new long[arrSize][3];
        for(int x = 0; x < arrSize; x++) {
            openSpots[x][0] = -1;
            openSpots[x][1] = -1;
            openSpots[x][2] = -1;
        }
        openSpots[arrSize - 1][0] = 0;
    }

    public synchronized long findSpot(int size, int index) {
        int loc = (int) (Math.log((float)size / minWidth) / Math.log(2));
        int ogLoc = loc - 1;
        long spot = -1;
        while(loc != arrSize) {
            loc++;
            for(int z = 2; z >= 0; z--) {
                spot = openSpots[loc][z];
                if(spot != -1) {
                    openSpots[loc][z] = -1;
                    while(loc != ogLoc + 1) {
                        loc--;
                        int x = (int)(spot >> 32);
                        int y = (int)spot;
                        int width = minWidth << (loc);
                        openSpots[loc][0] = spot;
                        openSpots[loc][1] = ((long) x << 32) | (y + width);
                        openSpots[loc][2] = ((long) (x + width) << 32) | y;
                        spot = ((long) (x + width) << 32) | (y + width);
                    }
                    int x = (int)(spot >> 32);
                    int y = (int)spot;
                    ids.put(index,getLong(size, x, y));
                    return spot;
                }
            }
        }
        return spot;
    }

    public TextAtlas() {
        image = new Image(size, size);
    }

    public synchronized int register(ImageLocation imageLocation) {
        String key = imageLocation.modId + imageLocation.path;
        if(cache.containsKey(key)) {
            return cache.get(key);
        }
        images.add(imageLocation);
        imageLocation.textAtlas = this;
        imageLocation.index = id;
        if(id == -1) {
            System.out.println("ran out of space");
        }
        cache.put(key,imageLocation.index);
        id++;
        return imageLocation.index;
    }

    public void clear() {
        id = 0;
        map.clear();
        ids.clear();
        start();
    }

    private long getLong(long size, int x, int y) {
        return ((long) (short) size << 32) | ((short)x << 16) | (short) y;
    }

    public void assemble() {
        long start = System.currentTimeMillis();
        executorService = Executors.newFixedThreadPool(1,new NamedThreadFactory("texture_atlas_builder"));
        for(ImageLocation imageLocation : images) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Image tempImage;
                    try {
                        tempImage = (Image)Ourcraft.GAME_INSTANCE.RESOURCE_LOADER.getResource(new ResourceLocation(imageLocation.path, imageLocation.modId));
                        if(tempImage == null) {
                            System.out.println(new ResourceLocation(imageLocation.path, imageLocation.modId).toIdentifier());
                            return;
                        }
                        tempImage.flip(false);

                        long spot = findSpot(tempImage.width, imageLocation.index);
                        image.putImage(tempImage, (int)(spot >> 32), (int)(spot));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10000, TimeUnit.MILLISECONDS);
        } catch (Exception ignored) {}



        System.out.println(System.currentTimeMillis() - start);
    }

    public int upload() {
        start();

        assemble();
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        return texture;
    }

    public int loadTextureId(String path, String textureName, String source) {
        return register(new ImageLocation("Images/" + path,source));
    }

    public final static TextAtlas instance = new TextAtlas();

    public static float getMinX(int id) {
        return (get(id,16)) / (float)instance.size;
    }
    public static float getMaxX(int id) {
        return (get(id,16) + get(id,32)) / (float)instance.size;
    }
    public static float getMinY(int id) {
        return (get(id,0)) / (float)instance.size;
    }
    public static float getMaxY(int id) {
        return (get(id,0) + get(id,32)) / (float)instance.size;
    }

    public static int get(int id, int shift) {
        return (short)(instance.ids.get(id) >> shift);
    }
}
