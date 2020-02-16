package exnihilofabrico.modules.barrels.modes

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import net.minecraft.nbt.CompoundTag

interface BarrelMode {
    fun toTag(): CompoundTag
    fun tagKey(): String
}

fun barrelModeFactory(tag: CompoundTag): BarrelMode {
    return when {
        tag.contains("item_mode") -> ItemMode.fromTag(tag.getCompound("item_mode"))
        tag.contains("fluid_mode") -> FluidMode.fromTag(tag.getCompound("fluid_mode"))
        tag.contains("alchemy_mode") -> AlchemyMode.fromTag(tag.getCompound("alchemy_mode"))
        tag.contains("compost_mode") -> CompostMode.fromTag(tag.getCompound("compost_mode"))
        else -> EmptyMode()
    }
}

fun barrelModeFactory(json: JsonElement, context: JsonDeserializationContext): BarrelMode {
    val obj = json.asJsonObject
    return when {
        obj.has("fluid_mode") -> context.deserialize(obj["fluid_mode"], object: TypeToken<FluidMode>(){}.type)
        obj.has("item_mode") -> context.deserialize(obj["item_mode"], object: TypeToken<ItemMode>(){}.type)
        obj.has("alchemy_mode") -> context.deserialize(obj["alchemy_mode"], object: TypeToken<AlchemyMode>(){}.type)
        obj.has("compost_mode") -> context.deserialize(obj["compost_mode"], object: TypeToken<CompostMode>(){}.type)
        else -> EmptyMode()
    }
}