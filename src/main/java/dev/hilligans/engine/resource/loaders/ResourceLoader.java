package dev.hilligans.engine.resource.loaders;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.resource.ResourceLocation;
import dev.hilligans.engine.save.FileLoader;
import dev.hilligans.engine.tag.CompoundNBTTag;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class ResourceLoader<T> implements IRegistryElement {

    public String name;
    public String category;
    public GameInstance gameInstance;
    public ArrayList<String> fileTypes = new ArrayList<>();
    public ModContainer source;

    public ResourceLoader(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public ResourceLoader<T> withFileTypes(String... fileTypes) {
        this.fileTypes.addAll(Arrays.asList(fileTypes));
        return this;
    }

    public T read(ResourceLocation path) {
        ByteBuffer buffer = source.gameInstance.getResource(path);
        if(buffer == null) {
            return null;
        }
        return read(buffer);
    }

    public abstract T read(ByteBuffer buffer);

    public void write(String path, T t) {
        FileLoader.write(path, write(t));
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

    public static CompoundNBTTag toCompoundTag(ByteBuffer buffer) {
        if(buffer == null) {
            return null;
        }
        CompoundNBTTag compoundTag = new CompoundNBTTag();
        compoundTag.readFrom(buffer);
        return compoundTag;
    }

    public static String[] toStrings(ByteBuffer buffer) {
        String s = new String(buffer.array());
        return s.split("\n");
    }

    public static ByteBuffer toByteBuffer(String string) {
        return ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
    }

    public static ByteBuffer toByteBuffer(CompoundNBTTag compoundNBTTag) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10000000);
        byteBuffer.mark();
        compoundNBTTag.writeTo(byteBuffer);
        byteBuffer.limit(byteBuffer.position());
        byteBuffer.reset();
        return byteBuffer;
    }

    @Override
    public void assignOwner(ModContainer source) {
        this.source = source;
        this.gameInstance = source.gameInstance;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }

    @Override
    public String getResourceType() {
        return "resource_loader";
    }
}
