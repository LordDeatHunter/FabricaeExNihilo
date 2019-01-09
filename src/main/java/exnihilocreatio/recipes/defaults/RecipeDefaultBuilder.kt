package exnihilocreatio.recipes.defaults

import exnihilocreatio.api.registries.ICompostRegistry
import exnihilocreatio.registries.registries.*

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

        override fun registerCrook(registry: CrookRegistry) {
            super.registerCrook(registry)
        }

        override fun registerSieve(registry: SieveRegistry) {
            super.registerSieve(registry)
        }

        override fun registerHammer(registry: HammerRegistry) {
            super.registerHammer(registry)
        }

        override fun registerHeat(registry: HeatRegistry) {
            super.registerHeat(registry)
        }

        override fun registerBarrelLiquidBlacklist(registry: BarrelLiquidBlacklistRegistry) {
            super.registerBarrelLiquidBlacklist(registry)
        }

        override fun registerFluidOnTop(registry: FluidOnTopRegistry) {
            super.registerFluidOnTop(registry)
        }

        override fun registerOreChunks(registry: OreRegistry) {
            super.registerOreChunks(registry)
        }

        override fun registerFluidTransform(registry: FluidTransformRegistry) {
            super.registerFluidTransform(registry)
        }

        override fun registerFluidBlockTransform(registry: FluidBlockTransformerRegistry) {
            super.registerFluidBlockTransform(registry)
        }

        override fun registerFluidItemFluid(registry: FluidItemFluidRegistry) {
            super.registerFluidItemFluid(registry)
        }

        override fun registerCrucibleStone(registry: CrucibleRegistry) {
            super.registerCrucibleStone(registry)
        }

        override fun registerCrucibleWood(registry: CrucibleRegistry) {
            super.registerCrucibleWood(registry)
        }

        override fun registerMilk(registry: MilkEntityRegistry) {
            super.registerMilk(registry)
        }
    }
}