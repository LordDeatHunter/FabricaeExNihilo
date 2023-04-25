package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
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
        entity = RegistryEntryLists.fromJson(RegistryKeys.ENTITY_TYPE, json.get("entity"));
        fluid = CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, json.get("fluid"));
        amount = json.get("amount").getAsLong();
        cooldown = json.get("cooldown").getAsInt();
    }

    @Override
    public void serialize() {
        if (serializeInputs)
            json.add("entity", RegistryEntryLists.toJson(RegistryKeys.ENTITY_TYPE, entity));
        if (serializeOutputs) {
            json.add("fluid", CodecUtils.toJson(CodecUtils.FLUID_VARIANT, fluid));
            json.addProperty("amount", amount);
            json.addProperty("cooldown", cooldown);
        }
    }
}

