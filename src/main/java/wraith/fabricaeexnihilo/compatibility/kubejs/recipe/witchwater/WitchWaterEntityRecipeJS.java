package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater;

import dev.latvian.mods.kubejs.recipe.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

public class WitchWaterEntityRecipeJS extends RecipeJS {
    private EntityType<?> result;
    private RegistryEntryList<EntityType<?>> target;
    private @Nullable VillagerProfession profession = null;

    @Override
    public void create(RecipeArguments args) {
        result = Registry.ENTITY_TYPE.get(new Identifier(args.get(0).toString()));
        target = FENKubePlugin.getEntryList(args, 1, Registry.ENTITY_TYPE);
    }

    public WitchWaterEntityRecipeJS profession(String id) {
        if (target.stream().map(RegistryEntry::value).allMatch(type -> type == EntityType.VILLAGER))
            throw new IllegalStateException("Cannot call profession() on non-villager entity " + target.toString());
        this.profession = Registry.VILLAGER_PROFESSION.get(new Identifier(id));
        save();
        return this;
    }

    @Override
    public boolean hasInput(IngredientMatch ingredientMatch) {
        return false;
    }

    @Override
    public boolean replaceInput(IngredientMatch ingredientMatch, Ingredient ingredient, ItemInputTransformer itemInputTransformer) {
        return false;
    }

    @Override
    public boolean hasOutput(IngredientMatch ingredientMatch) {
        return false;
    }

    @Override
    public boolean replaceOutput(IngredientMatch ingredientMatch, ItemStack itemStack, ItemOutputTransformer itemOutputTransformer) {
        return false;
    }

    @Override
    public void deserialize() {
        target = RegistryEntryLists.fromJson(Registry.ENTITY_TYPE_KEY, json.get("target"));
        profession = json.has("profession") ? Registry.VILLAGER_PROFESSION.get(new Identifier(JsonHelper.getString(json, "profession"))) : null;
        result = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "result")));
    }

    @Override
    public void serialize() {
        if (serializeOutputs)
            json.addProperty("result", Registry.ENTITY_TYPE.getId(result).toString());
        if (serializeInputs) {
            json.add("target", RegistryEntryLists.toJson(Registry.ENTITY_TYPE_KEY, target));
            if (profession != null)
                json.addProperty("profession", Registry.VILLAGER_PROFESSION.getId(profession).toString());
        }
    }
}

