package dev.hilligans.ourcraft.ModHandler;

public class Event {

    public boolean shouldRun = true;

    public void cancel() {
        shouldRun = false;
    }

    public void shouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    public boolean shouldRun() {
        return shouldRun;
    }



}