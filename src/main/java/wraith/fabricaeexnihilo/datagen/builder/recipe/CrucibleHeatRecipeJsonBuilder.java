package wraith.fabricaeexnihilo.datagen.builder.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;

import java.util.function.Consumer;

public class CrucibleHeatRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final BlockIngredient block;
    private final int heat;

    public CrucibleHeatRecipeJsonBuilder(BlockIngredient block, int heat) {
        this.block = block;
        this.heat = heat;
    }

    public CrucibleHeatRecipeJsonBuilder(Block block, int heat) {
        this(BlockIngredient.single(block), heat);
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
        exporter.accept(new CrucibleHeatRecipeJsonProvider(block, heat, recipeId));
    }

    private record CrucibleHeatRecipeJsonProvider(BlockIngredient block, int heat, Identifier id) implements RecipeJsonProvider {
        @Override
        public void serialize(JsonObject json) {
            json.add("block", block.toJson());
            json.addProperty("heat", heat);
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.CRUCIBLE_HEAT_SERIALIZER;
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
