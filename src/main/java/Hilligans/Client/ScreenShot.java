package Hilligans.Client;

import Hilligans.Client.Rendering.World.Managers.TextureManager;
import Hilligans.ClientMain;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenShot {

    public static void takeScreenShot() {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(ClientMain.windowX * ClientMain.windowY * 3);

        GL11.glReadPixels(0,0, ClientMain.windowX,ClientMain.windowY, GL11.GL_RGB,GL11.GL_UNSIGNED_BYTE,byteBuffer);

        BufferedImage bufferedImage = new BufferedImage(ClientMain.windowX, ClientMain.windowY, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < ClientMain.windowY; y++) {
            for(int x = 0; x < ClientMain.windowX; x++) {
                bufferedImage.setRGB(x,y,new Color(byteBuffer.get() & 255,byteBuffer.get() & 255,byteBuffer.get() & 255).getRGB());
            }
        }

        bufferedImage = TextureManager.createFlipped(bufferedImage);
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
}
