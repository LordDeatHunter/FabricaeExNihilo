package wraith.fabricaeexnihilo.modules;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterStatusEffect;
import wraith.fabricaeexnihilo.util.Color;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModEffects {

    public static final StatusEffect MILKED = new StatusEffect(StatusEffectCategory.NEUTRAL, Color.WHITE.toInt()) {
    };
    /**
     * A status effect used to mark entities that have been processed by the witch water code so that they are no longer
     * processed.
     */
    public static final WitchWaterStatusEffect WITCH_WATERED = new WitchWaterStatusEffect();

    public static void registerEffects() {
        Registry.register(Registries.STATUS_EFFECT, id("witch_watered"), WITCH_WATERED);
        Registry.register(Registries.STATUS_EFFECT, id("milked"), MILKED);
    }

}
