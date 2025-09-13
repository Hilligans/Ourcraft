package dev.hilligans.engine.client.input.key;

import java.util.ArrayList;

public class KeyCategory {

    public ArrayList<KeyBind> keyBinds = new ArrayList<>();

    public String name;

    public KeyCategory(String name) {
        this.name = name;
    }

    public KeyCategory addKeyBind(KeyBind keyBind) {
        this.keyBinds.add(keyBind);
        return this;
    }

    public static final KeyCategory MOVEMENT = new KeyCategory("movement");


}
