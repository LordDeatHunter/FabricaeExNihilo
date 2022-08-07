package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.util.EntityTypeIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

public class WitchWaterEntityRecipeJS extends RecipeJS {

    private EntityType<?> result;
    private EntityTypeIngredient target;
    private @Nullable VillagerProfession profession = null;

    @Override
    public void create(RecipeArguments listJS) {
        result = Registry.ENTITY_TYPE.get(new Identifier(listJS.get(0).toString()));
        target = CodecUtils.fromJson(EntityTypeIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
    }

    public WitchWaterEntityRecipeJS profession(String id) {
        if (target.getValue().left().map(type -> type == EntityType.VILLAGER).orElse(false))
            throw new IllegalStateException("Cannot call profession() on non-villager entity " + target.toString());
        this.profession = Registry.VILLAGER_PROFESSION.get(new Identifier(id));
        return this;
    }

    @Override
    public void deserialize() {
        target = CodecUtils.fromJson(EntityTypeIngredient.CODEC, json.get("target"));
        profession = json.has("profession") ? Registry.VILLAGER_PROFESSION.get(new Identifier(JsonHelper.getString(json, "profession"))) : null;
        result = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "result")));
    }

    @Override
    public void serialize() {
        json.addProperty("result", Registry.ENTITY_TYPE.getId(result).toString());
        if (profession != null)
            json.addProperty("profession", Registry.VILLAGER_PROFESSION.getId(profession).toString());
        json.add("target", CodecUtils.toJson(EntityTypeIngredient.CODEC, target));
    }
}
