package wraith.fabricaeexnihilo.config;

public final class MutableStrainerConfig {
    private int minWaitTime;
    private int maxWaitTime;

    public MutableStrainerConfig(Source source) {
        this.minWaitTime = source.minWaitTime();
        this.maxWaitTime = source.maxWaitTime();
    }

    public StrainerConfig toImmutable() {
        return new StrainerConfig(minWaitTime, maxWaitTime);
    }

    public int getMinWaitTime() { return minWaitTime; }
    public int getMaxWaitTime() { return maxWaitTime; }

    public void setMinWaitTime(int val) { this.minWaitTime = val; }
    public void setMaxWaitTime(int val) { this.maxWaitTime = val; }

    public interface Source {
        int minWaitTime();
        int maxWaitTime();
        default MutableStrainerConfig toMutable() {
            return new MutableStrainerConfig(this);
        }
    }
}
