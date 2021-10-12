package dev.Hilligans.Ourcraft.Client.Rendering.Screens;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.Button;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.InputField;
import dev.Hilligans.Ourcraft.Network.Packet.AuthServerPackets.CLogin;
import dev.Hilligans.Ourcraft.Ourcraft;

public class LoginScreen extends ScreenBase {

    InputField username = new InputField(100,100,200,100, "menu.account_name");
    InputField password = new InputField(100,250,200,100, "menu.password");
    InputField email = new InputField(100,400,200,100,"menu.email");

    public LoginScreen(Client client) {
        super(client);
        widgets.add(username);
        widgets.add(password);
        widgets.add(email);

        widgets.add(new Button(100, 550, 200, 50, "Log in", () -> {
            client.playerData.email = email.string;
            String passwordVal = Ourcraft.hashString(password.string, email.string);
            client.playerData.userName = username.string;
            client.saveUsernameAndPassword();
            client.authNetwork.sendPacket(new CLogin(username.string,passwordVal,email.string));
            client.openScreen(new JoinScreen(client));
        }));
    }

}
