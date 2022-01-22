package dev.Hilligans.ourcraft.Resource.DataLoader;

import dev.Hilligans.ourcraft.Util.PipelineStage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public interface ResourceDirectory {

    ByteBuffer get(String path) throws IOException;

    ByteBuffer getDirect(String path) throws IOException;

    ArrayList<String> getFiles(String path);

    ArrayList<String> getValidEnding(String path, ArrayList<String> fileEndings);
}
