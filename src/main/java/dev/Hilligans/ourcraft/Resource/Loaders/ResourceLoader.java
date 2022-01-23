package dev.Hilligans.ourcraft.Resource.Loaders;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class ResourceLoader<T> {

    public String name;
    public String category;
    public GameInstance gameInstance;
    public ArrayList<String> fileTypes = new ArrayList<>();

    public ResourceLoader(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public ResourceLoader<T> withFileTypes(String... fileTypes) {
        this.fileTypes.addAll(Arrays.asList(fileTypes));
        return this;
    }

    public T read(ResourceLocation path) {
        ByteBuffer buffer = gameInstance.getResource(path);
        if(buffer == null) {
            return null;
        }
        return read(buffer);
    }

    public abstract T read(ByteBuffer buffer);

    public void write(String path, T t) {
        WorldLoader.write(path, write(t));
    }

    public abstract ByteBuffer write(T t);

    public void write(Object o, String path) {
        write(path,(T)o);
    }

    public static String toString(ByteBuffer buffer) {
        if(buffer == null) {
            return null;
        }
        return new String(buffer.array());
    }

    public static String[] toStrings(ByteBuffer buffer) {
        String s = new String(buffer.array());
        return s.split("\n");
    }

    public ByteBuffer toByteBuffer(String string) {
        return ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
    }
}
