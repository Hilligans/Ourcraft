package dev.hilligans.ourcraft.mod.handler.pipeline;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.mod.handler.content.ModList;
import dev.hilligans.ourcraft.util.sections.ISection;
import dev.hilligans.ourcraft.util.sections.LoadingSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InstanceLoaderPipeline<T extends InstanceLoaderPipeline> {

    GameInstance gameInstance;

    public ModList modList;

    public LoadingSection section;

    public final ArrayList<Consumer<GameInstance>> POST_CORE_HOOKS = new ArrayList<>();
    public final ArrayList<Consumer<GameInstance>> POST_HOOKS = new ArrayList<>();

    public ArrayList<Tuple<String, PipelineStage<T>>> stages = new ArrayList<>();

    public InstanceLoaderPipeline(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void build() {
        section = new LoadingSection();
        gameInstance.loaderPipeline = this;

        InstanceLoaderPipeline<T> self = this;
        section.startSubSection((section1, stage) -> {
            stage.getTypeB().execute((T)self, section1);
            section.stopSubSection(stage.getTypeA());
        }, stages);
    }

    public void addPostHook(Consumer<GameInstance> consumer) {
        POST_HOOKS.add(consumer);
    }

    public void addPostCoreHook(Consumer<GameInstance> consumer) {
        POST_CORE_HOOKS.add(consumer);
    }

    public void runPostCoreHooks() {
        for(Consumer<GameInstance> consumer : POST_CORE_HOOKS) {
            consumer.accept(gameInstance);
        }
    }

    public void runPostHooks() {
        for(Consumer<GameInstance> consumer : POST_HOOKS) {
            consumer.accept(gameInstance);
        }
    }

    public void setModList(ModList modList) {
        this.modList = modList;
    }

    public ModList getModList() {
        return modList;
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public T addStage(String name, PipelineStage<T> stage) {
        stages.add(new Tuple<>(name, stage));
        return (T) this;
    }
}
