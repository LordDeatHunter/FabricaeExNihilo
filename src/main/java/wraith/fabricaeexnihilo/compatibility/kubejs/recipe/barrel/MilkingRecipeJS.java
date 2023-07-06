package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

@SuppressWarnings("UnstableApiUsage")
public class MilkingRecipeJS extends RecipeJS {
    private FluidVariant fluid;
    private RegistryEntryList<EntityType<?>> entity;
    private long amount;
    private int cooldown;

    @Override
    public void create(RecipeArguments args) {
        var fluidJS = FluidStackJS.of(args.get(0));
        fluid = FluidVariant.of(fluidJS.getFluid(), fluidJS.getNbt());
        entity = FENKubePlugin.getEntryList(args, 1, Registries.ENTITY_TYPE);
        amount = (long) args.getDouble(2, 810);
        cooldown = args.getInt(3, 20);
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
        entity = RegistryEntryLists.fromJson(Registries.ENTITY_TYPE.getKey(), JsonHelper.getElement(json, "entity"));
        fluid = CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, JsonHelper.getElement(json, "fluid"));
        amount = JsonHelper.getLong(json, "amount");
        cooldown = JsonHelper.getInt(json, "cooldown");
    }

    @Override
    public void serialize() {
        if (serializeInputs)
            json.add("entity", RegistryEntryLists.toJson(Registries.ENTITY_TYPE.getKey(), entity));
        if (serializeOutputs) {
            json.add("fluid", CodecUtils.toJson(CodecUtils.FLUID_VARIANT, fluid));
            json.addProperty("amount", amount);
            json.addProperty("cooldown", cooldown);
        }
    }
}

