package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidIngredient
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import java.util.*

interface IWitchWaterFluidRegistry {
    fun clear()
    fun register(ingredient: FluidIngredient, result: WeightedList<Block>)
    fun isRegistered(fluid: Fluid): Boolean
    fun getResult(fluid: Fluid, rand: Random): Block?
    fun getAllResults(fluid: Fluid): WeightedList<Block>?
}