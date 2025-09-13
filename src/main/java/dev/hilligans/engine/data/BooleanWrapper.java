package dev.hilligans.engine.data;

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
