package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.util.getId
import net.minecraft.item.ItemStack
import net.minecraft.nbt.StringNbtReader
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.lang.reflect.Type

object ItemStackJson: JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ItemStack {
        return if(json.isJsonPrimitive) {
            if(json.asString.isEmpty())
                ItemStack.EMPTY
            else{
                val splits = json.asString.split(" x ", ignoreCase = true, limit = 2)
                ItemStack(Registry.ITEM[Identifier(splits[1])], splits[0].toInt())
            }
        }
        else {
            val item = Registry.ITEM[Identifier(json.asJsonObject.get("item").asString)]
            val count = json.asJsonObject.get("count").asInt
            val tag = StringNbtReader.parse(json.asJsonObject.get("tag").asString)
            val stack = ItemStack(item, count)
            stack.tag = tag
            stack
        }
    }

    override fun serialize(src: ItemStack, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return if(src.isEmpty)
            JsonPrimitive("")
        else if(!src.hasTag())
            JsonPrimitive("${src.count} x ${src.item.getId()}")
        else {
            val obj = JsonObject()
            obj.add("item", JsonPrimitive(src.item.getId().toString()))
            obj.add("count", JsonPrimitive(src.count))
            obj.add("tag", JsonPrimitive(src.tag.toString()))
            obj
        }
    }

}