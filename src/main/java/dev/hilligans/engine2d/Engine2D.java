package dev.hilligans.engine2d;

import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.RenderPipeline;
import dev.hilligans.engine.client.graphics.RenderTarget;
import dev.hilligans.engine.client.input.RepeatingInput;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.handler.ModClass;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.RegistryView;
import dev.hilligans.engine2d.client.Client2D;
import dev.hilligans.engine2d.client.tasks.BorderBlackout;
import dev.hilligans.engine2d.client.tasks.WorldRenderer;
import dev.hilligans.engine2d.client.sprite.AnimatedSprite;
import dev.hilligans.engine2d.client.sprite.Sprite;
import dev.hilligans.engine.entity.EntityType;
import dev.hilligans.engine2d.world.MapSection;
import dev.hilligans.engine2d.world.Scene;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class Engine2D implements ModClass {

    public static final String MOD_ID = "engine2D";

    @Override
    public void registerRegistries(RegistryView view) {
        register(view,
                new Tuple<>(Sprite.class, "sprite"),
                new Tuple<>(MapSection.class, "map_section"),
                new Tuple<>(Scene.class, "scene"));
    }

    @Override
    public void registerContent(ModContainer container) {

        if(container.getGameInstance().getSide().isClient()) {
            container.register("engine2D:sprite", new AnimatedSprite("test_sprite", "Images/Items/stone_packed.png", "sprites/TestSprite.json"));
            container.registerEntityType(new EntityType("entity_type", MOD_ID));

            container.registerRenderPipelines(new RenderPipeline("pipeline2d"));

            container.registerRenderTask(new WorldRenderer());
            container.registerRenderTask(new BorderBlackout());

            container.registerRenderTarget(new RenderTarget("second_world_renderer", "engine2D:pipeline2d", "engine2D:world_renderer_2d")
                    .setPipelineState(new PipelineState()));
            container.registerRenderTarget(new RenderTarget("border_blackout_renderer", "engine2D:pipeline2d", "engine2D:border_blackout")
                    .afterTarget("second_world_renderer", "engine2D").setPipelineState(new PipelineState()));

            container.register("engine2D:map_section", new MapSection("test_section", "Images/Sample.png"));
            container.register("engine2D:scene", new Scene("test_scene", "scenes/TestScene.json"));

            container.registerApplication(new Client2D());


            final int MOVE_SPEED = 32;

            container.registerKeybinds(new RepeatingInput("move_up", "ourcraft:key_press_handler::" + GLFW_KEY_W,
                    (window, strength) -> window.getCamera().move(0, (float) (MOVE_SPEED * strength), 0)).onlyWithPipelines("engine2D:pipeline2d"));

            container.registerKeybinds(new RepeatingInput("move_left", "ourcraft:key_press_handler::" + GLFW_KEY_A,
                    (window, strength) -> window.getCamera().move((float) (MOVE_SPEED * strength), 0, 0)).onlyWithPipelines("engine2D:pipeline2d"));

            container.registerKeybinds(new RepeatingInput("move_down", "ourcraft:key_press_handler::" + GLFW_KEY_S,
                    (window, strength) -> window.getCamera().move(0, (float) (-MOVE_SPEED * strength), 0)).onlyWithPipelines("engine2D:pipeline2d"));

            container.registerKeybinds(new RepeatingInput("move_right", "ourcraft:key_press_handler::" + GLFW_KEY_D,
                    (window, strength) -> window.getCamera().move((float) (-MOVE_SPEED * strength), 0, 0)).onlyWithPipelines("engine2D:pipeline2d"));

        }
    }

    @Override
    public String getModID() {
        return MOD_ID;
    }
}
