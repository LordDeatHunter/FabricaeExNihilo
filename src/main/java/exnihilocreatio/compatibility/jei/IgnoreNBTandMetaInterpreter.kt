package exnihilocreatio.compatibility.jei

import mezz.jei.api.ISubtypeRegistry
import net.minecraft.item.ItemStack

class IgnoreNBTandMetaInterpreter : ISubtypeRegistry.ISubtypeInterpreter {
    override fun apply(itemStack: ItemStack?): String {
        return itemStack?.item?.registryName?.toString() ?: ""
    }

}