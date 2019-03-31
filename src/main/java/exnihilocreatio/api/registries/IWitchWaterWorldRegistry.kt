package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.WitchWaterWorld
import exnihilocreatio.util.BlockInfo
import net.minecraftforge.fluids.Fluid

interface IWitchWaterWorldRegistry: IRegistryMap<Fluid, WitchWaterWorld> {
    fun contains(fluid: Fluid): Boolean
    fun getResult(fluid: Fluid, chance: Float): BlockInfo
}