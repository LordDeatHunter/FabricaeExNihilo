package exnihilocreatio.compatibility.crafttweaker

import crafttweaker.IAction
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.minecraft.CraftTweakerMC
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCRemoveAll
import exnihilocreatio.registries.manager.ExNihiloRegistryManager
import exnihilocreatio.registries.types.HammerReward
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenClass("mods.exnihilocreatio.Hammer")
@ZenRegister
object Hammer {

    @ZenMethod
    @JvmStatic
    fun removeAll() {
        CrTIntegration.removeActions += ENCRemoveAll(ExNihiloRegistryManager.HAMMER_REGISTRY, "Hammer")
    }

    @ZenMethod
    @JvmStatic
    fun addRecipe(block: IIngredient, drop: IItemStack, miningLevel: Int, chance: Float, fortuneChance: Float) {
        CrTIntegration.addActions += AddRecipe(block, drop, miningLevel, chance, fortuneChance)
    }

    private class AddRecipe(
            block: IIngredient,
            private val drop: IItemStack,
            private val miningLevel: Int,
            private val chance: Float,
            private val fortuneChance: Float
    ) : IAction {
        private val input: Ingredient = CraftTweakerMC.getIngredient(block)

        override fun apply() {
            ExNihiloRegistryManager.HAMMER_REGISTRY.register(input, HammerReward(drop.internal as ItemStack, miningLevel, chance, fortuneChance))
        }

        override fun describe() = "Adding Hammer recipe for $input with drop $drop at a chance of $chance with mining level $miningLevel"
    }
}
