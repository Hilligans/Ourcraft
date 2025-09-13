package dev.hilligans.ourcraft.client.rendering.layout;

import java.util.ArrayList;
import java.util.List;

public abstract non-sealed class LList extends LWidget {

    public List<LWidget> children = new ArrayList<>();

    public List<LWidget> getWidgets() {
        return children;
    }
}
