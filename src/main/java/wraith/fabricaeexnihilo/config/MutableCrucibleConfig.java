package wraith.fabricaeexnihilo.config;

public final class MutableCrucibleConfig {
    private int stoneProcessingRate;
    private int woodProcessingRate;
    private int stoneVolume;
    private int woodVolume;
    private boolean efficiency;
    private boolean fireAspect;
    private int tickRate;

    public MutableCrucibleConfig(Source source) {
        this.stoneProcessingRate = source.stoneProcessingRate();
        this.woodProcessingRate = source.woodProcessingRate();
        this.stoneVolume = source.stoneVolume();
        this.woodVolume = source.woodVolume();
        this.efficiency = source.efficiency();
        this.fireAspect = source.fireAspect();
        this.tickRate = source.tickRate();
    }

    public CrucibleConfig toImmutable() {
        return new CrucibleConfig(stoneProcessingRate, woodProcessingRate, stoneVolume, woodVolume, efficiency, fireAspect, tickRate);
    }

    public int getStoneProcessingRate() { return stoneProcessingRate; }
    public int getWoodProcessingRate() { return woodProcessingRate; }
    public int getStoneVolume() { return stoneVolume; }
    public int getWoodVolume() { return woodVolume; }
    public boolean getEfficiency() { return efficiency; }
    public boolean getFireAspect() { return fireAspect; }
    public int getTickRate() { return tickRate; }

    public void setStoneProcessingRate(int stoneProcessingRate) { this.stoneProcessingRate = stoneProcessingRate; }
    public void setWoodProcessingRate(int woodProcessingRate) { this.woodProcessingRate = woodProcessingRate; }
    public void setStoneVolume(int stoneVolume) { this.stoneVolume = stoneVolume; }
    public void setWoodVolume(int woodVolume) { this.woodVolume = woodVolume; }
    public void setEfficiency(boolean efficiency) { this.efficiency = efficiency; }
    public void setFireAspect(boolean fireAspect) { this.fireAspect = fireAspect; }
    public void setTickRate(int tickRate) { this.tickRate = tickRate; }

    public interface Source {
        int stoneProcessingRate();
        int woodProcessingRate();
        int stoneVolume();
        int woodVolume();
        boolean efficiency();
        boolean fireAspect();
        int tickRate();
        default MutableCrucibleConfig toMutable() {
            return new MutableCrucibleConfig(this);
        }
    }
}
