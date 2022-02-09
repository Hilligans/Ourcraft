package dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL;

import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Client.Camera;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.PlayerMovementThread;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.GLRenderer;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.TextAtlas;
import dev.Hilligans.ourcraft.Client.Rendering.Renderer;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.WorldTextureManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Entity.Entity;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.GLInitEvent;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.Util.TwoInt2ObjectMap;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLEngine implements IGraphicsEngine<OpenGLGraphicsContainer, OpenGLWindow> {

    public TwoInt2ObjectMap<OpenGLGraphicsContainer> chunks = new TwoInt2ObjectMap<>();

    public long window;
    public int texture;

    public Client client;

    public OpenGLEngine(Client client) {
        this.client = client;
    }

    @Override
    public OpenGLWindow createWindow() {
        renderWindow = new OpenGLWindow(window,client);
        return renderWindow;
    }

    @Override
    public OpenGLGraphicsContainer getChunkGraphicsContainer(Chunk chunk) {
        return chunks.get(chunk.x,chunk.z);
    }

    @Override
    public OpenGLGraphicsContainer createChunkGraphicsContainer() {
        return null;
    }

    @Override
    public void putChunkGraphicsContainer(Chunk chunk, OpenGLGraphicsContainer container) {
        chunks.put(chunk.x, chunk.z, container);
    }

    public static OpenGLWindow renderWindow;

    @Override
    public void render(OpenGLWindow window) {
        client.mouseLocked = client.screen == null;
        glGetError();
        GLRenderer.resetFrame();
        long currentTime = System.nanoTime();
        if(currentTime - Client.timeSinceLastDraw < Client.drawTime) {
            return;
        }
        window.frameTracker.count();
        Client.timeSinceLastDraw = currentTime;

        client.unloadQueue.forEach(VAOManager::destroyBuffer);
        client.unloadQueue.clear();

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        if(client.refreshTexture) {
            Blocks.generateTextures();
            texture = TextAtlas.instance.upload();
            client.refreshTexture = false;
            System.out.println("Time: " + (System.currentTimeMillis() - ClientMain.start));
           // getGameInstance().RESOURCE_LOADER.saveResource((Image)getGameInstance().RESOURCE_LOADER.getResource(new ResourceLocation("Images/player.png", "ourcraft")), "out.jpg");
           // getGameInstance().RESOURCE_LOADER.saveResource(TextAtlas.instance.image, "out.png");
        }

        glUseProgram(client.shaderManager.shaderProgram);
        glBindTexture(GL_TEXTURE_2D, texture);
        MatrixStack matrixStack = Camera.getWorldStack();
        matrixStack.applyColor();
        matrixStack.applyTransformation();


        MatrixStack screenStack = Camera.getScreenStack();
        screenStack.applyColor();
        screenStack.applyTransformation();

        client.draw(matrixStack,screenStack);
    }

    @Override
    public void setup() {
        System.setProperty("java.awt.headless", "true");

        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        window = glfwCreateWindow(client.windowX,client.windowY,"Ourcraft",NULL,NULL);
        client.window = window;
        if(window == NULL) {
            System.out.println("Failed to create window");
            glfwTerminate();
            return;
        }
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        client.glStarted = true;
        client.gameInstance.EVENT_BUS.postEvent(new GLInitEvent(window));

        client.shaderManager = new ShaderManager();

        Blocks.generateTextures();
        PlayerEntity.imageId = WorldTextureManager.loadAndRegisterTexture("player.png");
        StringRenderer.instance.buildChars();

        for(Texture texture : Textures.TEXTURES) {
            texture.register();
        }
        client.screen = new JoinScreen(client);
        texture = -1;
        Renderer.create(texture);
        client.clientWorld = new ClientWorld(client);

        client.texture = texture;

        glfwWindowHint(GLFW_SAMPLES, 4);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        glFrontFace(GL_CW);
        glEnable(GL_PROGRAM_POINT_SIZE);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        PlayerMovementThread playerMovementThread = new PlayerMovementThread(window);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("player_movement"));
        executorService.scheduleAtFixedRate(playerMovementThread, 0, 5, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<OpenGLWindow> getWindows() {
        return null;
    }

    @Override
    public GameInstance getGameInstance() {
        return client.gameInstance;
    }

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public void renderWorld(MatrixStack matrixStack, ClientWorld world) {
        world.vertices = 0;
        world.count = 0;
        if(!Settings.renderTransparency) {
            glDisable(GL_BLEND);
        }
        glUseProgram(ClientMain.getClient().shaderManager.colorShader);

        Vector3d pos = Camera.renderPos;
        for(int x = 0; x < Settings.renderDistance; x++) {
            for(int z = 0; z < Settings.renderDistance; z++) {
                drawChunk(matrixStack,pos,x,z, world);
                if(x != 0) {
                    drawChunk(matrixStack, pos, -x, z, world);
                    if(z != 0) {
                        drawChunk(matrixStack,pos,-x,-z, world);
                    }
                }
                if(z != 0) {
                    drawChunk(matrixStack,pos,x,-z, world);
                }
            }
        }
        glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        // TODO: 2021-02-06 server removes entities at a bad time
        try {
            for (Entity entity : world.entities.values()) {
                if (entity.id != ClientMain.getClient().playerId || Camera.thirdPerson) {
                    matrixStack.push();
                    entity.render(matrixStack);
                    matrixStack.pop();
                }
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}
        glEnable(GL_BLEND);
    }

    @Override
    public void renderScreen(MatrixStack screenStack) {
    }

    public void drawChunk(MatrixStack matrixStack, Vector3d pos, int x, int z, ClientWorld world) {
        Vector3i playerChunkPos = new Vector3i((int)pos.x >> 4, 0, (int)pos.z >> 4);
        Chunk chunk = world.getChunk(x * 16 + (int)pos.x >> 4,z * 16 + (int)pos.z >> 4);
        if(chunk == null) {
            if(!ClientMain.getClient().joinServer) {
                world.generateChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4);
            } else {
                if (ClientMain.getClient().valid && Settings.requestChunks) {
                    world.requestChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4);
                }
            }
        } else {
            world.vertices += chunk.getTotalVertices();
            world.count++;
            if(Camera.shouldRenderChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4,matrixStack)) {
                if(matrixStack.frustumIntersection.testAab(new Vector3f((chunk.x - playerChunkPos.x) * 16, 0, (chunk.z - playerChunkPos.z) * 16),new Vector3f((chunk.x + 1 - playerChunkPos.x) * 16, 256f, (chunk.z + 1 - playerChunkPos.z) * 16))) {
                    matrixStack.push();
                    matrixStack.translate((chunk.x - playerChunkPos.x) * 16, 0, (chunk.z - playerChunkPos.z) * 16);
                    chunk.render(matrixStack);
                    matrixStack.pop();
                }
            }
        }
    }

    @Override
    public void load(GameInstance gameInstance) {

    }

    @Override
    public String getResourceName() {
        return "openglEngine";
    }

    @Override
    public String getIdentifierName() {
        return "ourcraft:openglEngine";
    }

    @Override
    public String getUniqueName() {
        return "graphicsEngine.ourcraft.openglEngine";
    }
}
