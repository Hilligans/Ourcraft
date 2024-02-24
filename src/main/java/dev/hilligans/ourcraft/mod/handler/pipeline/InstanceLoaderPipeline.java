package dev.hilligans.ourcraft.mod.handler.pipeline;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.mod.handler.content.ModList;
import dev.hilligans.ourcraft.util.sections.ISection;
import dev.hilligans.ourcraft.util.sections.LoadingSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class InstanceLoaderPipeline<T extends InstanceLoaderPipeline> {

    GameInstance gameInstance;

    public ModList modList;

    public LoadingSection section;

    public ArrayList<Tuple<String, PipelineStage<T>>> stages = new ArrayList<>();

    public InstanceLoaderPipeline(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void build() {
        section = new LoadingSection();

        InstanceLoaderPipeline<T> self = this;
        section.startSubSection((section1, stage) -> stage.getTypeB().execute((T)self, section1), stages);
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
