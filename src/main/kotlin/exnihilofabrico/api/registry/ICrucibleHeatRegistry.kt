package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidIngredient
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient

interface ICrucibleHeatRegistry {
    fun clear()
    fun register(block: Ingredient, heat: Int)
    fun register(fluid: FluidIngredient, heat: Int)
    fun register(block: Block, heat: Int)
    fun register(fluid: Fluid, heat: Int)

    fun getHeat(block: Block): Int
    fun getHeat(fluid: Fluid): Int
    fun getHeat(item: Item): Int
    fun getHeat(stack: ItemStack): Int
}