package exnihilocreatio.compatibility.crafttweaker

import crafttweaker.IAction
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.minecraft.CraftTweakerMC
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCRemoveAll
import exnihilocreatio.registries.manager.ExNihiloRegistryManager
import exnihilocreatio.registries.types.CrookReward
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenClass("mods.exnihilocreatio.Crook")
@ZenRegister
object Crook {

    @ZenMethod
    @JvmStatic
    fun removeAll() {
        CrTIntegration.removeActions += ENCRemoveAll(ExNihiloRegistryManager.CROOK_REGISTRY, "Crook")
    }

    @ZenMethod
    @JvmStatic
    fun addRecipe(input: IIngredient, reward: IItemStack, chance: Float, fortuneChance: Float) {
        CrTIntegration.addActions += AddRecipe(input, reward, chance, fortuneChance)
    }

    private class AddRecipe(
            input: IIngredient,
            reward: IItemStack,
            chance: Float,
            fortuneChance: Float
    ) : IAction {
        private val input: Ingredient = CraftTweakerMC.getIngredient(input)
        private val reward = CrookReward(reward.internal as ItemStack, chance, fortuneChance)

        override fun apply() = ExNihiloRegistryManager.CROOK_REGISTRY.register(input, reward)
        override fun describe() = "Adding Compost recipe for ${input.matchingStacks} with reward $reward"
    }
}
