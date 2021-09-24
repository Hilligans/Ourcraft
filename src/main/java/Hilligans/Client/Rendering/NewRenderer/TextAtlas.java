package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Util.NamedThreadFactory;
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
    public int size = 8192;
    public int minWidth = 16;
    public ExecutorService executorService;

    public Long2BooleanOpenHashMap map = new Long2BooleanOpenHashMap();
    public ArrayList<ImageLocation> images = new ArrayList<>();
    public Int2LongOpenHashMap ids = new Int2LongOpenHashMap();
    public HashMap<String, Integer> cache = new HashMap<>();

    public int texture;
    int id = 0;

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
        cache.put(key,imageLocation.index);
        id++;
        return imageLocation.index;
    }

    public void clear() {
        id = 0;
        map.clear();
        ids.clear();
        generate();

    }

    public void generate() {
        recursivelyAdd(size,0,0);
    }

    public synchronized void add(int tempSize, Image tempImage, int index) {
        for(int x = 0; x < size / tempSize; x++) {
            for(int y = 0; y < size / tempSize; y++) {
                if(map.containsKey(getLong(tempSize,  x * tempSize,  y * tempSize))) {
                    recursivelyRemove(tempSize,x * tempSize, y * tempSize);
                    removeUp(tempSize,x * tempSize, y * tempSize);
                    image.putImage(tempImage, x * tempSize, y * tempSize);
                    ids.put(index,getLong(tempSize,  x * tempSize,  y * tempSize));
                    return;
                }
            }
        }
        System.err.println("Texture atlas ran out of locations");
    }

    private void recursivelyAdd(int tempSize, int x, int y) {
        if(tempSize >= minWidth) {
            map.put(getLong(tempSize, x, y), true);
            recursivelyAdd(tempSize/2,x,y);
            recursivelyAdd(tempSize/2,x + tempSize/2,y);
            recursivelyAdd(tempSize/2,x,y + tempSize/2);
            recursivelyAdd(tempSize/2,x + tempSize/2,y + tempSize/2);
        }
    }

    private void recursivelyRemove(int tempSize, int x, int y) {
        if(tempSize >= minWidth) {
            map.remove(getLong(tempSize, x, y));
            recursivelyRemove(tempSize/2,x,y);
            recursivelyRemove(tempSize/2,x + tempSize/2,y);
            recursivelyRemove(tempSize/2,x,y + tempSize/2);
            recursivelyRemove(tempSize/2,x + tempSize/2,y + tempSize/2);
        }
    }

    private void removeUp(int tempSize, int x, int y) {
        if(tempSize <= size) {
            map.remove(getLong(tempSize, x, y));
            int newSize = tempSize * 2;
            removeUp(newSize, x % newSize == 0 ? x : Math.floorDiv(x,newSize) * newSize, y % newSize == 0 ? y : Math.floorDiv(y,newSize) * newSize);
        }
    }

    private long getLong(long size, int x, int y) {
        return ((long) (short) size << 32) | ((short)x << 16) | (short) y;
    }

    public void assemble() {
        long start = System.currentTimeMillis();
        executorService = Executors.newFixedThreadPool(3,new NamedThreadFactory("texture_atlas_builder"));
        for(ImageLocation imageLocation : images) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Image tempImage = new Image(imageLocation.path, imageLocation.modId);
                        add(tempImage.width, tempImage, imageLocation.index);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (Exception ignored) {}



        System.out.println(System.currentTimeMillis() - start);
    }

    public int upload() {
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
