package dev.hilligans.ourcraft.WorldSave;

import org.lwjgl.PointerBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class FileLoader {

    public static void openFile(String acceptedFiles, String defaultPath, HandleFile handleFile) {
        PointerBuffer outPath = memAllocPointer(1);
        try {
            checkResult(
                    NFD_OpenDialog(acceptedFiles, defaultPath, outPath),
                    outPath, handleFile
            );
        } finally {
            memFree(outPath);
        }
    }

    private static void checkResult(int result, PointerBuffer path, HandleFile handleFile) {
        switch (result) {
            case NFD_OKAY:
                handleFile.success(path);
                nNFD_Free(path.get(0));
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
