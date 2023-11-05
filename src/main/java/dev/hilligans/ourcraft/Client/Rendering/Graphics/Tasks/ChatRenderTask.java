package dev.hilligans.ourcraft.Client.Rendering.Graphics.Tasks;

import dev.hilligans.ourcraft.Client.ChatMessages;
import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderTask;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderTaskSource;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.World.StringRenderer;

public class ChatRenderTask extends RenderTaskSource {

    public ChatRenderTask() {
        super("chat_render_task", "ourcraft:chat_renderer");
    }

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                ChatMessages chatMessages = client.chatMessages;
                StringRenderer stringRenderer = window.getStringRenderer();

                int y = (int) (window.getWindowHeight() - stringRenderer.stringHeight * 1.5);
                int size = chatMessages.messages.size();
                for(int x = 0; x < size; x++) {
                    if(chatMessages.messages.get(size - x - 1).getTypeB() < System.currentTimeMillis() && !chatMessages.typing) {
                        break;
                    }
                    stringRenderer.drawStringInternal(window, screenStack, chatMessages.messages.get(size - x - 1).getTypeA(),0,y,0.5f);

                    y -= stringRenderer.stringHeight / 2;
                    if(y <= 0 ) {
                        break;
                    }
                }
                long time = System.currentTimeMillis();
                chatMessages.messages.removeIf(stringLongTuple -> stringLongTuple.typeB < time);
                String val = chatMessages.getString();
                if(!val.equals("")) {
                    stringRenderer.drawStringInternal(window, screenStack, val,0, (int) (window.getWindowHeight() - stringRenderer.stringHeight / 2),0.5f);
                }
            }
        };
    }
}
