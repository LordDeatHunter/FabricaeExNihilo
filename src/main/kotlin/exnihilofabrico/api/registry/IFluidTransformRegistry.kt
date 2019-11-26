package exnihilofabrico.api.registry

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.barrel.FluidTransformRecipe
import exnihilofabrico.modules.barrels.modes.BarrelMode
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.util.asStack
import exnihilofabrico.util.asVolume
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid

interface IFluidTransformRegistry: IRegistry<FluidTransformRecipe> {
    fun register(fluid: FluidIngredient, block: ItemIngredient, result: BarrelMode) =
        register(FluidTransformRecipe(fluid, block, result))
    fun register(fluid: Fluid, block: Block, result: Block) =
        register(FluidIngredient(fluid), ItemIngredient(block), ItemMode(result.asStack()))
    fun register(fluid: Fluid, block: Block, result: FluidVolume) =
        register(FluidIngredient(fluid), ItemIngredient(block), FluidMode(result))
    fun register(fluid: Fluid, block: Block, result: Fluid) =
        register(FluidIngredient(fluid), ItemIngredient(block), FluidMode(result.asVolume()))

    fun getResult(contents: Fluid, block: Block): BarrelMode?
    fun getResult(contents: FluidVolume, block: Block) = contents.rawFluid?.let { fluid -> getResult(fluid, block) }

    // All recipes, chunked/broken up for pagination
    fun getREIRecipes(): Collection<FluidTransformRecipe>
}