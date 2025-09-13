package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import java.util.ArrayList;

import static org.lwjgl.vulkan.VK10.VK_SUCCESS;

public class QueueSubmitResult {

    public ArrayList<Runnable> runnables = new ArrayList<>();
    public int submitResult;

    public QueueSubmitResult(int submitResult) {
        this.submitResult = submitResult;
    }

    public QueueSubmitResult withRunnables(ArrayList<Runnable> runnables) {
        this.runnables = runnables;
        return this;
    }

    public boolean wasSuccessful() {
        return submitResult == VK_SUCCESS;
    }

    public void signal() {
        for(Runnable runnable : runnables) {
            runnable.run();;
        }
    }
}
