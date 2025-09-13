package dev.hilligans.ourcraft.client;

import dev.hilligans.engine.EngineMain;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IApplication;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.ourcraft.client.audio.SoundEngine;
import dev.hilligans.engine.client.input.InputHandler;
import dev.hilligans.engine.client.input.key.MouseHandler;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.Screen;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.IInputProvider;
import dev.hilligans.engine.client.graphics.opengl.OpenGLEngine;
import dev.hilligans.engine.client.graphics.vulkan.VulkanEngine;
import dev.hilligans.ourcraft.client.rendering.screens.JoinScreen;
import dev.hilligans.ourcraft.client.rendering.screens.container.screens.InventoryScreen;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.data.other.PlayerList;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.engine.mod.handler.events.client.OpenScreenEvent;
import dev.hilligans.engine.network.engine.NetworkSocket;
import dev.hilligans.engine.save.FileLoader;
import dev.hilligans.engine.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.util.Logger;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.engine.util.ThreadContext;
import dev.hilligans.engine.util.argument.Argument;
import dev.hilligans.engine.util.argument.ArgumentContainer;
import dev.hilligans.ourcraft.util.registry.Registry;
import dev.hilligans.ourcraft.world.newworldsystem.ClientCubicWorld;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import java.nio.DoubleBuffer;

public class Client implements IApplication {

    public long window;

    public boolean mouseLocked = false;

    public int playerId;

    public boolean valid = false;
    public boolean screenShot = false;
    public boolean renderWorld = true;
    public PlayerList playerList;
    public boolean glStarted = false;
    public long renderTime = 0;
    public String serverIP = "";
    Client client = this;
    public Logger logger;

    public MouseHandler mouseHandler;
    public Screen screen;
    public SoundEngine soundEngine;

    public ClientPlayerData playerData = new ClientPlayerData();
    public IWorld newClientWorld;

    public boolean rendering = false;

    public NetworkSocket<?> socket;

    public GameInstance gameInstance;

    public IGraphicsEngine<?,?,?> graphicsEngine;
    public InputHandler inputHandler;
    public IInputProvider mouseBind;

    public int renderDistance = 12;
    public int renderYDistance = 8;
    public ArgumentContainer argumentContainer;
    public ChatMessages chatMessages = new ChatMessages();

    public boolean transition = false;

    public Client(GameInstance gameInstance, ArgumentContainer argumentContainer) {
        this.gameInstance = gameInstance;
        this.newClientWorld = new ClientCubicWorld(gameInstance, 0,"", 64);
        logger = gameInstance.LOGGER.withKey("client");
        graphicsEngine = ((Registry<IGraphicsEngine<?,?,?>>)gameInstance.REGISTRIES.getExcept("ourcraft:graphics_engine")).get("ourcraft:openglEngine");
        for(IGraphicsEngine<?,?,?> engine : ((Registry<IGraphicsEngine<?,?,?>>)gameInstance.REGISTRIES.getExcept("ourcraft:graphics_engine")).ELEMENTS) {
            System.out.println(engine.getIdentifierName());
        }
        ((OpenGLEngine)graphicsEngine).client = this;
        soundEngine = new SoundEngine(gameInstance);
        this.argumentContainer = argumentContainer;
    }

    public Client setGraphicsEngine(IGraphicsEngine<?,?,?> graphicsEngine) {
        if(graphicsEngine != null) {

            this.graphicsEngine = graphicsEngine;
            if(graphicsEngine instanceof VulkanEngine engine) {
                engine.client = this;
            }
            System.out.println(graphicsEngine.getResourceName());
        }
        return this;
    }

    public void setupClient() {
        RenderWindow window = graphicsEngine.startEngine();
        window.setClient(this);
        client.screen = new JoinScreen();
        client.screen.setWindow(window);

        window.setClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        if(soundEngine.camera == null) {
            soundEngine.init(window.getCamera());
            soundEngine.setAttenuationModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
        }
        //registerKeyHandlers();

        // SplitWindow splitWindow = new SplitWindow(graphicsEngine, window);
        // SubWindow subWindow = new SubWindow(graphicsEngine, splitWindow, window.getWindowWidth()/2, window.getWindowHeight()/2, graphicsEngine.getContext());
        // subWindow.setRenderPipeline(window.renderPipeline);
        // splitWindow.setRenderPipeline("ourcraft:split_window_pipeline");
        // splitWindow.addWindow(subWindow);

        rWindow = window;
    }

