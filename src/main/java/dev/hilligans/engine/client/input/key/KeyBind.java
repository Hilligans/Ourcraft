package dev.hilligans.engine.client.input.key;

public class KeyBind {

    public String name;
    public KeyCategory keyCategory = KeyCategory.MOVEMENT;
    public int[] keyCombo;
    public KeyPress keyPress;
    private KeyPress[] keyComboPresses;
    public boolean valid = false;


    public KeyBind(String name) {
        this.name = name;
    }

    public KeyBind(String name, int... defaultCombo) {
        this.name = name;
        this.keyCombo = defaultCombo;
    }

    public KeyBind setCategory(KeyCategory keyCategory) {
        this.keyCategory = keyCategory;
        return this;
    }

    public void setKeyCombo(int[] keyCombo) {
        this.keyCombo = keyCombo;
        if(keyPress != null) {
            if(keyComboPresses != null) {
                for (KeyPress keyPress : keyComboPresses) {
                    KeyHandler.remove(keyPress);
                }
            }
            if (keyCombo != null) {
                keyComboPresses = new KeyPress[keyCombo.length];
                for (int x = 0; x < keyCombo.length; x++) {
                    if(x == 0) {
                        keyComboPresses[x] = new KeyPress() {
                            public void onPress() { onPressKey(); }
                            public void onRepeat() { onRepeatKey(); }
                            public void onUnPress() { onUnPressKey(); }
                        };
                    } else {
                        keyComboPresses[x] = new KeyPress() {
                            public void onPress() { onPressKey(); }
                            public void onUnPress() { onUnPressKey(); }
                        };
                    }
                    KeyHandler.register(keyComboPresses[x],keyCombo[x]);
                }
            }
        }
    }

    public KeyBind setAction(KeyPress keyPress) {
        this.keyPress = keyPress;
        setKeyCombo(keyCombo);
        return this;
    }

    public boolean isPressed() {
        if(keyCombo.length == 0) {
            return false;
        }
        for(int key : keyCombo) {
            if(!KeyHandler.keyPressed[key]) {
                return false;
            }
        }
        return true;
    }

    private void onPressKey() {
        boolean oldValid = valid;
        valid = isPressed();
        if(valid && !oldValid) {
            keyPress.onPress();
        }
    }

    private void onRepeatKey() {
        valid = isPressed();
        if(valid) {
            keyPress.onRepeat();
        }
    }

    private void onUnPressKey() {
        boolean oldValid = valid;
        valid = isPressed();
        if(oldValid && !valid) {
            keyPress.onUnPress();
        }
    }

    public String asString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int x = 0; x < keyCombo.length; x++) {
            stringBuilder.append(KeyHandler.mappedKeys.get(keyCombo[x]));
            if(x + 1 < keyCombo.length) {
                stringBuilder.append(" + ");
            }
        }
        return stringBuilder.toString();
    }


}
