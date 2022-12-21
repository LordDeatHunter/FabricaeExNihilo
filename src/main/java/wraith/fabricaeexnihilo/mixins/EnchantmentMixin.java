package wraith.fabricaeexnihilo.mixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;

import java.util.Collections;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    /**
     * This call is used (through super calls) by each enchantment instance and is invoked by anvils.
     *
     * @param stack Stack to be enchanted
     * @param cir   Callback
     */
    @Inject(method = "isAcceptableItem", at = @At(value = "RETURN"), cancellable = true)
    private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && BonusEnchantingManager.DATA.getOrDefault((Enchantment) (Object) this, Collections.emptyList()).contains(stack.getItem())) {
            cir.setReturnValue(true);
        }
    }
}
