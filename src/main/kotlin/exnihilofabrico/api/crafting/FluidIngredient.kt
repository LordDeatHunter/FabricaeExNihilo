package exnihilofabrico.api.crafting

import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.tag.Tag

class FluidIngredient(val matches: Collection<Fluid>, val tag: Tag<Fluid>?) {
    constructor(tag: Tag<Fluid>): this(tag.values(), tag)
    constructor(matches: Collection<Fluid>): this(matches, null)
    constructor(fluid: Fluid): this(listOf(fluid), null)

    fun test(fluid: FluidState) = test(fluid.fluid)
    fun test(fluid: FluidBlock) = test(fluid.getFluidState(fluid.defaultState).fluid)
    fun test(fluid: Fluid?): Boolean {
        if(fluid == null)
            return false
        return tag?.contains(fluid) ?: matches.any { it.matchesType(fluid) }
    }
}