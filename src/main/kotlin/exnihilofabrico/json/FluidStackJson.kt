package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.FluidStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import java.lang.reflect.Type

object FluidStackJson: JsonDeserializer<FluidStack>, JsonSerializer<FluidStack> {
    val TYPE_TOKEN = object: TypeToken<FluidStack>(){}.type

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FluidStack {
        val stack = if(json.isJsonPrimitive) json.asString else
            json.asJsonObject.get("fluid").asString
        val splits = stack.split(" x ")

        val fluid = Identifier(splits[1])
        val amount = splits[0].toInt()
        if(fluid.path == "empty") return FluidStack.EMPTY
        if(amount == 0) return FluidStack.EMPTY

        val data = if(json.isJsonPrimitive) CompoundTag() else
            context.deserialize(json.asJsonObject.get("data"), object: TypeToken<CompoundTag>(){}.type)

        return FluidStack(fluid, amount, data)
    }

    @Throws(JsonParseException::class)
    override fun serialize(src: FluidStack, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val stack = "${src.amount} x ${src.fluid}"
        if(src.data.isEmpty)
            return JsonPrimitive(stack)
        val obj = JsonObject()
        obj.add("fluid", JsonPrimitive(stack))
        obj.add("data", context.serialize(src.data))
        return obj
    }
}