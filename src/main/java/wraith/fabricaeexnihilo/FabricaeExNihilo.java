package wraith.fabricaeexnihilo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.fabricaeexnihilo.loot.CopyEnchantmentsLootFunction;
import wraith.fabricaeexnihilo.modules.*;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;
import wraith.fabricaeexnihilo.util.EntrypointHelper;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.nio.file.Files;

public class FabricaeExNihilo implements ModInitializer {
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> ItemUtils.getExNihiloItemStack("wooden_crook"))
            .entries((context, entries) -> {
                ModTools.CROOKS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModTools.HAMMERS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.BARRELS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.CRUCIBLES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.CRUSHED.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.INFESTED_LEAVES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.SIEVES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.STRAINERS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.DOLLS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.MESHES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.ORE_PIECES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.PEBBLES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.SEEDS.values().stream().map(ItemStack::new).forEach(entries::add);

                entries.add(new ItemStack(ModItems.COOKED_SILKWORM));
                entries.add(new ItemStack(ModItems.RAW_SILKWORM));
                entries.add(new ItemStack(ModItems.PORCELAIN));
                entries.add(new ItemStack(ModItems.UNFIRED_PORCELAIN_CRUCIBLE));
                entries.add(new ItemStack(ModItems.SALT_BOTTLE));
                entries.add(new ItemStack(ModBlocks.END_CAKE));
                entries.add(new ItemStack(WitchWaterFluid.BUCKET));
                entries.add(new ItemStack(BrineFluid.BUCKET));
                entries.add(new ItemStack(BloodFluid.BUCKET));
            })
            .build();
    public static final Logger LOGGER = LogManager.getLogger("Fabricae Ex Nihilo");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();
    public static final FabricaeExNihiloConfig CONFIG = initConfig();

    public static Identifier id(String path) {
        return new Identifier("fabricaeexnihilo", path);
    }

    public static FabricaeExNihiloConfig initConfig() {
        var file = FabricLoader.getInstance().getConfigDir().resolve("fabricaeexnihilo.json");
        var defaultConf = new FabricaeExNihiloConfig();

        if (Files.isRegularFile(file)) {
            try (var reader = Files.newBufferedReader(file)) {
                var config = GSON.fromJson(reader, FabricaeExNihiloConfig.class);
                if (config == null)
                    throw new IllegalStateException("Invalid config file " + file + ". Please fix or delete it.");
                return config;
            } catch (Exception ex) {
                LOGGER.error("Config-Error", ex);
                LOGGER.info("Something is wrong with the config file, loading with default...");
                return defaultConf;
            }
        } else if (!Files.exists(file)) {
            LOGGER.info("Missing config file, writing defaults!");
            try {
                Files.createFile(file);
                Files.writeString(file, GSON.toJson(defaultConf));
            } catch (Exception ex) {
                LOGGER.error("Config-Error", ex);
            }
            return defaultConf;
        } else {
            LOGGER.error("Config file " + file + " is not a file! Please delete whatever it is.");
            LOGGER.info("Loading with default...");
            return defaultConf;
        }
    }

    @Override
    public void onInitialize() {
        EntrypointHelper.callEntrypoints();

        ModLootContextTypes.register();
        ResourceConditions.register(id("all_items_present"), json -> {
            var values = JsonHelper.getArray(json, "values");

            for (var value : values) {
                if (!Registries.ITEM.containsId(new Identifier(value.getAsString()))) {
                    return false;
                }
            }
            return true;
        });
        ResourceConditions.register(id("all_blocks_present"), json -> {
            var values = JsonHelper.getArray(json, "values");

            for (var value : values) {
                if (!Registries.BLOCK.containsId(new Identifier(value.getAsString()))) {
                    return false;
                }
            }
            return true;
        });
        Registry.register(Registries.LOOT_FUNCTION_TYPE, id("copy_enchantments"), CopyEnchantmentsLootFunction.TYPE);
        Registry.register(Registries.ITEM_GROUP, id("general"), ITEM_GROUP);

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

}
