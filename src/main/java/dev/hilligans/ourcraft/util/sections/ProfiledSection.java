package dev.hilligans.ourcraft.util.sections;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.util.FormattedString;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static dev.hilligans.ourcraft.util.FormattedString.FSTR;

public class ProfiledSection implements ISection {

    protected final Stack<StackFrame> stackFrames = new Stack<>();
    public StackFrame head;

    @Override
    public SectionView startSection(String name) {
        stackFrames.push(new StackFrame(name, Ourcraft.getTime()));
        return new SectionView(name, this);
    }

    @Override
    public void stopSection(@NotNull String name) {
        StackFrame stackFrame = stackFrames.pop();
        if(!name.equals(stackFrame.sectionName)) {
            throw new IllegalArgumentException(STR."Expected frame \{name} does not match actual frame \{stackFrame.sectionName}");
        }
        processFrame(stackFrame);
    }

    private void processFrame(StackFrame stackFrame) {
        stackFrame.setEndTime(Ourcraft.getTime());
        StackFrame lastFrame = stackFrames.empty() ? null : stackFrames.peek();
        if(lastFrame != null) {
            stackFrame.setParent(lastFrame);
            lastFrame.addFrame(stackFrame);
        } else {
            head = stackFrame;
        }
    }

    @Override
    public String toString() {
        if(head == null) {
            if(stackFrames.empty()) {
                return "empty";
            } else {
                StackFrame f = stackFrames.peek().frames.get(0);
                return f.toString1(f.totalTime);
            }
        } else {
            return head.toString();
        }
    }

    public static class StackFrame {
        public final String sectionName;
        public final long startTime;
        public long endTime;
        public long totalTime;
        public StackFrame parent;

        public HashMap<String, StackFrame> stackFrames = new HashMap<>();
        public ArrayList<StackFrame> frames = new ArrayList<>();

        public StackFrame(String sectionName, long startTime) {
            this.sectionName = sectionName;
            this.startTime = startTime;
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
                return STR."\{parent.getIndentLevel()}  ";
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

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(STR."\{getIndentLevel()}\{sectionName}: \{Ourcraft.getConvertedTime(totalTime)}\n");
            for(StackFrame frame : frames) {
                builder.append(frame.toString());
            }
            return builder.toString();
        }

        public String toString1(long time) {
            StringBuilder builder = new StringBuilder();
            builder.append(FSTR."\{getIndentLevel()}\{sectionName}: %2.2f%%\{(double)totalTime/time*100}\n");
            for(StackFrame frame : frames) {
                builder.append(frame.toString1(time));
            }
            return builder.toString();
        }
    }
}
