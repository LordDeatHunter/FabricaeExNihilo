package exnihilocreatio.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilocreatio.items.ore.Ore
import exnihilocreatio.texturing.Color
import exnihilocreatio.util.ItemInfo
import java.lang.reflect.Type
import java.util.*

object CustomOreJson : JsonDeserializer<Ore>, JsonSerializer<Ore> {
    override fun serialize(src: Ore, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.addProperty("name", src.name)
        obj.add("color", context.serialize(src.color, Color::class.java))
        if(src.result != null)
            obj.add("ingot", context.serialize(src.result, ItemInfo::class.java))
        if(src.dustResult != null)
            obj.add("dust", context.serialize(src.dustResult, ItemInfo::class.java))

        if (src.oredictName != null) {
            obj.addProperty("oredictName", src.oredictName)
        }

        if (src.translations != null) {
            obj.add("translations", context.serialize(src.translations, object : TypeToken<HashMap<String, String>>() {

            }.type))
        }

        return obj
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Ore {
        val helper = JsonHelper(json)

        val name = helper.getString("name")
        val color = context.deserialize<Color>(json.asJsonObject.get("color"), Color::class.java)
        val ingot =
                when {
                    json.asJsonObject.has("result") //Backwards compat
                    -> context.deserialize<ItemInfo>(json.asJsonObject.get("result"), ItemInfo::class.java)
                    json.asJsonObject.has("ingot") -> context.deserialize<ItemInfo>(json.asJsonObject.get("ingot"), ItemInfo::class.java)
                    else -> null
                }
        val dust =
                if(json.asJsonObject.has("dust"))
                    context.deserialize<ItemInfo>(json.asJsonObject.get("dust"), ItemInfo::class.java)
                else
                    null

        var translations: HashMap<String, String>? = null
        if (json.isJsonObject && json.asJsonObject.has("translations")) {
            translations = context.deserialize<HashMap<String, String>>(json.asJsonObject.get("translations"), object : TypeToken<HashMap<String, String>>() {

            }.type)
        }

        var oredictName: String? = null
        if (json.isJsonObject && json.asJsonObject.has("oredictName")) {
            oredictName = helper.getString("oredictName")
        }

        return Ore(name, color, ingot, dust, translations, oredictName)
    }
}
