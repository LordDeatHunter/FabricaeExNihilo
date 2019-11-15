package exnihilofabrico.modules.barrels.modes

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import net.minecraft.nbt.CompoundTag

interface BarrelMode {
    fun toTag(): CompoundTag
    fun tagKey(): String
}

fun BarrelModeFactory(tag: CompoundTag): BarrelMode {
    return if(tag.containsKey("item_mode"))
        ItemMode.fromTag(tag.getCompound("item_mode"))
    else if(tag.containsKey("fluid_mode"))
        FluidMode.fromTag(tag.getCompound("fluid_mode"))
    else if(tag.containsKey("alchemy_mode"))
        AlchemyMode.fromTag(tag.getCompound("alchemy_mode"))
    else if(tag.containsKey("compost_mode"))
        AlchemyMode.fromTag(tag.getCompound("compost_mode"))
    else
        EmptyMode()
}

fun BarrelModeFactory(json: JsonElement,  context: JsonDeserializationContext): BarrelMode {
    val obj = json.asJsonObject
    ExNihiloFabrico.LOGGER.info("Deserializing:\n${obj.get("fluid")}")
    return if(obj.has("fluid_mode"))
        context.deserialize(obj["fluid_mode"], object: TypeToken<FluidMode>(){}.type)
    else if(obj.has("item_mode"))
        context.deserialize(obj["item_mode"], object: TypeToken<ItemMode>(){}.type)
    else if(obj.has("alchemy_mode"))
        context.deserialize(obj["alchemy_mode"], object: TypeToken<AlchemyMode>(){}.type)
    else if(obj.has("compost_mode"))
        context.deserialize(obj["compost_mode"], object: TypeToken<CompostMode>(){}.type)
    else
        EmptyMode()
}