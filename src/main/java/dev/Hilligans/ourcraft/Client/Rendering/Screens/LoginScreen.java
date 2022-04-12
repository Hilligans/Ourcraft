package dev.Hilligans.ourcraft.Client.Rendering.Screens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.InputField;
import dev.Hilligans.ourcraft.Network.Packet.AuthServerPackets.CLogin;
import dev.Hilligans.ourcraft.Ourcraft;

public class LoginScreen extends ScreenBase {

    InputField username = new InputField(100,100,200,100, "menu.account_name");
    InputField password = new InputField(100,250,200,100, "menu.password");
    InputField email = new InputField(100,400,200,100,"menu.email");

    public LoginScreen(Client client) {
        super(client);
        addWidget(username);
        addWidget(password);
        addWidget(email);

        addWidget(new Button(100, 550, 200, 50, "Log in", () -> {
            client.playerData.email = email.string;
            String passwordVal = Ourcraft.hashString(password.string, email.string);
            client.playerData.userName = username.string;
            client.saveUsernameAndPassword();
            client.authNetwork.sendPacket(new CLogin(username.string,passwordVal,email.string));
            client.openScreen(new JoinScreen(client));
        }));
    }

}
