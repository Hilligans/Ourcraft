package dev.Hilligans.ourcraft.Client.Input.Key;

public interface KeyPress {

    default void onPress() {}

    default void onUnPress() {}

    default void onRepeat() {}

}
