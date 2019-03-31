package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.util.LogUtil
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import java.lang.reflect.Type


object CustomFluidJson : JsonDeserializer<Fluid>, JsonSerializer<Fluid> {
    override fun serialize(src: Fluid, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        LogUtil.debug("Serialized fluid $src as ${src.name}")
        return JsonPrimitive(src.name)
    }


    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Fluid {
        if (json.isJsonPrimitive && json.asJsonPrimitive.isString) {
            return FluidRegistry.getFluid(json.asString)
        } else {
            val helper = JsonHelper(json)

            val name = helper.getString("name")
            val fluid = FluidRegistry.getFluid(name)

            if (fluid == null) {
                LogUtil.error("Error parsing JSON: Invalid Fluid: $json")
                LogUtil.error("This may result in crashing or other undefined behavior")
                return FluidRegistry.WATER
            }

            return fluid
        }
    }
}