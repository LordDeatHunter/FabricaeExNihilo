package exnihilocreatio.modules.forestry.registry;

import com.google.gson.*
import exnihilocreatio.json.JsonHelper
import exnihilocreatio.util.BlockInfo
import java.lang.reflect.Type

object CustomHiveRequirementsJson : JsonDeserializer<HiveRequirements>, JsonSerializer<HiveRequirements> {
    override fun serialize(src: HiveRequirements, typeOfSrc: Type, context: JsonSerializationContext) =
            JsonPrimitive(src.getHive().toString())


    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): HiveRequirements {
        val helper = JsonHelper(json)

        val hive = BlockInfo(helper.getString("hive"))
        val dimension = helper.getInteger("dim")

        val minT = helper.getDouble("minTemperature") as Float
        val maxT = helper.getDouble("maxTemperature") as Float

        val allowedBiomes = null;

        val minL = helper.getNullableInteger("minLight", 0)
        val maxL = helper.getNullableInteger("maxLight", 15)

        val minY = helper.getNullableInteger("minElevation", 0)
        val maxY = helper.getNullableInteger("maxElevation", 255)

        val adjBlocks = null;
        val nearBlocks = null;

        return HiveRequirements(hive, dimension, allowedBiomes, minT, maxT, minL, maxL, minY, maxY, adjBlocks, nearBlocks)
    }
}
