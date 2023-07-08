package wraith.fabricaeexnihilo.datagen.builder.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.*;
import java.util.function.Consumer;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class SieveRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private static final Codec<Map<Item, double[]>> ROLLS_CODEC = Codec.unboundedMap(Registries.ITEM.getCodec(), Codec.DOUBLE.listOf()
            .xmap(doubles -> ArrayUtils.toPrimitive(doubles.toArray(new Double[0])), array -> List.of(ArrayUtils.toObject(array))));
    private final ItemStack result;
    private final List<Segment> segments = new ArrayList<>();
    private Segment currentSegment;

    private SieveRecipeJsonBuilder(ItemStack result) {
        this.result = result;
    }

    public static SieveRecipeJsonBuilder of(ItemStack result) {
        return new SieveRecipeJsonBuilder(result);
    }

    public static SieveRecipeJsonBuilder of(ItemConvertible result) {
        return new SieveRecipeJsonBuilder(new ItemStack(result));
    }

    public SieveRecipeJsonBuilder from(Ingredient input, String name) {
        if (currentSegment != null) segments.add(currentSegment);
        currentSegment = new Segment(input, false, name, new HashMap<>());
        return this;
    }

    public SieveRecipeJsonBuilder from(ItemConvertible input) {
        return from(Ingredient.ofItems(input), Registries.ITEM.getId(input.asItem()).getPath());
    }

    public SieveRecipeJsonBuilder fromWaterlogged(Ingredient input, String name) {
        if (currentSegment != null) segments.add(currentSegment);
        currentSegment = new Segment(input, true, name, new HashMap<>());
        return this;
    }

    public SieveRecipeJsonBuilder fromWaterlogged(ItemConvertible input) {
        return fromWaterlogged(Ingredient.ofItems(input), Registries.ITEM.getId(input.asItem()).getPath());
    }

    public SieveRecipeJsonBuilder mesh(MeshItem mesh, double... chances) {
        if (currentSegment == null) throw new IllegalStateException("No active segment");
        currentSegment.rolls.put(mesh, chances);
        return this;
    }

    public SieveRecipeJsonBuilder meshes(Map<MeshItem, double[]> meshes) {
        meshes.forEach(this::mesh);
        return this;
    }

    public SieveRecipeJsonBuilder stringMesh(double... chances) {
        return mesh(ModItems.MESHES.get(id("string_mesh")), chances);
    }

    public SieveRecipeJsonBuilder flintMesh(double... chances) {
        return mesh(ModItems.MESHES.get(id("flint_mesh")), chances);
    }

    public SieveRecipeJsonBuilder ironMesh(double... chances) {
        return mesh(ModItems.MESHES.get(id("iron_mesh")), chances);
    }

    public SieveRecipeJsonBuilder diamondMesh(double... chances) {
        return mesh(ModItems.MESHES.get(id("diamond_mesh")), chances);
    }

    public SieveRecipeJsonBuilder netheriteMesh(double... chances) {
        return mesh(ModItems.MESHES.get(id("netherite_mesh")), chances);
    }

    public SieveRecipeJsonBuilder copperMesh(double... chances) {
        return mesh(ModItems.MESHES.get(id("copper_mesh")), chances);
    }

    public SieveRecipeJsonBuilder goldMesh(double... chances) {
        return mesh(ModItems.MESHES.get(id("gold_mesh")), chances);
    }

    public SieveRecipeJsonBuilder emeraldMesh(double... chances) {
        return mesh(ModItems.MESHES.get(id("emerald_mesh")), chances);
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
        if (currentSegment == null) throw new IllegalStateException("No segments");
        segments.add(currentSegment);
        for (var segment : segments) {
            exporter.accept(new Provider(result, segment, recipeId, segments.size() > 1));
        }
    }

    private record Segment(Ingredient input, boolean waterlogged, String name, Map<Item, double[]> rolls) {
    }

    private record Provider(ItemStack result, Segment segment, Identifier id, boolean useLongId) implements RecipeJsonProvider {
        @Override
        public void serialize(JsonObject json) {
            json.add("result", CodecUtils.toJson(CodecUtils.ITEM_STACK, result));
            json.add("input", segment.input.toJson());
            json.addProperty("waterlogged", segment.waterlogged);
            json.add("rolls", CodecUtils.toJson(ROLLS_CODEC, segment.rolls));
        }

        @Override
        public Identifier getRecipeId() {
            return useLongId ? id.withSuffixedPath("_from_" + segment.name) : id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.SIEVE_SERIALIZER;
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
