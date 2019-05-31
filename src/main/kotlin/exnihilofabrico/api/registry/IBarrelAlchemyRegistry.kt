package exnihilofabrico.api.registry

import exnihilofabrico.api.recipes.BarrelAlchemyRecipe

interface IBarrelAlchemyRegistry {
    fun clear()
    fun <T> register(recipe: BarrelAlchemyRecipe<T>)
}