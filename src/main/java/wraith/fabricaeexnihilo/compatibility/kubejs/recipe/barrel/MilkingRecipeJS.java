package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import wraith.fabricaeexnihilo.recipe.util.EntityTypeIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

@SuppressWarnings("UnstableApiUsage")
public class MilkingRecipeJS extends RecipeJS {

    private FluidVariant fluid;
    private EntityTypeIngredient entity;
    private long amount;
    private int cooldown;

    @Override
    public void create(RecipeArguments listJS) {
        var fluidJS = FluidStackJS.of(listJS.get(0));
        fluid = FluidVariant.of(fluidJS.getFluid(), fluidJS.getNbt());
        entity = CodecUtils.fromJson(EntityTypeIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
        amount = (long) (double) listJS.get(2);
        cooldown = (int) (double) listJS.get(3);
    }

    @Override
    public void deserialize() {
        entity = CodecUtils.fromJson(EntityTypeIngredient.CODEC, json.get("entity"));
        fluid = CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, json.get("fluid"));
        amount = json.get("amount").getAsLong();
        cooldown = json.get("cooldown").getAsInt();
    }

    @Override
    public void serialize() {
        json.add("entity", CodecUtils.toJson(EntityTypeIngredient.CODEC, entity));
        json.add("fluid", CodecUtils.toJson(CodecUtils.FLUID_VARIANT, fluid));
        json.addProperty("amount", amount);
        json.addProperty("cooldown", cooldown);
    }
}
