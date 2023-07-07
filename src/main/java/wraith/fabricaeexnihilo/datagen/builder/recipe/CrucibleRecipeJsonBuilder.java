package wraith.fabricaeexnihilo.datagen.builder.recipe;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class CrucibleRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final Ingredient input;
    private final long amount;
    private final FluidVariant fluid;
    private boolean requiresFireproofCrucible = false;

    public CrucibleRecipeJsonBuilder(Ingredient input, FluidVariant fluid, long amount) {
        this.input = input;
        this.amount = amount;
        this.fluid = fluid;
    }

    public static CrucibleRecipeJsonBuilder lava(Ingredient input, long amount) {
        return new CrucibleRecipeJsonBuilder(input, FluidVariant.of(Fluids.LAVA), amount)
                .requiresFireproofCrucible();
    }

    public static CrucibleRecipeJsonBuilder lava(ItemConvertible input, long amount) {
        return lava(Ingredient.ofItems(input), amount);
    }

    public static CrucibleRecipeJsonBuilder water(Ingredient input, long amount) {
        return new CrucibleRecipeJsonBuilder(input, FluidVariant.of(Fluids.WATER), amount);
    }

    public static CrucibleRecipeJsonBuilder water(ItemConvertible input, long amount) {
        return water(Ingredient.ofItems(input), amount);
    }

    public static CrucibleRecipeJsonBuilder water(TagKey<Item> input, long amount) {
        return water(Ingredient.fromTag(input), amount);
    }

    public CrucibleRecipeJsonBuilder requiresFireproofCrucible() {
        this.requiresFireproofCrucible = true;
        return this;
    }

    @Override
    public CraftingRecipeJsonBuilder criterion(String name, CriterionConditions conditions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CraftingRecipeJsonBuilder group(@Nullable String group) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item getOutputItem() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, String recipePath) {
        offerTo(exporter, new Identifier(recipePath));
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        exporter.accept(new Provider(input, amount, fluid, requiresFireproofCrucible, recipeId));
    }

    private record Provider(Ingredient input,
                            long amount,
                            FluidVariant fluid,
                            boolean requiresFireproofCrucible,
                            Identifier id) implements RecipeJsonProvider {
        @Override
        public void serialize(JsonObject json) {
            json.add("input", input.toJson());
            json.addProperty("amount", amount);
            json.add("fluid", CodecUtils.toJson(CodecUtils.FLUID_VARIANT, fluid));
            json.addProperty("requiresFireproofCrucible", requiresFireproofCrucible);
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.CRUCIBLE_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return null;
        }
    }
}
