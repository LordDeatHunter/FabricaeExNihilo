package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.modules.sieves.MeshProperties
import exnihilofabrico.util.Color
import net.minecraft.util.Identifier
import java.lang.reflect.Type

object MeshPropertiesJson: JsonDeserializer<MeshProperties>, JsonSerializer<MeshProperties> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): MeshProperties {
        return MeshProperties(
            context.deserialize(json.asJsonObject.get("id"), object:TypeToken<Identifier>(){}.type),
            context.deserialize(json.asJsonObject.get("displayName"), object:TypeToken<String>(){}.type),
            context.deserialize(json.asJsonObject.get("color"), object:TypeToken<Color>(){}.type),
            context.deserialize(json.asJsonObject.get("keyIngredient"), object:TypeToken<Identifier>(){}.type))
    }

    override fun serialize(src: MeshProperties, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        json.add("id", context.serialize(src.identifier))
        json.add("displayName", context.serialize(src.displayName))
        json.add("color", context.serialize(src.color))
        json.add("keyIngredient", context.serialize(src.keyIngredient))
        return json
    }

}