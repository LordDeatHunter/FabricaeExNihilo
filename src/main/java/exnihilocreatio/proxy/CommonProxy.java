package exnihilocreatio.proxy;

import exnihilocreatio.*;
import exnihilocreatio.compatibility.CompatTOP;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ore.EnumOreSubtype;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.manager.ExNihiloDefaultRecipes;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.Data;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;

import static exnihilocreatio.util.Data.RECIPES;

@Mod.EventBusSubscriber
public abstract class CommonProxy {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.registerItems(event.getRegistry());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerItemsLower(RegistryEvent.Register<Item> event) {
        ExNihiloDefaultRecipes.registerDefaults();
        ExNihiloRegistryManager.ORE_REGISTRY.loadJson(new File(ExNihiloCreatio.configDirectory, "OreRegistry.json"));
        ExNihiloRegistryManager.ORE_REGISTRY.registerToGameRegistry(event.getRegistry());
        ExNihiloRegistryManager.ORE_REGISTRY.doRecipes();

        ItemOre oreYellorium = ExNihiloRegistryManager.ORE_REGISTRY.getOreItem("yellorium");
        if (ModConfig.compatibility.addYelloriteOreDict && oreYellorium != null){
            OreDictionary.registerOre("oreYellorite", new ItemStack(oreYellorium, 1, EnumOreSubtype.CHUNK.getMeta()));
            OreDictionary.registerOre("oreUranium", new ItemStack(oreYellorium, 1, EnumOreSubtype.CHUNK.getMeta()));
            OreDictionary.registerOre("dustUranium", new ItemStack(oreYellorium, 1, EnumOreSubtype.DUST.getMeta()));
            OreDictionary.registerOre("pieceUranium", new ItemStack(oreYellorium, 1, EnumOreSubtype.PIECE.getMeta()));
        }
    }

    @SubscribeEvent
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> e) {
        // Recipes.init();

        ExNihiloCreatio.loadConfigs();

        Recipes.init();
        e.getRegistry().registerAll(Data.RECIPES.toArray(new IRecipe[RECIPES.size()]));
    }

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        ModEnchantments.registerEnchantments(event.getRegistry());
    }

    public void preInit(FMLPreInitializationEvent event) {
        ModFluids.init();
        if (Loader.isModLoaded("theoneprobe")) {
            CompatTOP.register();
        }
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void registerModels(ModelRegistryEvent event) {
    }

    public boolean runningOnServer() {
        return true;
    }
}
