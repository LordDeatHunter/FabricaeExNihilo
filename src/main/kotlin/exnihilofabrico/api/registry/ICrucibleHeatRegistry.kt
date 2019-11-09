package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.recipes.CrucibleHeatRecipe
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient

interface ICrucibleHeatRegistry {
    fun clear()
    fun register(recipe: CrucibleHeatRecipe): Boolean
    fun register(block: Ingredient, heat: Int): Boolean
    fun register(fluid: FluidIngredient, heat: Int): Boolean
    fun register(block: Block, heat: Int): Boolean
    fun register(fluid: Fluid, heat: Int): Boolean

    fun getHeat(block: Block): Int
    fun getHeat(fluid: Fluid): Int
    fun getHeat(item: Item): Int
    fun getHeat(stack: ItemStack): Int
}