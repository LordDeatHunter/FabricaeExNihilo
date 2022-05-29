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
import wraith.fabricaeexnihilo.recipe.barrel.*;
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
    public static final CategoryIdentifier<FluidOnTopDisplay> FLUID_ABOVE = CategoryIdentifier.of(id("rei/barrel/fluid_on_top"));
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
        registry.add(new ToolCategory(CROOK, ItemUtils.getExNihiloItemStack("wooden_crook"), "fabricaeexnihilo.rei.category.crook"));
        registry.add(new CrucibleCategory(WOOD_CRUCIBLE, ItemUtils.getExNihiloItemStack("oak_crucible"), "fabricaeexnihilo.rei.category.wood_crucible"));
        registry.add(new CrucibleCategory(PORCELAIN_CRUCIBLE, ItemUtils.getExNihiloItemStack("porcelain_crucible"), "fabricaeexnihilo.rei.category.porcelain_crucible"));
        registry.add(new CrucibleHeatCategory(ItemUtils.getExNihiloItemStack("porcelain_crucible"), "fabricaeexnihilo.rei.category.crucible_heat"));
        registry.add(new SieveCategory(ItemUtils.getExNihiloItemStack("oak_sieve"), "fabricaeexnihilo.rei.category.sieve"));
        registry.add(new AlchemyCategory(ItemUtils.getExNihiloItemStack("oak_barrel"), "fabricaeexnihilo.rei.category.barrel.alchemy"));
        registry.add(new TransformingCategory(ItemUtils.getExNihiloItemStack("oak_barrel"), "fabricaeexnihilo.rei.category.barrel.transforming"));
        registry.add(new CompostCategory(ItemUtils.getExNihiloItemStack("oak_barrel"), "fabricaeexnihilo.rei.category.barrel.composting"));
        registry.add(new LeakingCategory(ItemUtils.getExNihiloItemStack("oak_barrel"), "fabricaeexnihilo.rei.category.barrel.leaking"));
        registry.add(new MilkingCategory(ItemUtils.getExNihiloItemStack("oak_barrel"), "fabricaeexnihilo.rei.category.barrel.milking"));
        registry.add(new FluidOnTopCategory(ItemUtils.getExNihiloItemStack("oak_barrel"), "fabricaeexnihilo.rei.category.barrel.fluid_on_top"));
        ModTools.HAMMERS.values().forEach(hammer -> registry.addWorkstations(CRUSHING, EntryStacks.of(hammer)));
        ModTools.CROOKS.values().forEach(crook -> registry.addWorkstations(CROOK, EntryStacks.of(crook)));
        ModBlocks.CRUCIBLES.values().forEach(crucible -> {
            registry.addWorkstations(HEATING, EntryStacks.of(crucible));
            // TODO: Replace materials with something else
            if (crucible.getMaterial() == Material.WOOD) {
                registry.addWorkstations(WOOD_CRUCIBLE, EntryStacks.of(crucible));
            }
            registry.addWorkstations(PORCELAIN_CRUCIBLE, EntryStacks.of(crucible));
        });
        ModBlocks.SIEVES.values().forEach(sieve -> registry.addWorkstations(SIFTING, EntryStacks.of(sieve)));
        ModBlocks.BARRELS.values().forEach(barrel -> {
            registry.addWorkstations(ALCHEMY, EntryStacks.of(barrel));
            registry.addWorkstations(TRANSFORMING, EntryStacks.of(barrel));
            registry.addWorkstations(COMPOSTING, EntryStacks.of(barrel));
            if (barrel.getMaterial() != Material.STONE) {
                registry.addWorkstations(LEAKING, EntryStacks.of(barrel));
            }
            registry.addWorkstations(MILKING, EntryStacks.of(barrel));
            registry.addWorkstations(FLUID_ABOVE, EntryStacks.of(barrel));
        });
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
        registry.registerRecipeFiller(ToolRecipe.class, ModRecipes.CROOK, recipe -> new ToolDisplay(recipe, CROOK));
        // TODO: Add better check for hot fluids
        registry.registerRecipeFiller(CrucibleRecipe.class, type -> Objects.equals(ModRecipes.CRUCIBLE, type), recipe -> !recipe.getFluid().isOf(Fluids.LAVA), recipe -> new CrucibleDisplay(recipe, WOOD_CRUCIBLE));
        registry.registerRecipeFiller(CrucibleRecipe.class, ModRecipes.CRUCIBLE, recipe -> new CrucibleDisplay(recipe, PORCELAIN_CRUCIBLE));
        registry.registerRecipeFiller(CrucibleHeatRecipe.class, ModRecipes.CRUCIBLE_HEAT, CrucibleHeatDisplay::new);
        // TODO: Actually implement this properly
        registry.registerRecipeFiller(SieveRecipe.class, ModRecipes.SIEVE, SieveDisplay::new);
        registry.registerRecipeFiller(AlchemyRecipe.class, ModRecipes.ALCHEMY, AlchemyDisplay::new);
        registry.registerRecipeFiller(FluidTransformationRecipe.class, ModRecipes.FLUID_TRANSFORMATION, TransformingDisplay::new);
        // TODO: Actually implement this properly
        registry.registerRecipeFiller(CompostRecipe.class, ModRecipes.COMPOST, CompostDisplay::new);
        registry.registerRecipeFiller(LeakingRecipe.class, ModRecipes.LEAKING, LeakingDisplay::new);
        registry.registerRecipeFiller(MilkingRecipe.class, ModRecipes.MILKING, MilkingDisplay::new);
        registry.registerRecipeFiller(FluidCombinationRecipe.class, ModRecipes.FLUID_COMBINATION, FluidOnTopDisplay::new);
        //FabricaeExNihiloRegistries.WITCHWATER_ENTITY.getREIRecipes().forEach(recipe -> registry.add(new WitchWaterEntityDisplay(recipe)));
        //FabricaeExNihiloRegistries.WITCHWATER_WORLD.getREIRecipes().forEach(recipe -> registry.add(new WitchWaterWorldDisplay(recipe)));
    }

}
