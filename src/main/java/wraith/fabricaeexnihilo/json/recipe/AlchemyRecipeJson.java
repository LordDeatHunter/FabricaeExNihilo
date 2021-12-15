package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.EntityStack;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Lootable;
import wraith.fabricaeexnihilo.api.recipes.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;

import java.lang.reflect.Type;

public final class AlchemyRecipeJson extends BaseJson<AlchemyRecipe> {

    private AlchemyRecipeJson() {}

    public static final AlchemyRecipeJson INSTANCE = new AlchemyRecipeJson();

    @Override
    public AlchemyRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new AlchemyRecipe(
                FluidIngredient.fromJson(obj.get("reactant"), context),
                ItemIngredient.fromJson(obj.get("catalyst"), context),
                !obj.has("product") ? new EmptyMode() : BarrelMode.of(obj.get("product"), context),
                !obj.has("byproduct") ? Lootable.EMPTY : context.deserialize(obj.get("byproduct"), LOOTABLE_TYPE_TOKEN),
                obj.get("delay").getAsInt(),
                !obj.has("toSpawn") ? EntityStack.EMPTY : context.deserialize(obj.get("toSpawn"), ENTITY_STACK_TYPE_TOKEN)
        );
    }

    @Override
    public JsonElement serialize(AlchemyRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("reactant", src.reactant().toJson(context));
        obj.add("catalyst", src.catalyst().toJson(context));
        if(!(src.product() instanceof EmptyMode)) {
            var product = new JsonObject();
            product.add(src.product().nbtKey(), context.serialize(src.product()));
            obj.add("product", product);
        }
        if(!src.byproduct().isEmpty()) {
            obj.add("byproduct", context.serialize(src.byproduct()));
        }
        obj.add("delay", new JsonPrimitive(src.delay()));
        if(!src.toSpawn().isEmpty()) {
            obj.add("toSpawn", context.serialize(src.toSpawn(), ENTITY_STACK_TYPE_TOKEN));
        }
        return obj;
    }

}
