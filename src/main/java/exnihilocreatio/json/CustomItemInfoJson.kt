package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.util.ItemInfo
import exnihilocreatio.util.LogUtil
import net.minecraft.item.Item

import java.lang.reflect.Type

object CustomItemInfoJson : JsonDeserializer<ItemInfo>, JsonSerializer<ItemInfo> {
    override fun serialize(src: ItemInfo, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.item.registryName!!.toString() + ":" + src.meta)
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ItemInfo {
        if (json.isJsonPrimitive && json.asJsonPrimitive.isString) {
            val name = json.asString
            return ItemInfo(name)
        } else {
            val helper = JsonHelper(json)

            val name = helper.getString("name")
            val meta = helper.getNullableInteger("meta", 0)

            val item = Item.getByNameOrId(name)

            if (item == null) {
                LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString())
                LogUtil.error("This may result in crashing or other undefined behavior")
                return ItemInfo.EMPTY
            }

            return ItemInfo(item, meta)
        }
    }
}
