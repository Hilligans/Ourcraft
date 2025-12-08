package dev.hilligans.engine.client.graphics.layout.composite;

import dev.hilligans.engine.client.graphics.layout.LBox;
import dev.hilligans.engine.client.graphics.layout.LCustomWidget;
import dev.hilligans.engine.client.graphics.layout.LText;
import dev.hilligans.engine.client.graphics.layout.LWidget;
import dev.hilligans.engine.client.graphics.layout.standard.LHBox;

public class LCheckBox extends LCustomWidget {

    public boolean checked;
    public String text;

    public LCheckBox() {
    }

    public LCheckBox(String text) {
        this.text = text;
    }

    @Override
    public LWidget decompose() {
        return new LBox(
                new LHBox(
                        new LBox(10, 10), // check box
                        new LText(text)
                ));
    }

    public String getText() {
        return text;
    }

    @Override
    public void recalculate() {

    }
}
