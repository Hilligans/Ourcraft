package dev.hilligans.engine.resource.dataloader;

import dev.hilligans.engine.resource.IBufferAllocator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Fallback in case we are running in an exe or an elf file. Otherwise, we would
 * be really screwed.
 */
public class StreamResourceDirectory implements ResourceDirectory {

    @Override
    public String getName() {
        return "internal strema";
    }

    @Override
    public ByteBuffer get(String path) throws IOException {
        try(InputStream stream = StreamResourceDirectory.class.getResourceAsStream("/" + path)) {
            if(stream != null) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(stream.available());
                byteBuffer.put(stream.readAllBytes()).flip();
                return byteBuffer;
            }
        }
        return null;
    }

    @Override
    public ByteBuffer getDirect(String path) throws IOException {
        try(InputStream stream = StreamResourceDirectory.class.getResourceAsStream("/" + path)) {
            if(stream != null) {
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(stream.available());
                byteBuffer.put(stream.readAllBytes()).flip();
                return byteBuffer;
            }
        }
        return null;
    }

    @Override
    public ByteBuffer get(String path, IBufferAllocator allocator) throws IOException {
        try(InputStream stream = StreamResourceDirectory.class.getResourceAsStream("/" + path)) {
            if(stream != null) {
                ByteBuffer byteBuffer = allocator.malloc(stream.available());
                byteBuffer.put(stream.readAllBytes()).flip();
                return byteBuffer;
            }
        }
        return null;
    }

    @Override
    public ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<>();

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL dirURL = cl.getResource(path);

            if(dirURL == null) {
                return files;
            }

            if (dirURL.getProtocol().equals("jar")) {
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
                try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(path) && !entry.isDirectory()) {
                            files.add(name);
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return files;
    }

    @Override
    public ArrayList<String> getValidEnding(String path, ArrayList<String> fileEndings) {
        return new ArrayList<>();
    }
}
