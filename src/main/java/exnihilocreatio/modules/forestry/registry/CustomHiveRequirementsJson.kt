package exnihilocreatio.modules.forestry.registry

import com.google.gson.*
import exnihilocreatio.json.JsonHelper
import exnihilocreatio.util.BlockInfo
import net.minecraft.item.crafting.Ingredient
import java.lang.reflect.Type

object CustomHiveRequirementsJson : JsonDeserializer<HiveRequirements>, JsonSerializer<HiveRequirements> {
    override fun serialize(src: HiveRequirements, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            add("hive", context.serialize(src.hive))
            addProperty("dim", src.dimension)
            addProperty("minTemperature", src.minTemperature)
            addProperty("maxTemperature", src.maxTemperature)
            addProperty("minLight", src.minLight)
            addProperty("maxLight", src.maxLight)
            addProperty("minElevation", src.minElevation)
            addProperty("maxElevation", src.maxElevation)

            if (src.allowedBiomes != null)
                add("allowedBiomes", context.serialize(src.allowedBiomes))

            if (!src.adjacentBlocks.isEmpty()) {
                add("adjacentBlocks", context.serialize(src.adjacentBlocks))
            }

            if (!src.nearbyBlocks.isEmpty())
                add("nearbyBlocks", context.serialize(src.nearbyBlocks))
        }
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): HiveRequirements {
        val helper = JsonHelper(json)

        val hive = BlockInfo(helper.getString("hive"))
        val dimension = helper.getInteger("dim")

        val minT = helper.getNullableFloat("minTemperature", 0f)
        val maxT = helper.getNullableFloat("maxTemperature", 1000f)


        val minL = helper.getNullableInteger("minLight", 0)
        val maxL = helper.getNullableInteger("maxLight", 15)

        val minY = helper.getNullableInteger("minElevation", 0)
        val maxY = helper.getNullableInteger("maxElevation", 255)

        var allowedBiomes: Set<Int> = HashSet()
        if (json.asJsonObject.has("allowedBiomes")) {
            allowedBiomes = context.deserialize<Set<Int>>(json.asJsonObject.get("allowedBiomes"), Set::class.java)
        }

        var adjBlocks: Map<Ingredient, Int> = HashMap()
        if (json.asJsonObject.has("adjBlocks")) {
            adjBlocks = context.deserialize<Map<Ingredient, Int>>(json.asJsonObject.get("adjBlocks"), Map::class.java)
        }

        var nearBlocks: Map<Ingredient, Int> = HashMap()
        if (json.asJsonObject.has("nearBlocks")) {
            nearBlocks = context.deserialize<Map<Ingredient, Int>>(json.asJsonObject.get("nearBlocks"), Map::class.java)
        }

        return HiveRequirements(hive, dimension, allowedBiomes, minT, maxT, minL, maxL, minY, maxY, adjBlocks, nearBlocks)
    }
}
