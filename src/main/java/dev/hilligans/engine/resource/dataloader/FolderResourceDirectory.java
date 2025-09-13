package dev.hilligans.engine.resource.dataloader;

import dev.hilligans.engine.resource.IBufferAllocator;
import dev.hilligans.engine.save.FileLoader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class FolderResourceDirectory implements ResourceDirectory {

    public File folder;
    public int size;

    public FolderResourceDirectory(File folder) {
        this.folder = folder;
        this.size = folder.getAbsolutePath().length();
    }

    @Override
    public ByteBuffer get(String path) throws IOException {
        return FileLoader.readBuffer(folder.getPath() + "/" + path);
    }

    @Override
    public ByteBuffer getDirect(String path) {
        return FileLoader.readBufferDirect(folder.getPath() + "/" + path);
    }

    @Override
    public ByteBuffer get(String path, IBufferAllocator allocator) throws IOException {
        try {
            File file = new File(folder.getPath() + "/" + path);
            if(file.exists()) {
                try (RandomAccessFile aFile = new RandomAccessFile(folder.getPath() + "/" + path, "r")) {
                    ByteBuffer buf = allocator.malloc((int) aFile.length());
                    aFile.getChannel().read(buf);
                    return buf.rewind();
                }
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<>();
        recursivelyAdd(path,folder,files);
        return files;
    }

    @Override
    public ArrayList<String> getValidEnding(String path, ArrayList<String> fileEndings) {
        return null;
    }

    private void recursivelyAdd(String path, File source, ArrayList<String> files) {
        File[] fileList = source.listFiles();
        if(fileList != null) {
            for (File file : fileList) {
                if(file.isDirectory()) {
                    if(path.startsWith(getPath(file))) {
                        recursivelyAdd(path, file, files);
                    }
                } else {
                    if(getPath(file).startsWith(path)) {
                        files.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private String getPath(File file) {
        return file.getAbsolutePath().substring(size);
    }
}
