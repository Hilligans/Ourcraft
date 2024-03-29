package dev.hilligans.ourcraft.data.primitives;

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
