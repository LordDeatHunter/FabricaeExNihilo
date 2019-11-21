package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.modules.sieves.MeshProperties
import exnihilofabrico.util.Color
import net.minecraft.util.Identifier
import java.lang.reflect.Type

object MeshPropertiesJson: JsonDeserializer<MeshProperties>, JsonSerializer<MeshProperties> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): MeshProperties {
        val obj = json.asJsonObject
        return MeshProperties(
            context.deserialize(obj.get("id"), object:TypeToken<Identifier>(){}.type),
            if(obj.has("enchantability")) obj.get("enchantability").asInt else 0,
            obj.get("displayName").asString,
            context.deserialize(obj.get("color"), object:TypeToken<Color>(){}.type),
            context.deserialize(obj.get("keyIngredient"), object:TypeToken<Identifier>(){}.type))
    }

    override fun serialize(src: MeshProperties, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        json.add("id", context.serialize(src.identifier))
        json.add("enchantability", context.serialize(src.enchantability))
        json.add("displayName", context.serialize(src.displayName))
        json.add("color", context.serialize(src.color))
        json.add("keyIngredient", context.serialize(src.keyIngredient))
        return json
    }

}