package wraith.fabricaeexnihilo.config;

import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.List;

public final class MutableWitchWaterConfig {
    private List<WitchWaterConfig.StatusEffectStats> effects;

    public MutableWitchWaterConfig(Source source) {
        this.effects = new ArrayList<>(source.effects());
    }

    public WitchWaterConfig toImmutable() {
        return new WitchWaterConfig(List.copyOf(effects));
    }

    public List<WitchWaterConfig.StatusEffectStats> getEffects() { return effects; }
    public void setEffects(List<WitchWaterConfig.StatusEffectStats> effects) { this.effects = effects; }

    public interface Source {
        List<WitchWaterConfig.StatusEffectStats> effects();
        default MutableWitchWaterConfig toMutable() {
            return new MutableWitchWaterConfig(this);
        }
    }

    public static final class MutableStatusEffectStats {
        private Identifier type;
        private int duration;
        private int amplifier;

        public MutableStatusEffectStats(Source source) {
            this.type = source.type();
            this.duration = source.duration();
            this.amplifier = source.amplifier();
        }

        public WitchWaterConfig.StatusEffectStats toImmutable() {
            return new WitchWaterConfig.StatusEffectStats(type, duration, amplifier);
        }

        public Identifier getType() { return type; }
        public int getDuration() { return duration; }
        public int getAmplifier() { return amplifier; }

        public void setType(Identifier type) { this.type = type; }
        public void setDuration(int duration) { this.duration = duration; }
        public void setAmplifier(int amplifier) { this.amplifier = amplifier; }

        public interface Source {
            Identifier type();
            int duration();
            int amplifier();
            default MutableStatusEffectStats toMutable() {
                return new MutableStatusEffectStats(this);
            }
        }
    }
}
