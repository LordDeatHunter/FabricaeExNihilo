package wraith.fabricaeexnihilo.compatibility.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Material;
import net.minecraft.fluid.Fluids;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.*;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleCategory;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleHeatCategory;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleHeatDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.sieve.SieveCategory;
import wraith.fabricaeexnihilo.compatibility.rei.sieve.SieveDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.tools.ToolCategory;
import wraith.fabricaeexnihilo.compatibility.rei.tools.ToolDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterEntityDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterWorldDisplay;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModTools;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.Objects;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class PluginEntry implements REIClientPlugin {

    public static final CategoryIdentifier<SieveDisplay> SIFTING = CategoryIdentifier.of(id("rei/sieve"));
    public static final CategoryIdentifier<AlchemyDisplay> ALCHEMY = CategoryIdentifier.of(id("rei/barrel/alchemy"));
    public static final CategoryIdentifier<CompostDisplay> COMPOSTING = CategoryIdentifier.of(id("rei/barrel/composting"));
    public static final CategoryIdentifier<LeakingDisplay> LEAKING = CategoryIdentifier.of(id("rei/barrel/leaking"));
    public static final CategoryIdentifier<MilkingDisplay> MILKING = CategoryIdentifier.of(id("rei/barrel/milking"));
    public static final CategoryIdentifier<TransformingDisplay> TRANSFORMING = CategoryIdentifier.of(id("rei/barrel/transforming"));
    public static final CategoryIdentifier<FluidOnTopDisplay> ON_TOP = CategoryIdentifier.of(id("rei/barrel/fluid_on_top"));
    public static final CategoryIdentifier<CrucibleDisplay> WOOD_CRUCIBLE = CategoryIdentifier.of(id("rei/crucible/wood"));
    public static final CategoryIdentifier<CrucibleDisplay> PORCELAIN_CRUCIBLE = CategoryIdentifier.of(id("rei/crucible/stone"));
    public static final CategoryIdentifier<CrucibleHeatDisplay> HEATING = CategoryIdentifier.of(id("rei/crucible/heat"));
    public static final CategoryIdentifier<WitchWaterWorldDisplay> WITCH_WATER_WORLD = CategoryIdentifier.of(id("rei/witchwater/world"));
    public static final CategoryIdentifier<WitchWaterEntityDisplay> WITCH_WATER_ENTITY = CategoryIdentifier.of(id("rei/witchwater/entity"));
    public static final CategoryIdentifier<ToolDisplay> CROOK = CategoryIdentifier.of(id("rei/tools/crook"));
    public static final CategoryIdentifier<ToolDisplay> CRUSHING = CategoryIdentifier.of(id("rei/tools/hammer"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        FabricaeExNihilo.LOGGER.info("Registering REI Categories");
        registry.add(new ToolCategory(CRUSHING, ItemUtils.getExNihiloItemStack("iron_hammer"), "fabricaeexnihilo.rei.category.hammer"));
        registry.add(new CrucibleCategory(WOOD_CRUCIBLE, ItemUtils.getExNihiloItemStack("oak_crucible"), "fabricaeexnihilo.rei.category.wood_crucible"));
        registry.add(new CrucibleCategory(PORCELAIN_CRUCIBLE, ItemUtils.getExNihiloItemStack("porcelain_crucible"), "fabricaeexnihilo.rei.category.porcelain_crucible"));
        registry.add(new CrucibleHeatCategory(ItemUtils.getExNihiloItemStack("porcelain_crucible"), "fabricaeexnihilo.rei.category.crucible_heat"));
        registry.add(new SieveCategory(ItemUtils.getExNihiloItemStack("oak_sieve"), "fabricaeexnihilo.rei.category.sieve"));
        ModTools.HAMMERS.values().forEach(hammer -> registry.addWorkstations(CRUSHING, EntryStacks.of(hammer)));
        ModBlocks.CRUCIBLES.values().forEach(crucible -> {
            registry.addWorkstations(HEATING, EntryStacks.of(crucible));
            // TODO: Replace materials with something else
            if (crucible.getMaterial() == Material.WOOD) {
                registry.addWorkstations(WOOD_CRUCIBLE, EntryStacks.of(crucible));
            }
            registry.addWorkstations(PORCELAIN_CRUCIBLE, EntryStacks.of(crucible));
        });
        ModBlocks.SIEVES.values().forEach(sieve -> registry.addWorkstations(SIFTING, EntryStacks.of(sieve)));
//        registry.add(new SieveCategory());
//
//        registry.add(new ToolCategory(CROOK, ItemUtils.getExNihiloItemStack("wooden_crook"), "Crook"));
//
//        registry.add(new CompostCategory());
//        registry.add(new LeakingCategory());
//        registry.add(new FluidOnTopCategory());
//        registry.add(new MilkingCategory());
//        registry.add(new TransformingCategory());
//        registry.add(new AlchemyCategory());
//
//        registry.add(new WitchWaterEntityCategory());
//        registry.add(new WitchWaterWorldCategory());
//
//        // Hackishly Remove the autocrafting button
//        registry.removePlusButton(SIEVE);
//        registry.removePlusButton(CROOK);
//        registry.removePlusButton(HAMMER);
//        registry.removePlusButton(CRUCIBLE_HEAT);
//        registry.removePlusButton(WOOD_CRUCIBLE);
//        registry.removePlusButton(PORCELAIN_CRUCIBLE);
//        registry.removePlusButton(COMPOSTING);
//        registry.removePlusButton(LEAKING);
//        registry.removePlusButton(ON_TOP);
//        registry.removePlusButton(MILKING);
//        registry.removePlusButton(TRANSFORMING);
//        registry.removePlusButton(WITCH_WATER_ENTITY);
//        registry.removePlusButton(WITCH_WATER_WORLD);
//        registry.removePlusButton(ALCHEMY);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        FabricaeExNihilo.LOGGER.info("Registering REI Displays");
        registry.registerRecipeFiller(ToolRecipe.class, ModRecipes.HAMMER, recipe -> new ToolDisplay(recipe, CRUSHING));
        // TODO: Add better check for hot fluids
        registry.registerRecipeFiller(CrucibleRecipe.class, type -> Objects.equals(ModRecipes.CRUCIBLE, type), recipe -> !recipe.getFluid().isOf(Fluids.LAVA), recipe -> new CrucibleDisplay(recipe, WOOD_CRUCIBLE));
        registry.registerRecipeFiller(CrucibleRecipe.class, ModRecipes.CRUCIBLE, recipe -> new CrucibleDisplay(recipe, PORCELAIN_CRUCIBLE));
        registry.registerRecipeFiller(CrucibleHeatRecipe.class, ModRecipes.CRUCIBLE_HEAT, CrucibleHeatDisplay::new);
        // TODO: Actually implement this properly
        registry.registerRecipeFiller(SieveRecipe.class, ModRecipes.SIEVE, SieveDisplay::new);
        //FabricaeExNihiloRegistries.CROOK.getREIRecipes().forEach(recipe -> registry.add(new ToolDisplay(recipe, CROOK)));
        //FabricaeExNihiloRegistries.BARREL_ALCHEMY.getREIRecipes().forEach(recipe -> registry.add(new AlchemyDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_COMPOST.getREIRecipes().forEach(recipe -> registry.add(new CompostDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_LEAKING.getREIRecipes().forEach(recipe -> registry.add(new LeakingDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_ON_TOP.getREIRecipes().forEach(recipe -> registry.add(new FluidOnTopDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_MILKING.getREIRecipes().forEach(recipe -> registry.add(new MilkingDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_TRANSFORM.getREIRecipes().forEach(recipe -> registry.add(new TransformingDisplay(recipe)));
        //FabricaeExNihiloRegistries.WITCHWATER_ENTITY.getREIRecipes().forEach(recipe -> registry.add(new WitchWaterEntityDisplay(recipe)));
        //FabricaeExNihiloRegistries.WITCHWATER_WORLD.getREIRecipes().forEach(recipe -> registry.add(new WitchWaterWorldDisplay(recipe)));
    }

}
