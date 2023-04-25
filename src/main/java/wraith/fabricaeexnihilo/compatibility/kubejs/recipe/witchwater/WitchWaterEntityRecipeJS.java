package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater;

import dev.latvian.mods.kubejs.recipe.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
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
        result = Registries.ENTITY_TYPE.get(new Identifier(args.get(0).toString()));
        target = FENKubePlugin.getEntryList(args, 1, Registries.ENTITY_TYPE);
    }

    public WitchWaterEntityRecipeJS profession(String id) {
        if (target.stream().map(RegistryEntry::value).allMatch(type -> type == EntityType.VILLAGER))
            throw new IllegalStateException("Cannot call profession() on non-villager entity " + target.toString());
        this.profession = Registries.VILLAGER_PROFESSION.get(new Identifier(id));
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
        target = RegistryEntryLists.fromJson(RegistryKeys.ENTITY_TYPE, json.get("target"));
        profession = json.has("profession") ? Registries.VILLAGER_PROFESSION.get(new Identifier(JsonHelper.getString(json, "profession"))) : null;
        result = Registries.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "result")));
    }

    @Override
    public void serialize() {
        if (serializeOutputs)
            json.addProperty("result", Registries.ENTITY_TYPE.getId(result).toString());
        if (serializeInputs) {
            json.add("target", RegistryEntryLists.toJson(RegistryKeys.ENTITY_TYPE, target));
            if (profession != null)
                json.addProperty("profession", Registries.VILLAGER_PROFESSION.getId(profession).toString());
        }
    }
}

