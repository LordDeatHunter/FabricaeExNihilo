package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.LeakingRecipe
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemConvertible

interface ILeakingRegistry {
    fun clear()

    fun register(recipe: LeakingRecipe): Boolean

    fun register(target: ItemIngredient, fluid: FluidIngredient, loss: Int, result: Block) =
        register(LeakingRecipe(target, fluid, loss, result))

    fun register(target: ItemIngredient, fluid: FluidStack, result: Block) =
        register(LeakingRecipe(target, FluidIngredient(fluid.asFluid()), fluid.amount, result))

    fun register(target: ItemConvertible, fluid: Fluid, amount: Int, result: Block) =
        register(LeakingRecipe(ItemIngredient(target), FluidIngredient(fluid), amount, result))

    /**
     * Returns the block to transform the input block into, and the resulting FluidStack; null if not a valid recipe.
     */
    fun getResult(block: Block, fluid: FluidStack): Pair<Block, FluidStack>?
}