package wraith.fabricaeexnihilo.mixins;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;

import java.util.Collections;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    /**
     * Inject into getHighestApplicableEnchantmentsAtPower (used by EnchantmentHelper and thus enchantment tables)
     * and check item tags for the applicability of enchantments. The enchantment table skips
     * Enchantment$isAcceptableItem and goes straight for the EnchantmentType's member ... which are all overridden
     * with anonymous functions ....
     *
     * @param power       Enchantment setup power
     * @param stack       Stack to be enchanted
     * @param hasTreasure Include treasure enchantments?
     * @param cir         Callback info.
     */
    @Inject(method = "getPossibleEntries", at = @At(value = "RETURN"), cancellable = true)
    private static void getHighestApplicableEnchantmentsAtPower(int power, ItemStack stack, boolean hasTreasure, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        var list = cir.getReturnValue();
        Registry.ENCHANTMENT.stream()
                .filter(enchantment -> (hasTreasure || !enchantment.isTreasure()) && BonusEnchantingManager.DATA.getOrDefault(enchantment, Collections.emptyList()).contains(stack.getItem()))
                .map(enchantmentLevelEntry -> {
                    for (var level = enchantmentLevelEntry.getMaxLevel(); level > enchantmentLevelEntry.getMinLevel() - 1; level--) {
                        if (power >= enchantmentLevelEntry.getMinPower(level) && power <= enchantmentLevelEntry.getMaxPower(level)) {
                            return new EnchantmentLevelEntry(enchantmentLevelEntry, level);
                        }
                    }
                    return new EnchantmentLevelEntry(enchantmentLevelEntry, 1);
                })
                .filter(tag -> list.stream().noneMatch(entry -> entry.enchantment == tag.enchantment && entry.level == tag.level))
                .forEach(list::add);
    }
}
