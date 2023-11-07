package dev.hilligans.ourcraft.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentProfiler extends Profiler {

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<Long>> profiles = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Integer,String> idToName = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String,Integer> nameToId = new ConcurrentHashMap<>();
    public int index = 0;
    public boolean nano = true;

    public ConcurrentProfiler() {}

    public ConcurrentProfiler Nano(boolean nano) {
        this.nano = nano;
        return this;
    }

    public synchronized void profile(String name) {
        if(nameToId.getOrDefault(name,-1) == -1) {
            idToName.put(index,name);
            nameToId.put(name,index);
            index++;
        }
        profiles.computeIfAbsent(name,a -> new ConcurrentLinkedQueue<Long>()).add(nano ? System.nanoTime() : System.currentTimeMillis());
    }

    public void printProfile() {
        double lastAvg = -1;
        for(int x = 0; x < index; x++) {
            String name = idToName.get(x);
            double avg = 0;
            int t = 1;
            for(double y : profiles.get(name)) {
                avg += (y - avg) / t;
                t++;
            }
            if(lastAvg != -1) {
                System.out.println(name + " " + (avg - lastAvg) + (nano ? " ns" : " ms"));
            }
            lastAvg = avg;
        }
    }



}
