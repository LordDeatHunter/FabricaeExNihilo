package exnihilofabrico.api.registry

interface IRegistry<T> {
    fun clear()
    fun register(recipe: T): Boolean
}