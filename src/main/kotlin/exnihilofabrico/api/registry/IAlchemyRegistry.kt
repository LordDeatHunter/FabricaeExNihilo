package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.*
import exnihilofabrico.api.recipes.AlchemyRecipe
import exnihilofabrico.modules.barrels.modes.EmptyMode
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack

interface IAlchemyRegistry {
    fun clear()

    fun register(recipe: AlchemyRecipe): Boolean

    fun getRecipe(reactant: FluidStack, catalyst: ItemStack): AlchemyRecipe?

    fun hasRecipe(reactant: FluidStack, catalyst: ItemStack) = getRecipe(reactant, catalyst) != null


    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, byproduct: Lootable, delay: Int, spawn: EntityStack) =
        register(AlchemyRecipe(reactant, catalyst, EmptyMode(), byproduct, delay, spawn))
    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: ItemStack, byproduct: Lootable, delay: Int, spawn: EntityStack) =
        register(AlchemyRecipe(reactant, catalyst, ItemMode(product), byproduct, delay, spawn))
    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: FluidStack, byproduct: Lootable, delay: Int, spawn: EntityStack) =
        register(AlchemyRecipe(reactant, catalyst, FluidMode(product), byproduct, delay, spawn))

    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: FluidStack) =
        register(AlchemyRecipe(reactant, catalyst, FluidMode(product)))
    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: ItemStack) =
        register(AlchemyRecipe(reactant, catalyst, ItemMode(product)))

    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, spawn: EntityStack) =
        register(reactant, catalyst, Lootable.EMPTY, 20, spawn)

    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: FluidStack, spawn: EntityStack) =
        register(reactant, catalyst, product, Lootable.EMPTY, 20, spawn)
    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: ItemStack, spawn: EntityStack) =
        register(reactant, catalyst, product, Lootable.EMPTY, 20, spawn)

    fun register(reactant: Fluid, catalyst: ItemStack, product: Fluid) =
        register(FluidIngredient(reactant), ItemIngredient(catalyst), FluidStack(product))
    fun register(reactant: Fluid, catalyst: ItemStack, product: ItemStack) =
        register(FluidIngredient(reactant), ItemIngredient(catalyst), product)
}