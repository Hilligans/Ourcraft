package dev.Hilligans.ourcraft.Block;

import dev.Hilligans.ourcraft.Block.BlockState.*;
import dev.Hilligans.ourcraft.World.NewWorldSystem.IMethodResult;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.TextAtlas;
import dev.Hilligans.ourcraft.Client.Rendering.Renderer;
import dev.Hilligans.ourcraft.Data.Descriptors.TagCollection;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.Item.ItemStack;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockProperties;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.ourcraft.Data.Other.RayResult;
import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.Util.Side;
import dev.Hilligans.ourcraft.World.DataProvider;
import dev.Hilligans.ourcraft.World.DataProviders.ShortBlockState;
import dev.Hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.Hilligans.ourcraft.World.World;
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
    public ModContent modContent;

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

    public void setModContent(ModContent modContent) {
        this.modId = modContent.getModID();
        this.modContent = modContent;
        this.blockProperties.source = modId;
        this.blockProperties.blockTextureManager.source = modId;
        this.blockProperties.blockTextureManager.textureSource = modId;
    }

    public void registerBlockStates(BlockStateBuilder builder) {
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

    public BlockState getDefaultState() {
        return new BlockState(this);
    }

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

    public Item getBlockItem() {
        return modContent.gameInstance.getItem(getName());
    }

    public boolean getAllowedMovement(Vector3d motion, Vector3d pos, BlockPos blockPos, BoundingBox boundingBox, World world) {
        return blockProperties.canWalkThrough || !getBoundingBox(world, blockPos).intersectsBox(boundingBox, blockPos.get3d(), pos, motion.x, motion.y, motion.z);
    }

    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return blockProperties.blockShape.getBoundingBox(world,pos);
    }

    public void generateTextures(TextAtlas textAtlas) {
        blockProperties.blockTextureManager.generate(textAtlas);
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockPos blockPos, int x, int z) {
        blockProperties.blockShape. addVertices(primitiveBuilder,side,size,blockState,blockProperties.blockTextureManager, new BlockPos(x,blockPos.y,z).get3f());
    }

    public int getSide(BlockState blockState, int side) {
        return blockProperties.blockShape.getSide(blockState,side);
    }

    public int getSide(IBlockState blockState, int side) {
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

    public void addMethodKeys(ArrayList<String> keys) {}

    public IBlockState randomTick(IMethodResult result, IBlockState state, IWorld world, BlockPos pos, Random random) {
        return null;
    }

    //TODO add break source
    public void onBreak(IMethodResult result, IBlockState state, IWorld world) {}

    //TODO add placer
    public void onPlace(IMethodResult result, IBlockState state, IWorld world) {}

    //TODO add placing context
    public IBlockState getStateForPlacement() {
        return getDefaultState1();
    }

    //TODO pull state from table
    public IBlockState getDefaultState1() {
        return table.getBlockState(0);
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, IBlockState blockState, BlockPos blockPos, int x, int z) {
        blockProperties.blockShape.addVertices(primitiveBuilder,side,size,blockState,blockProperties.blockTextureManager,new Vector3f(), x,blockPos.y,z);
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
            JSONObject jsonObject = (JSONObject) Ourcraft.GAME_INSTANCE.RESOURCE_LOADER.getResource(new ResourceLocation(blockProperties.path, modId));
            if(jsonObject != null) {
                if (overrides != null) {
                    BlockProperties.recursivelyOverride(jsonObject, overrides);
                }
                blockProperties.read(jsonObject);
            }
        }

        if(modContent.gameInstance.side == Side.CLIENT) {
           // generateTextures();
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
    public String getIdentifierName() {
        return modId + ":" + name;
    }

    @Override
    public String getUniqueName() {
        return "block." + modId + "." + name;
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
