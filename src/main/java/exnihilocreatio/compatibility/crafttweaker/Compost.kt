package exnihilocreatio.compatibility.crafttweaker

import crafttweaker.IAction
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.minecraft.CraftTweakerMC
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCRemoveAll
import exnihilocreatio.registries.manager.ExNihiloRegistryManager
import exnihilocreatio.registries.types.Compostable
import exnihilocreatio.texturing.Color
import exnihilocreatio.util.BlockInfo
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenClass("mods.exnihilocreatio.Compost")
@ZenRegister
object Compost {

    @ZenMethod
    @JvmStatic
    fun removeAll() {
        CrTIntegration.removeActions += ENCRemoveAll(ExNihiloRegistryManager.COMPOST_REGISTRY, "Compost")
    }

    @ZenMethod
    @JvmStatic
    fun addRecipe(input: IIngredient, value: Float, color: String, block: IItemStack) {
        CrTIntegration.addActions += AddRecipe(input, value, color, block)
    }

    private class AddRecipe(
            input: IIngredient,
            private val value: Float,
            color: String,
            block: IItemStack
    ) : IAction {
        private val input: Ingredient = CraftTweakerMC.getIngredient(input)
        private val color: Color = Color(color)
        private val block: BlockInfo = BlockInfo(block.internal as ItemStack)

        override fun apply() {
            ExNihiloRegistryManager.COMPOST_REGISTRY.register(input, Compostable(value, color, block))
        }

        override fun describe() = "Adding Compost recipe for ${input.matchingStacks.joinToString(prefix = "[", postfix = "]")} with value $value and Color $color"
    }
}
