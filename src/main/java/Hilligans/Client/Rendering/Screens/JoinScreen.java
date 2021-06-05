package Hilligans.Client.Rendering.Screens;

import Hilligans.Block.Blocks;
import Hilligans.Client.Client;
import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.Key.KeyPress;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.Widgets.ServerSelectorWidget;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.Client.Rendering.Widgets.InputField;
import Hilligans.Network.ClientAuthNetworkHandler;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.AuthServerPackets.CCreateAccount;
import Hilligans.Network.Packet.AuthServerPackets.CGetToken;
import Hilligans.Network.Packet.Client.CSendBlockChanges;
import Hilligans.Util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;

public class JoinScreen extends ScreenBase {

    public ServerSelectorWidget selected;
    Button play = new Button(100, ClientMain.getWindowY() / 2 + 100, 200, 50, "menu.join", new ButtonAction() {
        @Override
        public void onPress() {
            if(selected != null) {
                selected.joinServer();
            }
        }
    }).isEnabled(false);

    public JoinScreen() {
        widgets.add(play);
        widgets.add(new ServerSelectorWidget(100,200,200,80,"72.172.99.188","25586",this));
        widgets.add(new ServerSelectorWidget(100,300,200,80,"localhost","25586",this));
        widgets.add(new Button(500, 200, 200, 50, "menu.create_account", () -> ClientMain.getClient().openScreen(new AccountCreationScreen())));
        widgets.add(new Button(500, 300, 200, 50, "menu.log_in", () -> ClientMain.getClient().openScreen(new LoginScreen())));
        //widgets.add(new Button(500,500,500,200,"CAN YOU SEE THIS",() -> {}));

        registerKeyPress(new KeyPress() {
            @Override
            public void onPress() {
                ClientMain.getClient().openScreen(new TagEditorScreen());
            }
        }, GLFW_KEY_H);
    }

    public void setActive(ServerSelectorWidget selected) {
        this.selected = selected;
        play.enabled = true;
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        super.drawScreen(matrixStack);
        if(ClientMain.getClient().playerData.valid_account) {
            StringRenderer.drawString(matrixStack,ClientMain.getClient().playerData.userName, (int) (Settings.guiSize * 8), (int) (1 * Settings.guiSize),0.5f);
            Textures.CHECK_MARK.drawTexture(matrixStack,0,0,(int)(8 * Settings.guiSize), (int)(8 * Settings.guiSize));
        } else {
            Textures.X_MARK.drawTexture(matrixStack,0,0,(int)(8 * Settings.guiSize), (int)(8 * Settings.guiSize));
        }


    }
}
