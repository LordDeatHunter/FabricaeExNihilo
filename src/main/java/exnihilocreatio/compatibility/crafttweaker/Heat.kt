package exnihilocreatio.compatibility.crafttweaker

import crafttweaker.IAction
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IItemStack
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCRemoveAll
import exnihilocreatio.registries.manager.ExNihiloRegistryManager
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenClass("mods.exnihilocreatio.Heat")
@ZenRegister
object Heat {

    @ZenMethod
    @JvmStatic
    fun removeAll() {
        CrTIntegration.removeActions += ENCRemoveAll(ExNihiloRegistryManager.HEAT_REGISTRY, "Heat")
    }

    @ZenMethod
    @JvmStatic
    fun addRecipe(input: IItemStack, value: Int) {
        CrTIntegration.addActions += AddRecipe(input, value)
    }

    private class AddRecipe(
            private val input: IItemStack,
            private val value: Int
    ) : IAction {
        override fun apply() {
            ExNihiloRegistryManager.HEAT_REGISTRY.register(input.internal as ItemStack, value)
        }

        override fun describe() = "Adding Heat recipe for $input with value $value"
    }
}
