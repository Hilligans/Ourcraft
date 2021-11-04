package dev.Hilligans.ourcraft.Util.Registry;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Events.Common.RegisterEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class Registry<T> {

    public HashMap<String,T> MAPPED_ELEMENTS = new HashMap<>();
    public ArrayList<T> ELEMENTS = new ArrayList<>();
    public GameInstance gameInstance;

    public Registry(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }


    public void clear() {
        MAPPED_ELEMENTS.clear();
        ELEMENTS.clear();
    }

    public T get(int index) {
        return ELEMENTS.get(index);
    }

    public T get(String name) {
        return MAPPED_ELEMENTS.get(name);
    }

    public void put(String name, T element) {
        if(gameInstance.EVENT_BUS.postEvent(new RegisterEvent<>(this,name,element)).shouldRun()) {
            MAPPED_ELEMENTS.put(name, element);
            ELEMENTS.add(element);
        }
    }

    public T remove(String name) {
        T element = MAPPED_ELEMENTS.remove(name);
        for(int x = 0; x < ELEMENTS.size(); x++) {
            T t = ELEMENTS.get(x);
            if(t == element) {
                ELEMENTS.remove(x);
                x--;
            }
        }
        return element;
    }

}
