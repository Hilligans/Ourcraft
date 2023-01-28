package dev.hilligans.ourcraft.Client.Input.Key;

public interface CharPress {

    default void onPress(char key) {}

    default void onUnPress(char key) {}

    default void onRepeat(char key) {}

}
