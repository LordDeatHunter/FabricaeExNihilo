package exnihilofabrico.json

import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import com.google.gson.*
import exnihilofabrico.util.getId
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.lang.reflect.Type


object FluidVolumeJson: JsonDeserializer<FluidVolume>, JsonSerializer<FluidVolume> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FluidVolume {
        val splits = json.asString.split(" x ")
        val amount = splits[0].toInt()
        val key = FluidKeys.get(Registry.FLUID[Identifier(splits[1])])
        return FluidVolume.create(key, amount)
    }

    override fun serialize(src: FluidVolume, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive("${src.amount} x ${src.rawFluid?.getId()}")
}