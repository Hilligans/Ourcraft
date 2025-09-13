package dev.hilligans.engine.client.input.key;

public interface KeyPress {

    default void onPress() {}

    default void onUnPress() {}

    default void onRepeat() {}

}
