package dev.hilligans.engine.client.graphics.vulkan;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.client.input.InputHandler;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.FreeCamera;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.*;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline.*;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.window.*;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkQueue;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.vulkan.KHRSurface.vkDestroySurfaceKHR;
import static org.lwjgl.vulkan.VK10.VK_SHADER_STAGE_FRAGMENT_BIT;
import static org.lwjgl.vulkan.VK10.VK_SHADER_STAGE_VERTEX_BIT;

public class VulkanWindow extends RenderWindow {

    public LogicalDevice device;
    public long window;
    public long surface;
    public VkQueue presentationQueue;
    public QueueFamily graphicsFamily;
    public int vulkanWidth;
    public int vulkanHeight;



    public FrameManager frameManager;
    public SwapChain swapChain;
    public ArrayList<FrameBuffer> frameBuffers = new ArrayList<>();
    public ImageView imageView;
    public Viewport viewport;

    public CommandPool renderPool;
    public RenderPass renderPass;
    public Shader vertexShader;
    public Shader fragmentShader;
    public GraphicsPipeline graphicsPipeline;

    public VertexBuffer buffer;

    public int glfwWidth;
    public int glfwHeight;
    public WindowRenderer windowRenderer;
    public VulkanGraphicsContext context;

    public VkExtent2D extent2D = VkExtent2D.calloc();

    public Vector4f clearColor = new Vector4f();

    public VulkanWindow(VulkanInstance vulkanInstance, int width, int height, IGraphicsEngine<?,?,?> graphicsEngine) {
        super(graphicsEngine);
        System.out.println("New vulkan window");
        windowRenderer = new WindowRenderer(this);
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        this.window = glfwCreateWindow(width, height, "Vulkan",0,0);

        surface = VkInterface.glfwCreateWindowSurface(vulkanInstance.vkInstance, window, null);
        GLFW.glfwSetWindowSizeCallback(window, this::resize);
        camera = new FreeCamera(this);
    }

    public VulkanWindow addDevice(LogicalDevice device) {
        this.device = device;
        return this;
    }

    public VulkanWindow addData() {
        swapChain = new SwapChain(this);
        imageView = new ImageView(this);
        renderPass = new RenderPass(this);
        viewport = new Viewport(this);
        vertexShader = new Shader(device, ShaderCompiler.compileShader(shader, "shader.glsl", VK_SHADER_STAGE_VERTEX_BIT),VK_SHADER_STAGE_VERTEX_BIT);
        vertexShader.set(Engine.position_RGB);

        //SingleUseCommandBuffer buf = device.queueFamilyManager.getSingleCommandPool(false, false, true, false);
        //CommandBuffer commandBuffer = new CommandBuffer(buf.getBuffer());
        //buffer = new VertexBuffer(device, new float[] {0.0f, -0.8f, 1.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 1.0f, 0.0f, 1.0f, -0.5f, 0.5f, 1.0f,  1.0f, 0.0f, 1.0f}, commandBuffer);
        //buf.endAndSubmit(commandBuffer.onCompletion);

        fragmentShader = new Shader(device,ShaderCompiler.compileShader(fragment, "shader.glsl", VK_SHADER_STAGE_FRAGMENT_BIT),VK_SHADER_STAGE_FRAGMENT_BIT);
        graphicsPipeline = new GraphicsPipeline(device, null);
        graphicsPipeline.build(renderPass, viewport, vertexShader, fragmentShader);

        renderPool = new CommandPool(device, graphicsFamily.findQueue());
        for (int x = 0; x < swapChain.size; x++) {
            frameBuffers.add(new FrameBuffer(this, x));
        }
        renderPool.allocCommandBuffers(swapChain.size);

        this.context = new VulkanGraphicsContext(renderPool, device, this);
        this.frameManager = new FrameManager(this);
        return this;
    }

    @Override
    public VulkanGraphicsContext getGraphicsContext() {
        return context;
    }

    public VulkanWindow selectFamily() {
        graphicsFamily = device.queueFamilyManager.getQueueFamily(true,true,true,true);
        if(graphicsFamily == null) {
            device.vulkanInstance.exit("No queue family found");
        }
        vulkanWidth = clamp(vulkanWidth,device.physicalDevice.surfaceCapabilities.minImageExtent().width(),device.physicalDevice.surfaceCapabilities.maxImageExtent().width());
        vulkanHeight = clamp(vulkanHeight,device.physicalDevice.surfaceCapabilities.minImageExtent().height(),device.physicalDevice.surfaceCapabilities.maxImageExtent().height());
        resize(window, 500,500);
        return this;
    }

    public void resize(long window, int width, int height) {
        IntBuffer widthBuffer = MemoryStack.stackInts(1);
        IntBuffer heightBuffer = MemoryStack.stackInts(1);
        glfwGetFramebufferSize(window,widthBuffer,heightBuffer);
        vulkanWidth = clamp(widthBuffer.get(0),device.physicalDevice.surfaceCapabilities.minImageExtent().width(),device.physicalDevice.surfaceCapabilities.maxImageExtent().width());
        vulkanHeight = clamp(heightBuffer.get(0),device.physicalDevice.surfaceCapabilities.minImageExtent().height(),device.physicalDevice.surfaceCapabilities.maxImageExtent().height());
        glfwWidth = width;
        glfwHeight = height;
        extent2D.width(vulkanWidth);
        extent2D.height(vulkanHeight);
    }

    /*
    public void startDrawing() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            //buffer.vertices.put(2,-buffer.vertices.get(2));
        }
        cleanup();
    }
     */

    public void cleanup() {
        frameManager.cleanup();
        swapChain.cleanup();
        for(FrameBuffer frameBuffer : frameBuffers) {
            frameBuffer.cleanup();
        }
        graphicsPipeline.cleanup();
        renderPass.free();
        buffer.cleanup();
        vertexShader.free();
        fragmentShader.free();
        imageView.cleanup();
        renderPool.cleanup();
        graphicsFamily.cleanup();
        vkDestroySurfaceKHR(device.vulkanInstance.vkInstance,surface,null);
        glfwDestroyWindow(window);
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static String shader = """
            #version 450
            layout(location = 0) in vec3 inPosition;
            layout(location = 1) in vec3 inColor;

            layout(location = 0) out vec3 fragColor;

            void main() {
                gl_Position = vec4(inPosition, 1.0);
                fragColor = inColor;
            }""";


    public static String fragment = """
            #version 450

            layout(location = 0) in vec3 fragColor;

            layout(location = 0) out vec4 outColor;

            void main() {
                outColor = vec4(fragColor, 1.0);
            }""";

    @Override
    public long getWindowID() {
        return window;
    }

    @Override
    public void close() {
        cleanup();
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    @Override
    public void swapBuffers(GraphicsContext graphicsContext) {
        windowRenderer.present(context.bufferIndex);
        glfwPollEvents();
        //startDrawing();
    }

    @Override
    public InputHandler getInputProvider() {
        return null;
    }

    @Override
    public String getClipboardString() {
        return GLFW.glfwGetClipboardString(window);
    }

    @Override
    public void setMousePosition(int x, int y) {
        GLFW.glfwSetCursorPos(window, x, y);
    }

    @Override
    public int getWindowWidth() {
        return glfwWidth;
    }

    @Override
    public int getWindowHeight() {
        return glfwHeight;
    }

    @Override
    public boolean isWindowFocused() {
        return false;
    }

    @Override
    public String getWindowingName() {
        return "glfw";
    }

    @Override
    public void setClearColor(float r, float g, float b, float a) {
        this.clearColor.set(r,g,b,a);
    }
}
