package exnihilocreatio.proxy;

import exnihilocreatio.*;
import exnihilocreatio.registries.OreRegistry;
import exnihilocreatio.util.Data;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerItemsLower(RegistryEvent.Register<Item> event) {
        OreRegistry.loadJson(new File(ExNihiloCreatio.configDirectory, "OreRegistry.json"));
        OreRegistry.registerToGameRegistry(event.getRegistry());
    }

    @SubscribeEvent
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> e) {
        // Recipes.init();
        e.getRegistry().registerAll(Data.RECIPES.toArray(new IRecipe[RECIPES.size()]));
        OreRegistry.doRecipes();
    }

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        ModEnchantments.registerEnchantments(event.getRegistry());
    }

    public void preInit(FMLPreInitializationEvent event) {
        ModFluids.init();
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

    public void registerConfigs(File configDirectory) {

    }

}
