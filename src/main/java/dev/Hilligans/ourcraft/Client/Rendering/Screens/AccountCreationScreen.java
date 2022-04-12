package dev.Hilligans.ourcraft.Client.Rendering.Screens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.InputField;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.Network.Packet.AuthServerPackets.CCreateAccount;
import dev.Hilligans.ourcraft.Ourcraft;

public class AccountCreationScreen extends ScreenBase {

    public String debug = "";

    InputField username = new InputField(100,100,200,100, "Account name");
    InputField password = new InputField(100,250,200,100, "Password");
    InputField email = new InputField(100,400,200,100,"Email");
    InputField token = new InputField(100,550,200,100,"Email Token");

    public AccountCreationScreen(Client client) {
        super(client);
        addWidget(username);
        addWidget(password);
        addWidget(email);
        addWidget(token);

        addWidget(new Button(500, 200, 200, 50, "Create Account", () -> {
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
