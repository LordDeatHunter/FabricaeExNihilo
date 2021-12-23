package wraith.fabricaeexnihilo.modules;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.recipe.barrel.*;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ModRecipes {
    public static final ModRecipeType<CompostRecipe> COMPOST = new ModRecipeType<>(id("compost"));
    public static final ModRecipeType<FluidCombinationRecipe> FLUID_COMBINATION = new ModRecipeType<>(id("fluid_combination"));
    public static final ModRecipeType<FluidTransformationRecipe> FLUID_TRANSFORMATION = new ModRecipeType<>(id("fluid_transformation"));
    public static final ModRecipeType<MilkingRecipe> MILKING = new ModRecipeType<>(id("milking"));
    public static final ModRecipeType<LeakingRecipe> LEAKING = new ModRecipeType<>(id("leaking"));
    public static final ModRecipeType<AlchemyRecipe> ALCHEMY = new ModRecipeType<>(id("alchemy"));
    
    public static final RecipeSerializer<CompostRecipe> COMPOST_SERIALIZER = new CompostRecipe.Serializer();
    public static final RecipeSerializer<FluidCombinationRecipe> FLUID_COMBINATION_SERIALIZER = new FluidCombinationRecipe.Serializer();
    public static final RecipeSerializer<FluidTransformationRecipe> FLUID_TRANSFORMATION_SERIALIZER = new FluidTransformationRecipe.Serializer();
    public static final RecipeSerializer<MilkingRecipe> MILKING_SERIALIZER = new MilkingRecipe.Serializer();
    public static final RecipeSerializer<LeakingRecipe> LEAKING_SERIALIZER = new LeakingRecipe.Serializer();
    public static final RecipeSerializer<AlchemyRecipe> ALCHEMY_SERIALIZER = new AlchemyRecipe.Serializer();
    
    public static void register() {
        register(COMPOST, COMPOST_SERIALIZER);
        register(FLUID_COMBINATION, FLUID_COMBINATION_SERIALIZER);
        register(FLUID_TRANSFORMATION, FLUID_TRANSFORMATION_SERIALIZER);
        register(MILKING, MILKING_SERIALIZER);
        register(LEAKING, LEAKING_SERIALIZER);
        register(ALCHEMY, ALCHEMY_SERIALIZER);
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
}
