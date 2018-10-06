package exnihilocreatio.modules.forestry.registry;

import com.google.gson.*
import exnihilocreatio.json.JsonHelper
import exnihilocreatio.util.BlockInfo
import net.minecraft.item.crafting.Ingredient
import java.lang.reflect.Type

object CustomHiveRequirementsJson : JsonDeserializer<HiveRequirements>, JsonSerializer<HiveRequirements> {
    override fun serialize(src: HiveRequirements, typeOfSrc: Type, context: JsonSerializationContext) : JsonElement {
        var obj = JsonObject()
        obj.add("hive", context.serialize(src.hive))
        obj.addProperty("dim", src.dimension)
        obj.addProperty("minTemperature", src.minTemperature)
        obj.addProperty("maxTemperature", src.maxTemperature)
        obj.addProperty("minLight", src.minLight)
        obj.addProperty("maxLight", src.maxLight)
        obj.addProperty("minElevation", src.minElevation)
        obj.addProperty("maxElevation", src.maxElevation)
        if(src.allowedBiomes != null)
            obj.add("allowedBiomes", context.serialize(src.allowedBiomes))
        if(src.adjacentBlocks != null){
            obj.add("adjacentBlocks", context.serialize(src.adjacentBlocks))
        }
        if(src.nearbyBlocks != null)
            obj.add("nearbyBlocks", context.serialize(src.nearbyBlocks))

        return obj
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): HiveRequirements {
        val helper = JsonHelper(json)

        val hive = BlockInfo(helper.getString("hive"))
        val dimension = helper.getInteger("dim")

        val minT = helper.getDouble("minTemperature") as Float
        val maxT = helper.getDouble("maxTemperature") as Float


        val minL = helper.getNullableInteger("minLight", 0)
        val maxL = helper.getNullableInteger("maxLight", 15)

        val minY = helper.getNullableInteger("minElevation", 0)
        val maxY = helper.getNullableInteger("maxElevation", 255)

        var allowedBiomes : Set<Int> = HashSet<Int>();
        if(json.asJsonObject.has("allowedBiomes")){
            allowedBiomes = context.deserialize<Set<Int>>(json.asJsonObject.get("allowedBiomes"), Set::class.java)
        }

        var adjBlocks : Map<Ingredient, Int> = HashMap<Ingredient, Int>()
        if(json.asJsonObject.has("adjBlocks")){
            adjBlocks = context.deserialize<Map<Ingredient, Int>>(json.asJsonObject.get("adjBlocks"), Map::class.java)
        }

        var nearBlocks : Map<Ingredient, Int>  = HashMap<Ingredient, Int>()
        if(json.asJsonObject.has("nearBlocks")){
            nearBlocks = context.deserialize<Map<Ingredient, Int>>(json.asJsonObject.get("nearBlocks"), Map::class.java)
        }

        return HiveRequirements(hive, dimension, allowedBiomes, minT, maxT, minL, maxL, minY, maxY, adjBlocks, nearBlocks)
    }
}
