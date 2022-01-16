package dev.Hilligans.ourcraft.Resource.DataLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipResourceDirectory implements ResourceDirectory {

    public ZipFile zipFile;

    public ZipResourceDirectory(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public ByteBuffer get(String path) throws IOException {
        ZipEntry zipEntry = zipFile.getEntry(path);
        if(zipEntry == null) {
            return null;
        }
        return ByteBuffer.wrap(zipFile.getInputStream(zipEntry).readAllBytes());
    }

    @Override
    public ByteBuffer getDirect(String path) throws IOException {
        ZipEntry zipEntry = zipFile.getEntry(path);
        if(zipEntry == null) {
            return null;
        }
        byte[] vals = zipFile.getInputStream(zipEntry).readAllBytes();
        ByteBuffer buf = ByteBuffer.allocateDirect(vals.length);
        buf.put(vals);
        return buf;
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
