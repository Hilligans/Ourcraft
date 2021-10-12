package dev.Hilligans.Data.Other;

public class JoinedBlockTemplate extends BlockTemplate {

    public JoinedBlockTemplate(BlockTemplate... blockTemplates) {
        int size = 0;
        BlockPos[] blockPos;
        for(BlockTemplate blockTemplate : blockTemplates) {
            size += blockTemplate.positions.length;
        }
        blockPos = new BlockPos[size];
        size = 0;
        for(BlockTemplate blockTemplate : blockTemplates) {
            int arraySize = blockTemplate.positions.length;
            System.arraycopy(blockTemplate.positions,0,blockPos,size,arraySize);
            size += arraySize;
        }
        this.positions = blockPos;
    }
}
