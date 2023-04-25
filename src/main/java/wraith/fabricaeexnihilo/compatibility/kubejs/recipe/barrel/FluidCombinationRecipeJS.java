package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

public class FluidCombinationRecipeJS extends RecipeJS {

    private BarrelMode result;
    private RegistryEntryList<Fluid> contained;
    private RegistryEntryList<Fluid> other;

    @Override
    public void create(RecipeArguments args) {
        result = CodecUtils.fromJson(BarrelMode.CODEC, MapJS.json(args.get(0)));
        contained = FENKubePlugin.getEntryList(args, 1, Registries.FLUID);
        other = FENKubePlugin.getEntryList(args, 2, Registries.FLUID);
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
        contained = RegistryEntryLists.fromJson(RegistryKeys.FLUID, json.get("contained"));
        other = RegistryEntryLists.fromJson(RegistryKeys.FLUID, json.get("other"));
        result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            json.add("contained", RegistryEntryLists.toJson(RegistryKeys.FLUID, contained));
            json.add("other", RegistryEntryLists.toJson(RegistryKeys.FLUID, other));
        }
        if (serializeOutputs)
            json.add("result", CodecUtils.toJson(BarrelMode.CODEC, result));
    }
}
