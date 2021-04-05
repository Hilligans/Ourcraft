package Hilligans;

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
import Hilligans.Entity.Entity;
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

    public static long window;

    public static int playerId;

    public static int windowX = 1600;
    public static int windowY = 800;
    public static boolean joinServer = true;
    public static boolean valid = false;

    public static boolean screenShot = false;
    public static boolean renderWorld = false;

    public static Screen screen;

    public static int shaderProgram;
    public static int colorShader;
    public static int opaqueColorShader;
    public static int transparentColorShader;
    public static int lineShader;
    public static int texture;

    public static boolean refreshTexture = false;

    public static int outLine;

    public static ClientWorld clientWorld;

    public static String name = "";

    public static Clipboard clipboard;

    public static void main(String[] args) {
        PacketBase.register();
        Container.register();
        Tag.register();
        Widget.register();

        //System.out.println( ClientPlayerData.email + " is valid? " +
        //        isAddressValid( ClientPlayerData.email ) );


        System.setProperty("java.awt.headless", "true");

        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);


        window = glfwCreateWindow(windowX,windowY,"Ourcraft",NULL,NULL);
        if(window == NULL) {
            System.out.println("Failed to create window");
            glfwTerminate();
            return;
        }
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        Blocks.generateTextures();

        outLine = WorldTextureManager.loadAndRegisterTexture("outline.png");
        PlayerEntity.imageId = WorldTextureManager.loadAndRegisterTexture("player.png");
        Renderer.cursorId = WorldTextureManager.loadAndRegisterTexture("cursor.png");

        StringRenderer.instance.loadCharacters1();
        for(Texture texture : Textures.TEXTURES) {
            texture.register();
        }


        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(renderWorld) {
                    if (screen == null) {
                        openScreen(new EscapeScreen());
                        //glfwSetWindowShouldClose(window, true);
                    } else {
                        closeScreen();
                    }
                } else {
                    openScreen(new JoinScreen());
                }
            }
        },KeyHandler.GLFW_KEY_ESCAPE);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                System.exit(1);
            }
        },KeyHandler.GLFW_KEY_F11);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(screen == null) {
                    if(ClientPlayerData.creative) {
                        openScreen(new CreativeInventoryScreen());
                    } else {
                        openScreen(new InventoryScreen());
                    }
                } else if(screen instanceof ContainerScreen) {
                    closeScreen();
                }
            }
        },GLFW_KEY_E);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                ClientPlayerData.f3 = !ClientPlayerData.f3;
            }
        },GLFW_KEY_F3);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(screen != null) {
                    if(ClientPlayerData.openContainer != null) {
                        Slot slot = ClientPlayerData.openContainer.getSlotAt((int)Camera.newX,(int)Camera.newY);
                        if(slot != null) {
                            if(KeyHandler.keyPressed[GLFW_KEY_LEFT_CONTROL]) {
                                slot.setContents(ItemStack.emptyStack());
                                ClientNetworkHandler.sendPacketDirect(new CDropItem(slot.id,(byte)-1));
                            } else {
                                slot.getContents().removeCount(1);
                                ClientNetworkHandler.sendPacketDirect(new CDropItem(slot.id,(byte)1));
                            }
                        }
                    }
                } else {

                }
            }
        },GLFW_KEY_Q);

        //glReadPixels();

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                screenShot = true;
            }
        }, GLFW_KEY_F2);

       /* KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                openScreen(new TagEditorScreen());
            }
        }, GLFW_KEY_H);
        */

        screen = new JoinScreen();


        texture = WorldTextureManager.instance.registerTexture();

        clientWorld = new ClientWorld();

        shaderProgram = ShaderManager.registerShader(Util.shader,Util.fragmentShader1);
        colorShader = ShaderManager.registerShader(Util.coloredShader,Util.fragmentShader1);
        transparentColorShader = colorShader;
        opaqueColorShader = ShaderManager.registerShader(Util.coloredShader,Util.fragmentShader2);
        lineShader = ShaderManager.registerShader(Util.lineShader, Util.lineFragment);
        Renderer.register();
        Entity.register();


        glEnable(GL_DEPTH);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);


        glCullFace(GL_FRONT);
        glFrontFace(GL_CW);

        PlayerMovementThread playerMovementThread = new PlayerMovementThread(window);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(playerMovementThread, 0, 5, TimeUnit.MILLISECONDS);

        //Renderer.create(StringRenderer.instance.mappedCharacters);
        Renderer.create(texture);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        createCallbacks();



        while(!glfwWindowShouldClose(window)) {
            //processInput(window);

            mouseLocked = screen == null;
            render();
            glfwPollEvents();

            Camera.updateMouse();
        }
        ClientNetworkHandler.close();
        glfwTerminate();
        System.exit(1);
    }

    static long timeSinceLastDraw = 0;
    static float drawTime = 1000f / Settings.maxFps;

    public static void render() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - timeSinceLastDraw < drawTime) {
            return;
        }
        countFPS();
        timeSinceLastDraw = currentTime;
        //System.out.println("RENDERING");

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(refreshTexture) {
            Blocks.generateTextures();
            texture = WorldTextureManager.instance.registerTexture();
            refreshTexture = false;
        }

        ServerSidedData.getInstance().tick();

        glUseProgram(shaderProgram);
        glBindTexture(GL_TEXTURE_2D, texture);
        MatrixStack matrixStack = Camera.getWorldStack();
        matrixStack.applyColor();
        matrixStack.applyTransformation();

        MatrixStack screenStack = Camera.getScreenStack();
        screenStack.applyColor();
        screenStack.applyTransformation();


        if(renderWorld) {
            clientWorld.tick();
            clientWorld.render(matrixStack);

            BlockPos pos = clientWorld.traceBlockToBreak(Camera.pos.x, Camera.pos.y + Camera.playerBoundingBox.eyeHeight, Camera.pos.z, Camera.pitch, Camera.yaw);
            if (pos != null) {
                BlockState blockState = clientWorld.getBlockState(pos);
                int id = blockState.getBlock().blockShape.generateOutline(clientWorld, pos);
                glUseProgram(lineShader);
                GL30.glBindVertexArray(id);
                matrixStack.push();
                matrixStack.translate(pos.x, pos.y, pos.z);
                matrixStack.applyTransformation(ClientMain.lineShader);
                glDrawElements(GL_LINES, 24, GL_UNSIGNED_INT, 0);
                matrixStack.pop();
                VAOManager.destroyBuffer(id);
            }

            glUseProgram(shaderProgram);

            if (ClientPlayerData.f3) {
                StringRenderer.drawString(screenStack, Camera.getString(), windowX / 2, 0, 0.5f);
                StringRenderer.drawString(screenStack, "FPS:" + fps, windowX / 2, 29, 0.5f);
                StringRenderer.drawString(screenStack, clientWorld.biomeMap.getBiome((int) Camera.pos.x, (int) Camera.pos.z).name, windowX / 2, 58, 0.5f);
                StringRenderer.drawString(screenStack, "vel y:" + Camera.velY, windowX / 2, 87, 0.5f);
                Runtime runtime = Runtime.getRuntime();
                long usedMB = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                StringRenderer.drawString(screenStack, "Memory:" + usedMB + "MB",windowX / 2, 126, 0.5f);
            }

            InventoryScreen.drawHotbar(screenStack);
            ChatWindow.render1(screenStack);
        }

        if(screen != null) {
            screen.render(screenStack);
            ClientPlayerData.heldStack.renderStack(screenStack, (int) (Camera.newX - Settings.guiSize * 8), (int) (Camera.newY - Settings.guiSize * 8));
        }

        
        glfwSwapBuffers(window);

        if(screenShot) {
            screenShot = false;
            ScreenShot.takeScreenShot();
        }
    }

    static double lastTime = glfwGetTime();
    static int nbFrames = 0;

    static int fps;

    static void countFPS() {

            double currentTime = glfwGetTime();
            nbFrames++;
            if ( currentTime - lastTime >= 1.0 ){

                fps = nbFrames;
                nbFrames = 0;
                lastTime += 1.0;
            }
    }

    public static void processInput(long window) {
        if(screen == null) {
            if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
                Camera.strafeLeft();
            if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
                Camera.strafeRight();
            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
                Camera.moveForeWard();
            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
                Camera.moveBackWard();
            if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS)
                Camera.moveUp();
            if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
                Camera.moveDown();
        }
    }

    public static boolean mouseLocked = false;


    public static void closeScreen() {
        if(!ClientPlayerData.heldStack.isEmpty()) {
            ClientNetworkHandler.sendPacketDirect(new CDropItem((short)-1,(byte)-1));
            ClientPlayerData.heldStack = ItemStack.emptyStack();
        }
        if(screen != null) {
            glfwSetCursorPos(window, (double)windowX / 2, (double)windowY / 2);
            screen.close(false);
            screen = null;
        }
        if(ClientPlayerData.openContainer != null) {
            ClientPlayerData.openContainer.closeContainer();
        }

        ClientNetworkHandler.sendPacketDirect(new CCloseScreen(false));
    }

    public static void openScreen(Screen screen1) {
        if(screen != null) {
            screen.close(true);
        }
        screen = screen1;
        if(screen1 instanceof ContainerScreen) {
            if(!(screen1 instanceof InventoryScreen)) {

            }
            ContainerScreen<?> screen2 = (ContainerScreen<?>) screen1;
            Container container = screen2.getContainer();
            screen2.setContainer(container);
            if(ClientPlayerData.openContainer != null) {
                ClientPlayerData.openContainer.closeContainer();
            }
            ClientPlayerData.openContainer = container;
        }
        ClientNetworkHandler.sendPacketDirect(new COpenScreen(screen1));
    }

    public static void openScreen(Container container) {
        ContainerScreen<?> containerScreen = container.getContainerScreen();
        if(screen != null) {
            screen.close(true);
        }
        screen = containerScreen;
        containerScreen.setContainer(container);
        if(ClientPlayerData.openContainer != null) {
            ClientPlayerData.openContainer.closeContainer();
        }
        ClientPlayerData.openContainer = container;
    }

    public static void createCallbacks() {
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            if(height % 2 == 1) {
                height += 1;
            }
            if(width % 2 == 1) {
                width += 1;
            }
            GL30.glViewport(0, 0, width, height);
            windowX = width;
            windowY = height;
            if(ClientPlayerData.openContainer != null) {
                ClientPlayerData.openContainer.resize();
            }
        });


        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if(screen != null) {
                    screen.mouseScroll(0,0,(float)yoffset);
                }
                if(yoffset == 1.0) {

                    ClientPlayerData.handSlot--;
                    if(ClientPlayerData.handSlot <= -1) {
                        ClientPlayerData.handSlot = 8;
                    }
                } else if(yoffset == -1.0) {

                    ClientPlayerData.handSlot++;
                    if(ClientPlayerData.handSlot >= 9) {
                        ClientPlayerData.handSlot = 0;
                    }
                }
            }
        });


        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    if (screen == null) {
                        if (button == GLFW_MOUSE_BUTTON_1) {
                            BlockPos pos = clientWorld.traceBlockToBreak(Camera.pos.x, Camera.pos.y + Camera.playerBoundingBox.eyeHeight, Camera.pos.z, Camera.pitch, Camera.yaw);
                            if (pos != null) {
                                if (joinServer) {
                                    ClientNetworkHandler.sendPacketDirect(new CSendBlockChanges(pos.x, pos.y, pos.z, Blocks.AIR.id));
                                    // clientWorld.entities.put(100,new ItemEntity(pos.x,pos.y,pos.z,100,clientWorld.getBlockState(pos).block));
                                } else {
                                    clientWorld.setBlockState(pos, Blocks.AIR.getDefaultState());
                                }
                            }

                        } else if (button == GLFW_MOUSE_BUTTON_2) {
                            BlockPos blockPos = clientWorld.traceBlockToBreak(Camera.pos.x,Camera.pos.y + Camera.playerBoundingBox.eyeHeight,Camera.pos.z,Camera.pitch,Camera.yaw);
                            if(blockPos != null) {
                                BlockState blockState = clientWorld.getBlockState(blockPos);
                                if (blockState != null && blockState.getBlock().activateBlock(clientWorld, null, blockPos)) {
                                    ClientNetworkHandler.sendPacketDirect(new CUseItem((byte) ClientPlayerData.handSlot));
                                    return;
                                }
                            }
                            ItemStack itemStack = ClientPlayerData.inventory.getItem(ClientPlayerData.handSlot);
                            if(!itemStack.isEmpty()) {
                                if(itemStack.item.onActivate(clientWorld,null)) {
                                    ClientNetworkHandler.sendPacketDirect(new CUseItem((byte) ClientPlayerData.handSlot));
                                    if(!ClientPlayerData.creative) {
                                        itemStack.removeCount(1);
                                    }
                                }
                            } else {
                                ClientNetworkHandler.sendPacketDirect(new CUseItem((byte) ClientPlayerData.handSlot));
                            }
                        }
                    } else {
                        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

                        glfwGetCursorPos(window, x, y);
                        screen.mouseClick((int) x.get(), (int) y.get(), button);
                    }
                }
            }
        });

        glfwSetWindowFocusCallback(window, (window, focused) -> {
            if(!focused) {
                if(screen == null) {
                    if (renderWorld) {
                        openScreen(new EscapeScreen());
                    } else {
                        openScreen(new JoinScreen());
                    }
                }
            }
        });
    }

    public static DoubleBuffer getMousePos() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, x, y);
        return BufferUtils.createDoubleBuffer(2).put(x.get()).put(y.get());
    }


        private static int hear( BufferedReader in ) throws IOException {
            String line = null;
            int res = 0;
            while ( (line = in.readLine()) != null ) {
                String pfx = line.substring( 0, 3 );
                try {
                    res = Integer.parseInt( pfx );
                }
                catch (Exception ex) {
                    res = -1;
                }
                if ( line.charAt( 3 ) != '-' ) break;
            }
            return res;
        }
        private static void say(BufferedWriter wr, String text )
                throws IOException {
            wr.write( text + "\r\n" );
            wr.flush();
            return;
        }
        private static ArrayList getMX(String hostName )
                throws NamingException {
            // Perform a DNS lookup for MX records in the domain
            Hashtable env = new Hashtable();
            env.put("java.naming.factory.initial",
                    "com.sun.jndi.dns.DnsContextFactory");
            DirContext ictx = new InitialDirContext( env );
            Attributes attrs = ictx.getAttributes
                    ( hostName, new String[] { "MX" });
            Attribute attr = attrs.get( "MX" );
            // if we don't have an MX record, try the machine itself
            if (( attr == null ) || ( attr.size() == 0 )) {
                attrs = ictx.getAttributes( hostName, new String[] { "A" });
                attr = attrs.get( "A" );
                if( attr == null )
                    throw new NamingException
                            ( "No match for name '" + hostName + "'" );
            }
            // Huzzah! we have machines to try. Return them as an array list
            // NOTE: We SHOULD take the preference into account to be absolutely
            //   correct. This is left as an exercise for anyone who cares.
            ArrayList res = new ArrayList();
            NamingEnumeration en = attr.getAll();
            while ( en.hasMore() ) {
                String x = (String) en.next();
                String f[] = x.split( " " );
                if ( f[1].endsWith( "." ) )
                    f[1] = f[1].substring( 0, (f[1].length() - 1));
                res.add( f[1] );
            }
            return res;
        }
        public static boolean  isAddressValid( String address ) {
            // Find the separator for the domain name
            int pos = address.indexOf( '@' );
            // If the address does not contain an '@', it's not valid
            if ( pos == -1 ) return false;
            // Isolate the domain/machine name and get a list of mail exchangers
            String domain = address.substring( ++pos );
            ArrayList mxList = null;
            try {
                mxList = getMX( domain );
            }
            catch (NamingException ex) {
                return false;
            }
            // Just because we can send mail to the domain, doesn't mean that the
            // address is valid, but if we can't, it's a sure sign that it isn't
            if ( mxList.size() == 0 ) return false;
            // Now, do the SMTP validation, try each mail exchanger until we get
            // a positive acceptance. It *MAY* be possible for one MX to allow
            // a message [store and forwarder for example] and another [like
            // the actual mail server] to reject it. This is why we REALLY ought
            // to take the preference into account.
            for ( int mx = 0 ; mx < mxList.size() ; mx++ ) {
                boolean valid = false;
                try {
                    int res;
                    Socket skt = new Socket( (String) mxList.get( mx ), 25 );
                    BufferedReader rdr = new BufferedReader
                            ( new InputStreamReader( skt.getInputStream() ) );
                    BufferedWriter wtr = new BufferedWriter
                            ( new OutputStreamWriter( skt.getOutputStream() ) );
                    res = hear( rdr );
                    if ( res != 220 ) throw new Exception( "Invalid header" );
                    say( wtr, "EHLO orbaker.com" );
                    res = hear( rdr );
                    if ( res != 250 ) throw new Exception( "Not ESMTP" );
                    // validate the sender address
                    say( wtr, "MAIL FROM: <tim@orbaker.com>" );
                    res = hear( rdr );
                    if ( res != 250 ) throw new Exception( "Sender rejected" );
                    say( wtr, "RCPT TO: <" + address + ">" );
                    res = hear( rdr );
                    // be polite
                    say( wtr, "RSET" ); hear( rdr );
                    say( wtr, "QUIT" ); hear( rdr );
                    if ( res != 250 )
                        throw new Exception( "Address is not valid!" );
                    valid = true;
                    rdr.close();
                    wtr.close();
                    skt.close();
                }
                catch (Exception ex) {
                    // Do nothing but try next host
                }
                finally {
                    if ( valid ) return true;
                }
            }
            return false;
        }
        public  String call_this_to_validate( String email ) {
            String testData[] = {email};
            String return_string="";
            for ( int ctr = 0 ; ctr < testData.length ; ctr++ ) {
                return_string=( testData[ ctr ] + " is valid? " +
                        isAddressValid( testData[ ctr ] ) );
            }
            return return_string;
        }
}
