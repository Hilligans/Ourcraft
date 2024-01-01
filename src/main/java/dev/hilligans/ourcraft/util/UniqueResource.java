package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.GameInstance;

import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class UniqueResource <T> {

    Object[] values;

    public UniqueResource() {
        values = new Object[1];
    }

    public T get(GameInstance gameInstance) {
        return get(gameInstance.getUniqueID());
    }

    public T get(int index) {
        return (T) values[index];
    }

    public synchronized void add(GameInstance gameInstance, T value) {
        if(gameInstance.getUniqueID() >= values.length) {
            Object[] vals = new Object[gameInstance.getUniqueID() + 1];
            for(int x = 0; x < values.length; x++) {
                vals[x] = values[x];
            }
            values = vals;
        }
        values[gameInstance.getUniqueID()] = value;
    }
}
