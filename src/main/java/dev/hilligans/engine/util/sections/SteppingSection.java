package dev.hilligans.engine.util.sections;

import dev.hilligans.engine.util.ConsoleReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SteppingSection implements ISection {

    public ConsoleReader consoleReader;
    public AtomicInteger step = new AtomicInteger();

    public static ConsoleReader reader;
    public static final ArrayList<AtomicInteger> vals = new ArrayList<>();

    public SteppingSection() {
        synchronized (vals) {
            if (reader == null) {
                reader = new ConsoleReader(s -> {
                    if (s.equals("n")) {
                        synchronized (vals) {
                            for (AtomicInteger a : vals) {
                                a.getAndIncrement();
                            }
                        }
                    }
                });
            }
            vals.add(step);
        }
        //consoleReader = new ConsoleReader(s -> {
        //    if(s.equals("n")) {step.incrementAndGet(); }
        //});
    }

    @Override
    public SectionView startSection(String name) {
        System.out.println("Waiting on section:"+name);
        while(step.get() == 0) {}
        step.decrementAndGet();
        return new SectionView(name, this);
    }

    @Override
    public void stopSection(String name) {

    }

    @Override
    public synchronized <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, T... names) {
        //todo implement
        throw new NullPointerException("Unimplemented");
    }

    @Override
    public <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, List<T> names) {

    }

    @Override
    public void stopSubSection(String name) {

    }
}
