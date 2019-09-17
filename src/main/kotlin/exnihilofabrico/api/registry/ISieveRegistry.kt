package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.SieveRecipe
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*

interface ISieveRegistry {
    fun clear()
    fun getResult(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack, player: PlayerEntity?, rand: Random): List<ItemStack>
    fun getAllResults(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack): List<Lootable>

    fun isValidRecipe(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack): Boolean
    fun isValidMesh(mesh: ItemStack): Boolean

    fun register(sieveRecipe: SieveRecipe)
    fun register(mesh: Ingredient, fluid: FluidIngredient?, sievable: Ingredient, loot: Collection<Lootable>) =
        register(SieveRecipe(mesh, fluid, sievable, loot.toMutableList()))
    fun register(mesh: Ingredient, fluid: FluidIngredient?, sievable: Ingredient, vararg loot: Lootable) =
        register(mesh, fluid, sievable, loot.toMutableList())
    fun register(mesh: Ingredient, sievable: Ingredient, vararg loot: Lootable) =
        register(mesh, null, sievable, loot.toMutableList())
    fun register(mesh: ItemStack, fluid: Fluid, sievable: ItemStack, vararg loot: Lootable) =
        register(Ingredient.ofStacks(mesh), FluidIngredient(fluid), Ingredient.ofStacks(sievable), loot.toMutableList())
    fun register(mesh: ItemConvertible, fluid: Fluid, sievable: ItemConvertible, vararg loot: Lootable) =
        register(Ingredient.ofItems(mesh), FluidIngredient(fluid), Ingredient.ofItems(sievable), *loot)
    fun register(mesh: ItemConvertible, fluid: Fluid, sievable: Identifier, vararg loot: Lootable) =
        register(Ingredient.ofItems(mesh), FluidIngredient(fluid), Ingredient.ofItems(Registry.ITEM.get(sievable)), *loot)
    fun register(mesh: ItemConvertible, sievable: ItemConvertible, vararg loot: Lootable) =
        register(Ingredient.ofItems(mesh), null, Ingredient.ofItems(sievable), loot.toMutableList())

    fun register(mesh: Identifier, fluid: FluidIngredient?, sievable: Ingredient, loot: Collection<Lootable>) =
        register(SieveRecipe(Ingredient.ofItems(Registry.ITEM[mesh]), fluid, sievable, loot.toMutableList()))
    fun register(mesh: Identifier, fluid: FluidIngredient?, sievable: Ingredient, vararg loot: Lootable) =
        register(Ingredient.ofItems(Registry.ITEM[mesh]), fluid, sievable, loot.toMutableList())
    fun register(mesh: Identifier, sievable: Ingredient, vararg loot: Lootable) =
        register(mesh, null, sievable, *loot)
    fun register(mesh: Identifier, sievable: ItemConvertible, vararg loot: Lootable) =
        register(mesh, Ingredient.ofItems(sievable), *loot)
    fun register(mesh: ItemConvertible, sievable: Identifier, vararg loot: Lootable) =
        register(mesh, Registry.ITEM.get(sievable), *loot)
}

