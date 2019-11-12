package exnihilofabrico.api.crafting

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import exnihilofabrico.util.getFluid
import exnihilofabrico.util.getId
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class FluidIngredient(tags: MutableCollection<Tag<Fluid>> = mutableListOf(), matches: MutableSet<Fluid> = mutableSetOf()):
    AbstractIngredient<Fluid>(tags, matches) {

    constructor(vararg matches: Fluid): this(mutableListOf(), matches.toMutableSet())
    constructor(vararg tags: Tag<Fluid>): this(tags.toMutableList(), mutableSetOf())

    fun test(block: FluidBlock) = test(block.getFluid())
    fun test(state: FluidState) = test(state.getFluid())

    override fun serializeElement(t: Fluid, context: JsonSerializationContext) =
        JsonPrimitive(t.getId().toString())

    companion object {
        val EMPTY = FluidIngredient(Fluids.EMPTY)

        fun fromJson(json: JsonElement, context: JsonDeserializationContext) =
            fromJson(json,
                context,
                { deserializeTag(it, context) },
                { deserializeMatch(it, context) },
                { tags: MutableCollection<Tag<Fluid>>, matches: MutableSet<Fluid> ->
                    FluidIngredient(
                        tags,
                        matches
                    )
                })

        fun deserializeTag(json: JsonElement, context: JsonDeserializationContext) =
            TagRegistry.fluid(Identifier(json.asString.split("#").last()))
        fun deserializeMatch(json: JsonElement, context: JsonDeserializationContext) =
            Registry.FLUID[(Identifier(json.asString))]
    }
}