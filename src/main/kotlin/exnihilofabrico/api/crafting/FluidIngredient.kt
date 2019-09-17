package exnihilofabrico.api.crafting

import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.registry.Registry

class FluidIngredient(val matches: Collection<Fluid>, val tag: Tag<Fluid>? = null) {
    constructor(tag: Tag<Fluid>): this(tag.values(), tag)
    constructor(vararg fluids: Fluid): this(fluids.toList())
    constructor(fluid: Fluid): this(listOf(fluid))

    fun test(fluid: FluidState) = test(fluid.fluid)
    fun test(fluid: FluidBlock) = test(fluid.getFluidState(fluid.defaultState).fluid)
    fun test(fluid: Fluid?): Boolean {
        if(fluid == null)
            return false
        return matches.any { it.matchesType(fluid) }
    }
    fun test(item: Item) = if(tag != null) tag.values().any { it.bucketItem == item } else matches.any { it.bucketItem == item }

    override fun toString(): String {
        return tag?.toString() ?: Registry.FLUID.getId(matches.first()).toString()
    }

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is FluidIngredient ->
                this.tag == other.tag || (this.matches.containsAll(other.matches) && this.matches.size == other.matches.size)
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = matches.hashCode()
        result = 31 * result + (tag?.hashCode() ?: 0)
        return result
    }
}