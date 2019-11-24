package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.WeightedList
import exnihilofabrico.api.recipes.witchwater.WitchWaterWorldRecipe
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import java.util.*

interface IWitchWaterWorldRegistry: IRegistry<WitchWaterWorldRecipe> {
    fun register(fluid: FluidIngredient, result: WeightedList) = register(WitchWaterWorldRecipe(fluid, result))
    fun isRegistered(fluid: Fluid): Boolean
    fun getResult(fluid: Fluid, rand: Random): Block?
    fun getAllResults(fluid: Fluid): WeightedList?
}