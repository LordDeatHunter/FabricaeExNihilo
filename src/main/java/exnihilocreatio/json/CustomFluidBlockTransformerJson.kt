package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.registries.types.FluidBlockTransformer
import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.EntityInfo
import exnihilocreatio.util.StackInfo
import net.minecraft.item.crafting.Ingredient

import java.lang.reflect.Type

object CustomFluidBlockTransformerJson : JsonDeserializer<FluidBlockTransformer>, JsonSerializer<FluidBlockTransformer> {
    override fun serialize(src: FluidBlockTransformer, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.addProperty("fluidName", src.fluidName)
        obj.add("input", context.serialize(src.input))
        obj.add("output", context.serialize(src.output))

        if (src.toSpawn !== EntityInfo.EMPTY)
            obj.add("toSpawn", context.serialize(src.toSpawn))

        if (src.spawnCount != 0) {
            obj.addProperty("spawnCount", src.spawnCount)
        }

        if (src.spawnRange != 0) {
            obj.addProperty("spawnRange", src.spawnRange)
        }

        return obj
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FluidBlockTransformer {
        val obj = json.asJsonObject

        return FluidBlockTransformer(
                obj.getAsJsonPrimitive("fluidName").asString,
                context.deserialize(obj.get("input"), Ingredient::class.java),
                context.deserialize(obj.get("output"), BlockInfo::class.java),
                if (obj.has("toSpawn")) context.deserialize<Any>(obj.get("toSpawn"), EntityInfo::class.java) as EntityInfo else EntityInfo.EMPTY,
                if (obj.has("spawnCount")) obj.getAsJsonPrimitive("spawnCount").asInt else 0,
                if (obj.has("spawnRange")) obj.getAsJsonPrimitive("spawnRange").asInt else 0
        )
    }
}
