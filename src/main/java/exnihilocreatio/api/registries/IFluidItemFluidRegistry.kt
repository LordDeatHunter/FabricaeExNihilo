package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.FluidItemFluid
import exnihilocreatio.util.StackInfo
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.Fluid

interface IFluidItemFluidRegistry : IRegistryList<FluidItemFluid> {
    fun register(inputFluid: String, reactant: StackInfo, outputFluid: String)
    fun register(inputFluid: Fluid, reactant: StackInfo, outputFluid: Fluid)

    fun getFLuidForTransformation(fluid: Fluid, stack: ItemStack): String?
}
