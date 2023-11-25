package dev.hilligans.ourcraft.resource.loaders;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.resource.ResourceLocation;
import dev.hilligans.ourcraft.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.save.WorldLoader;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class ResourceLoader<T> implements IRegistryElement {

    public String name;
    public String category;
    public GameInstance gameInstance;
    public ArrayList<String> fileTypes = new ArrayList<>();
    public ModContent source;

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
    public void assignModContent(ModContent modContent) {
        this.source = modContent;
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
