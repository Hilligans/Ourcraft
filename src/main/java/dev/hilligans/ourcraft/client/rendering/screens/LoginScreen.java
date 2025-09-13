package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;
import dev.hilligans.ourcraft.client.rendering.widgets.InputField;

public class LoginScreen extends ScreenBase {

    InputField username = new InputField(100,100,200,100, "menu.account_name");
    InputField password = new InputField(100,250,200,100, "menu.password");
    InputField email = new InputField(100,400,200,100,"menu.email");

    public LoginScreen() {
        addWidget(username);
        addWidget(password);
        addWidget(email);

        addWidget(new Button(100, 550, 200, 50, "Log in", () -> {
            getClient().playerData.email = email.string;
            String passwordVal = Ourcraft.hashString(password.string, email.string);
            getClient().playerData.userName = username.string;
            getClient().saveUsernameAndPassword();
            //getClient().authNetwork.sendPacket(new CLogin(username.string,passwordVal,email.string));
            getClient().openScreen(new JoinScreen());
        }));
    }

}
