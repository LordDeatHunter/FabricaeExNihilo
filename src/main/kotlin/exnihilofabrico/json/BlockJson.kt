package exnihilofabrico.json

import com.google.gson.*
import net.minecraft.block.Block
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.lang.reflect.Type

object BlockJson: JsonDeserializer<Block>, JsonSerializer<Block> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        Registry.BLOCK[Identifier(json.asString)]

    override fun serialize(src: Block, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive(Registry.BLOCK.getId(src).toString())

}