package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import java.util.ArrayList;

public class QueueGroup {

    public ArrayList<QueueFamily> graphics_queues = new ArrayList<>();
    public ArrayList<QueueFamily> compute_queues = new ArrayList<>();
    public ArrayList<QueueFamily> transfer_queues = new ArrayList<>();


    public void addQueue(QueueFamily queueFamily) {
        if(queueFamily.hasGraphics()) {
            graphics_queues.add(queueFamily);
        }
        if(queueFamily.hasCompute()) {
            compute_queues.add(queueFamily);
        }
        if(queueFamily.hasTransfer()) {
            transfer_queues.add(queueFamily);
        }

    }

    public QueueFamily findSupported(boolean graphics, boolean compute, boolean transfer, boolean present) {
        if(transfer) {
            for(QueueFamily family : transfer_queues) {
                if(present) {
                    if(family.hasPresent()) {
                        return family;
                    }
                } else {
                    return family;
                }
            }
        }
        if(compute) {
            for(QueueFamily family : compute_queues) {
                if(present) {
                    if(family.hasPresent()) {
                        return family;
                    }
                } else {
                    return family;
                }
            }
        }
        if(graphics) {
            for(QueueFamily family : graphics_queues) {
                if(present) {
                    if(family.hasPresent()) {
                        return family;
                    }
                } else {
                    return family;
                }
            }
        }
        return null;
    }


}
