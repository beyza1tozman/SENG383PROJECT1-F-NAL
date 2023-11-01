public class PerformanceMetrics {
    private long loadTime;
    private long clearTime;
    private long searchTime;
    private long removeTime;

    public long getLoadTime() {
        return loadTime;
    }

    public long getClearTime() {
        return clearTime;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public long getRemoveTime() {
        return removeTime;
    }

    public void accumulateLoadTime(long time) {
        loadTime += time;
    }

    public void accumulateClearTime(long time) {
        clearTime += time;
    }

    public void accumulateSearchTime(long time) {
        searchTime += time;
    }

    public void accumulateRemoveTime(long time) {
        removeTime += time;
    }
}
