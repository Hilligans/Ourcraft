package dev.hilligans.ourcraft.client;

import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.newrenderer.Image;
import dev.hilligans.ourcraft.client.rendering.world.managers.WorldTextureManager;
import dev.hilligans.ourcraft.data.primitives.Triplet;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30C.glBindFramebuffer;

public class ScreenShot {

    public static void takeScreenShot(RenderWindow window) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int) (window.getWindowWidth() * window.getWindowHeight() * 3));

        GL11.glReadPixels(0,0, (int) window.getWindowWidth(), (int) window.getWindowHeight(), GL11.GL_RGB,GL11.GL_UNSIGNED_BYTE,byteBuffer);

        BufferedImage bufferedImage = new BufferedImage((int) window.getWindowWidth(), (int) window.getWindowHeight(), BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < window.getWindowHeight(); y++) {
            for(int x = 0; x < window.getWindowWidth(); x++) {
                bufferedImage.setRGB(x,y,new Color(byteBuffer.get() & 255,byteBuffer.get() & 255,byteBuffer.get() & 255).getRGB());
            }
        }

        writeImage(bufferedImage);
    }

    public static void largeScreenshot(int width, int height, Client client) {
        Image image = getImage(width,height,client,0,0,0,0);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                bufferedImage.setRGB(x,y,new Color(image.buffer.get() & 255,image.buffer.get() & 255,image.buffer.get() & 255).getRGB());
            }
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0,0,1920,1080);
       // client.windowX = 1920;
       // client.windowY = 1080;

        writeImage(bufferedImage);
    }

    private static Image getImage(int width, int height, Client client, int W, int H, int startX, int startY) {
  /*      int maxSize = GL30.glGetInteger(GL_MAX_VIEWPORT_DIMS);
        int sizeX = (int)Math.ceil(width / (float)maxSize);
        int sizeY = (int)Math.ceil(height / (float)maxSize);
        Image image = new Image(width,height,3);
        image.buffer.mark();

        int factorX = width / sizeX;
        int factorY = height / sizeY;
        Triplet<Integer,Integer,Integer> triplet = setFrameBuffer(factorX, factorY ,client);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                draw(client,W + sizeX,H + sizeY,startX + x,startY + y);
                image.putImage(getImage(factorX,factorY),factorX * x, factorY * y);
            }
        }
        image.buffer.reset();

   */
        return null;
    }

    private static void writeImage(BufferedImage bufferedImage) {
        bufferedImage = WorldTextureManager.createFlipped(bufferedImage);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime time = LocalDateTime.now();

        File file1 = new File("screenshots/");
        File file = new File("screenshots/" + dtf.format(time) + ".png");

        try {
            file1.mkdir();
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Image getImage(int width, int height) {
      //  ByteBuffer byteBuffer = ByteBuffer.allocateDirect(ClientMain.getWindowX() * ClientMain.getWindowY() * 3);
       // GL11.glReadPixels(0,0, ClientMain.getWindowX(),ClientMain.getWindowY(), GL11.GL_RGB,GL11.GL_UNSIGNED_BYTE,byteBuffer);
      //  return new Image(width,height,3,byteBuffer);
        return null;
    }

    private static Triplet<Integer,Integer,Integer> setFrameBuffer(int width, int height, Client client) {

    /*
        int buffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, buffer);
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);

        int rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height); // use a single renderbuffer object for both a depth AND stencil buffer.
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);

        glViewport(0, 0, width, height);
        client.windowX = width;
        client.windowY = height;
        return new Triplet<>(buffer,texture,rbo);

     */
        return null;
    }
}
