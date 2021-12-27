package wraith.fabricaeexnihilo.modules;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.recipe.DummyRecipe;
import wraith.fabricaeexnihilo.recipe.barrel.*;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ModRecipes {
    public static final ModRecipeType<CompostRecipe> COMPOST = new ModRecipeType<>(id("compost"));
    public static final ModRecipeType<FluidCombinationRecipe> FLUID_COMBINATION = new ModRecipeType<>(id("fluid_combination"));
    public static final ModRecipeType<FluidTransformationRecipe> FLUID_TRANSFORMATION = new ModRecipeType<>(id("fluid_transformation"));
    public static final ModRecipeType<MilkingRecipe> MILKING = new ModRecipeType<>(id("milking"));
    public static final ModRecipeType<LeakingRecipe> LEAKING = new ModRecipeType<>(id("leaking"));
    public static final ModRecipeType<AlchemyRecipe> ALCHEMY = new ModRecipeType<>(id("alchemy"));
    public static final ModRecipeType<CrucibleRecipe> CRUCIBLE = new ModRecipeType<>(id("crucible"));
    public static final ModRecipeType<CrucibleHeatRecipe> CRUCIBLE_HEAT = new ModRecipeType<>(id("crucible_heat"));
    public static final ModRecipeType<WitchWaterEntityRecipe> WITCH_WATER_ENTITY = new ModRecipeType<>(id("witch_water_entity"));
    public static final ModRecipeType<WitchWaterWorldRecipe> WITCH_WATER_WORLD = new ModRecipeType<>(id("witch_water_world"));
    
    public static final RecipeSerializer<?> COMPOST_SERIALIZER = ProxySerializer.of(new CompostRecipe.Serializer());
    public static final RecipeSerializer<?> FLUID_COMBINATION_SERIALIZER = ProxySerializer.of(new FluidCombinationRecipe.Serializer());
    public static final RecipeSerializer<?> FLUID_TRANSFORMATION_SERIALIZER = ProxySerializer.of(new FluidTransformationRecipe.Serializer());
    public static final RecipeSerializer<?> MILKING_SERIALIZER = ProxySerializer.of(new MilkingRecipe.Serializer());
    public static final RecipeSerializer<?> LEAKING_SERIALIZER = ProxySerializer.of(new LeakingRecipe.Serializer());
    public static final RecipeSerializer<?> ALCHEMY_SERIALIZER = ProxySerializer.of(new AlchemyRecipe.Serializer());
    public static final RecipeSerializer<?> CRUCIBLE_SERIALIZER = ProxySerializer.of(new CrucibleRecipe.Serializer());
    public static final RecipeSerializer<?> CRUCIBLE_HEAT_SERIALIZER = ProxySerializer.of(new CrucibleHeatRecipe.Serializer());
    public static final RecipeSerializer<?> WITCH_WATER_ENTITY_SERIALIZER = ProxySerializer.of(new WitchWaterEntityRecipe.Serializer());
    public static final RecipeSerializer<?> WITCH_WATER_WORLD_SERIALIZER = ProxySerializer.of(new WitchWaterWorldRecipe.Serializer());
    
    public static void register() {
        register(COMPOST, COMPOST_SERIALIZER);
        register(FLUID_COMBINATION, FLUID_COMBINATION_SERIALIZER);
        register(FLUID_TRANSFORMATION, FLUID_TRANSFORMATION_SERIALIZER);
        register(MILKING, MILKING_SERIALIZER);
        register(LEAKING, LEAKING_SERIALIZER);
        register(ALCHEMY, ALCHEMY_SERIALIZER);
        register(CRUCIBLE, CRUCIBLE_SERIALIZER);
        register(CRUCIBLE_HEAT, CRUCIBLE_HEAT_SERIALIZER);
        register(WITCH_WATER_ENTITY, WITCH_WATER_ENTITY_SERIALIZER);
    }
    
    private static void register(ModRecipeType<?> type, RecipeSerializer<?> serializer) {
        Registry.register(Registry.RECIPE_TYPE, type.id, type);
        Registry.register(Registry.RECIPE_SERIALIZER, type.id, serializer);
    }
    
    private static record ModRecipeType<T extends Recipe<?>>(Identifier id) implements RecipeType<T> {
        @Override
        public String toString() {
            return id.toString();
        }
    }
    
    /**
     * Allows us to skip recipes we don't want to use. Specifically exclude mod specific recipes included by default.
     * Works by delegating the given serializer if conditions match. Otherwise, it produces a dummy recipe that (should) do nothing.
     * @apiNote Should be replaced by resource conditions once they are added to fapi.
     */
    // TODO: Switch to recipe conditions from fapi once they are added and delete this mess
    @SuppressWarnings("ClassCanBeRecord") // No!
    private static class ProxySerializer implements RecipeSerializer<Recipe<?>> {
        private final RecipeSerializer<?> delegate;
    
        private ProxySerializer(RecipeSerializer<?> delegate) {
            this.delegate = delegate;
        }
    
        public static ProxySerializer of(RecipeSerializer<?> delegate) {
            return new ProxySerializer(delegate);
        }
        
        @Override
        public Recipe<?> read(Identifier id, JsonObject json) {
            if (json.has("requiredMod") && !FabricLoader.getInstance().isModLoaded(JsonHelper.getString(json, "requiredMod"))) {
                return DummyRecipe.SERIALIZER.read(id, json);
            }
            return delegate.read(id, json);
        }
    
        @Override
        public Recipe<?> read(Identifier id, PacketByteBuf buf) {
            return delegate.read(id, buf);
        }
    
        @Override
        public void write(PacketByteBuf buf, Recipe<?> recipe) {
            //Very concern, but should work... Unless someone misuses this
            delegate.write(buf, hack(recipe));
        }
        
        @SuppressWarnings("unchecked") // Hack
        private <T extends Recipe<?>> T hack(Recipe<?> recipe) {
            return (T) recipe;
        }
    }
}
