package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class FrameTracker {

    public int maxFrameRate = 144;

    public long timeSinceLastUpdate;
    public long lastFrame;
    public int framesInFlight;
    public int fps;
    public long[] frameTimes;
    public int pointer;

    public FrameTracker setMaxFrameRate(int time) {
        this.maxFrameRate = time;
        return this;
    }

    public FrameTracker setFrameTimeBufferSize(int size) {
        this.frameTimes = new long[size];
        return this;
    }

    public void count() {
        long currentTime = System.currentTimeMillis();
        framesInFlight++;
        if (currentTime - timeSinceLastUpdate >= 1000L ){
            fps = framesInFlight;
            framesInFlight = 0;
            timeSinceLastUpdate = currentTime;
        }
        if(frameTimes != null) {
            pointer = pointer >= frameTimes.length ? 0 : pointer;
            frameTimes[pointer] = currentTime - lastFrame;
        }
        lastFrame = currentTime;
    }

    public boolean shouldDraw() {
        return false;
    }

    public int getFPS() {
        return fps;
    }

    public int getPointer() {
        return pointer;
    }

    public long[] getFrameTimes() {
        return frameTimes;
    }
}
