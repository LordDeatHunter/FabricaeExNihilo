package exnihilofabrico.mixins;

import exnihilofabrico.impl.enchantments.EnchantmentTagManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    /**
     * Inject into getHighestApplicableEnchantmentsAtPower (used by EnchantmentHelper and thus enchantment tables)
     * and check item tags for the applicability of enchantments.
     *
     * @param power
     * @param stack
     * @param hasTreasure
     * @param cir
     */
    @Inject(method = "getHighestApplicableEnchantmentsAtPower", at=@At(value = "RETURN"), cancellable = true)
    private static void getHighestApplicableEnchantmentsAtPower(int power, ItemStack stack, boolean hasTreasure, CallbackInfoReturnable<List<InfoEnchantment>> cir) {
        List<InfoEnchantment> taggedEnchantments = EnchantmentTagManager.INSTANCE.getHighestApplicableEnchantmentsAtPower(power, stack, hasTreasure);

        cir.setReturnValue(EnchantmentTagManager.INSTANCE.mergeInfoLists(taggedEnchantments, cir.getReturnValue()));

    }

}
