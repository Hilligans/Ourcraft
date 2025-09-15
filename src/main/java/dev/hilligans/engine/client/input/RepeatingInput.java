package dev.hilligans.engine.client.input;

import dev.hilligans.engine.client.graphics.RenderWindow;

import java.util.function.BiConsumer;

public class RepeatingInput extends Input {

    BiConsumer<RenderWindow,Float> consumer;

    public RepeatingInput(String name, String defaultBind) {
        super(name, defaultBind);
    }

    public RepeatingInput(String name, String defaultBind, BiConsumer<RenderWindow,Float> consumer) {
        super(name, defaultBind, true);
        this.consumer = consumer;
    }

    public void repeat(RenderWindow renderWindow, float strength) {
        consumer.accept(renderWindow,strength);
    }

    @Override
    public void press(RenderWindow renderWindow, float strength) {
        consumer.accept(renderWindow,strength);
    }
}
