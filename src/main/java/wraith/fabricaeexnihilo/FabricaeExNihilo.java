package wraith.fabricaeexnihilo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.fabricaeexnihilo.loot.CopyEnchantmentsLootFunction;
import wraith.fabricaeexnihilo.modules.*;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;
import wraith.fabricaeexnihilo.util.EntrypointHelper;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.io.IOException;
import java.nio.file.Files;

public class FabricaeExNihilo implements ModInitializer {
    public static final String MODID = "fabricaeexnihilo";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MODID, "general")).icon(() -> ItemUtils.getExNihiloItemStack("wooden_crook")).build();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();
    public static final Logger LOGGER = LogManager.getLogger("Fabricae Ex Nihilo");
    public static final FabricaeExNihiloConfig CONFIG = initConfig();
    
    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
    
    @Override
    public void onInitialize() {
        EntrypointHelper.callEntrypoints();
    
        Registry.register(Registry.LOOT_FUNCTION_TYPE, id("copy_enchantments"), CopyEnchantmentsLootFunction.TYPE);
        
        LOGGER.debug("Registering Status Effects");
        ModEffects.registerEffects();

        LOGGER.debug("Registering Fluids");
        ModFluids.registerFluids();

        LOGGER.debug("Registering Blocks");
        ModBlocks.registerBlocks();
        
        LOGGER.debug("Registering Items");
        ModBlocks.registerBlockItems();
        ModItems.registerItems();
        ModTools.registerItems();
        
        LOGGER.debug("Registering Block Entities");
        ModBlocks.registerBlockEntities();
        
        LOGGER.debug("Creating Tags");
        BonusEnchantingManager.generateDefaultTags();
        LOGGER.debug("Creating Recipes");
        ModRecipes.register();
    }
    
    public static FabricaeExNihiloConfig initConfig() {
        var file = FabricLoader.getInstance().getConfigDir().resolve("fabricaeexnihilo.json");
        
        try {
            if (Files.isRegularFile(file)) {
                var config = GSON.fromJson(Files.newBufferedReader(file), FabricaeExNihiloConfig.class);
                if (config == null) throw new IllegalStateException("Invalid config file " + file + ". Please fix or delete it.");
                return config;
            } else if (!Files.exists(file)) {
                LOGGER.info("Missing config file, writing defaults!");
                Files.createFile(file);
                try (var writer = Files.newBufferedWriter(file)) {
                    GSON.toJson(new FabricaeExNihiloConfig(), writer);
                }
                return new FabricaeExNihiloConfig();
            } else {
                throw new IllegalStateException("Config file " + file + " is not a file! Please delete whatever it is.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("I/O error while reading config file " + file + "!");
        } catch (JsonParseException e) {
            throw new IllegalStateException("Invalid config file " + file + ". Please fix or delete it.", e);
        }
    }
}
