package wraith.fabricaeexnihilo.mixins;

import wraith.fabricaeexnihilo.impl.EnchantmentTagManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
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
     * and check item tags for the applicability fromPacket enchantments. The enchantment table skips
     * Enchantment$isAcceptableItem and goes straight for the EnchantmentType's member ... which are all overridden
     * with anonymous functions ....
     *
     * @param power Enchantment setup power
     * @param stack Stack to be enchanted
     * @param hasTreasure Include treasure enchantments?
     * @param cir Callback info.
     */
    @Inject(method = "getPossibleEntries", at=@At(value = "RETURN"), cancellable = true)
    private static void getHighestApplicableEnchantmentsAtPower(int power, ItemStack stack, boolean hasTreasure, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        List<EnchantmentLevelEntry> taggedEnchantments = EnchantmentTagManager.getHighestApplicableEnchantmentsAtPower(power, stack, hasTreasure);

        if(taggedEnchantments.isEmpty())
            cir.cancel();
        else
            cir.setReturnValue(EnchantmentTagManager.mergeInfoLists(taggedEnchantments, cir.getReturnValue()));

    }

}
