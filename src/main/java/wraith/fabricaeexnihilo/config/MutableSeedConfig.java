package wraith.fabricaeexnihilo.config;

public final class MutableSeedConfig {
    private boolean enabled;
    private boolean cactus;
    private boolean chorus;
    private boolean flowerSeeds;
    private boolean grass;
    private boolean kelp;
    private boolean mycelium;
    private boolean netherSpores;
    private boolean seaPickle;
    private boolean sugarCane;

    public MutableSeedConfig(Source source) {
        this.enabled = source.enabled();
        this.cactus = source.cactus();
        this.chorus = source.chorus();
        this.flowerSeeds = source.flowerSeeds();
        this.grass = source.grass();
        this.kelp = source.kelp();
        this.mycelium = source.mycelium();
        this.netherSpores = source.netherSpores();
        this.seaPickle = source.seaPickle();
        this.sugarCane = source.sugarCane();
    }

    public SeedConfig toImmutable() {
        return new SeedConfig(enabled, cactus, chorus, flowerSeeds, grass, kelp, mycelium, netherSpores, seaPickle, sugarCane);
    }

    public boolean getEnabled() { return enabled; }
    public boolean getCactus() { return cactus; }
    public boolean getChorus() { return chorus; }
    public boolean getFlowerSeeds() { return flowerSeeds; }
    public boolean getGrass() { return grass; }
    public boolean getKelp() { return kelp; }
    public boolean getMycelium() { return mycelium; }
    public boolean getNetherSpores() { return netherSpores; }
    public boolean getSeaPickle() { return seaPickle; }
    public boolean getSugarCane() { return sugarCane; }

    public void setEnabled(boolean val) { this.enabled = val; }
    public void setCactus(boolean val) { this.cactus = val; }
    public void setChorus(boolean val) { this.chorus = val; }
    public void setFlowerSeeds(boolean val) { this.flowerSeeds = val; }
    public void setGrass(boolean val) { this.grass = val; }
    public void setKelp(boolean val) { this.kelp = val; }
    public void setMycelium(boolean val) { this.mycelium = val; }
    public void setNetherSpores(boolean val) { this.netherSpores = val; }
    public void setSeaPickle(boolean val) { this.seaPickle = val; }
    public void setSugarCane(boolean val) { this.sugarCane = val; }

    public interface Source {
        boolean enabled();
        boolean cactus();
        boolean chorus();
        boolean flowerSeeds();
        boolean grass();
        boolean kelp();
        boolean mycelium();
        boolean netherSpores();
        boolean seaPickle();
        boolean sugarCane();
        default MutableSeedConfig toMutable() {
            return new MutableSeedConfig(this);
        }
    }
}
