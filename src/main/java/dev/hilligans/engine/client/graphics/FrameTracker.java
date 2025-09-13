package dev.hilligans.engine.client.graphics;

public class FrameTracker {

    public int maxFrameRate = 144;

    public long timeSinceLastUpdate;
    public long lastFrame;
    public int framesInFlight;
    public int fps;
    public long[] frameTimes;
    public int pointer;

    public FrameTracker() {
        setFrameTimeBufferSize(1000);
        lastFrame = System.currentTimeMillis();
    }
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
        pointer++;
        lastFrame = currentTime;
    }

    public long getFrame(int index) {
        index = (pointer % frameTimes.length) - index;
        index = index < 0 ? index + frameTimes.length: index;
        return frameTimes[index];
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
