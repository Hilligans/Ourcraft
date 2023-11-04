package dev.hilligans.ourcraft.WorldSave;

import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import dev.hilligans.ourcraft.World.Chunk;
import dev.hilligans.ourcraft.Util.BitArray;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldCompressor {




    public static ArrayList<Long> asCompressedStream(Chunk chunk) {
        ArrayList<Long> vals = new ArrayList<>();
        try {
            ArrayList<Tuple<BlockState, Integer>> list = chunk.getBlockChainedList();
            HashMap<BlockState, Boolean> uniqueBlocks = new HashMap<>();
            int maxChain = 0;
            for (Tuple<BlockState, Integer> tuple : list) {
                uniqueBlocks.putIfAbsent(tuple.typeA, true);
                if (tuple.typeB > maxChain) {
                    maxChain = tuple.typeB;
                }
            }
            int paletteSize = getBitCount(uniqueBlocks.size());
            if (maxChain > 8) {
                maxChain = 8;
            }
            BitArray bitArray = new BitArray();
            bitArray.write(uniqueBlocks.size(),16);
            bitArray.write(paletteSize,6);
            bitArray.write(maxChain,6);
            StringBuilder stringBuilder = new StringBuilder(ensureSize(Integer.toBinaryString(uniqueBlocks.size()), 16));
            stringBuilder.append(ensureSize(Integer.toBinaryString(paletteSize),6));
            stringBuilder.append(ensureSize(Integer.toBinaryString(maxChain),6));
            HashMap<String, Integer> blocks = new HashMap<>();
            int x = 0;
            for (BlockState blockState : uniqueBlocks.keySet()) {
                bitArray.write(blockState.get(),32);
                stringBuilder.append(ensureSize(Integer.toBinaryString(blockState.get()), 32));
                blocks.put(blockState.getBlock().name, x);
                x++;
            }
            for (Tuple<BlockState, Integer> tuple : list) {
              //  bitArray.write(blocks.get(doubleTypeWrapper.typeA.getBlock().name),paletteSize);
                stringBuilder.append(ensureSize(Integer.toBinaryString(blocks.get(tuple.typeA.getBlock().name)), paletteSize));
                int size = tuple.typeB;
                while (size > 255) {
                    stringBuilder.append("11111111");
                   // bitArray.write(0b11111111,8);
                    size -= 255;
                }
                stringBuilder.append(ensureSize(Integer.toBinaryString(blocks.get(tuple.typeA.getBlock().name)), maxChain));
            }
            // createChunkFromCompressedStream(bitArray);
            //System.out.println(stringBuilder);
            //System.out.println("0000000000000" + bitArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vals;
    }

    private static int getBitCount(int count) {
        return (int) Math.ceil(Math.log(count) / Math.log(2));
    }

    private static String ensureSize(String input, int size) {
        if(input.length() > size) {
            return input.substring(input.length() - size);
        } else if(input.length() < size) {
            StringBuilder inputBuilder = new StringBuilder(input);
            for(int x = 0; x < size - input.length(); x++) {
                inputBuilder.insert(0, "0");
            }
            return inputBuilder.toString();
        } else {
            return input;
        }
    }

}
