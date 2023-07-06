package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.crucible;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.util.CodecUtils;

@SuppressWarnings("UnstableApiUsage")
public class CrucibleRecipeJS extends RecipeJS {
    private long amount;
    private FluidVariant fluid;
    private Ingredient input;

    @Override
    public void create(RecipeArguments listJS) {
        var fluid = FluidStackJS.of(listJS.get(0));
        this.fluid = FluidVariant.of(fluid.getFluid(), fluid.getNbt());
        this.amount = fluid.getAmount();
        input = parseItemInput(listJS.get(1));
        json.addProperty("requiresFireproofCrucible", false);
    }

    @SuppressWarnings("unused") // Used from js
    public CrucibleRecipeJS wooden() {
        json.addProperty("requiresFireproofCrucible", true);
        save();
        return this;
    }

    @Override
    public boolean hasInput(IngredientMatch match) {
        return match.contains(input);
    }

    @Override
    public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
        if (hasInput(match)) {
            input = transformer.transform(this, match, input, with);
            return true;
        }
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
        input = Ingredient.fromJson(JsonHelper.getElement(json, "input"));
        amount = JsonHelper.getLong(json, "amount");
        fluid = CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, JsonHelper.getElement(json, "fluid"));
    }

    @Override
    public void serialize() {
        if (serializeInputs)
            json.add("input", input.toJson());

        if (serializeOutputs) {
            json.addProperty("amount", amount);
            json.add("fluid", CodecUtils.toJson(CodecUtils.FLUID_VARIANT, fluid));
        }
    }
}

