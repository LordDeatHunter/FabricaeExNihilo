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
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
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
    public void postInit(FMLPostInitializationEvent event){
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

        }
        if(Loader.isModLoaded("magicbees")){

        }
        if(Loader.isModLoaded("morebees") && !Loader.isModLoaded("extrabees")){

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
        adj.put(IngredientUtil.parseFromString("minecraft:water"), 1);
        return adj;
    }
}
