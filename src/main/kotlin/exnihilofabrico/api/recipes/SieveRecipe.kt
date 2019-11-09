package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.crafting.test
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient

data class SieveRecipe(
        val mesh: TagIngredient<Item>,
        val fluid: TagIngredient<Fluid>?,
        val sievable: TagIngredient<Item>,
        val loot: MutableCollection<Lootable>
) {
    fun test(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack) =
        this.mesh.test(mesh) && (fluid?.let { this.fluid?.test(it) } ?: fluid == null) && this.sievable.test(sievable)
    fun test(other: SieveRecipe) = this.mesh == other.mesh && this.fluid == other.fluid && this.sievable == other.sievable
}