package exnihilofabrico.api.crafting

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import exnihilofabrico.util.asStack
import exnihilofabrico.util.getID
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class FluidIngredient(val matches: Collection<Fluid>, val tag: Tag<Fluid>? = null) {
    constructor(tag: Tag<Fluid>): this(tag.values(), tag)
    constructor(vararg fluids: Fluid): this(fluids.toList())
    constructor(fluid: Fluid): this(listOf(fluid))

    fun test(fluid: FluidState) = test(fluid.fluid)
    fun test(block: FluidBlock) = test(block.getFluidState(block.defaultState).fluid)
    fun test(fluid: Fluid?): Boolean {
        if(fluid == null)
            return false
        return matches.any { it.matchesType(fluid) }
    }
    fun test(item: Item) = if(tag != null) tag.values().any { it.bucketItem == item } else matches.any { it.bucketItem == item }

    fun toItemStacks() = matches.map { it.bucketItem.asStack() }

    fun toJson(): JsonElement {
        return if(tag != null) {
            return JsonPrimitive("#${tag.id}")
        }
        else {
            val array = JsonArray()
            matches.forEach { array.add(Registry.FLUID.getId(it).toString()) }
            array
        }
    }

    override fun toString() = tag?.toString() ?: matches.first().getID().toString()

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

    companion object {
        fun fromJson(json: JsonElement): FluidIngredient {
            return if(json.isJsonPrimitive && json.asJsonPrimitive.isString) {
                FluidIngredient(Tag(Identifier(json.asJsonPrimitive.asString)))
            }
            else {
                FluidIngredient(json.asJsonArray.map { Identifier(it.asString) }.map { Registry.FLUID.get(it) })
            }
        }
    }
}