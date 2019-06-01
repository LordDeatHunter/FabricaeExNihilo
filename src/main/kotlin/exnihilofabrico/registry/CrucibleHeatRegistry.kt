package exnihilofabrico.registry

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.recipes.CrucibleHeatRecipe
import exnihilofabrico.api.registry.ICrucibleHeatRegistry
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient

data class CrucibleHeatRegistry(val registry: MutableList<CrucibleHeatRecipe> = ArrayList()): ICrucibleHeatRegistry {
    override fun clear() = registry.clear()

    override fun register(block: Ingredient, heat: Int) {
        registry.add(CrucibleHeatRecipe(block, null, heat))
    }

    override fun register(fluid: FluidIngredient, heat: Int) {
        registry.add(CrucibleHeatRecipe(null, fluid, heat))
    }

    override fun register(block: Block, heat: Int) = register(Ingredient.ofItems(block), heat)
    override fun register(fluid: Fluid, heat: Int) = register(FluidIngredient(fluid), heat)

    override fun getHeat(block: Block) = registry.firstOrNull { it.test(block) }?.value ?: 0
    override fun getHeat(fluid: Fluid) = registry.firstOrNull { it.test(fluid) }?.value ?: 0
    override fun getHeat(item: Item) = registry.firstOrNull { it.test(item) }?.value ?: 0
    override fun getHeat(stack: ItemStack) = registry.firstOrNull { it.test(stack) }?.value ?: 0

}