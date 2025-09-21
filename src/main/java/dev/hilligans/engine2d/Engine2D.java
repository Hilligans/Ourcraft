package dev.hilligans.engine2d;

import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.RenderPipeline;
import dev.hilligans.engine.client.graphics.RenderTarget;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.handler.ModClass;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.RegistryView;
import dev.hilligans.engine2d.client.Client2D;
import dev.hilligans.engine2d.client.WorldRenderer;
import dev.hilligans.engine2d.client.sprite.AnimatedSprite;
import dev.hilligans.engine2d.client.sprite.Sprite;
import dev.hilligans.ourcraft.entity.EntityType;

public class Engine2D implements ModClass {

    public static final String MOD_ID = "engine2D";

    @Override
    public void registerRegistries(RegistryView view) {
        register(view,
                new Tuple<>(Sprite.class, "sprite"));
    }


    @Override
    public void registerContent(ModContainer container) {

        if(container.getGameInstance().getSide().isClient()) {
            container.register("engine2D:sprite", new AnimatedSprite("test_sprite", "Images/Items/stone_packed.png", "sprites/TestSprite.json"));
            container.registerEntityType(new EntityType("entity_type", MOD_ID));

            container.registerRenderPipelines(new RenderPipeline("pipeline2d"));

            container.registerRenderTask(new WorldRenderer());

            container.registerRenderTarget(new RenderTarget("second_world_renderer", "engine2D:pipeline2d", "engine2D:world_renderer_2d")
                    .setPipelineState(new PipelineState()));

            container.registerApplication(new Client2D());
        }
    }

    @Override
    public String getModID() {
        return MOD_ID;
    }
}
