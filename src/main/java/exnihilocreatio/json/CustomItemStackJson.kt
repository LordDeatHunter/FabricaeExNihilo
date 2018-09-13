package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.util.LogUtil
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

import java.lang.reflect.Type

object CustomItemStackJson : JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ItemStack {
        val helper = JsonHelper(json)

        val name = helper.getString("name")
        val amount = helper.getNullableInteger("amount", 1)
        val meta = helper.getNullableInteger("meta", 0)

        var item = Item.getByNameOrId(name)

        if (item == null) {
            LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString())
            LogUtil.error("This may result in crashing or other undefined behavior")

            item = Items.AIR
        }

        return ItemStack(item!!, amount, meta)
    }

    override fun serialize(src: ItemStack, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()

        jsonObject.addProperty("name", if (src.item.registryName == null) "" else src.item.registryName!!.toString())
        jsonObject.addProperty("amount", src.count)
        jsonObject.addProperty("meta", src.itemDamage)

        return jsonObject
    }
}
