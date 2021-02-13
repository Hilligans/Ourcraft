package Hilligans.Tag;

import java.nio.ByteBuffer;

public class IntegerTag extends Tag {

    public int val;

    public IntegerTag() {}

    public IntegerTag(int val) {
        this.val = val;
    }


    @Override
    int getSize() {
        return 4;
    }

    @Override
    byte getId() {
        return 3;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = byteBuf.getInt();
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putInt(val);
    }

}
