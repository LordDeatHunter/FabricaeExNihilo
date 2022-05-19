package wraith.fabricaeexnihilo.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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
    public static final ModRecipeType<ToolRecipe> HAMMER = new ModRecipeType<>(id("hammer"));
    public static final ModRecipeType<ToolRecipe> CROOK = new ModRecipeType<>(id("crook"));
    public static final ModRecipeType<SieveRecipe> SIEVE = new ModRecipeType<>(id("sieve"));
    
    public static final CompostRecipe.Serializer COMPOST_SERIALIZER = new CompostRecipe.Serializer();
    public static final FluidCombinationRecipe.Serializer FLUID_COMBINATION_SERIALIZER = new FluidCombinationRecipe.Serializer();
    public static final FluidTransformationRecipe.Serializer FLUID_TRANSFORMATION_SERIALIZER = new FluidTransformationRecipe.Serializer();
    public static final MilkingRecipe.Serializer MILKING_SERIALIZER = new MilkingRecipe.Serializer();
    public static final LeakingRecipe.Serializer LEAKING_SERIALIZER = new LeakingRecipe.Serializer();
    public static final AlchemyRecipe.Serializer ALCHEMY_SERIALIZER = new AlchemyRecipe.Serializer();
    public static final CrucibleRecipe.Serializer CRUCIBLE_SERIALIZER = new CrucibleRecipe.Serializer();
    public static final CrucibleHeatRecipe.Serializer CRUCIBLE_HEAT_SERIALIZER = new CrucibleHeatRecipe.Serializer();
    public static final WitchWaterEntityRecipe.Serializer WITCH_WATER_ENTITY_SERIALIZER = new WitchWaterEntityRecipe.Serializer();
    public static final WitchWaterWorldRecipe.Serializer WITCH_WATER_WORLD_SERIALIZER = new WitchWaterWorldRecipe.Serializer();
    public static final ToolRecipe.Serializer HAMMER_SERIALIZER = new ToolRecipe.Serializer();
    public static final ToolRecipe.Serializer CROOK_SERIALIZER = new ToolRecipe.Serializer();
    public static final SieveRecipe.Serializer SIEVE_SERIALIZER = new SieveRecipe.Serializer();
    
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
        register(WITCH_WATER_WORLD, WITCH_WATER_WORLD_SERIALIZER);
        register(HAMMER, HAMMER_SERIALIZER);
        register(CROOK, CROOK_SERIALIZER);
        register(SIEVE, SIEVE_SERIALIZER);
    }
    
    private static void register(ModRecipeType<?> type, RecipeSerializer<?> serializer) {
        Registry.register(Registry.RECIPE_TYPE, type.id, type);
        Registry.register(Registry.RECIPE_SERIALIZER, type.id, serializer);
    }
    
    public record ModRecipeType<T extends Recipe<?>>(Identifier id) implements RecipeType<T> {
        @Override
        public String toString() {
            return id.toString();
        }
    }
}
