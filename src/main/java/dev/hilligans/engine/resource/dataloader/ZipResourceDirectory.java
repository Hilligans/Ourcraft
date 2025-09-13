package dev.hilligans.engine.resource.dataloader;

import dev.hilligans.engine.resource.IBufferAllocator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipResourceDirectory implements ResourceDirectory {

    public ZipFile zipFile;
    public Path jarPath;
    public FileSystem jarFS;

    public ZipResourceDirectory(ZipFile zipFile, String path) {
        this.zipFile = zipFile;
        jarPath = Paths.get(path);
        try {
            jarFS = FileSystems.newFileSystem(jarPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ByteBuffer get(String path) throws IOException {
        Path someFileInJarPath = jarFS.getPath("/" + path);
        SeekableByteChannel rbc = Files.newByteChannel(someFileInJarPath, EnumSet.of(StandardOpenOption.READ));

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) rbc.size());
        rbc.read(byteBuffer);
        byteBuffer.rewind();
        rbc.close();
        return byteBuffer;
    }

    @Override
    public ByteBuffer getDirect(String path) throws IOException {
        Path someFileInJarPath = jarFS.getPath("/" + path);
        SeekableByteChannel rbc = Files.newByteChannel(someFileInJarPath, EnumSet.of(StandardOpenOption.READ));

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int) rbc.size());
        rbc.read(byteBuffer);
        byteBuffer.flip();
        rbc.close();
        return byteBuffer;
    }

    @Override
    public ByteBuffer get(String path, IBufferAllocator allocator) throws IOException {
        Path someFileInJarPath = jarFS.getPath("/" + path);
        SeekableByteChannel rbc = Files.newByteChannel(someFileInJarPath, EnumSet.of(StandardOpenOption.READ));

        ByteBuffer byteBuffer = allocator.malloc((int) rbc.size());
        rbc.read(byteBuffer);
        byteBuffer.flip();
        rbc.close();
        return byteBuffer;
    }

    @Override
    public ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<>();
        for(ZipEntry zipEntry : zipFile.stream().toList()) {
            if(zipEntry.getName().startsWith(path)) {
                files.add(zipEntry.getName());
            }
        }
        return files;
    }

    @Override
    public ArrayList<String> getValidEnding(String path, ArrayList<String> fileEndings) {
        ArrayList<String> endings = new ArrayList<>();
        for(String ending : fileEndings) {
            ZipEntry entry = zipFile.getEntry(path + "." + ending);
            if(entry != null) {
                endings.add(ending);
            }
        }
        return endings;
    }
}
