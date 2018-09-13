package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.FluidFluidBlock
import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.ItemInfo
import net.minecraftforge.fluids.Fluid

interface IFluidOnTopRegistry : IRegistryList<FluidFluidBlock> {
    fun register(fluidInBarrel: Fluid, fluidOnTop: Fluid, result: BlockInfo)
    fun register(fluidInBarrel: Fluid, fluidOnTop: Fluid, result: ItemInfo)

    fun isValidRecipe(fluidInBarrel: Fluid?, fluidOnTop: Fluid?): Boolean

    fun getTransformedBlock(fluidInBarrel: Fluid, fluidOnTop: Fluid): BlockInfo
}
