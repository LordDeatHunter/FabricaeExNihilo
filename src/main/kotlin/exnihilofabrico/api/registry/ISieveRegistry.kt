package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.recipes.SieveRecipe
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import java.util.*

interface ISieveRegistry {
    fun clear()
    fun getResult(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack, player: PlayerEntity?, rand: Random): List<ItemStack>
    fun getAllResults(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack): List<Lootable>

    fun isValidRecipe(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack): Boolean

    fun register(sieveRecipe: SieveRecipe)
    fun register(mesh: Ingredient, fluid: FluidIngredient?, sievable: Ingredient, loot: Collection<Lootable>) = register(SieveRecipe(mesh, fluid, sievable, loot.toMutableList()))
    fun register(mesh: Ingredient, fluid: FluidIngredient?, sievable: Ingredient, vararg loot: Lootable) =
            register(SieveRecipe(mesh, fluid, sievable, loot.toMutableList()))
    fun register(mesh: Ingredient, sievable: Ingredient, vararg loot: Lootable) =
            register(SieveRecipe(mesh, null, sievable, loot.toMutableList()))
    fun register(mesh: ItemStack, fluid: Fluid, sievable: ItemStack, vararg loot: Lootable) =
            register(SieveRecipe(Ingredient.ofStacks(mesh), FluidIngredient(fluid), Ingredient.ofStacks(sievable), loot.toMutableList()))
}

