package wraith.fabricaeexnihilo.config;

public final class MutableBarrelConfig {
    private double compostRate;
    private boolean bleeding;
    private boolean milking;
    private int leakRadius;
    private int tickRate;
    private boolean efficiency;
    private boolean thorns;

    public MutableBarrelConfig(Source source) {
        this.compostRate = source.compostRate();
        this.bleeding = source.bleeding();
        this.milking = source.milking();
        this.leakRadius = source.leakRadius();
        this.tickRate = source.tickRate();
        this.efficiency = source.efficiency();
        this.thorns = source.thorns();
    }

    public BarrelConfig toImmutable() {
        return new BarrelConfig(compostRate, bleeding, milking, leakRadius, tickRate, efficiency, thorns);
    }

    public double getCompostRate() { return compostRate; }
    public boolean getBleeding() { return bleeding; }
    public boolean getMilking() { return milking; }
    public int getLeakRadius() { return leakRadius; }
    public int getTickRate() { return tickRate; }
    public boolean getEfficiency() { return efficiency; }
    public boolean getThorns() { return thorns; }

    public void setCompostRate(double compostRate) { this.compostRate = compostRate; }
    public void setBleeding(boolean bleeding) { this.bleeding = bleeding; }
    public void setMilking(boolean milking) { this.milking = milking; }
    public void setLeakRadius(int leakRadius) { this.leakRadius = leakRadius; }
    public void setTickRate(int tickRate) { this.tickRate = tickRate; }
    public void setEfficiency(boolean efficiency) { this.efficiency = efficiency; }
    public void setThorns(boolean thorns) { this.thorns = thorns; }

    public interface Source {
        double compostRate();
        boolean bleeding();
        boolean milking();
        int leakRadius();
        int tickRate();
        boolean efficiency();
        boolean thorns();
        default MutableBarrelConfig toMutable() {
            return new MutableBarrelConfig(this);
        }
    }
}
