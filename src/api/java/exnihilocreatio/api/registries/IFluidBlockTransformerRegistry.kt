package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.FluidBlockTransformer
import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.EntityInfo
import exnihilocreatio.util.StackInfo
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.fluids.Fluid

interface IFluidBlockTransformerRegistry : IRegistryList<FluidBlockTransformer> {
    fun register(fluid: Fluid?, inputBlock: StackInfo, outputBlock: StackInfo, entityName: String? = null)
    fun register(fluid: Fluid, oredict: String, outputBlock: StackInfo, entityName: String? = null)
    fun register(fluid: String, input: Ingredient, outputBlock: StackInfo, entityName: String?, spawnCount: Int, spawnRange: Int)

    fun canBlockBeTransformedWithThisFluid(fluid: Fluid, stack: ItemStack): Boolean
    fun getBlockForTransformation(fluid: Fluid, stack: ItemStack): BlockInfo
    fun getSpawnCountForTransformation(fluid: Fluid, stack: ItemStack): Int
    fun getSpawnRangeForTransformation(fluid: Fluid, stack: ItemStack): Int

    fun getTransformation(fluid: Fluid, stack: ItemStack): FluidBlockTransformer?
    fun getSpawnForTransformation(fluid: Fluid, stack: ItemStack): EntityInfo?
}
