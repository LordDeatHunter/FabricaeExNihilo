package wraith.fabricaeexnihilo.modules;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterStatusEffect;
import wraith.fabricaeexnihilo.util.Color;

public final class ModEffects {
    /**
     * A status effect used to mark entities that have been processed by the witch water code so that they are no longer processed.
     */
    public static final WitchWaterStatusEffect WITCH_WATERED = new WitchWaterStatusEffect();
    public static final StatusEffect MILKED = new StatusEffect(StatusEffectCategory.NEUTRAL, Color.WHITE.toInt()){};

    public static void registerEffects() {
        Registry.register(Registry.STATUS_EFFECT, FabricaeExNihilo.id("witch_watered"), WITCH_WATERED);
        Registry.register(Registry.STATUS_EFFECT, FabricaeExNihilo.id("milked"), MILKED);
    }

}
