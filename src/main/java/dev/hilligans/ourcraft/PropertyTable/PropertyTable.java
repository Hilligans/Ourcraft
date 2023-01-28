package dev.hilligans.ourcraft.PropertyTable;

import java.util.ArrayList;

public class PropertyTable {

    public PropertyTableKeyset set;
    public long[] table;

    public PropertyTable(PropertyTableKeyset set, ArrayList<String> keys) {
        this.set = set;
        table = new long[set.getArrayLength()];
        for(String key : keys) {
            int val = set.getOffset(key);
            if(val == -1) {
                throw new RuntimeException("Key missing from keyset : " + key);
            }
            set(val,true);
        }
    }

    public void set(int index, boolean val) {
        int pos = Math.floorDiv(index, 64);
        int offset = index % 64;
        long toReplace = table[pos] & 1L << offset;
        if((val && toReplace == 0) || (!val && toReplace != 0)) {
            table[pos] = table[pos] ^ (1L << offset);
        }
    }

    public boolean get(int index) {
        int pos = Math.floorDiv(index, 64);
        int offset = index % 64;
        return (table[pos] & 1L << offset) != 0;
    }
}
