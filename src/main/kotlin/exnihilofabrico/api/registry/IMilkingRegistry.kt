package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.EntityTypeIngredient
import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.api.recipes.MilkingRecipe
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.tag.Tag

interface IMilkingRegistry {
    fun clear()

    fun register(recipe: MilkingRecipe): Boolean

    fun register(entity: EntityTypeIngredient, fluid: FluidStack, cooldown: Int) =
        register(MilkingRecipe(entity, fluid, cooldown))
    fun register(entity: EntityType<*>, fluid: FluidStack, cooldown: Int) =
        register(EntityTypeIngredient(entity), fluid, cooldown)
    fun register(entity: EntityType<*>, fluid: Fluid, amount: Int, cooldown: Int) =
        register(EntityTypeIngredient(entity), FluidStack(fluid, amount), cooldown)
    fun register(entity: Tag<EntityType<*>>, fluid: Fluid, amount: Int, cooldown: Int) =
        register(EntityTypeIngredient(entity), FluidStack(fluid, amount), cooldown)
    fun register(entity: Tag<EntityType<*>>, fluid: FluidStack, cooldown: Int) =
        register(EntityTypeIngredient(entity), fluid, cooldown)

    fun getResult(entity: Entity): Pair<FluidStack, Int>?
    fun hasResult(entity: Entity): Boolean
}