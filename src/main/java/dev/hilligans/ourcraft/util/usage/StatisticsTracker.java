package dev.hilligans.ourcraft.util.usage;

public class StatisticsTracker implements IUsageTracker {


    @Override
    public String getResourceName() {
        return "statistics_tracker";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }
}
