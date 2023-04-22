package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngineException;

import java.util.ArrayList;

public class QueueGroup {

    public ArrayList<QueueFamily> graphics_queues = new ArrayList<>();
    public ArrayList<QueueFamily> compute_queues = new ArrayList<>();
    public ArrayList<QueueFamily> transfer_queues = new ArrayList<>();
    public ArrayList<QueueFamily> queueFamilies = new ArrayList<>();


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
        queueFamilies.add(queueFamily);
    }

    public QueueFamily findSupported(boolean graphics, boolean compute, boolean transfer, boolean present) {
        int best = -1;
        int bestIndex = -1;
        for(int x = 0; x < queueFamilies.size(); x++) {
            QueueFamily queueFamily = queueFamilies.get(x);
            int score = 0;
            if((graphics && !queueFamily.hasGraphics()) || (compute && !queueFamily.hasCompute()) || (transfer && !queueFamily.hasTransfer())) {
                //System.out.println("graphics=" + queueFamily.hasGraphics());
                //System.out.println("compute=" + queueFamily.hasCompute());
                //System.out.println("transfer=" + queueFamily.hasTransfer());
                continue;
            }
            score += graphics == queueFamily.hasGraphics() ? 1 : 0;
            score += compute == queueFamily.hasCompute() ? 1 : 0;
            score += transfer == queueFamily.hasTransfer() ? 1 : 0;
            score += present == queueFamily.hasPresent() ? 1 : 0;
            if(score > best) {
                best = score;
                bestIndex = x;
            }
        }
        if(bestIndex == -1) {
            throw new VulkanEngineException("Failed to find queue family graphics=" + graphics + " compute=" + compute + " transfer=" + transfer + " present=" + present);
        }
        System.out.println(queueFamilies.get(bestIndex));
        return queueFamilies.get(bestIndex);

        /*

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

         */
        //return null;
    }
}
