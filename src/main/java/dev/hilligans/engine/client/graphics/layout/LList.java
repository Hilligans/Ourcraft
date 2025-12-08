package dev.hilligans.engine.client.graphics.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract non-sealed class LList extends LWidget {

    public List<LWidget> children = new ArrayList<>();

    public List<LWidget> getWidgets() {
        return children;
    }

    public void foreach(Consumer<LWidget> consumer) {
        children.forEach(consumer);
    }
}
