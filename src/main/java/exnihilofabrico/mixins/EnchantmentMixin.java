package exnihilofabrico.mixins;

import exnihilofabrico.impl.EnchantmentTagManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    /**
     * This call is used (through super calls) by each enchantment instance and is invoked by anvils.
     * @param stack Stack to be enchanted
     * @param cir Callback
     */
    @Inject(method = "isAcceptableItem", at=@At(value = "RETURN"), cancellable = true)
    private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValue())
            cir.cancel();
        else
            cir.setReturnValue(EnchantmentTagManager.INSTANCE.itemIsAcceptableForEnchantment(stack, (Enchantment) (Object) this) );
    }
}
