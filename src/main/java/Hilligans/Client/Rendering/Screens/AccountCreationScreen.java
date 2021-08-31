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

    public AccountCreationScreen(Client client) {
        super(client);
        widgets.add(username);
        widgets.add(password);
        widgets.add(email);
        widgets.add(token);

        widgets.add(new Button(500, 200, 200, 50, "Create Account", () -> {
            client.playerData.email = email.string;
            String passwordVal = Ourcraft.hashString(password.string, email.string);
            client.playerData.userName = username.string;
            client.saveUsernameAndPassword();
            client.authNetwork.sendPacket(new CCreateAccount(username.string,passwordVal,email.string,token.string));
        }));

    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        super.drawScreen(matrixStack);
        StringRenderer.drawString(matrixStack,debug,100,700,0.5f);
    }
}
