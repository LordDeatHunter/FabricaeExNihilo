package exnihilofabrico.mixins;

import exnihilofabrico.ExNihiloFabrico;
import exnihilofabrico.impl.EnchantmentTagManager;
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
     * and check item tags for the applicability of enchantments. The enchantment table skips
     * Enchantment$isAcceptableItem and goes straight for the EnchantmentType's member ... which are all overridden
     * with anonymous functions ....
     *
     * @param power Enchantment setup power
     * @param stack Stack to be enchanted
     * @param hasTreasure Include treasure enchantments?
     * @param cir Callback info.
     */
    @Inject(method = "getHighestApplicableEnchantmentsAtPower", at=@At(value = "RETURN"), cancellable = true)
    private static void getHighestApplicableEnchantmentsAtPower(int power, ItemStack stack, boolean hasTreasure, CallbackInfoReturnable<List<InfoEnchantment>> cir) {
        List<InfoEnchantment> taggedEnchantments = EnchantmentTagManager.INSTANCE.getHighestApplicableEnchantmentsAtPower(power, stack, hasTreasure);

        ExNihiloFabrico.INSTANCE.getLOGGER().info("Attempting to Enchant: "+stack.getItem().toString());
        ExNihiloFabrico.INSTANCE.getLOGGER().info("---------------------: "+taggedEnchantments.size());

        if(taggedEnchantments.isEmpty())
            cir.cancel();
        else
            cir.setReturnValue(EnchantmentTagManager.INSTANCE.mergeInfoLists(taggedEnchantments, cir.getReturnValue()));

    }

}
