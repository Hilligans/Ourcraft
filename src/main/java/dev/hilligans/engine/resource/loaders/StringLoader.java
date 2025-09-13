package dev.hilligans.engine.resource.loaders;

import java.nio.ByteBuffer;

public class StringLoader extends ResourceLoader<String> {

    public StringLoader() {
        super("string_loader", "string");
        withFileTypes("txt");
    }

    @Override
    public String read(ByteBuffer buffer) {
        return toString(buffer);
    }

    @Override
    public ByteBuffer write(String s) {
        return toByteBuffer(s);
    }
}
