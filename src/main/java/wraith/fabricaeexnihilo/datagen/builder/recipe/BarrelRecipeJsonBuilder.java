package wraith.fabricaeexnihilo.datagen.builder.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeAction;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeCondition;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeTrigger;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class BarrelRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final BarrelRecipeTrigger trigger;
    private final List<BarrelRecipeCondition> conditions = new ArrayList<>();
    private final List<BarrelRecipeAction> actions = new ArrayList<>();
    private boolean hasStateCondition = false;
    private int duration = -1;
    private Item icon = null;

    private BarrelRecipeJsonBuilder(BarrelRecipeTrigger trigger) {
        this.trigger = trigger;
    }

    public static BarrelRecipeJsonBuilder tickTriggered(float chance) {
        return new BarrelRecipeJsonBuilder(new BarrelRecipeTrigger.Tick(chance));
    }

    public static BarrelRecipeJsonBuilder tickTriggered() {
        return tickTriggered(1);
    }

    public static BarrelRecipeJsonBuilder itemTriggered(Ingredient ingredient) {
        return new BarrelRecipeJsonBuilder(new BarrelRecipeTrigger.ItemInserted(ingredient));
    }

    public static BarrelRecipeJsonBuilder itemTriggered(ItemConvertible... items) {
        return itemTriggered(Ingredient.ofItems(items));
    }

    public static BarrelRecipeJsonBuilder itemTriggered(TagKey<Item> items) {
        return itemTriggered(Ingredient.fromTag(items));
    }

    public BarrelRecipeJsonBuilder fluid(FluidIngredient fluid) {
        if (hasStateCondition) throw new IllegalStateException("Multiple state conditions are not a good idea");
        conditions.add(new BarrelRecipeCondition.FluidIn(fluid));
        hasStateCondition = true;
        return this;
    }

    public BarrelRecipeJsonBuilder fluid(Fluid fluid) {
        fluid(FluidIngredient.single(fluid));
        return this;
    }

    public BarrelRecipeJsonBuilder fluid(TagKey<Fluid> tag) {
        fluid(FluidIngredient.tag(tag));
        return this;
    }

    public BarrelRecipeJsonBuilder above(BlockIngredient block) {
        conditions.add(new BarrelRecipeCondition.BlockAbove(block));
        return this;
    }

    public BarrelRecipeJsonBuilder above(Block block) {
        return above(BlockIngredient.single(block));
    }

    public BarrelRecipeJsonBuilder above(FluidIngredient fluid) {
        conditions.add(new BarrelRecipeCondition.FluidAbove(fluid));
        return this;
    }

    public BarrelRecipeJsonBuilder above(Fluid fluid) {
        return above(FluidIngredient.single(fluid));
    }

    public BarrelRecipeJsonBuilder above(TagKey<Fluid> tag) {
        return above(FluidIngredient.tag(tag));
    }

    public BarrelRecipeJsonBuilder below(BlockIngredient block) {
        conditions.add(new BarrelRecipeCondition.BlockBelow(block));
        return this;
    }

    public BarrelRecipeJsonBuilder below(Block block) {
        return below(BlockIngredient.single(block));
    }

    public BarrelRecipeJsonBuilder duration(int barrelTicks) {
        this.duration = barrelTicks;
        return this;
    }

    public BarrelRecipeJsonBuilder instant() {
        this.duration = 0;
        return this;
    }

    public BarrelRecipeJsonBuilder spawnEntity(EntityStack stack) {
        this.actions.add(new BarrelRecipeAction.SpawnEntity(stack));
        return this;
    }

    public BarrelRecipeJsonBuilder spawnEntity(EntityType<?> type) {
        return spawnEntity(new EntityStack(type, 1));
    }

    public BarrelRecipeJsonBuilder spawnEntity(EntityType<?> type, NbtCompound nbt) {
        return spawnEntity(new EntityStack(type, 1, nbt));
    }

    public BarrelRecipeJsonBuilder storeItem(ItemStack stack) {
        if (icon == null) icon(stack.getItem());
        this.actions.add(new BarrelRecipeAction.StoreItem(stack));
        return this;
    }

    public BarrelRecipeJsonBuilder storeItem(ItemConvertible item) {
        return storeItem(item.asItem().getDefaultStack());
    }

    public BarrelRecipeJsonBuilder dropItem(ItemStack stack) {
        this.actions.add(new BarrelRecipeAction.DropItem(stack));
        return this;
    }

    public BarrelRecipeJsonBuilder dropItem(ItemConvertible item) {
        return dropItem(item.asItem().getDefaultStack());
    }

    public BarrelRecipeJsonBuilder storeFluid(FluidVariant fluid, long amount) {
        this.actions.add(new BarrelRecipeAction.StoreFluid(fluid, amount));
        return this;
    }

    public BarrelRecipeJsonBuilder storeFluid(Fluid fluid, long amount) {
        return storeFluid(FluidVariant.of(fluid), amount);
    }

    public BarrelRecipeJsonBuilder storeFluid(Fluid fluid) {
        return storeFluid(fluid, FluidConstants.BUCKET);
    }

    public BarrelRecipeJsonBuilder consumeFluid(FluidIngredient fluid, long amount) {
        if (hasStateCondition) throw new IllegalStateException("Multiple state conditions are not a good idea");
        this.actions.add(new BarrelRecipeAction.ConsumeFluid(fluid, amount));
        hasStateCondition = true;
        return this;
    }

    public BarrelRecipeJsonBuilder consumeFluid(TagKey<Fluid> tag, long amount) {
        return consumeFluid(FluidIngredient.tag(tag), amount);
    }

    public BarrelRecipeJsonBuilder consumeFluid(Fluid fluid, long amount) {
        return consumeFluid(FluidIngredient.single(fluid), amount);
    }

    public BarrelRecipeJsonBuilder convertBlock(BlockIngredient filter, BlockState result) {
        if (icon == null) icon(result.getBlock());
        this.actions.add(new BarrelRecipeAction.ConvertBlock(filter, result));
        return this;
    }

    public BarrelRecipeJsonBuilder convertBlock(Block from, Block to) {
        return convertBlock(BlockIngredient.single(from), to.getDefaultState());
    }

    public BarrelRecipeJsonBuilder fillCompost(ItemStack item, float amount) {
        if (hasStateCondition) throw new IllegalStateException("Multiple state conditions are not a good idea");
        icon(item.getItem());
        this.actions.add(new BarrelRecipeAction.FillCompost(item, amount));
        hasStateCondition = true;
        return this;
    }

    public BarrelRecipeJsonBuilder fillCompost(Item item, float amount) {
        return fillCompost(new ItemStack(item), amount);
    }

    public BarrelRecipeJsonBuilder icon(ItemConvertible icon) {
        this.icon = icon.asItem();
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
        if (duration == -1) throw new IllegalStateException("Duration not set");
        if (actions.size() == 0) throw new IllegalStateException("No actions set");
        if (icon == null) throw new IllegalStateException("No icon set");
        if (!hasStateCondition) throw new IllegalStateException("No state condition set, this recipe could trigger on all kinds of barrels");

        exporter.accept(new Provider(duration, icon, trigger, actions, conditions, recipeId));
    }

    private record Provider(int duration,
                            Item icon,
                            BarrelRecipeTrigger trigger,
                            List<BarrelRecipeAction> actions,
                            List<BarrelRecipeCondition> conditions,
                            Identifier id) implements RecipeJsonProvider {

        @Override
        public void serialize(JsonObject json) {
            json.add("trigger", trigger.toJson());

            var actions = new JsonArray();
            this.actions.forEach(action -> actions.add(action.toJson()));
            json.add("actions", actions);

            if (!conditions.isEmpty()) {
                var conditions = new JsonArray();
                this.conditions.forEach(action -> conditions.add(action.toJson()));
                json.add("conditions", conditions);
            }

            if (duration != 0) json.addProperty("duration", duration);
            json.addProperty("icon", Registries.ITEM.getId(icon).toString());
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.BARREL_SERIALIZER;
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