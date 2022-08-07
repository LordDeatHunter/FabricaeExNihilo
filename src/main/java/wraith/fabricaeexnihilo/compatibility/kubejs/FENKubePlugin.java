package wraith.fabricaeexnihilo.compatibility.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeTypesEvent;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.SieveRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.ToolRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel.*;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.crucible.CrucibleHeatRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.crucible.CrucibleRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater.WitchWaterEntityRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater.WitchWaterWorldRecipeJS;
import wraith.fabricaeexnihilo.recipe.ModRecipes;

public class FENKubePlugin extends KubeJSPlugin {

    @Override
    public void registerRecipeTypes(RegisterRecipeTypesEvent event) {
        event.register(ModRecipes.SIEVE.id(), SieveRecipeJS::new);
        event.register(ModRecipes.CROOK.id(), ToolRecipeJS::new);
        event.register(ModRecipes.HAMMER.id(), ToolRecipeJS::new);

        event.register(ModRecipes.CRUCIBLE.id(), CrucibleRecipeJS::new);
        event.register(ModRecipes.CRUCIBLE_HEAT.id(), CrucibleHeatRecipeJS::new);

        event.register(ModRecipes.ALCHEMY.id(), AlchemyRecipeJS::new);
        event.register(ModRecipes.COMPOST.id(), CompostRecipeJS::new);
        event.register(ModRecipes.FLUID_COMBINATION.id(), FluidCombinationRecipeJS::new);
        event.register(ModRecipes.FLUID_TRANSFORMATION.id(), FluidTransformationRecipeJS::new);
        event.register(ModRecipes.LEAKING.id(), LeakingRecipeJS::new);
        event.register(ModRecipes.MILKING.id(), MilkingRecipeJS::new);

        event.register(ModRecipes.WITCH_WATER_ENTITY.id(), WitchWaterEntityRecipeJS::new);
        event.register(ModRecipes.WITCH_WATER_WORLD.id(), WitchWaterWorldRecipeJS::new);
    }
}
