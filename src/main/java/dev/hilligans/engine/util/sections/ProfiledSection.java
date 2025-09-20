package dev.hilligans.engine.util.sections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

//import static dev.hilligans.ourcraft.util.FormattedString.FSTR;

public class ProfiledSection implements ISection {

    protected final Stack<StackFrame> stackFrames = new Stack<>();
    public StackFrame monitor;
    public StackFrame monitoredTotalTime;
    public String monitorName;

    @Override
    public SectionView startSection(String name) {
        stackFrames.push(new StackFrame(name, getTime()));
        return new SectionView(name, this);
    }

    public ProfiledSection setMonitorName(String name) {
        this.monitorName = name;
        return this;
    }

    @Override
    public void stopSection(@NotNull String name) {
        StackFrame stackFrame = stackFrames.pop();
        if(!name.equals(stackFrame.sectionName)) {
            throw new IllegalArgumentException("Expected frame "+name+" does not match actual frame "+stackFrame.sectionName);
        }
        processFrame(stackFrame);
    }

    @Override
    public synchronized <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, T... names) {
        //todo implement
        throw new RuntimeException("Unimplemented");
    }

    @Override
    public <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, List<T> names) {

    }

    @Override
    public void stopSubSection(String name) {

    }

    public @Nullable StackFrame getMonitor() {
        return monitor;
    }

    public @Nullable StackFrame getMonitoredTotalTime() {
        return monitoredTotalTime;
    }

    private void processFrame(StackFrame stackFrame) {
        stackFrame.setEndTime(getTime());
        StackFrame lastFrame = stackFrames.empty() ? null : stackFrames.peek();
        if(lastFrame != null) {
            stackFrame.setParent(lastFrame);
            lastFrame.addFrame(stackFrame);
        }
        if(monitorName == null) {
            if(lastFrame == null) {
                monitor = stackFrame;
            }
        } else {
            if(monitorName.equals(stackFrame.sectionName)) {
                monitor = stackFrame;
                if(lastFrame != null) {
                    monitoredTotalTime = lastFrame.stackFrames.get(monitorName).copy();
                }
            }
        }
    }

    @Override
    public String toString() {
        if(monitor == null) {
            return "empty";
        } else {
            return monitor.toString();
        }
    }

    public static class StackFrame {
        public final String sectionName;
        public long startTime;
        public long endTime;
        public long totalTime;
        public StackFrame parent;

        public HashMap<String, StackFrame> stackFrames = new HashMap<>();
        public ArrayList<StackFrame> frames = new ArrayList<>();

        public StackFrame(String sectionName, long startTime) {
            this.sectionName = sectionName;
            this.startTime = startTime;
        }

        public StackFrame(String sectionName, long totalTime, StackFrame parent) {
            this.sectionName = sectionName;
            this.totalTime = totalTime;
            this.parent = parent;
        }

        public void setEndTime(long time) {
            endTime = time;
            this.addTime(getRunTime());
        }

        public long getRunTime() {
            return endTime - startTime;
        }

        public void addTime(long time) {
            this.totalTime += time;
        }

        public void setParent(StackFrame stackFrame) {
            this.parent = stackFrame;
        }

        public String getIndentLevel() {
            if(parent == null) {
                return "";
            } else {
                return parent.getIndentLevel()+"  ";
            }
        }

        public void addFrame(StackFrame stackFrame) {
            StackFrame frame = stackFrames.get(stackFrame.sectionName);
            if(frame != null) {
                frame.addTime(stackFrame.getRunTime());
                for(StackFrame stackFrame1 : stackFrame.frames) {
                    frame.addFrame(stackFrame1);
                }
                return;
            }
            stackFrames.put(stackFrame.sectionName, stackFrame);
            frames.add(stackFrame);
        }

        public StackFrame copy() {
            StackFrame stackFrame = new StackFrame(sectionName, totalTime, parent);
            for(StackFrame child : frames) {
                child = child.copy();
                stackFrame.frames.add(child);
                stackFrame.stackFrames.put(child.sectionName, child);
            }
            return stackFrame;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(getIndentLevel() + sectionName + ": " + getConvertedTime(totalTime)+"\n");
            //builder.append(STR."\{getIndentLevel()}\{sectionName}: \{Ourcraft.getConvertedTime(totalTime)}\n");
            for(StackFrame frame : frames) {
                builder.append(frame.toString());
            }
            return builder.toString();
        }

        public String getPercentagesString() {
            return toString1(totalTime);
        }

        String toString1(long time) {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("%s%s: %2.2f\n", getIndentLevel(), sectionName, (double)totalTime/time*100));
            //builder.append(FSTR."\{getIndentLevel()}\{sectionName}: %2.2f%%\{(double)totalTime/time*100}\n");
            for(StackFrame frame : frames) {
                builder.append(frame.toString1(time));
            }
            return builder.toString();
        }
    }

    public static String getConvertedTime(long time) {
        return String.format("%2.2fms", time/1000000f);
    }

    public static long getTime() {
        return System.nanoTime();
    }
}
