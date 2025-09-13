package dev.hilligans.engine.client.input.key;

public interface CharPress {

    default void onPress(char key) {}

    default void onUnPress(char key) {}

    default void onRepeat(char key) {}

}
