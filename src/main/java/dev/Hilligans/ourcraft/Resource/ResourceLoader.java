package dev.Hilligans.ourcraft.Resource;

import dev.Hilligans.ourcraft.WorldSave.WorldLoader;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class ResourceLoader<T> {

    public String name;
    public String category;
    public ArrayList<String> fileTypes = new ArrayList<>();

    public ResourceLoader(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public ResourceLoader<T> withFileTypes(String... fileTypes) {
        this.fileTypes.addAll(Arrays.asList(fileTypes));
        return this;
    }

    public T read(String path) {
        return read(WorldLoader.readResource(path));
    }

    public abstract T read(ByteBuffer buffer);

    public void write(String path, T t) {
        WorldLoader.write(path, write(t));
    }

    public abstract ByteBuffer write(T t);

    public String toString(ByteBuffer buffer) {
        return new String(buffer.array());
    }

    public String[] toStrings(ByteBuffer buffer) {
        String s = new String(buffer.array());
        return s.split("\\n");
    }

    public ByteBuffer toByteBuffer(String string) {
        return ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
    }
}
