package exnihilocreatio.recipes.defaults

import exnihilocreatio.api.registries.ICompostRegistry
import exnihilocreatio.registries.registries.CompostRegistry

class RecipeDefaultBuilder {
    private val _modid = "test"
    var compost: (ICompostRegistry.() -> Unit)? = null

    fun compost(arg: ICompostRegistry.() -> Unit) {
        this.compost = arg
    }

    inner class InnerIRecipeDefault : IRecipeDefaults {
        override fun getMODID(): String = _modid

        override fun registerCompost(registry: CompostRegistry) {
            compost?.invoke(registry)
        }

    }
}