package wraith.fabricaeexnihilo;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloAPI;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.compatibility.modules.FabricaeExNihiloModule;
import wraith.fabricaeexnihilo.compatibility.modules.techreborn.TechReborn;
import wraith.fabricaeexnihilo.modules.*;
import wraith.fabricaeexnihilo.util.ARRPUtils;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.nio.file.Path;

public class FabricaeExNihilo implements ModInitializer {

    public static final String MODID = "fabricaeexnihilo";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MODID, "general")).icon(() -> ItemUtils.getExNihiloItemStack("crook_wood")).build();
    public static final Logger LOGGER = LogManager.getLogger("Fabricae Ex Nihilo");
    private static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(ID("data"));
    public static final FabricaeExNihiloConfig CONFIG = AutoConfig.register(FabricaeExNihiloConfig.class, GsonConfigSerializer::new).get();

    public static Identifier ID(String path) {
        return new Identifier(MODID, path);
    }

    @Override
    public void onInitialize() {
        registerCompatModules();
        // Programmatically generate blocks and items
        LOGGER.info("Generating Blocks/Items");

        // Load the early registries that create items/blocks
        FabricaeExNihiloRegistries.loadEarlyRegistries();

        /* Register Status Effects */
        LOGGER.info("Registering Status Effects");
        ModEffects.registerEffects();
        /* Register Fluids*/
        LOGGER.info("Registering Fluids");
        ModFluids.registerFluids();
        /* Register Blocks */
        LOGGER.info("Registering Blocks");
        ModBlocks.registerBlocks();
        /* Register Items */
        LOGGER.info("Registering Items");
        ModBlocks.registerBlockItems();
        ModItems.registerItems();
        ModTools.registerItems();

        /* Register Block Entities */
        LOGGER.info("Registering Block Entities");
        ModBlocks.registerBlockEntities();

        /* Load the rest of the Fabricae Ex Nihilo registries */
        LOGGER.info("Loading Fabricae Ex Nihilo Registries");
        FabricaeExNihiloRegistries.loadRecipeRegistries();

        LOGGER.info("Creating Tags");
        ARRPUtils.generateTags(RESOURCE_PACK);
        LOGGER.info("Creating Recipes");
        ARRPUtils.generateRecipes(RESOURCE_PACK);
        LOGGER.info("Creating Loot Tables");
        ARRPUtils.generateLootTables(RESOURCE_PACK);

        if (CONFIG.dumpGeneratedResource) {
            RESOURCE_PACK.dump(Path.of("fabricaeexnihilo_generated"));
        }

        RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));
    }

    private void registerCompatModules() {
        FabricaeExNihiloAPI.registerCompatabilityModule(FabricaeExNihiloModule.INSTANCE);
        if (FabricLoader.getInstance().isModLoaded("techreborn")) {
            FabricaeExNihiloAPI.registerCompatabilityModule(new TechReborn());
        }
    }
}
