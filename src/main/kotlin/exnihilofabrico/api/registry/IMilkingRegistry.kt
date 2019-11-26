package exnihilofabrico.api.registry

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.EntityTypeIngredient
import exnihilofabrico.api.recipes.barrel.MilkingRecipe
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.tag.Tag

interface IMilkingRegistry: IRegistry<MilkingRecipe> {
    fun register(entity: EntityTypeIngredient, fluid: FluidVolume, cooldown: Int) =
        register(MilkingRecipe(entity, fluid, cooldown))
    fun register(entity: EntityType<*>, fluid: FluidVolume, cooldown: Int) =
        register(EntityTypeIngredient(entity), fluid, cooldown)
    fun register(entity: EntityType<*>, fluid: Fluid, amount: Int, cooldown: Int) =
        register(EntityTypeIngredient(entity), FluidVolume.create(fluid, amount), cooldown)
    fun register(entity: Tag<EntityType<*>>, fluid: Fluid, amount: Int, cooldown: Int) =
        register(EntityTypeIngredient(entity), FluidVolume.create(fluid, amount), cooldown)
    fun register(entity: Tag<EntityType<*>>, fluid: FluidVolume, cooldown: Int) =
        register(EntityTypeIngredient(entity), fluid, cooldown)

    fun getResult(entity: Entity): Pair<FluidVolume, Int>?
    fun hasResult(entity: Entity): Boolean

    // All recipes, chunked/broken up for pagination
    fun getREIRecipes(): Collection<MilkingRecipe>
}