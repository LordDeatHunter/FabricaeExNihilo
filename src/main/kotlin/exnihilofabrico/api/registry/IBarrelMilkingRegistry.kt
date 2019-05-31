package exnihilofabrico.api.registry

import exnihilofabrico.api.recipes.BarrelMilkingRecipe

interface IBarrelMilkingRegistry {
    fun clear()
    fun register(recipe: BarrelMilkingRecipe)
}