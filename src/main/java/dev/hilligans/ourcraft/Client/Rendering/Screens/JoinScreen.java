package dev.hilligans.ourcraft.Client.Rendering.Screens;

import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.Textures;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.ServerSelectorWidget;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.hilligans.ourcraft.Network.ServerNetworkInit;
import dev.hilligans.ourcraft.Server.MultiPlayerServer;
import dev.hilligans.ourcraft.ServerMain;
import dev.hilligans.ourcraft.Util.Settings;
import dev.hilligans.ourcraft.World.Builders.OreBuilder;
import dev.hilligans.ourcraft.World.ServerWorld;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
public class JoinScreen extends ScreenBase {

    public ServerSelectorWidget selected;
    Button play;

    public JoinScreen(Client client) {
        super(client);
        registerKeyPress(new KeyPress() {
            @Override
            public void onPress() {
                client.openScreen(new TagEditorScreen(client));
            }
        }, GLFW_KEY_H);
    }

    @Override
    public void buildContentForWindow(RenderWindow window) {
        int windowY = (int) window.getWindowHeight();

        play = new Button(100, windowY / 2 + 100, 200, 50, "menu.join", () -> {
            if(selected != null) {
                selected.joinServer();
            }
        }).isEnabled(false);
        addWidget(play);
        addWidget(new ServerSelectorWidget(100,300,200,80,"localhost","25588",this));
        addWidget(new ServerSelectorWidget(100,400,200,80,"198.100.150.46","25588",this));
        addWidget(new Button(500, 200, 200, 50, "menu.create_account", () -> client.openScreen(new AccountCreationScreen(client))));
        addWidget(new Button(500, 300, 200, 50, "menu.log_in", () -> client.openScreen(new LoginScreen(client))));
        addWidget(new Button(500,400,200,50,"menu.singleplayer", () -> {
            ServerWorld world = new ServerWorld(ClientMain.gameInstance);
            world.worldBuilders.add( new OreBuilder("stone", Blocks.GRASS,Blocks.STONE).setFrequency(20));

            client.multiPlayerServer = new MultiPlayerServer();
            client.multiPlayerServer.addWorld(0,world);
            ServerMain.server = client.multiPlayerServer;
            int port = 0;
            try {
                port = ServerNetworkInit.getOpenPort();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            String portString = port + "";
            Thread thread = new Thread(() -> client.multiPlayerServer.startServer(portString));
            thread.setName("client_networking");
            thread.start();
            this.portString = portString;
        }));

        addWidget(new Button(500,500,200,50,"menu.singleplayerjoin", () -> {
            try {
                client.network.joinServer("localhost",portString,client);
                client.closeScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        addWidget(new Button(500,600,200,50,"menu.mod_list", () -> client.openScreen(new ModListScreen(client))));

    }

    public String portString;

    public void setActive(ServerSelectorWidget selected) {
        this.selected = selected;
        play.enabled = true;
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
        super.drawScreen(window, matrixStack);
        if(client.playerData.valid_account) {
            window.getStringRenderer().drawStringInternal(window, matrixStack,client.playerData.userName, (int) (Settings.guiSize * 8), (int) (1 * Settings.guiSize),0.5f);
            Textures.CHECK_MARK.drawTexture(window, matrixStack,0,0,(int)(8 * Settings.guiSize), (int)(8 * Settings.guiSize));
        } else {
            Textures.X_MARK.drawTexture(window, matrixStack,0,0,(int)(8 * Settings.guiSize), (int)(8 * Settings.guiSize));
        }
    }

}
