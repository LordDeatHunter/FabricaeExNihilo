package wraith.fabricaeexnihilo.util;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public final class PlayerHelper {

    private PlayerHelper() {
    }

    public static void applyPotion(PlayerEntity player, StatusEffectInstance effect) {
        var playerEffect = player.getStatusEffect(effect.getEffectType());
        if ((playerEffect == null ? 0 : playerEffect.getDuration()) <= effect.getDuration()) {
            player.addStatusEffect(effect);
        }
    }
}