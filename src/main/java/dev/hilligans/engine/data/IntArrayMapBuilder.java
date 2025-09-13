package dev.hilligans.engine.data;

import java.util.ArrayList;

public class IntArrayMapBuilder {

    public int maxChainSize;
    public int size;
    public ArrayList<Integer> vals = new ArrayList<>();

    public IntArrayMapBuilder(int size, int maxChainSize) {
        this.maxChainSize = maxChainSize;
        this.size = size;
    }

    public void add(int val) {
        vals.add(val);
    }

    public IntArrayMap build() {
        ArrayList<Integer> list = new ArrayList<>(vals.size());
        IntArrayMap map;

        do {
            map = new IntArrayMap(size);
            size++;
            list.clear();

            for (Integer val : vals) {
                if (!map.add(val)) {
                    list.add(val);
                }
            }
            for (Integer val : list) {
                map.addLast(val);
            }
        } while(map.getMaxChainSize() > maxChainSize);

        return map;
    }
}
