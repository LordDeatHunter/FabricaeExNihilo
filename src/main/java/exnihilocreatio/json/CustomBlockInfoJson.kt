package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.LogUtil
import net.minecraft.block.Block

import java.lang.reflect.Type

object CustomBlockInfoJson : JsonDeserializer<BlockInfo>, JsonSerializer<BlockInfo> {
    override fun serialize(src: BlockInfo, typeOfSrc: Type, context: JsonSerializationContext) =
            JsonPrimitive(src.block.registryName?.toString() + ":" + src.meta)


    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BlockInfo {
        if (json.isJsonPrimitive && json.asJsonPrimitive.isString) {
            val name = json.asString
            return BlockInfo(name)
        } else {
            val helper = JsonHelper(json)

            val name = helper.getString("name")
            val meta = helper.getNullableInteger("meta", 0)

            val block = Block.getBlockFromName(name)

            if (block == null) {
                LogUtil.error("Error parsing JSON: Invalid BlockStoneAxle: " + json.toString())
                LogUtil.error("This may result in crashing or other undefined behavior")
                return BlockInfo.EMPTY
            }

            return BlockInfo(block, meta)
        }
    }
}
