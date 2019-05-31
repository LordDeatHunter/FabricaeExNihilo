package exnihilofabrico.registry

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.registry.IWitchWaterFluidRegistry
import exnihilofabrico.api.registry.WeightedList
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import java.util.*

data class WitchWaterFluidRegistry(val registry: MutableMap<FluidIngredient, WeightedList<Block>> = HashMap()): IWitchWaterFluidRegistry {
    override fun clear() = registry.clear()

    override fun register(ingredient: FluidIngredient, result: WeightedList<Block>) {
        registry[ingredient]?.amend(result) ?: registry.put(ingredient, result)
    }

    override fun isRegistered(fluid: Fluid) = registry.any { it.key.test(fluid) }

    override fun getResult(fluid: Fluid, rand: Random) = getAllResults(fluid)?.choose(rand)
    override fun getAllResults(fluid: Fluid): WeightedList<Block>? {
        registry.forEach {
            if(it.key.test(fluid))
                return it.value
        }
        return null
    }
}