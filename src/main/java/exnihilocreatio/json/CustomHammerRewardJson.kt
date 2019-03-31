package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.registries.types.HammerReward
import exnihilocreatio.util.ItemInfo
import exnihilocreatio.util.LogUtil
import net.minecraft.item.ItemStack

import java.lang.reflect.Type

object CustomHammerRewardJson : JsonDeserializer<HammerReward>, JsonSerializer<HammerReward> {
    override fun serialize(src: HammerReward, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()

        obj.addProperty("item", src.stack.item.registryName!!.toString() + ":" + src.stack.metadata)
        obj.addProperty("amount", src.stack.count)
        obj.addProperty("miningLevel", src.miningLevel)
        obj.addProperty("chance", src.chance)
        obj.addProperty("fortuneChance", src.fortuneChance)

        return obj
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): HammerReward {
        val helper = JsonHelper(json)
        val obj = json.asJsonObject


        // gets the item in both the new and the old way
        val stack: ItemStack
        stack = if (obj.has("item")) {
            val info = ItemInfo(helper.getString("item"))
            if (!info.isValid) {
                LogUtil.error("Error parsing JSON: Invalid Item: $json")
                return HammerReward(ItemStack.EMPTY, 0, 0f, 0f)
            }

            ItemStack(info.item, helper.getInteger("amount"), info.meta)
        } else {
            context.deserialize(json.asJsonObject.get("stack"), ItemStack::class.java)
        }

        val miningLevel = helper.getInteger("miningLevel")
        val chance = helper.getDouble("chance").toFloat()
        val fortuneChance = helper.getDouble("fortuneChance").toFloat()

        return HammerReward(stack, miningLevel, chance, fortuneChance)
    }
}
