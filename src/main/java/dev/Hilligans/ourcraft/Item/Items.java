package dev.Hilligans.ourcraft.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class Items {

    static short id = 0;
    public static short getNextId() {
        short val = id;
        id++;
        return val;
    }
}
