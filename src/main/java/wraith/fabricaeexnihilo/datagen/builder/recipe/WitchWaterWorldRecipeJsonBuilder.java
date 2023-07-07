package wraith.fabricaeexnihilo.datagen.builder.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WitchWaterWorldRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final FluidIngredient target;
    private final Map<Block, Integer> result = new HashMap<>();

    public WitchWaterWorldRecipeJsonBuilder(FluidIngredient target) {
        this.target = target;
    }

    public WitchWaterWorldRecipeJsonBuilder(Fluid target) {
        this.target = FluidIngredient.single(target);
    }

    public WitchWaterWorldRecipeJsonBuilder(TagKey<Fluid> target) {
        this.target = FluidIngredient.tag(target);
    }

    public WitchWaterWorldRecipeJsonBuilder result(Block block, int weight) {
        var prev = result.put(block, weight);
        if (prev != null) throw new IllegalStateException("Two entries for one block");
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
        exporter.accept(new Provider(target, result, recipeId));
    }

    private record Provider(FluidIngredient target, Map<Block, Integer> result, Identifier id) implements RecipeJsonProvider {
        @Override
        public void serialize(JsonObject json) {
            json.add("target", target.toJson());
            json.add("result", CodecUtils.toJson(Codec.unboundedMap(Registries.BLOCK.getCodec(), Codec.INT), result));
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.WITCH_WATER_WORLD_SERIALIZER;
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
