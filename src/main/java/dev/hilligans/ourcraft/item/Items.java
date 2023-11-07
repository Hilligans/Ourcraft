package dev.hilligans.ourcraft.item;

public class Items {

    static short id = 0;
    public static short getNextId() {
        short val = id;
        id++;
        return val;
    }
}
