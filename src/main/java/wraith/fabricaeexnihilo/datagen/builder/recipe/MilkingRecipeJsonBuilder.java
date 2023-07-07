package wraith.fabricaeexnihilo.datagen.builder.recipe;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class MilkingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final EntityType<?> entity;
    private final FluidVariant fluid;
    private long amount = 810;
    private int cooldown = 20;

    public MilkingRecipeJsonBuilder(EntityType<?> entity, FluidVariant fluid) {
        this.entity = entity;
        this.fluid = fluid;
    }

    public MilkingRecipeJsonBuilder amount(long amount) {
        this.amount = amount;
        return this;
    }

    public MilkingRecipeJsonBuilder cooldown(int cooldown) {
        this.cooldown = cooldown;
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
        exporter.accept(new Provider(entity, fluid, amount, cooldown, recipeId));
    }

    private record Provider(EntityType<?> entity,
                            FluidVariant fluid,
                            long amount,
                            int cooldown,
                            Identifier id) implements RecipeJsonProvider {

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("entity", Registries.ENTITY_TYPE.getId(entity).toString());
            json.add("fluid", CodecUtils.toJson(CodecUtils.FLUID_VARIANT, fluid));
            json.addProperty("amount", amount);
            json.addProperty("cooldown", cooldown);
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.MILKING_SERIALIZER;
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
