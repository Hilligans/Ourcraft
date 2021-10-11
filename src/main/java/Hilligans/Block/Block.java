package Hilligans.Block;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Data.Other.*;
import Hilligans.Data.Other.BlockStates.BlockState;
import Hilligans.Data.Other.BlockStates.DataBlockState;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Item.ItemStack;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.Ourcraft;
import Hilligans.World.DataProvider;
import Hilligans.World.DataProviders.ShortBlockState;
import Hilligans.World.World;
import Hilligans.WorldSave.WorldLoader;
import org.joml.Vector3d;
import org.json.JSONObject;

public class Block {

    public String name;
    public String modId;
    public short id;
    public BlockProperties blockProperties;
    private Block droppedBlock;
    public ModContent modContent;

    public Block(String name, BlockProperties blockProperties) {
        this.name = name;
        id = Blocks.getNextId();
        this.blockProperties = blockProperties;
        droppedBlock = this;
    }

    public Block(String name, BlockProperties blockProperties, String modId) {
        this(name,blockProperties);
        this.modId = modId;
    }

    public Block(String name, String path, String modId, JSONObject overrides) {
        this(name,BlockProperties.loadProperties(path,overrides));
    }

    public Block setBlockDrop(Block blockDrop) {
        this.droppedBlock = blockDrop;
        return this;
    }

    public void setModContent(ModContent modContent) {
        this.modId = modContent.modID;
    }

    public String getName() {
        return "block." + modId + "." + name;
    }

    public boolean activateBlock(World world, PlayerEntity playerEntity, BlockPos pos) {
        return false;
    }

    public void onPlace(World world, BlockPos blockPos) {}

    public void onBreak(World world, BlockPos blockPos) {}

    public void onUpdate(World world, BlockPos blockPos) {}

    public void tickBlock(World world, BlockPos blockPos) {}

    public void tick(World world, BlockPos pos) {}

    public void randomTick(World world, BlockPos pos) {}

    public void reload() {
        if(blockProperties.fromFile) {
            BlockTextureManager blockTextureManager = blockProperties.blockTextureManager;
            blockProperties = BlockProperties.loadProperties(blockProperties.path, blockProperties.overrides);
            blockProperties.blockTextureManager = blockTextureManager;
        }
    }

    public BlockState getDefaultState() {
        return new BlockState(this);
    }

    public boolean hasBlockState() {
        return blockProperties.blockStateSize != 0;
    }

    public int blockStateByteCount() {
        return blockProperties.blockStateSize;
    }

    public BlockState getStateWithData(short data) {
        if(blockProperties.blockStateSize != 0) {
            return new DataBlockState(this,new ShortBlockState(data));
        } else {
            return new BlockState(this);
        }
    }

    public BlockState getStateForPlacement(Vector3d playerPos, RayResult rayResult) {
        switch (blockProperties.placementMode) {
            default -> {
                return getDefaultState();
            }
            case "slab" -> {
                return new DataBlockState(this, new ShortBlockState((short) rayResult.side));
            }
            case "directional" -> {
                double x = playerPos.x - rayResult.pos.x;
                double z = playerPos.z - rayResult.pos.z;
                return new DataBlockState(this,new ShortBlockState((short)getSideFromLargest(x,z)));
            }

            case "directionalInverted" -> {
                double x = playerPos.x - rayResult.pos.x;
                double z = playerPos.z - rayResult.pos.z;
                return new DataBlockState(this,new ShortBlockState((short)getSideFromLargestInverted(x,z)));
            }
        }
    }

    public Block getDroppedBlock() {
        return droppedBlock;
    }

    public boolean getAllowedMovement(Vector3d motion, Vector3d pos, BlockPos blockPos, BoundingBox boundingBox, World world) {
        return blockProperties.canWalkThrough || !getBoundingBox(world, blockPos).intersectsBox(boundingBox, blockPos.get3d(), pos, motion.x, motion.y, motion.z);
    }

    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return blockProperties.blockShape.getBoundingBox(world,pos);
    }

    public void generateTextures() {
        blockProperties.blockTextureManager.generate();
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockPos blockPos, int x, int z) {
        blockProperties.blockShape. addVertices(primitiveBuilder,side,size,blockState,blockProperties.blockTextureManager, new BlockPos(x,blockPos.y,z).get3f());
    }

    public int getSide(BlockState blockState, int side) {
        return blockProperties.blockShape.getSide(blockState,side);
    }

    public static BlockPos getBlockPos(int side) {
        switch (side) {
            case 0:
                return new BlockPos(0,0,-1);
            case 1:
                return new BlockPos(0,0,1);
            case 2:
                return new BlockPos(-1,0,0);
            case 3:
                return new BlockPos(1,0,0);
            case 4:
                return new BlockPos(0,-1,0);
            default:
                return new BlockPos(0,1,0);
        }
    }

    public static int getSideFromLargest(double x, double z) {
        if(Math.abs(x) > Math.abs(z)) {
            return x > 0 ? 3 : 2;
        } else {
            return z > 0 ? 1 : 0;
        }
    }

    public static int getSideFromLargestInverted(double x, double z) {
        if(Math.abs(x) > Math.abs(z)) {
            return x > 0 ? 2 : 3;
        } else {
            return z > 0 ? 0 : 1;
        }
    }

    public void renderItem(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        Renderer.renderBlockItem(matrixStack,x,y,size,this, itemStack);
    }

    public DataProvider getDataProvider() {
        return null;
    }

    @Override
    public String toString() {
        return "Block{" +
                "name='" + name + '\'' +
                ", modId='" + modId + '\'' +
                ", id=" + id +
                ", blockProperties=" + blockProperties +
                '}';
    }

    public static final int UP = 5;
    public static final int DOWN = 4;
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    public static final int EAST = 3;

    public static final int[] rotationSides = {0,1,2,3,4,5,0,0,   5,4,2,3,0,1,0,0,    1,0,2,3,5,4,0,0,    4,5,2,3,1,0,0,0,
                                               3,2,0,1,4,5,0,0,   5,4,0,1,3,2,0,0,    3,2,0,1,5,4,0,0,    5,4,1,0,2,3,0,0,
                                               1,0,3,2,4,5,0,0,   5,4,3,2,1,0,0,0,    0,1,3,2,5,4,0,0,    4,5,3,2,0,1,0,0,
                                               2,3,1,0,4,5,0,0,   5,4,1,0,2,3,0,0,    3,2,1,0,5,4,0,0,    4,5,1,0,3,2,0,0};

    public static int getSide(int side, int rotX, int rotY) {
        int value = side | rotX << 3 | rotY << 5;
        return Block.rotationSides[value];
    }
}
