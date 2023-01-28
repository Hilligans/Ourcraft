package dev.hilligans.ourcraft.Client.Input;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;

import java.util.function.BiConsumer;

public class RepeatingInput extends Input {

    BiConsumer<RenderWindow,Float> consumer;

    public RepeatingInput(String defaultBind) {
        super(defaultBind);
    }

    public RepeatingInput(String defaultBind, BiConsumer<RenderWindow,Float> consumer) {
        super(defaultBind, true);
        this.consumer = consumer;
    }

    public void repeat(RenderWindow renderWindow, float strength) {
        consumer.accept(renderWindow,strength);
    }
}
