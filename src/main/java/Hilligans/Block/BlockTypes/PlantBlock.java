package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockShapes.XBlockShape;
import Hilligans.Data.Other.BlockState;
import Hilligans.Util.Vector5f;

public class PlantBlock extends Block {
    public PlantBlock(String name) {
        super(name);
        blockShape = new XBlockShape();
        transparentTexture = true;
        canWalkThrough = true;
    }
}
