package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidIngredient
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import java.util.*

interface IWitchWaterWorldRegistry {
    fun clear()
    fun register(fluid: FluidIngredient, result: WeightedList)
    fun isRegistered(fluid: Fluid): Boolean
    fun getResult(fluid: Fluid, rand: Random): Block?
    fun getAllResults(fluid: Fluid): WeightedList?
}