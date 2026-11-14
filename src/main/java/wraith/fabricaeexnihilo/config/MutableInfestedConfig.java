package wraith.fabricaeexnihilo.config;

public final class MutableInfestedConfig {
    private int infestedSpreadAttempts;
    private int infestingSpreadAttempts;
    private double minimumSpreadProgress;
    private double progressPerUpdate;
    private double spreadChance;
    private int updateFrequency;

    public MutableInfestedConfig(Source source) {
        this.infestedSpreadAttempts = source.infestedSpreadAttempts();
        this.infestingSpreadAttempts = source.infestingSpreadAttempts();
        this.minimumSpreadProgress = source.minimumSpreadProgress();
        this.progressPerUpdate = source.progressPerUpdate();
        this.spreadChance = source.spreadChance();
        this.updateFrequency = source.updateFrequency();
    }

    public InfestedConfig toImmutable() {
        return new InfestedConfig(infestedSpreadAttempts, infestingSpreadAttempts, minimumSpreadProgress, progressPerUpdate, spreadChance, updateFrequency);
    }

    public int getInfestedSpreadAttempts() { return infestedSpreadAttempts; }
    public int getInfestingSpreadAttempts() { return infestingSpreadAttempts; }
    public double getMinimumSpreadProgress() { return minimumSpreadProgress; }
    public double getProgressPerUpdate() { return progressPerUpdate; }
    public double getSpreadChance() { return spreadChance; }
    public int getUpdateFrequency() { return updateFrequency; }

    public void setInfestedSpreadAttempts(int val) { this.infestedSpreadAttempts = val; }
    public void setInfestingSpreadAttempts(int val) { this.infestingSpreadAttempts = val; }
    public void setMinimumSpreadProgress(double val) { this.minimumSpreadProgress = val; }
    public void setProgressPerUpdate(double val) { this.progressPerUpdate = val; }
    public void setSpreadChance(double val) { this.spreadChance = val; }
    public void setUpdateFrequency(int val) { this.updateFrequency = val; }

    public interface Source {
        int infestedSpreadAttempts();
        int infestingSpreadAttempts();
        double minimumSpreadProgress();
        double progressPerUpdate();
        double spreadChance();
        int updateFrequency();
        default MutableInfestedConfig toMutable() {
            return new MutableInfestedConfig(this);
        }
    }
}
