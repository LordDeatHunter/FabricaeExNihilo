package exnihilofabrico.api.recipes.barrel

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.modules.barrels.modes.BarrelMode
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock

data class FluidOnTopRecipe(val inBarrel: FluidIngredient, val onTop: FluidIngredient, val result: BarrelMode) {
    fun test(contents: FluidVolume, block: FluidBlock) = inBarrel.test(contents) && onTop.test(block)
    fun test(contents: FluidVolume, block: Block) = inBarrel.test(contents) && onTop.test(block)
    fun test(contents: FluidVolume, block: BlockState) = inBarrel.test(contents) && onTop.test(block)
}