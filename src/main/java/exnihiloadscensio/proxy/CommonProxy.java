package exnihiloadscensio.proxy;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.ModBlocks;
import exnihiloadscensio.ModItems;
import exnihiloadscensio.Recipes;
import exnihiloadscensio.registries.OreRegistry;
import exnihiloadscensio.util.Data;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

import static exnihiloadscensio.util.Data.RECIPES;

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
        OreRegistry.loadJson(new File(ExNihiloAdscensio.configDirectory, "OreRegistry.json"));
        OreRegistry.registerToGameRegistry(event.getRegistry());
    }

    @SubscribeEvent
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> e) {
        // Recipes.init();
        e.getRegistry().registerAll(Data.RECIPES.toArray(new IRecipe[RECIPES.size()]));
        OreRegistry.doRecipes();
    }

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public boolean runningOnServer() {
        return true;
    }

    public void registerConfigs(File configDirectory) {

    }

}
