package dev.Hilligans.ourcraft.Resource.DataLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public interface ResourceDirectory {

    ByteBuffer get(String path) throws IOException;

    ArrayList<String> getFiles(String path);

    ArrayList<String> getValidEnding(String path, ArrayList<String> fileEndings);
}
