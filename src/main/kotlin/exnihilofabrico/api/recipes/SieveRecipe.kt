package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack

data class SieveRecipe(val mesh: ItemIngredient,
                       val fluid: FluidIngredient,
                       val sievable: ItemIngredient,
                       val loot: MutableCollection<Lootable>) {
    fun test(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack) =
        this.mesh.test(mesh)
                && fluid?.let { this.fluid.test(fluid) } ?: this.fluid.isEmpty()
                && this.sievable.test(sievable)
    fun test(other: SieveRecipe) = this.mesh == other.mesh && this.fluid == other.fluid && this.sievable == other.sievable
}