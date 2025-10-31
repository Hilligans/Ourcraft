package dev.hilligans.engine.client.graphics.multigl;

import dev.hilligans.engine.client.graphics.multigl.command.CommandBuffer;

import java.util.concurrent.*;

public class MultiGLExecutor extends Thread {

    public BlockingQueue<CommandBuffer> commandBuffers = new LinkedBlockingQueue<>();
    public volatile boolean isRunning;

    public void shutdown() {
        isRunning = false;
    }

    public void submitBuffer(CommandBuffer commandBuffer) {
        this.commandBuffers.add(commandBuffer);

        if(!isRunning) {
            synchronized (this) {
                if(!isRunning) {
                    this.isRunning = true;
                    this.start();
                }
            }
        }
    }

    @Override
    public void run() {
        while(isRunning) {
            try {
                commandBuffers.take().commands.forEach(Runnable::run);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
