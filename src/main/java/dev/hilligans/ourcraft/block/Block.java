package dev.hilligans.ourcraft.block;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.block.blockstate.BlockStateBuilder;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.blockstate.IBlockStateTable;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.Renderer;
import dev.hilligans.ourcraft.client.rendering.graphics.IPrimitiveBuilder;
import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.client.rendering.newrenderer.TextAtlas;
import dev.hilligans.ourcraft.data.descriptors.TagCollection;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.BlockProperties;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.data.other.RayResult;
import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.data.other.blockstates.DataBlockState;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.item.Item;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.resource.ResourceLocation;
import dev.hilligans.ourcraft.server.concurrent.Lock;
import dev.hilligans.ourcraft.util.Side;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.world.DataProvider;
import dev.hilligans.ourcraft.world.data.providers.ShortBlockState;
import dev.hilligans.ourcraft.world.newworldsystem.IAtomicChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IMethodResult;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Block implements IRegistryElement {

    public IBlockStateTable table;
    public String name;
    public String modId;
    public short id;
    public BlockProperties blockProperties;
    public Block droppedBlock;
    public ModContainer source;

    public String path;
    public JSONObject overrides;

    public Block(String name, BlockProperties blockProperties) {
        this.name = name;
        this.blockProperties = blockProperties;
        blockProperties.setName(name);
        droppedBlock = this;
    }

    public Block(String name, BlockProperties blockProperties, String modId) {
        this(name,blockProperties);
        this.modId = modId;
    }

    public Block(String name, String path, JSONObject overrides) {
        this(name, BlockProperties.loadProperties(path,overrides));
    }

    public Block setBlockDrop(Block blockDrop) {
        this.droppedBlock = blockDrop;
        return this;
    }



    public void assignOwner(ModContainer source) {
        this.modId = source.getModID();
        this.source = source;
        this.blockProperties.source = modId;
        this.blockProperties.blockTextureManager.source = modId;
        this.blockProperties.blockTextureManager.textureSource = modId;
    }

    public void registerBlockStates(BlockStateBuilder builder) {
    }
    public String getName() {
        return "block." + modId + "." + name;
    }

    public boolean activateBlock(IWorld world, PlayerEntity playerEntity, BlockPos pos) {
        return false;
    }

    public void onPlace(IWorld world, BlockPos blockPos) {}

    public void onBreak(IWorld world, BlockPos blockPos) {}


    //TODO fix
    /*
    public void reload() {
        if(blockProperties.fromFile) {
            BlockTextureManager blockTextureManager = blockProperties.blockTextureManager;
            blockProperties = BlockProperties.loadProperties(blockProperties.path, blockProperties.overrides);
            blockProperties.blockTextureManager = blockTextureManager;
        }
    }
     */

    public boolean hasBlockState() {
        return blockProperties.blockStateSize != 0;
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
                return null;
                //return getDefaultState();
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

    public Item getBlockItem() {
        return source.gameInstance.getItem(getName());
    }

    public boolean getAllowedMovement(Vector3d motion, Vector3d pos, BlockPos blockPos, BoundingBox boundingBox, IWorld world) {
        return blockProperties.canWalkThrough || !getBoundingBox(world, blockPos).intersectsBox(boundingBox, blockPos.get3d(), pos, motion.x, motion.y, motion.z);
    }

    public BoundingBox getBoundingBox(IWorld world, BlockPos pos) {
        return blockProperties.blockShape.getBoundingBox(world,pos);
    }

    public BoundingBox getBoundingBox(IBlockState blockState) {
        return blockProperties.blockShape.getBoundingBox(blockState);
    }

    public void generateTextures(TextAtlas textAtlas) {
        blockProperties.blockTextureManager.generate(textAtlas);
    }

    public void addVertices(TextAtlas textAtlas, PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockPos blockPos, int x, int z) {
        blockProperties.blockShape. addVertices(textAtlas, primitiveBuilder,side,size,blockState,blockProperties.blockTextureManager, new BlockPos(x,blockPos.y,z).get3f());
    }

    public int getSide(BlockState blockState, int side) {
        return blockProperties.blockShape.getSide(blockState,side);
    }

    public int getSide(IBlockState blockState, int side) {
        return blockProperties.blockShape.getSide(blockState,side);
    }


    public static BlockPos getBlockPos(int side) {
        return switch (side) {
            case 0 -> new BlockPos(0, 0, -1);
            case 1 -> new BlockPos(0, 0, 1);
            case 2 -> new BlockPos(-1, 0, 0);
            case 3 -> new BlockPos(1, 0, 0);
            case 4 -> new BlockPos(0, -1, 0);
            default -> new BlockPos(0, 1, 0);
        };
    }

    public static final BlockPos[] list = {
            new BlockPos(0, 0, -1),
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, -1, 0),
            new BlockPos(0, 1, 0)
    };

    public static BlockPos getBlockPosImmutable(int side) {
        return list[side];
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

    public void addMethodKeys(ArrayList<String> keys) {}

    public IBlockState randomTick(IMethodResult result, IBlockState state, IWorld world, BlockPos pos, Random random) {
        return null;
    }

    public void randomTick(Lock lock, IMethodResult methodResult, IBlockState state, IChunk chunk, IWorld world, BlockPos pos, Random random) {
        IBlockState newState = randomTick(methodResult, state, world, pos, random);
        if(newState != null) {
            if(chunk instanceof IAtomicChunk atomicChunk) {
                atomicChunk.setBlockStateAtomic(lock, pos.x, pos.y, pos.z, newState);
            } else {
                if(!lock.hasLock(chunk.getChunkPos())) {
                    lock.acquire(chunk.getChunkPos());
                }
                chunk.setBlockState(pos.x, pos.y, pos.z, newState);
            }
        }
    }

    //TODO add break source
    public void onBreak(IMethodResult result, IBlockState state, IWorld world) {}

    //TODO add placer
    public void onPlace(IMethodResult result, IBlockState state, IWorld world) {}

    //TODO add placing context
    public IBlockState getStateForPlacement() {
        return getDefaultState();
    }

    //TODO pull state from table
    public IBlockState getDefaultState() {
        return table.getBlockState(0);
    }

    public void addVertices(TextAtlas textAtlas, PrimitiveBuilder primitiveBuilder, int side, float size, IBlockState blockState, BlockPos blockPos, int x, int z) {
        blockProperties.blockShape.addVertices(textAtlas, primitiveBuilder,side,size,blockState,blockProperties.blockTextureManager,new Vector3f(), x,blockPos.y,z);
    }

    public void addVertices(IPrimitiveBuilder primitiveBuilder, int side, float size, IBlockState blockState, BlockPos blockPos, int offsetX, int offsetY, int offsetZ) {

    }

    public int getRotation(IBlockState state) {
        return 0;
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

    @Override
    public void load(GameInstance gameInstance) {
        if(blockProperties.path != null && blockProperties.fromFile) {
            JSONObject jsonObject = (JSONObject) gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation(blockProperties.path, modId));
            if(jsonObject != null) {
                if (overrides != null) {
                    BlockProperties.recursivelyOverride(jsonObject, overrides);
                }
                blockProperties.read(jsonObject);
            }
        }
    }

    @Override
    public void setUniqueID(int id) {
        this.id = (short) id;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return modId;
    }

    @Override
    public String getResourceType() {
        return "block";
    }

    @Override
    public TagCollection getTagCollection() {
        return blockProperties.tags;
    }

    public IBlockStateTable getTable() {
        return table;
    }

    public void setTable(IBlockStateTable table) {
        this.table = table;
    }
}
