package dev.hilligans.engine.tag;

import java.lang.foreign.MemorySegment;

public interface INBTTag {


    long getSize();
    int getType();

    void read(MemorySegment memorySegment);
    void write(MemorySegment memorySegment);

}
