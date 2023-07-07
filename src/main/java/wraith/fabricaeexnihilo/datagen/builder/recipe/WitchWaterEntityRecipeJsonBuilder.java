package wraith.fabricaeexnihilo.datagen.builder.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.ModRecipes;

import java.util.function.Consumer;

public class WitchWaterEntityRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final EntityType<?> target;
    private final String nbt;
    private final EntityType<?> result;

    public WitchWaterEntityRecipeJsonBuilder(EntityType<?> target, String nbt, EntityType<?> result) {
        this.target = target;
        this.nbt = nbt;
        this.result = result;
    }

    public static WitchWaterEntityRecipeJsonBuilder of(EntityType<?> from, EntityType<?> to) {
        return new WitchWaterEntityRecipeJsonBuilder(from, null, to);
    }

    public static WitchWaterEntityRecipeJsonBuilder villager(VillagerProfession from, EntityType<?> to) {
        return new WitchWaterEntityRecipeJsonBuilder(EntityType.VILLAGER, "VillagerData{profession:\"%s\"}".formatted(Registries.VILLAGER_PROFESSION.getId(from).toString()), to);
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
        exporter.accept(new Provider(target, nbt, result, recipeId));
    }

    private record Provider(EntityType<?> target, String nbt, EntityType<?> result, Identifier id) implements RecipeJsonProvider {
        @Override
        public void serialize(JsonObject json) {
            json.addProperty("target", Registries.ENTITY_TYPE.getId(target).toString());
            json.addProperty("result", Registries.ENTITY_TYPE.getId(result).toString());
            if (nbt != null) json.addProperty("nbt", nbt);
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.WITCH_WATER_ENTITY_SERIALIZER;
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
