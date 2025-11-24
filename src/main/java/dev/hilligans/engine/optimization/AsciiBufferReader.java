package dev.hilligans.engine.optimization;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;

public class AsciiBufferReader extends Reader {

    ByteBuffer buffer;

    public AsciiBufferReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() {
        if(buffer.remaining() == 0) {
            return -1;
        }

        return (char) buffer.get();
    }

    @Override
    public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
        if(buffer.remaining() == 0) {
            return -1;
        }

        int x;
        for(x = off; x < off + Math.min(len, buffer.remaining()); x++) {
            cbuf[x] = (char) buffer.get();
        }

        return x - off;
    }

    @Override
    public void close() throws IOException {
    }
}
