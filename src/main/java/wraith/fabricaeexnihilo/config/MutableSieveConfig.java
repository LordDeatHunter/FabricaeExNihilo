package wraith.fabricaeexnihilo.config;

public final class MutableSieveConfig {
    private double baseProgress;
    private boolean efficiency;
    private double efficiencyScaleFactor;
    private boolean fortune;
    private boolean haste;
    private double hasteScaleFactor;
    private int meshStackSize;
    private int sieveRadius;

    public MutableSieveConfig(Source source) {
        this.baseProgress = source.baseProgress();
        this.efficiency = source.efficiency();
        this.efficiencyScaleFactor = source.efficiencyScaleFactor();
        this.fortune = source.fortune();
        this.haste = source.haste();
        this.hasteScaleFactor = source.hasteScaleFactor();
        this.meshStackSize = source.meshStackSize();
        this.sieveRadius = source.sieveRadius();
    }

    public SieveConfig toImmutable() {
        return new SieveConfig(baseProgress, efficiency, efficiencyScaleFactor, fortune, haste, hasteScaleFactor, meshStackSize, sieveRadius);
    }

    public double getBaseProgress() { return baseProgress; }
    public boolean getEfficiency() { return efficiency; }
    public double getEfficiencyScaleFactor() { return efficiencyScaleFactor; }
    public boolean getFortune() { return fortune; }
    public boolean getHaste() { return haste; }
    public double getHasteScaleFactor() { return hasteScaleFactor; }
    public int getMeshStackSize() { return meshStackSize; }
    public int getSieveRadius() { return sieveRadius; }

    public void setBaseProgress(double val) { this.baseProgress = val; }
    public void setEfficiency(boolean val) { this.efficiency = val; }
    public void setEfficiencyScaleFactor(double val) { this.efficiencyScaleFactor = val; }
    public void setFortune(boolean val) { this.fortune = val; }
    public void setHaste(boolean val) { this.haste = val; }
    public void setHasteScaleFactor(double val) { this.hasteScaleFactor = val; }
    public void setMeshStackSize(int val) { this.meshStackSize = val; }
    public void setSieveRadius(int val) { this.sieveRadius = val; }

    public interface Source {
        double baseProgress();
        boolean efficiency();
        double efficiencyScaleFactor();
        boolean fortune();
        boolean haste();
        double hasteScaleFactor();
        int meshStackSize();
        int sieveRadius();
        default MutableSieveConfig toMutable() {
            return new MutableSieveConfig(this);
        }
    }
}
