package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.crucible;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.recipe.util.ItemIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

@SuppressWarnings("UnstableApiUsage")
public class CrucibleRecipeJS extends RecipeJS {
    private long amount;
    private FluidVariant fluid;
    private ItemIngredient input;
    private boolean isFireproof = true;
    
    @Override
    public void create(ListJS listJS) {
        var fluid = FluidStackJS.of(listJS.get(0));
        this.fluid = FluidVariant.of(fluid.getFluid(), fluid.getNbt());
        this.amount = fluid.getAmount();
        input = CodecUtils.fromJson(ItemIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
    }
    
    @SuppressWarnings("unused") // Used from js
    public CrucibleRecipeJS wooden() {
        isFireproof = false;
        return this;
    }
    
    @Override
    public void deserialize() {
        input = CodecUtils.fromJson(ItemIngredient.CODEC, json.get("input"));
        amount = JsonHelper.getLong(json, "amount");
        fluid = CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, json.get("fluid"));
        isFireproof = JsonHelper.getBoolean(json, "isFireproof");
    }
    
    @Override
    public void serialize() {
        json.add("input", CodecUtils.toJson(ItemIngredient.CODEC, input));
        json.addProperty("amount", amount);
        json.add("fluid", CodecUtils.toJson(CodecUtils.FLUID_VARIANT, fluid));
        json.addProperty("isFireproof", isFireproof);
    }
}
