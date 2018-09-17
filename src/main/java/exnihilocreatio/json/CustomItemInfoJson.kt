package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.util.ItemInfo
import exnihilocreatio.util.LogUtil
import net.minecraft.item.Item
import net.minecraft.nbt.JsonToNBT
import net.minecraft.nbt.NBTException
import net.minecraft.nbt.NBTTagCompound

import java.lang.reflect.Type

object CustomItemInfoJson : JsonDeserializer<ItemInfo>, JsonSerializer<ItemInfo> {
    override fun serialize(src: ItemInfo, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {

        val nbt = src.nbt
        if (nbt == null || nbt.isEmpty)
            return JsonPrimitive(src.item.registryName!!.toString() + ":" + src.meta)

        return JsonObject().apply {
            add("name", context.serialize(src.item.registryName!!.toString()))
            add("meta", context.serialize(src.meta))
            add("nbt", context.serialize(src.nbt.toString()))
        }
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

            var nbt = NBTTagCompound()
            if (json.asJsonObject.has("nbt")) {
                try {
                    nbt = JsonToNBT.getTagFromJson(json.asJsonObject.get("nbt").asString)
                } catch (e: NBTException) {
                    LogUtil.error("Could not convert JSON to NBT: " + json.asJsonObject.get("nbt").toString(), e)
                    e.printStackTrace()
                }
            }

            if (item == null) {
                LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString())
                LogUtil.error("This may result in crashing or other undefined behavior")
                return ItemInfo.EMPTY
            }

            return ItemInfo(item, meta, nbt)
        }
    }
}
