package Hilligans.Data.Other;

import Hilligans.Block.Block;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Container.ContainerFetcher;
import Hilligans.Container.Containers.SlabBlockContainer;
import Hilligans.Data.Primitives.TripleTypeWrapper;
import Hilligans.Item.Item;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.ClientNetworkInit;
import Hilligans.Network.Packet.Server.SCreateTexture;
import Hilligans.Network.Packet.Server.SRegisterBlock;
import Hilligans.Network.Packet.Server.SRegisterContainer;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Util.Settings;
import io.netty.channel.ChannelHandlerContext;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerSidedData {

    public final ArrayList<Block> BLOCKS = new ArrayList<>();
    public final HashMap<String, Block> MAPPED_BLOCKS = new HashMap<>();

    public final ArrayList<Item> ITEMS = new ArrayList<>();
    public final HashMap<String, Item> MAPPED_ITEMS = new HashMap<>();

    public final ArrayList<Texture> TEXTURES = new ArrayList<>();
    public final HashMap<String, Texture> MAPPED_TEXTURES = new HashMap<>();

    public final ArrayList<ContainerFetcher> CONTAINERS = new ArrayList<>();
    public final HashMap<String, ContainerFetcher> MAPPED_CONTAINER = new HashMap<>();

    public final ConcurrentLinkedQueue<Texture> QUEUED_TEXTURES = new ConcurrentLinkedQueue<>();
    public final ArrayList<TripleTypeWrapper<BufferedImage, String, Boolean>> IMAGES = new ArrayList<>();
    //Container,name,path
    public final ArrayList<TripleTypeWrapper<Container,String,String>> QUEUED_CONTAINERS = new ArrayList<>();

    public boolean hasGenerated = false;

    public long version;

    public ServerSidedData(long version) {
        this.version = version;
    }

    public void clear() {
        BLOCKS.clear();
        MAPPED_BLOCKS.clear();
        ITEMS.clear();
        MAPPED_ITEMS.clear();
        TEXTURES.clear();
        MAPPED_TEXTURES.clear();
        CONTAINERS.clear();
        MAPPED_CONTAINER.clear();
        IMAGES.clear();
        QUEUED_CONTAINERS.clear();
        hasGenerated = false;
        version = Long.MIN_VALUE;
    }

    public void tick() {
        if(!QUEUED_TEXTURES.isEmpty()) {
            for(Texture texture : QUEUED_TEXTURES) {
                texture.register1();
            }
            QUEUED_TEXTURES.clear();
        }
    }

    public void putBlock(String name, Block block) {
        BLOCKS.add(block);
        MAPPED_BLOCKS.put(name,block);
    }

    public void putItem(String name, Item item) {
        ITEMS.add(item);
        MAPPED_ITEMS.put(name,item);
    }

    public void putTexture(String name, Texture texture) {
        QUEUED_TEXTURES.add(texture);
        TEXTURES.add(texture);
        MAPPED_TEXTURES.put(name,texture);
    }

    public void putContainer(String name, ContainerFetcher containerFetcher) {
        CONTAINERS.add(containerFetcher);
        MAPPED_CONTAINER.put(name,containerFetcher);
    }

    public void generateData() {
        hasGenerated = true;
        IMAGES.clear();
        for(Block block : BLOCKS) {
            BlockTextureManager blockTextureManager = block.blockProperties.blockTextureManager;
            BufferedImage bufferedImage = WorldTextureManager.loadImage("/Blocks/" + blockTextureManager.location);
            IMAGES.add(new TripleTypeWrapper<>(bufferedImage, blockTextureManager.location.substring(0, blockTextureManager.location.length() - 4), true));
            for(int x = 0; x < 6; x++) {
                if(blockTextureManager.textureNames != null) {
                    if (blockTextureManager.textureNames[x] != null) {
                        BufferedImage bufferedImage1 = WorldTextureManager.loadImage("/Blocks/" + blockTextureManager.textureNames[x]);
                        IMAGES.add(new TripleTypeWrapper<>(bufferedImage1,blockTextureManager.textureNames[x].substring(0, blockTextureManager.textureNames[x].length() - 4), true));
                    }
                }
            }
        }

        for(TripleTypeWrapper<Container,String,String> type : QUEUED_CONTAINERS) {
            BufferedImage bufferedImage = WorldTextureManager.loadImage(type.getTypeC());
            IMAGES.add(new TripleTypeWrapper<>(bufferedImage,type.getTypeB(),false));
        }

    }

    public void sendDataToClient(ChannelHandlerContext ctx) {
        if(!hasGenerated) {
            generateData();
        }
        for(TripleTypeWrapper<BufferedImage, String, Boolean> type : IMAGES) {
            ServerNetworkHandler.sendPacket(new SCreateTexture(type.getTypeA(),type.getTypeB(),type.getTypeC()));
        }
        for(Block block: ServerSidedData.getInstance().BLOCKS) {
            ServerNetworkHandler.sendPacket(new SRegisterBlock(block),ctx);
        }
        for(TripleTypeWrapper<Container,String,String> type : QUEUED_CONTAINERS) {
            ServerNetworkHandler.sendPacket(new SRegisterContainer(type.getTypeA(),type.getTypeB()),ctx);
        }
    }

    public void register() {
        //QUEUED_CONTAINERS.add(new TripleTypeWrapper<>(new SlabBlockContainer(),"slab_chest","GUI/slab_chest.png"));
    }


    static ServerSidedData instance = new ServerSidedData(System.currentTimeMillis());

    public static ServerSidedData getInstance() {
        if(Settings.isServer) {
            return instance;
        } else {
            if(ClientNetworkHandler.clientNetworkHandler != null) {
                String path = ClientNetworkHandler.clientNetworkHandler.ip + ":" + ClientNetworkHandler.clientNetworkHandler.port;
                ServerSidedData data = cachedData.get(path);
                if (data == null) {
                    data = new ServerSidedData(Long.MIN_VALUE);
                    cachedData.put(path, data);
                }
                return data;
            }
            return new ServerSidedData(Long.MIN_VALUE);


        }
    }

    public static HashMap<String, ServerSidedData> cachedData = new HashMap<>();

}
