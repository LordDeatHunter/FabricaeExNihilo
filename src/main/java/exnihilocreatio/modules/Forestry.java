package exnihilocreatio.modules;

import exnihilocreatio.modules.forestry.blocks.BlockHive;
import exnihilocreatio.modules.forestry.items.ItemBlockHive;
import exnihilocreatio.modules.forestry.registry.HiveRequirements;
import exnihilocreatio.modules.forestry.registry.HiveRequirementsRegistry;
import exnihilocreatio.registries.ingredient.IngredientUtil;
import exnihilocreatio.registries.ingredient.OreIngredientStoring;
import exnihilocreatio.registries.manager.IHiveDefaultRegistryProvider;
import exnihilocreatio.util.BlockInfo;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forestry implements IExNihiloCreatioModule {
    @Getter
    private final String MODID = "forestry";

    public static final List<IHiveDefaultRegistryProvider> HIVE_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();

    public static HiveRequirementsRegistry HIVE_REQUIREMENTS_REGISTRY = new HiveRequirementsRegistry();

    public static final BlockHive EXNIHILO_HIVE = new BlockHive(Material.WOOD, "hive");

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(EXNIHILO_HIVE);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.register(new ItemBlockHive(EXNIHILO_HIVE).setRegistryName(EXNIHILO_HIVE.getRegistryName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initBlockModels(ModelRegistryEvent e){
        EXNIHILO_HIVE.initModel(e);
    }

    @Override
    public void init(FMLInitializationEvent event){
        // Why are all of our configs loaded in init?
        // Forest Hive
        HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("forestry:beehives:0"), 0,null, getDefaultForestAdjacent(), null));
        // Meadows Hive
        HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("forestry:beehives:1"), 0,null, getDefaultMeadowsAdjacent(), null));
        // Modest Hive
        HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("forestry:beehives:2"), 0, null, getDefaultModestAdjacent(), null));
        // Tropical Hive
        HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("forestry:beehives:3"), 0, null, getDefaultTropicalAdjacent(), null));
        // Ender Hive
        HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("forestry:beehives:4"), 1, null, getDefaultEnderAdjacent(), null));
        // Wintry Hive
        HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("forestry:beehives:5"), 0, null, getDefaultWintryAdjacent(), null));
        // Marshy Hive
        HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("forestry:beehives:6"), 0, null, getDefaultMarshyAdjacent(), null));
        if(Loader.isModLoaded("extrabees")){
            // Water hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("extrabees:hive:0"), 0, null, getDefaultWaterAdjacent(), null));
            // Rock hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("extrabees:hive:1"), 0, null, getDefaultRockAdjacent(), null));
            // Nether hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("extrabees:hive:2"), -1, null, getDefaultNetherAdjacent(), null));
            // Marble hive
            // HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("extrabees:hive:3"), 0, null, getDefaultMarbleAdjacent(), null));
        }
        if(Loader.isModLoaded("magicbees")){
            // Curious hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("magicbees:hiveblock:0"), 0, null, getDefaultCuriousAdjacent(), null));
            // Unusual hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("magicbees:hiveblock:1"), 0, null, getDefaultUnusualAdjacent(), null));
            // Resonating hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("magicbees:hiveblock:2"), 0, null, getDefaultResonatingAdjacent(), null));
            // Deep hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("magicbees:hiveblock:3"), 0, null, getDefaultDeepAdjacent(), null));
            // Infernal hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("magicbees:hiveblock:4"), -1, null, getDefaultInfernalAdjacent(), null));
            // Oblivion hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("magicbees:hiveblock:5"), 1, null, getDefaultOblivionAdjacent(), null));
        }
        if(Loader.isModLoaded("morebees") && !Loader.isModLoaded("extrabees")){
            // Rock hive
            HIVE_REQUIREMENTS_REGISTRY.register(new HiveRequirements(new BlockInfo("morebees:hive:0"), 0, null, getDefaultRockAdjacent(), null));
        }
    }

    private static Map<Ingredient, Integer> getDefaultForestAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("logWood"), 1);
        adj.put(new OreIngredientStoring("treeLeaves"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultMeadowsAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("flower"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultModestAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("sand"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultTropicalAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(IngredientUtil.parseFromString("minecraft:log:3"), 1);
        adj.put(new OreIngredientStoring("treeLeaves"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultEnderAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(IngredientUtil.parseFromString("minecraft:end_stone"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultWintryAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(IngredientUtil.parseFromString("minecraft:snow"), 1);
        adj.put(IngredientUtil.parseFromString("minecraft:ice"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultMarshyAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("dirt"), 1);
        return adj;
    }

    // Addon Hives
    private static Map<Ingredient, Integer> getDefaultWaterAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("water"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultRockAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("stone"), 4);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultNetherAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("minecraft:netherrack"), 3);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultMarbleAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("diorite"), 2);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultCuriousAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("grass"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultUnusualAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("grass"), 1);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultResonatingAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("stone"), 2);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultDeepAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("minecraft:redstone_block"), 3);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultInfernalAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("minecraft:netherrack"), 1);
        adj.put(new OreIngredientStoring("minecraft:glowstone"), 2);
        return adj;
    }
    private static Map<Ingredient, Integer> getDefaultOblivionAdjacent(){
        Map<Ingredient, Integer> adj = new HashMap<>();
        adj.put(new OreIngredientStoring("minecraft:end_stone"), 1);
        adj.put(new OreIngredientStoring("minecraft:obsidian"), 2);
        return adj;
    }
}
