package dev.Hilligans.Ourcraft.Data.Primitives;

public class BooleanWrapper {

    private boolean value;

    public BooleanWrapper(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void invertValue() {
        this.value = !this.value;
    }

}
