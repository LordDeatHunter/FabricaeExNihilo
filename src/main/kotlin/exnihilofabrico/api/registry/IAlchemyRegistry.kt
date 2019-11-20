package exnihilofabrico.api.registry

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.barrel.AlchemyRecipe
import exnihilofabrico.modules.barrels.modes.EmptyMode
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.util.asStack
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.tag.Tag

interface IAlchemyRegistry {
    fun clear()

    fun register(recipe: AlchemyRecipe): Boolean

    fun getRecipe(reactant: FluidVolume, catalyst: ItemStack): AlchemyRecipe?

    fun hasRecipe(reactant: FluidVolume, catalyst: ItemStack) = getRecipe(reactant, catalyst) != null


    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, byproduct: Lootable, delay: Int, spawn: EntityStack) =
        register(
            AlchemyRecipe(
                reactant,
                catalyst,
                EmptyMode(),
                byproduct,
                delay,
                spawn
            )
        )
    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: ItemStack, byproduct: Lootable, delay: Int, spawn: EntityStack) =
        register(
            AlchemyRecipe(
                reactant,
                catalyst,
                ItemMode(product),
                byproduct,
                delay,
                spawn
            )
        )
    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: FluidVolume, byproduct: Lootable, delay: Int, spawn: EntityStack) =
        register(
            AlchemyRecipe(
                reactant,
                catalyst,
                FluidMode(product),
                byproduct,
                delay,
                spawn
            )
        )

    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: FluidVolume) =
        register(AlchemyRecipe(reactant, catalyst, FluidMode(product)))
    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: ItemStack) =
        register(AlchemyRecipe(reactant, catalyst, ItemMode(product)))

    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, spawn: EntityStack) =
        register(reactant, catalyst, Lootable.EMPTY, 20, spawn)

    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: FluidVolume, spawn: EntityStack) =
        register(reactant, catalyst, product, Lootable.EMPTY, 20, spawn)
    fun register(reactant: FluidIngredient, catalyst: ItemIngredient, product: ItemStack, spawn: EntityStack) =
        register(reactant, catalyst, product, Lootable.EMPTY, 20, spawn)

    fun register(reactant: Fluid, catalyst: ItemStack, product: Fluid) =
        register(FluidIngredient(reactant), ItemIngredient(catalyst), FluidVolume.create(product, FluidVolume.BUCKET))
    fun register(reactant: Fluid, catalyst: ItemConvertible, product: Fluid) =
        register(FluidIngredient(reactant), ItemIngredient(catalyst), FluidVolume.create(product, FluidVolume.BUCKET))
    fun register(reactant: Fluid, catalyst: ItemStack, product: ItemStack) =
        register(FluidIngredient(reactant), ItemIngredient(catalyst), product)
    fun register(reactant: Fluid, catalyst: ItemConvertible, product: ItemConvertible) =
        register(FluidIngredient(reactant), ItemIngredient(catalyst), product.asStack())
    fun register(reactant: Tag<Fluid>, catalyst: ItemConvertible, product: ItemConvertible) =
        register(FluidIngredient(reactant), ItemIngredient(catalyst), product.asStack())
}