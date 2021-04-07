package Hilligans;

import Hilligans.Block.Block;
import Hilligans.Client.*;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Screen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.CreativeInventoryScreen;
import Hilligans.Client.Rendering.Screens.EscapeScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import Hilligans.Client.Rendering.Widgets.Widget;
import Hilligans.Client.Rendering.World.*;
import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.ClientPlayerData;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Entity.Entity;
import Hilligans.EventHandler.Event;
import Hilligans.EventHandler.EventBus;
import Hilligans.EventHandler.Events.GLInitEvent;
import Hilligans.Item.ItemStack;
import Hilligans.Network.Packet.Client.*;
import Hilligans.Network.PacketBase;
import Hilligans.Tag.Tag;
import Hilligans.Util.Settings;
import Hilligans.Util.Util;
import Hilligans.Block.Blocks;
import Hilligans.Client.Key.KeyHandler;
import Hilligans.Client.Key.KeyPress;
import Hilligans.Client.Rendering.Screens.JoinScreen;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Client.Camera;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BlockState;
import Hilligans.World.ClientWorld;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.awt.datatransfer.Clipboard;
import java.io.*;
import java.net.Socket;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }


    public static void main(String[] args) {

        client = new Client();
        client.startClient();
    }

    public static int getWindowX() {
        return client == null ? 0 : client.windowX;
    }

    public static int getWindowY() {
        return client == null ? 0 : client.windowY;
    }
}
