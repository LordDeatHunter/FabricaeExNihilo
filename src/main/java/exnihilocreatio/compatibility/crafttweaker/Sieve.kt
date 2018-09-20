package exnihilocreatio.compatibility.crafttweaker

import crafttweaker.IAction
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.minecraft.CraftTweakerMC
import exnihilocreatio.blocks.BlockSieve
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCRemoveAll
import exnihilocreatio.registries.manager.ExNihiloRegistryManager
import exnihilocreatio.registries.types.Siftable
import exnihilocreatio.util.ItemInfo
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenClass("mods.exnihilocreatio.Sieve")
@ZenRegister
object Sieve {

    @ZenMethod
    @JvmStatic
    fun removeAll() {
        CrTIntegration.removeActions += ENCRemoveAll(ExNihiloRegistryManager.SIEVE_REGISTRY, "Sieve")
    }

    @ZenMethod
    @JvmStatic
    fun addStringMeshRecipe(block: IIngredient, drop: IItemStack, chance: Float) {
        CrTIntegration.addActions += AddRecipe(block, drop, chance, BlockSieve.MeshType.STRING)
    }

    @ZenMethod
    @JvmStatic
    fun addFlintMeshRecipe(block: IIngredient, drop: IItemStack, chance: Float) {
        CrTIntegration.addActions += AddRecipe(block, drop, chance, BlockSieve.MeshType.FLINT)
    }

    @ZenMethod
    @JvmStatic
    fun addIronMeshRecipe(block: IIngredient, drop: IItemStack, chance: Float) {
        CrTIntegration.addActions += AddRecipe(block, drop, chance, BlockSieve.MeshType.IRON)
    }

    @ZenMethod
    @JvmStatic
    fun addDiamondMeshRecipe(block: IIngredient, drop: IItemStack, chance: Float) {
        CrTIntegration.addActions += AddRecipe(block, drop, chance, BlockSieve.MeshType.DIAMOND)
    }

    private class AddRecipe(
            block: IIngredient,
            private val drop: IItemStack,
            private val chance: Float,
            private val meshType: BlockSieve.MeshType
    ) : IAction {
        private val input: Ingredient = CraftTweakerMC.getIngredient(block)

        override fun apply() {
            ExNihiloRegistryManager.SIEVE_REGISTRY.register(input, Siftable(ItemInfo(drop.internal as ItemStack), chance, meshType.id))
        }

        override fun describe() = "Adding Sieve recipe for ${input.matchingStacks.joinToString(prefix = "[", postfix = "]")} for Mesh ${meshType.getName()}"
    }
}
