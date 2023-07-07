package wraith.fabricaeexnihilo.datagen.builder.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.function.Consumer;

public class ToolRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final ToolRecipe.ToolType tool;
    private final BlockIngredient block;
    private final Loot result;

    private ToolRecipeJsonBuilder(ToolRecipe.ToolType tool, BlockIngredient block, Loot result) {
        this.tool = tool;
        this.block = block;
        this.result = result;
    }

    public static ToolRecipeJsonBuilder hammering(Block block, ItemConvertible result, double... chances) {
        return new ToolRecipeJsonBuilder(ToolRecipe.ToolType.HAMMER, BlockIngredient.single(block), new Loot(new ItemStack(result), chances));
    }

    public static ToolRecipeJsonBuilder hammering(TagKey<Block> block, ItemConvertible result, double... chances) {
        return new ToolRecipeJsonBuilder(ToolRecipe.ToolType.HAMMER, BlockIngredient.tag(block), new Loot(new ItemStack(result), chances));
    }

    public static ToolRecipeJsonBuilder crooking(Block block, ItemConvertible result, double... chances) {
        return new ToolRecipeJsonBuilder(ToolRecipe.ToolType.CROOK, BlockIngredient.single(block), new Loot(new ItemStack(result), chances));
    }

    public static ToolRecipeJsonBuilder crooking(TagKey<Block> block, ItemConvertible result, double... chances) {
        return new ToolRecipeJsonBuilder(ToolRecipe.ToolType.CROOK, BlockIngredient.tag(block), new Loot(new ItemStack(result), chances));
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
        exporter.accept(new Provider(tool, block, result, recipeId));
    }

    private record Provider(ToolRecipe.ToolType tool,
                            BlockIngredient block,
                            Loot result,
                            Identifier id) implements RecipeJsonProvider {

        @Override
        public void serialize(JsonObject json) {
            json.add("block", block.toJson());
            json.add("result", CodecUtils.toJson(Loot.CODEC, result));
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return tool.serializer;
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
