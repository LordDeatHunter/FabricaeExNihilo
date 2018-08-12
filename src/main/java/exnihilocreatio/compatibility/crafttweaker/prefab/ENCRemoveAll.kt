package exnihilocreatio.compatibility.crafttweaker.prefab


import crafttweaker.IAction
import exnihilocreatio.registries.registries.prefab.BaseRegistry

class ENCRemoveAll(
        private val registry: BaseRegistry<*>,
        private val name: String
) : IAction {
    override fun apply() = registry.clearRegistry()
    override fun describe() = "Removing all recipes for the Ex Nihilo $name"
}
