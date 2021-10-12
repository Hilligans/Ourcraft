package dev.Hilligans.ourcraft.Client.Key;

public interface CharPress {

    default void onPress(char key) {}

    default void onUnPress(char key) {}

    default void onRepeat(char key) {}

}
