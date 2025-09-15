package dev.hilligans.engine.util.sections;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class LoadingSection implements ISection {

    public Stack<String> stackFrames = new Stack<>();

    public int subsectionLength = 0;
    public int subsectionCount = 0;
    public HashMap<String, LoadingSection> subSections = new HashMap<>();

    public boolean processingSubSections = false;

    @Override
    public SectionView startSection(String name) {
        stackFrames.push(name);
        return new SectionView(name, this);
    }

    @Override
    public void stopSection(@NotNull String name) {
        String stackFrame = stackFrames.pop();
        if(!name.equals(stackFrame)) {
            throw new IllegalArgumentException("Expected frame "+name+" does not match actual frame "+stackFrame);
            //throw new IllegalArgumentException(STR."Expected frame \{name} does not match actual frame \{stackFrame}");
        }
    }

    @Override
    public synchronized <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, T... names) {
        if (subsectionLength == subsectionCount) {
            subsectionLength = 0;
            subsectionCount = 0;
            subSections.clear();
        }

        subsectionLength += names.length;
        processingSubSections = true;

        for (T s : names) {
            LoadingSection section = new LoadingSection();
            subSections.put(s.get(), section);
            consumer.accept(section, s);
        }
    }

    @Override
    public synchronized <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, List<T> names) {
        if (subsectionLength == subsectionCount) {
            subsectionLength = 0;
            subsectionCount = 0;
            subSections.clear();
        }

        subsectionLength += names.size();
        processingSubSections = true;

        for (T s : names) {
            LoadingSection section = new LoadingSection();
            subSections.put(s.get(), section);
        }

        for (T s : names) {
            consumer.accept(subSections.get(s.get()), s);
        }
    }


    @Override
    public synchronized void stopSubSection(String name) {
        if(++subsectionCount == subsectionLength) {
            processingSubSections = false;
        }
        //System.err.println(STR."Completed Section: \{name} - \{subsectionCount} of \{subsectionLength}");
        System.err.println("Completed Section: "+name+" - "+subsectionCount+" of "+subsectionLength);
    }
}
