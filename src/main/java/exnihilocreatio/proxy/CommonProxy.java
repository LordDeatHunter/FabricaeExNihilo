package exnihilocreatio.proxy;

import exnihilocreatio.*;
import exnihilocreatio.compatibility.CompatTOP;
import exnihilocreatio.modules.Forestry;
import exnihilocreatio.modules.IExNihiloCreatioModule;
import exnihilocreatio.util.Data;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
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

import java.io.File;

import static exnihilocreatio.util.Data.RECIPES;

@Mod.EventBusSubscriber
public abstract class CommonProxy {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.registerBlocks(event.getRegistry());
        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules)
            module.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.registerItems(event.getRegistry());
        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules)
            module.registerItems(event.getRegistry());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerItemsLowest(RegistryEvent.Register<Item> event) {
        ModItems.registerItemsLowest(event.getRegistry());
    }

    @SubscribeEvent
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> e) {
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

        // Pre-Init call on the modules
        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules)
            module.preInit(event);
    }

    public void init(FMLInitializationEvent event) {
        // TODO split config loading, ores/item/block creating to preInit or the relavent register event, regisitry initialization here, recipe initialization in post
        ExNihiloCreatio.loadConfigs(); // Moved here to allow Forestry to register Bee templates.
        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules)
            module.init(event);
        if(Loader.isModLoaded("forestry")){
            // This needs to come after the modules
            Forestry.HIVE_REQUIREMENTS_REGISTRY.loadJson(new File(ExNihiloCreatio.configDirectory, "ScentedHiveRegistry.json"));
        }
    }

    public void postInit(FMLPostInitializationEvent event) {
        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules)
            module.postInit(event);
    }

    public void registerModels(ModelRegistryEvent event) {
    }

    public boolean runningOnServer() {
        return true;
    }
}
