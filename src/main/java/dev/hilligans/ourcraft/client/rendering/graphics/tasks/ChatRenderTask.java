package dev.hilligans.ourcraft.client.rendering.graphics.tasks;

import dev.hilligans.ourcraft.client.ChatMessages;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderTask;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderTaskSource;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.world.StringRenderer;

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
                    stringRenderer.drawStringInternal(window, graphicsContext, screenStack, chatMessages.messages.get(size - x - 1).getTypeA(),0,y,0.5f);

                    y -= stringRenderer.stringHeight / 2;
                    if(y <= 0 ) {
                        break;
                    }
                }
                long time = System.currentTimeMillis();
                chatMessages.messages.removeIf(stringLongTuple -> stringLongTuple.typeB < time);
                String val = chatMessages.getString();
                if(!val.equals("")) {
                    stringRenderer.drawStringInternal(window, graphicsContext, screenStack, val,0, (int) (window.getWindowHeight() - stringRenderer.stringHeight / 2),0.5f);
                }
            }
        };
    }
}
