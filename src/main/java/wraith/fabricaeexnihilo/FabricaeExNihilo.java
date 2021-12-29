package wraith.fabricaeexnihilo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloAPI;
import wraith.fabricaeexnihilo.recipe.util.EntityTypeIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.ItemIngredient;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;
import wraith.fabricaeexnihilo.api.recipes.SieveRecipe;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.compatibility.modules.FabricaeExNihiloModuleImpl;
import wraith.fabricaeexnihilo.compatibility.modules.techreborn.TechReborn;
import wraith.fabricaeexnihilo.json.basic.*;
import wraith.fabricaeexnihilo.json.ingredient.EntityTypeIngredientJson;
import wraith.fabricaeexnihilo.json.ingredient.FluidIngredientJson;
import wraith.fabricaeexnihilo.json.ingredient.ItemIngredientJson;
import wraith.fabricaeexnihilo.json.other.*;
import wraith.fabricaeexnihilo.json.recipe.SieveRecipeJson;
import wraith.fabricaeexnihilo.modules.*;
import wraith.fabricaeexnihilo.modules.sieves.MeshProperties;
import wraith.fabricaeexnihilo.util.ARRPUtils;
import wraith.fabricaeexnihilo.util.Color;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.nio.file.Path;

public class FabricaeExNihilo implements ModInitializer {

    public static final String MODID = "fabricaeexnihilo";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MODID, "general")).icon(() -> ItemUtils.getExNihiloItemStack("wooden_crook")).build();
    public static final Logger LOGGER = LogManager.getLogger("Fabricae Ex Nihilo");
    private static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(id("data"));
    public static final FabricaeExNihiloConfig CONFIG = AutoConfig.register(FabricaeExNihiloConfig.class, GsonConfigSerializer::new).get();
    public static final Gson RECIPE_GSON;

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    @Override
    public void onInitialize() {
        registerCompatModules();
        // Programmatically generate blocks and items
        LOGGER.debug("Generating Blocks/Items");

        // Load the early registries that create items/blocks
        FabricaeExNihiloRegistries.loadEarlyRegistries();

        /* Register Status Effects */
        LOGGER.debug("Registering Status Effects");
        ModEffects.registerEffects();
        /* Register Fluids*/
        LOGGER.debug("Registering Fluids");
        ModFluids.registerFluids();
        /* Register Blocks */
        LOGGER.debug("Registering Blocks");
        ModBlocks.registerBlocks();
        /* Register Items */
        LOGGER.debug("Registering Items");
        ModBlocks.registerBlockItems();
        ModItems.registerItems();
        ModTools.registerItems();

        /* Register Block Entities */
        LOGGER.debug("Registering Block Entities");
        ModBlocks.registerBlockEntities();

        /* Load the rest of the Fabricae Ex Nihilo registries */
        LOGGER.debug("Loading Fabricae Ex Nihilo Registries");
        FabricaeExNihiloRegistries.loadRecipeRegistries();

        LOGGER.debug("Creating Tags");
        ARRPUtils.generateTags(RESOURCE_PACK);
        LOGGER.debug("Creating Recipes");
        ModRecipes.register();
        ARRPUtils.generateRecipes(RESOURCE_PACK);
        LOGGER.debug("Creating Loot Tables");
        ARRPUtils.generateLootTables(RESOURCE_PACK);

        if (CONFIG.dumpGeneratedResource) {
            RESOURCE_PACK.dump(Path.of("fabricaeexnihilo_generated"));
        }

        RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));
    }

    private void registerCompatModules() {
        FabricaeExNihiloAPI.registerCompatabilityModule(FabricaeExNihiloModuleImpl.INSTANCE);
        if (FabricLoader.getInstance().isModLoaded("techreborn")) {
            FabricaeExNihiloAPI.registerCompatabilityModule(new TechReborn());
        }
    }
    
    static {
        RECIPE_GSON = new GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .registerTypeAdapter(Item.class, ItemJson.INSTANCE)
                .registerTypeAdapter(Block.class, BlockJson.INSTANCE)
                .registerTypeAdapter(Color.class, ColorJson.INSTANCE)
                .registerTypeAdapter(EntityType.class, EntityTypeJson.INSTANCE)
                .registerTypeAdapter(Fluid.class, FluidJson.INSTANCE)
                .registerTypeAdapter(Identifier.class, IdentifierJson.INSTANCE)
                .registerTypeAdapter(ItemStack.class, ItemStackJson.INSTANCE)
                .registerTypeAdapter(MeshProperties.class, MeshPropertiesJson.INSTANCE)
                .registerTypeAdapter(VillagerProfession.class, VillagerProfessionJson.INSTANCE)
                .registerTypeAdapter(WeightedList.class, WeightedListJson.INSTANCE)
                .registerTypeAdapter(ItemIngredient.class, ItemIngredientJson.INSTANCE)
                .registerTypeAdapter(FluidIngredient.class, FluidIngredientJson.INSTANCE)
                .registerTypeAdapter(EntityTypeIngredient.class, EntityTypeIngredientJson.INSTANCE)
                /*
                 * Recipes
                 */
                // Other
                .registerTypeAdapter(SieveRecipe.class, SieveRecipeJson.INSTANCE)
        
                .enableComplexMapKeySerialization()
                .create();
    }
}
