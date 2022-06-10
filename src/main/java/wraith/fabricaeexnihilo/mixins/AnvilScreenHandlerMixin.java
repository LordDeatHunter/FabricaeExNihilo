package wraith.fabricaeexnihilo.mixins;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;

import java.util.List;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamageable()Z", ordinal = 1))
    private boolean thing(ItemStack instance) {
        return instance.isDamageable() || BonusEnchantingManager.DATA.values().stream().flatMap(List::stream).anyMatch(instance::isOf);
    }
}
