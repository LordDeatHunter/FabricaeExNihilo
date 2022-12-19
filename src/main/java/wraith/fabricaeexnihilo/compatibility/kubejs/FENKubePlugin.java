package wraith.fabricaeexnihilo.compatibility.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeTypesEvent;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.SieveRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.ToolRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel.*;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.crucible.CrucibleHeatRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.crucible.CrucibleRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater.WitchWaterEntityRecipeJS;
import wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater.WitchWaterWorldRecipeJS;
import wraith.fabricaeexnihilo.recipe.ModRecipes;

import java.util.Optional;

public class FENKubePlugin extends KubeJSPlugin {

    public static <T> RegistryEntryList<T> getEntryList(RecipeArguments args, int index, DefaultedRegistry<T> registry) {
        var list = ListJS.of(args.get(index));
        var string = args.get(index).toString();
        if (list == null) {
            return string.startsWith("#") ? registry.getOrCreateEntryList(TagKey.of(registry.getKey(), new Identifier(string.substring(1))))
                : RegistryEntryList.of(registry.getEntry(RegistryKey.of(registry.getKey(), new Identifier(string))).orElseThrow());
        } else {
            return RegistryEntryList.of(list.stream().map(Object::toString).map(Identifier::new).map(id -> RegistryKey.of(registry.getKey(), id)).map(registry::getEntry).map(Optional::orElseThrow).toList());
        }
    }

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