    public void startClient() {
        setupClient();

        System.err.println("Time to start running: " + (System.currentTimeMillis() - EngineMain.start));
    }

    public void loop() {
        graphicsEngine.createRenderLoop(gameInstance, rWindow).run();
        graphicsEngine.close();
        cleanUp();
    }

    public void tick(ThreadContext threadContext) {
        try(var $0 = threadContext.getSection().startSection("tick_world")) {
            newClientWorld.tick();
        }
        if(transition) {
            client.gameInstance.build(client.graphicsEngine, null);
            transition = false;
            rWindow.setRenderPipeline("ourcraft:menu_pipeline");
        }
    }

    public RenderWindow rWindow;

    public void cleanUp() {
        soundEngine.cleanup();
        for(SoundBuffer soundBuffer : gameInstance.SOUNDS.ELEMENTS) {
            soundBuffer.cleanup();
        }
    }

    public void closeScreen() {
        if(!playerData.heldStack.isEmpty()) {
            //sendPacket(new CDropItem((short)-1,(byte)-1));
            playerData.heldStack = ItemStack.emptyStack();
        }
        if(playerData.openContainer != null) {
            playerData.openContainer.closeContainer();
        }
    }

    public void openScreen(Screen screen1) {
        RenderWindow renderWindow = rWindow;
        screen1.setWindow(renderWindow);
        gameInstance.EVENT_BUS.postEvent(new OpenScreenEvent(screen1,screen));
        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
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
            if(playerData.openContainer != null) {
                playerData.openContainer.closeContainer();
            }
            playerData.openContainer = container;
        }
        screen1.resize(renderWindow.getWindowWidth(),renderWindow.getWindowHeight());
    }

    public void openScreen(Container container) {

        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        ContainerScreen<?> containerScreen = container.getContainerScreen();
        gameInstance.EVENT_BUS.postEvent(new OpenScreenEvent(containerScreen,screen));
        if(screen != null) {
            screen.close(true);
        }
        screen = containerScreen;
        containerScreen.setContainer(container);
        if(playerData.openContainer != null) {
            playerData.openContainer.closeContainer();
        }
        playerData.openContainer = container;
    }

    public void openScreen(String screenName) {
        ScreenBuilder screenBuilder = gameInstance.SCREEN_BUILDERS.get(screenName);
        if(screenBuilder == null) {
            throw new RuntimeException("Failed to find screen: " + screenName);
        }
        openScreen(screenBuilder.get(this));
    }

    public static long timeSinceLastDraw = 0;
    public static float drawTime = 1000f * 1000000 / Settings.maxFps;

    public DoubleBuffer getMousePos() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        //glfwGetCursorPos(window, x, y);
        return BufferUtils.createDoubleBuffer(2);
        //return BufferUtils.createDoubleBuffer(2).put(x.get()).put(y.get());
    }

    public CompoundNBTTag writeUsernameAndPassword(CompoundNBTTag tag) {
         tag.putFullString("username",playerData.userName);
         tag.putFullString("loginToken",playerData.login_token);
         tag.putFullString("email",playerData.email);
         return tag;
    }

    public void saveUsernameAndPassword() {
         CompoundNBTTag tag = new CompoundNBTTag();
         writeUsernameAndPassword(tag);
         FileLoader.save(tag,"clientData.dat");
    }

    public long getRenderTime() {
         return renderTime;
    }

    public ClientPlayerData getPlayerData() {
        return playerData;
    }

    public IWorld getWorld() {
        return newClientWorld;
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public static final Argument<IGraphicsEngine> graphicsEngineArg = Argument.registryArg("--graphicsEngine", IGraphicsEngine.class, "ourcraft:openglEngine")
            .help("The default graphics engine to use, still need to lookup acceptable values based on registry.");

    @Override
    public void postCoreStartApplication(GameInstance gameInstance) {
        Client client = this;
        Thread thread = new Thread(() -> {
            client.setGraphicsEngine(graphicsEngineArg.get(gameInstance));

            client.startClient();

            client.loop();

            gameInstance.THREAD_PROVIDER.EXECUTOR.shutdownNow();
            //if(integratedServer.get(gameInstance)) {
            //    ServerMain.getServer().stop();
            //}
            System.exit(0);
        });
        thread.start();
    }

    @Override
    public void startApplication(GameInstance gameInstance) {
        this.transition = true;
    }

    @Override
    public String getResourceName() {
        return "client";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }
}
