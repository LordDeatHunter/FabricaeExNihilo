package wraith.fabricaeexnihilo.modules.witchwater;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import wraith.fabricaeexnihilo.util.Color;

public class WitchWaterStatusEffect extends StatusEffect {

    public WitchWaterStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, Color.DARK_PURPLE.toInt());
    }

    public StatusEffectInstance getInstance() {
        return new StatusEffectInstance(this, 72000, 1, false, false, false);
    }

}
