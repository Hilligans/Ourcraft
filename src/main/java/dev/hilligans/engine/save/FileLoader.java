package dev.hilligans.engine.save;

import dev.hilligans.engine.tag.CompoundNBTTag;
import org.lwjgl.PointerBuffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_CANCEL;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_OKAY;

public class FileLoader {

    public static int maxSize = 10000000;

    public static void write(String fileName, ByteBuffer byteBuffer) {
        try {
            File file = new File(fileName);
            if(file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            if(byteBuffer == null) {
                throw new RuntimeException("Writing nothing");
            }
            RandomAccessFile aFile = new RandomAccessFile(fileName, "rw");
            FileChannel inChannel = aFile.getChannel();
            inChannel.write(byteBuffer);
            inChannel.close();
        } catch (IOException ingored) {
            ingored.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<>();
    }

    public static CompoundNBTTag loadTag(String path) {
        try {
            ByteBuffer byteBuffer = readBuffer(path);
            if(byteBuffer != null) {
                CompoundNBTTag compoundTag = new CompoundNBTTag();
                compoundTag.readFrom(byteBuffer);
                return compoundTag;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ByteBuffer readBuffer(String path) {
        try {
            File file = new File(path);
            if(file.exists()) {
                RandomAccessFile aFile = new RandomAccessFile(path, "rw");
                int length = (int) aFile.length();
                ByteBuffer buf = ByteBuffer.allocate(length);
                buf.mark();
                aFile.getChannel().read(buf);
                buf.reset();
                return buf;
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static ByteBuffer readBufferDirect(String path) {
        try {
            File file = new File(path);
            if(file.exists()) {
                ByteBuffer buf;
                try (RandomAccessFile aFile = new RandomAccessFile(path, "rw")) {
                    int length = (int) aFile.length();
                    buf = ByteBuffer.allocateDirect(length);
                    buf.mark();
                    aFile.getChannel().read(buf);
                }
                buf.reset();
                return buf;
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String readString(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream stream = FileLoader.class.getResourceAsStream(path);
        if(stream == null) {
            System.out.println("Cant read file: " + path);
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().forEach(string -> stringBuilder.append(string).append("\n"));
        return stringBuilder.toString();
    }

    public static String readString(InputStream stream) {
        if(stream == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().forEach(string -> stringBuilder.append(string).append("\n"));
        return stringBuilder.toString();
    }

    public static void save(CompoundNBTTag compoundTag, String path) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxSize);
        byteBuffer.mark();
        compoundTag.writeTo(byteBuffer);
        byteBuffer.limit(byteBuffer.position());
        byteBuffer.reset();
        write(path,byteBuffer);
    }

    public static void openFile(String acceptedFiles, String defaultPath, HandleFile handleFile) {
        PointerBuffer outPath = memAllocPointer(1);
        try {
            /*
            checkResult(
                    NFD_OpenDialog(acceptedFiles, defaultPath, outPath),
                    outPath, handleFile

             */
            //);
        } finally {
            memFree(outPath);
        }
    }

    private static void checkResult(int result, PointerBuffer path, HandleFile handleFile) {
        switch (result) {
            case NFD_OKAY:
                handleFile.success(path);
                //nNFD_Free(path.get(0));
                break;
            case NFD_CANCEL:
                handleFile.cancel();
                break;
            default: // NFD_ERROR
                handleFile.error();
        }
    }

    public interface HandleFile {
        void success(PointerBuffer path);
        default void cancel() {}
        default void error() {}
    }
}
