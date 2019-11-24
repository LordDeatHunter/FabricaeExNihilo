package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.SieveRecipe
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*

interface ISieveRegistry: IRegistry<SieveRecipe> {
    fun getResult(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack, player: PlayerEntity?, rand: Random): List<ItemStack>
    fun getAllResults(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack): List<Lootable>

    fun isValidRecipe(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack): Boolean
    fun isValidMesh(mesh: ItemStack): Boolean
    
    fun register(mesh: ItemIngredient, fluid: FluidIngredient, sievable: ItemIngredient, loot: Collection<Lootable>) =
        register(SieveRecipe(mesh, fluid, sievable, loot.toMutableList()))
    fun register(mesh: ItemIngredient, fluid: FluidIngredient, sievable: ItemIngredient, vararg loot: Lootable) =
        register(mesh, fluid, sievable, loot.toMutableList())
    fun register(mesh: ItemIngredient, sievable: ItemIngredient, vararg loot: Lootable) =
        register(mesh, FluidIngredient.EMPTY, sievable, loot.toMutableList())
    fun register(mesh: ItemStack, fluid: Fluid, sievable: ItemStack, vararg loot: Lootable) =
        register(ItemIngredient(mesh), FluidIngredient(fluid), ItemIngredient(sievable), loot.toMutableList())
    fun register(mesh: ItemConvertible, fluid: Fluid, sievable: ItemConvertible, vararg loot: Lootable) =
        register(ItemIngredient(mesh), FluidIngredient(fluid), ItemIngredient(sievable), *loot)
    fun register(mesh: ItemConvertible, fluid: Fluid, sievable: Identifier, vararg loot: Lootable) =
        register(ItemIngredient(mesh),FluidIngredient(fluid),ItemIngredient(Registry.ITEM.get(sievable)), *loot)
    fun register(mesh: ItemConvertible, sievable: ItemConvertible, vararg loot: Lootable) =
        register(ItemIngredient(mesh), FluidIngredient.EMPTY, ItemIngredient(sievable), loot.toMutableList())

    fun register(mesh: Identifier, fluid: FluidIngredient, sievable: ItemIngredient, loot: Collection<Lootable>) =
        register(SieveRecipe(ItemIngredient(Registry.ITEM[mesh]), fluid, sievable, loot.toMutableList()))
    fun register(mesh: Identifier, fluid: FluidIngredient, sievable: ItemIngredient, vararg loot: Lootable) =
        register(ItemIngredient(Registry.ITEM[mesh]), fluid, sievable, loot.toMutableList())
    fun register(mesh: Identifier, sievable: ItemIngredient, vararg loot: Lootable) =
        register(mesh, FluidIngredient.EMPTY, sievable, *loot)
    fun register(mesh: Identifier, sievable: ItemConvertible, vararg loot: Lootable) =
        register(mesh, ItemIngredient(sievable), *loot)
    fun register(mesh: ItemConvertible, sievable: Identifier, vararg loot: Lootable) =
        register(mesh, Registry.ITEM.get(sievable), *loot)

    fun getAllRecipes(): Collection<SieveRecipe>
    // Like getAllRecipes, except paginated.
    fun getREIRecipes(): Collection<SieveRecipe>
}

