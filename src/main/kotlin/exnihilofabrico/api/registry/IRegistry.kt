package exnihilofabrico.api.registry

interface IRegistry<T> {
    fun clear()
    fun register(recipe: T): Boolean
    // All recipes, chunked/broken up for pagination
    fun getREIRecipes(): Collection<T>
}