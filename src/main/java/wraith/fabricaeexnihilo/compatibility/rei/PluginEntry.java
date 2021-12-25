package wraith.fabricaeexnihilo.compatibility.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.text.LiteralText;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.*;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleCategory;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleHeatCategory;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleHeatDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.sieve.SieveCategory;
import wraith.fabricaeexnihilo.compatibility.rei.sieve.SieveDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.tools.ToolCategory;
import wraith.fabricaeexnihilo.compatibility.rei.tools.ToolDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterEntityCategory;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterEntityDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterWorldCategory;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterWorldDisplay;
import wraith.fabricaeexnihilo.util.ItemUtils;

public class PluginEntry implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        FabricaeExNihilo.LOGGER.info("Registering REI Categories");
        registry.add(new SieveCategory());

        registry.add(new ToolCategory(CROOK, ItemUtils.getExNihiloItemStack("crook_wood"), "Crook"));
        registry.add(new ToolCategory(HAMMER, ItemUtils.getExNihiloItemStack("hammer_wood"), "Hammer"));

        registry.add(new CrucibleHeatCategory());
        registry.add(new CrucibleCategory(WOOD_CRUCIBLE, ItemUtils.getExNihiloItemStack("oak_crucible"), new LiteralText("Wood Crucible")));
        registry.add(new CrucibleCategory(STONE_CRUCIBLE, ItemUtils.getExNihiloItemStack("stone_crucible"), new LiteralText("Stone Crucible")));

        registry.add(new CompostCategory());
        registry.add(new LeakingCategory());
        registry.add(new FluidOnTopCategory());
        registry.add(new MilkingCategory());
        registry.add(new TransformingCategory());
        registry.add(new AlchemyCategory());

        registry.add(new WitchWaterEntityCategory());
        registry.add(new WitchWaterWorldCategory());

        // Hackishly Remove the autocrafting button
        registry.removePlusButton(SIEVE);
        registry.removePlusButton(CROOK);
        registry.removePlusButton(HAMMER);
        registry.removePlusButton(CRUCIBLE_HEAT);
        registry.removePlusButton(WOOD_CRUCIBLE);
        registry.removePlusButton(STONE_CRUCIBLE);
        registry.removePlusButton(COMPOSTING);
        registry.removePlusButton(LEAKING);
        registry.removePlusButton(ON_TOP);
        registry.removePlusButton(MILKING);
        registry.removePlusButton(TRANSFORMING);
        registry.removePlusButton(WITCH_WATER_ENTITY);
        registry.removePlusButton(WITCH_WATER_WORLD);
        registry.removePlusButton(ALCHEMY);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        FabricaeExNihilo.LOGGER.info("Registering REI Displays");
        FabricaeExNihiloRegistries.SIEVE.getREIRecipes().forEach(recipe ->
                registry.add(new SieveDisplay(recipe)));
        FabricaeExNihiloRegistries.CROOK.getREIRecipes().forEach(recipe ->
                registry.add(new ToolDisplay(recipe, CROOK)));
        FabricaeExNihiloRegistries.HAMMER.getREIRecipes().forEach(recipe ->
                registry.add(new ToolDisplay(recipe, HAMMER)));
        FabricaeExNihiloRegistries.CRUCIBLE_HEAT.getREIRecipes().forEach(recipe ->
                registry.add(new CrucibleHeatDisplay(recipe)));
        FabricaeExNihiloRegistries.CRUCIBLE_WOOD.getREIRecipes().forEach(recipe ->
                registry.add(new CrucibleDisplay(recipe, WOOD_CRUCIBLE)));
        FabricaeExNihiloRegistries.CRUCIBLE_STONE.getREIRecipes().forEach(recipe ->
                registry.add(new CrucibleDisplay(recipe, STONE_CRUCIBLE)));
        //FabricaeExNihiloRegistries.BARREL_ALCHEMY.getREIRecipes().forEach(recipe -> registry.add(new AlchemyDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_COMPOST.getREIRecipes().forEach(recipe -> registry.add(new CompostDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_LEAKING.getREIRecipes().forEach(recipe -> registry.add(new LeakingDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_ON_TOP.getREIRecipes().forEach(recipe -> registry.add(new FluidOnTopDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_MILKING.getREIRecipes().forEach(recipe -> registry.add(new MilkingDisplay(recipe)));
        //FabricaeExNihiloRegistries.BARREL_TRANSFORM.getREIRecipes().forEach(recipe -> registry.add(new TransformingDisplay(recipe)));
        FabricaeExNihiloRegistries.WITCHWATER_ENTITY.getREIRecipes().forEach(recipe ->
                registry.add(new WitchWaterEntityDisplay(recipe)));
        FabricaeExNihiloRegistries.WITCHWATER_WORLD.getREIRecipes().forEach(recipe ->
                registry.add(new WitchWaterWorldDisplay(recipe)));

    }

    public static final CategoryIdentifier<SieveDisplay> SIEVE = CategoryIdentifier.of(FabricaeExNihilo.id("rei/sieve"));

    public static final CategoryIdentifier<AlchemyDisplay> ALCHEMY = CategoryIdentifier.of(FabricaeExNihilo.id("rei/barrel/alchemy"));
    public static final CategoryIdentifier<CompostDisplay> COMPOSTING= CategoryIdentifier.of(FabricaeExNihilo.id("rei/barrel/composting"));
    public static final CategoryIdentifier<LeakingDisplay> LEAKING= CategoryIdentifier.of(FabricaeExNihilo.id("rei/barrel/leaking"));
    public static final CategoryIdentifier<MilkingDisplay> MILKING= CategoryIdentifier.of(FabricaeExNihilo.id("rei/barrel/milking"));
    public static final CategoryIdentifier<TransformingDisplay> TRANSFORMING= CategoryIdentifier.of(FabricaeExNihilo.id("rei/barrel/transforming"));
    public static final CategoryIdentifier<FluidOnTopDisplay> ON_TOP= CategoryIdentifier.of(FabricaeExNihilo.id("rei/barrel/fluid_on_top"));

    public static final CategoryIdentifier<CrucibleDisplay> WOOD_CRUCIBLE = CategoryIdentifier.of(FabricaeExNihilo.id("rei/crucible/wood"));
    public static final CategoryIdentifier<CrucibleDisplay> STONE_CRUCIBLE = CategoryIdentifier.of(FabricaeExNihilo.id("rei/crucible/stone"));
    public static final CategoryIdentifier<CrucibleHeatDisplay> CRUCIBLE_HEAT = CategoryIdentifier.of(FabricaeExNihilo.id("rei/crucible/heat"));

    public static final CategoryIdentifier<WitchWaterWorldDisplay> WITCH_WATER_WORLD = CategoryIdentifier.of(FabricaeExNihilo.id("rei/witchwater/world"));
    public static final CategoryIdentifier<WitchWaterEntityDisplay> WITCH_WATER_ENTITY = CategoryIdentifier.of(FabricaeExNihilo.id("rei/witchwater/entity"));

    public static final CategoryIdentifier<ToolDisplay> CROOK = CategoryIdentifier.of(FabricaeExNihilo.id("rei/tools/crook"));
    public static final CategoryIdentifier<ToolDisplay> HAMMER = CategoryIdentifier.of(FabricaeExNihilo.id("rei/tools/hammer"));

}
