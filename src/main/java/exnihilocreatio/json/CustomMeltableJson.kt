package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.registries.types.Meltable
import exnihilocreatio.util.BlockInfo

import java.lang.reflect.Type

object CustomMeltableJson : JsonDeserializer<Meltable>, JsonSerializer<Meltable> {
    override fun serialize(src: Meltable, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()

        obj.addProperty("fluid", src.fluid)
        obj.addProperty("amount", src.amount)

        if (src.textureOverride != BlockInfo.EMPTY) {
            obj.add("color", context.serialize(src.textureOverride, BlockInfo::class.java))
        }

        return obj
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Meltable {
        val helper = JsonHelper(json)
        val obj = json.asJsonObject

        val fluid = helper.getString("fluid")
        val amount = helper.getInteger("amount")

        var textureOverride = BlockInfo.EMPTY
        if (obj.has("textureOverride")) {
            textureOverride = context.deserialize(json.asJsonObject.get("textureOverride"), BlockInfo::class.java)
        }

        return Meltable(fluid, amount, textureOverride)
    }
}
