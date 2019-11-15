package exnihilofabrico.json

import com.google.gson.*
import net.minecraft.nbt.CompoundTag
import java.lang.reflect.Type

object NBTJson: JsonDeserializer<CompoundTag>, JsonSerializer<CompoundTag> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CompoundTag {
        val obj = json.asJsonObject
        val tag = CompoundTag()
        obj.entrySet().forEach { (k,v) ->
            if(v.isJsonPrimitive){
                val primative = v.asJsonPrimitive
                when {
                    primative.isString -> tag.putString(k, v.asString)
                    primative.isBoolean -> tag.putBoolean(k, v.asBoolean)
                }
            }
            else
                tag.put(k, deserialize(v, typeOfT, context))
        }
        return tag
    }

    override fun serialize(src: CompoundTag, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}