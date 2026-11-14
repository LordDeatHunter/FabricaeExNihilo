package wraith.fabricaeexnihilo.config;

public final class MutableMiscConfig {
    private boolean enableSaltCollection;

    public MutableMiscConfig(Source source) {
        this.enableSaltCollection = source.enableSaltCollection();
    }

    public MiscConfig toImmutable() {
        return new MiscConfig(enableSaltCollection);
    }

    public boolean getEnableSaltCollection() { return enableSaltCollection; }
    public void setEnableSaltCollection(boolean val) { this.enableSaltCollection = val; }

    public interface Source {
        boolean enableSaltCollection();
        default MutableMiscConfig toMutable() {
            return new MutableMiscConfig(this);
        }
    }
}
