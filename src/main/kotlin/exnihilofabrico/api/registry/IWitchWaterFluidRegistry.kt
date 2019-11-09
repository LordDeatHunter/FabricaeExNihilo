package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.TagIngredient
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import java.util.*

interface IWitchWaterFluidRegistry {
    fun clear()
    fun register(fluid: TagIngredient<Fluid>, result: WeightedList)
    fun isRegistered(fluid: Fluid): Boolean
    fun getResult(fluid: Fluid, rand: Random): Block?
    fun getAllResults(fluid: Fluid): WeightedList?
}