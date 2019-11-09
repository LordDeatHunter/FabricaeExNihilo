package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.recipes.SieveRecipe
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*

interface ISieveRegistry {
    fun clear()
    fun getResult(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack, player: PlayerEntity?, rand: Random): List<ItemStack>
    fun getAllResults(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack): List<Lootable>

    fun isValidRecipe(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack): Boolean
    fun isValidMesh(mesh: ItemStack): Boolean

    fun register(sieveRecipe: SieveRecipe): Boolean
    fun register(mesh: TagIngredient<Item>, fluid: TagIngredient<Fluid>?, sievable: TagIngredient<Item>, loot: Collection<Lootable>) =
        register(SieveRecipe(mesh, fluid, sievable, loot.toMutableList()))
    fun register(mesh: TagIngredient<Item>, fluid: TagIngredient<Fluid>?, sievable: TagIngredient<Item>, vararg loot: Lootable) =
        register(mesh, fluid, sievable, loot.toMutableList())
    fun register(mesh: TagIngredient<Item>, sievable: TagIngredient<Item>, vararg loot: Lootable) =
        register(mesh, null, sievable, loot.toMutableList())
    fun register(mesh: ItemStack, fluid: Fluid, sievable: ItemStack, vararg loot: Lootable) =
        register(TagIngredient.of(mesh), TagIngredient.of(fluid), TagIngredient.of(sievable), loot.toMutableList())
    fun register(mesh: ItemConvertible, fluid: Fluid, sievable: ItemConvertible, vararg loot: Lootable) =
        register(TagIngredient.of(mesh), TagIngredient.of(fluid), TagIngredient.of(sievable), *loot)
    fun register(mesh: ItemConvertible, fluid: Fluid, sievable: Identifier, vararg loot: Lootable) =
        register(TagIngredient.of(mesh), TagIngredient.of(fluid), TagIngredient.of(Registry.ITEM.get(sievable)), *loot)
    fun register(mesh: ItemConvertible, sievable: ItemConvertible, vararg loot: Lootable) =
        register(TagIngredient.of(mesh), null, TagIngredient.of(sievable), loot.toMutableList())

    fun register(mesh: Identifier, fluid: TagIngredient<Fluid>?, sievable: TagIngredient<Item>, loot: Collection<Lootable>) =
        register(SieveRecipe(TagIngredient.of(Registry.ITEM[mesh]), fluid, sievable, loot.toMutableList()))
    fun register(mesh: Identifier, fluid: TagIngredient<Fluid>?, sievable: TagIngredient<Item>, vararg loot: Lootable) =
        register(TagIngredient.of(Registry.ITEM[mesh]), fluid, sievable, loot.toMutableList())
    fun register(mesh: Identifier, sievable: TagIngredient<Item>, vararg loot: Lootable) =
        register(mesh, null, sievable, *loot)
    fun register(mesh: Identifier, sievable: ItemConvertible, vararg loot: Lootable) =
        register(mesh, TagIngredient.of(sievable), *loot)
    fun register(mesh: ItemConvertible, sievable: Identifier, vararg loot: Lootable) =
        register(mesh, Registry.ITEM.get(sievable), *loot)

    fun getAllRecipes(): Collection<SieveRecipe>
}

