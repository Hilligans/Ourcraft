package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Client;
import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.InputField;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Network.ClientAuthNetworkHandler;
import Hilligans.Network.Packet.AuthServerPackets.CCreateAccount;
import Hilligans.Ourcraft;

public class AccountCreationScreen extends ScreenBase {

    public String debug = "";

    InputField username = new InputField(100,100,200,100, "Account name");
    InputField password = new InputField(100,250,200,100, "Password");
    InputField email = new InputField(100,400,200,100,"Email");
    InputField token = new InputField(100,550,200,100,"Email Token");

    public AccountCreationScreen() {
        widgets.add(username);
        widgets.add(password);
        widgets.add(email);
        widgets.add(token);

        widgets.add(new Button(500, 200, 200, 50, "Create Account", () -> {
            ClientMain.getClient().playerData.email = email.string;
            ClientMain.getClient().playerData.password = Ourcraft.hashString(password.string);
            ClientMain.getClient().playerData.userName = username.string;
            ClientMain.getClient().saveUsernameAndPassword();
            ClientAuthNetworkHandler.sendPacketDirect(new CCreateAccount(username.string,ClientMain.getClient().playerData.password,email.string,token.string));
        }));

    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        super.drawScreen(matrixStack);
        StringRenderer.drawString(matrixStack,debug,100,700,0.5f);
    }
}
