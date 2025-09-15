package dev.hilligans.engine.mod.handler.pipeline;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.handler.content.ModList;
import dev.hilligans.engine.util.argument.Argument;
import dev.hilligans.engine.util.sections.LoadingSection;

import java.util.ArrayList;
import java.util.function.Consumer;

public class InstanceLoaderPipeline<T extends InstanceLoaderPipeline<?>> {

    public static final Argument<Integer> timeBetweenStages = Argument.integerArg("--timeBetweenLoadingStages", 0)
            .help("Sleep time between engine loading stages, mostly useful for debugging purposes");

    public GameInstance gameInstance;
    public final int sleepTime;

    public ModList modList;

    public LoadingSection section;

    public final ArrayList<Consumer<GameInstance>> POST_CORE_HOOKS = new ArrayList<>();
    public final ArrayList<Consumer<GameInstance>> POST_HOOKS = new ArrayList<>();

    public ArrayList<Tuple<String, PipelineStage<T>>> stages = new ArrayList<>();

    public InstanceLoaderPipeline(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.sleepTime = timeBetweenStages.get(gameInstance);
    }

    public void build() {
        section = new LoadingSection();
        gameInstance.loaderPipeline = this;

        InstanceLoaderPipeline<T> self = this;
        section.startSubSection((section1, stage) -> {
            if(sleepTime != 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (Exception e) {}
            }
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
