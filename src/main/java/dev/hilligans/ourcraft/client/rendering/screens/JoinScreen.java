package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.input.key.KeyPress;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;
import dev.hilligans.ourcraft.client.rendering.widgets.ServerSelectorWidget;
import dev.hilligans.engine.network.PortUtil;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.INetworkEngine;
import dev.hilligans.engine.network.engine.NetworkSocket;
import dev.hilligans.ourcraft.network.packet.CLogin;
import dev.hilligans.ourcraft.util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
public class JoinScreen extends ScreenBase {

    public ServerSelectorWidget selected;
    Button play;

    public JoinScreen() {
        registerKeyPress(new KeyPress() {
            @Override
            public void onPress() {
                getClient().openScreen(new TagEditorScreen());
            }
        }, GLFW_KEY_H);
    }

    @Override
    public void buildContentForWindow(RenderWindow window) {
        int windowY = (int) window.getWindowHeight();

        Client client = (Client) getClient();
        play = new Button(100, windowY / 2 + 100, 200, 50, "menu.join", () -> {
            if(selected != null) {
                selected.joinServer();
            }
        }).isEnabled(false);
        addWidget(play);
        addWidget(new ServerSelectorWidget(100,300,200,80,"localhost","25588",this));
        addWidget(new ServerSelectorWidget(100,400,200,80,"198.100.150.46","25588",this));
        addWidget(new Button(500, 200, 200, 50, "menu.create_account", () -> client.openScreen(new AccountCreationScreen())));
        addWidget(new Button(500, 300, 200, 50, "menu.log_in", () -> client.openScreen(new LoginScreen())));
        addWidget(new Button(500,400,200,50,"menu.singleplayer", () -> {
            //ServerWorld world = new ServerWorld(ClientMain.gameInstance);
            //world.worldBuilders.add( new OreBuilder("stone", Blocks.GRASS,Blocks.STONE).setFrequency(20));

          //  client.multiPlayerServer = new MultiPlayerServer();
            //client.multiPlayerServer.addWorld(0,world);
        //    ServerMain.server = client.multiPlayerServer;
            int port = 0;
            try {
                port = PortUtil.getOpenPort();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            String portString = port + "";
          //  Thread thread = new Thread(() -> client.multiPlayerServer.startServer(portString));
          //  thread.setName("client_networking");
          //  thread.setDaemon(true);
          //  thread.start();
            this.portString = portString;
        }));

        addWidget(new Button(500,500,200,50,"menu.singleplayerjoin", () -> {
            try {
                NetworkSocket<?> socket = client.getGameInstance().getExcept("ourcraft:nettyEngine", INetworkEngine.class)
                        .openClient(client.getGameInstance().getExcept("ourcraft:login", Protocol.class), client, "localhost", portString);
                socket.onConnected(e -> CLogin.send(e, "hilligans", "ourcraft:unathenticated_scheme"));
                client.socket = socket;
                socket.connectSocket();
                //client.network.joinServer("localhost",portString,client);
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
    public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        super.drawScreen(window, matrixStack, graphicsContext);
        Client client = (Client) getClient();
        if(client.playerData.valid_account) {
            window.getStringRenderer().drawStringInternal(window, graphicsContext, matrixStack,client.playerData.userName, (int) (Settings.guiSize * 8), (int) (1 * Settings.guiSize),0.5f);
            Textures.CHECK_MARK.drawTexture(window, graphicsContext, matrixStack,0,0,(int)(8 * Settings.guiSize), (int)(8 * Settings.guiSize));
        } else {
            Textures.X_MARK.drawTexture(window, graphicsContext, matrixStack,0,0,(int)(8 * Settings.guiSize), (int)(8 * Settings.guiSize));
        }
    }
}
