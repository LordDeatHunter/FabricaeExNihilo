package wraith.fabricaeexnihilo.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;

import java.util.Collections;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @ModifyReturnValue(method = "isAcceptableItem", at = @At(value = "RETURN"))
    private boolean fabricaeexnihilo$makeEnchantmentsAcceptable(boolean original, ItemStack stack) {
        if (BonusEnchantingManager.DATA.getOrDefault((Enchantment) (Object) this, Collections.emptyList()).contains(stack.getItem())) {
            return true;
        }
        return original;
    }
}
