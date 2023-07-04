package wraith.fabricaeexnihilo.datagen.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Consumer;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;
import static wraith.fabricaeexnihilo.datagen.DatagenItems.*;
import static wraith.fabricaeexnihilo.modules.ModItems.*;
import static wraith.fabricaeexnihilo.modules.ModTools.CROOKS;
import static wraith.fabricaeexnihilo.modules.ModTools.HAMMERS;

/**
 * Provider for all recipes using vanilla recipe types
 */
public class BaseRecipeProvider extends FabricRecipeProvider {
    public BaseRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        var mythicMetalsExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded("mythicmetals"));
        var indrevExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded("indrev"));
        var modernIndustrializationExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded("modern_industrialization"));
        var techRebornExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded("techreborn"));

        offerHammerRecipes(exporter);
        offerCrookRecipes(exporter);
        offerMeshRecipes(exporter, mythicMetalsExporter);
        offerPebbleToRockRecipes(exporter);
        offerOrePieceRecipes(exporter, mythicMetalsExporter, indrevExporter, modernIndustrializationExporter, techRebornExporter);
    }

    private static void offerCrookRecipes(Consumer<RecipeJsonProvider> exporter) {
        createCrookRecipe(PEBBLES.get(id("andesite_pebble")), CROOKS.get(id("andesite_crook"))).offerTo(exporter, id("crook/andesite"));
        createCrookRecipe(PEBBLES.get(id("basalt_pebble")), CROOKS.get(id("basalt_crook"))).offerTo(exporter, id("crook/basalt"));
        createCrookRecipe(PEBBLES.get(id("blackstone_pebble")), CROOKS.get(id("blackstone_crook"))).offerTo(exporter, id("crook/blackstone"));
        createCrookRecipe(Items.BLAZE_ROD, CROOKS.get(id("blaze_crook"))).offerTo(exporter, id("crook/blaze"));
        createCrookRecipe(Items.BONE, CROOKS.get(id("bone_crook"))).offerTo(exporter, id("crook/bone"));
        createCrookRecipe(Items.CLAY_BALL, CROOKS.get(id("clay_crook"))).offerTo(exporter, id("crook/clay"));
        createCrookRecipe(PEBBLES.get(id("deepslate_pebble")), CROOKS.get(id("deepslate_crook"))).offerTo(exporter, id("crook/deepslate"));
        createCrookRecipe(PEBBLES.get(id("diorite_pebble")), CROOKS.get(id("diorite_crook"))).offerTo(exporter, id("crook/diorite"));
        createCrookRecipe(PEBBLES.get(id("granite_pebble")), CROOKS.get(id("granite_crook"))).offerTo(exporter, id("crook/granite"));
        createCrookRecipe(Items.PRISMARINE_SHARD, CROOKS.get(id("prismarine_crook"))).offerTo(exporter, id("crook/prismarine"));
        createCrookRecipe(Items.POPPED_CHORUS_FRUIT, CROOKS.get(id("purpur_crook"))).offerTo(exporter, id("crook/purpur"));
        createCrookRecipe(PEBBLES.get(id("stone_pebble")), CROOKS.get(id("stone_crook"))).offerTo(exporter, id("crook/stone"));
        createCrookRecipe(Items.STICK, CROOKS.get(id("wooden_crook"))).offerTo(exporter, id("crook/wood"));
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(CROOKS.get(id("clay_crook"))), RecipeCategory.TOOLS, CROOKS.get(id("terracotta_crook")), 0.15f, 200)
                .criterion("has_material", conditionsFromItem(CROOKS.get(id("clay_crook"))))
                .offerTo(exporter, id("crook/terracotta"));
    }

    private static ShapedRecipeJsonBuilder createCrookRecipe(Item material, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .group("fabricaeexnihilo:crooks")
                .input('#', material)
                .pattern("##")
                .pattern(" #")
                .pattern(" #")
                .criterion("has_material", conditionsFromItem(material));
    }

    private static void offerHammerRecipes(Consumer<RecipeJsonProvider> exporter) {
        createHammerRecipe(ItemTags.PLANKS, HAMMERS.get(id("wooden_hammer"))).offerTo(exporter, id("hammer/wood"));
        createHammerRecipe(Items.COBBLESTONE, HAMMERS.get(id("stone_hammer"))).offerTo(exporter, id("hammer/stone"));
        createHammerRecipe(Items.IRON_INGOT, HAMMERS.get(id("iron_hammer"))).offerTo(exporter, id("hammer/iron"));
        createHammerRecipe(Items.GOLD_INGOT, HAMMERS.get(id("golden_hammer"))).offerTo(exporter, id("hammer/gold"));
        createHammerRecipe(Items.DIAMOND, HAMMERS.get(id("diamond_hammer"))).offerTo(exporter, id("hammer/diamond"));
        createNetheriteRecipe(HAMMERS.get(id("diamond_hammer")), HAMMERS.get(id("netherite_hammer")), RecipeCategory.TOOLS)
                .criterion("has_diamond_hammer", conditionsFromItem(HAMMERS.get(id("diamond_hammer"))))
                .offerTo(exporter, id("hammer/netherite"));
    }

    private static ShapedRecipeJsonBuilder createHammerRecipe(Item material, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .input('#', material)
                .input('/', Items.STICK)
                .pattern(" # ")
                .pattern(" /#")
                .pattern("/  ")
                .criterion("has_material", conditionsFromItem(material));
    }

    private static ShapedRecipeJsonBuilder createHammerRecipe(TagKey<Item> material, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .input('#', material)
                .input('/', Items.STICK)
                .pattern(" # ")
                .pattern(" /#")
                .pattern("/  ")
                .criterion("has_material", conditionsFromTag(material));
    }

    private static void offerMeshRecipes(Consumer<RecipeJsonProvider> exporter, Consumer<RecipeJsonProvider> mythicMetalsExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MESHES.get(id("string_mesh")))
                .input('#', Items.STRING)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_string", conditionsFromItem(Items.STRING))
                .offerTo(exporter, id("mesh/string"));

        createMeshRecipe(MESHES.get(id("string_mesh")), Items.FLINT, MESHES.get(id("flint_mesh"))).offerTo(exporter, id("mesh/flint"));
        createMeshRecipe(MESHES.get(id("flint_mesh")), Items.IRON_INGOT, MESHES.get(id("iron_mesh"))).offerTo(exporter, id("mesh/iron"));
        createMeshRecipe(MESHES.get(id("iron_mesh")), Items.DIAMOND, MESHES.get(id("diamond_mesh"))).offerTo(exporter, id("mesh/diamond"));
        createMeshRecipe(MESHES.get(id("diamond_mesh")), getDummyItem(MM_ADAMANTITE_INGOT_ID), MESHES.get(id("adamantite_mesh"))).offerTo(mythicMetalsExporter, id("mesh/adamantite"));
        createMeshRecipe(MESHES.get(id("flint_mesh")), Items.COPPER_INGOT, MESHES.get(id("copper_mesh"))).offerTo(exporter, id("mesh/copper"));
        createMeshRecipe(MESHES.get(id("copper_mesh")), Items.GOLD_INGOT, MESHES.get(id("gold_mesh"))).offerTo(exporter, id("mesh/gold"));
        createMeshRecipe(MESHES.get(id("gold_mesh")), Items.EMERALD, MESHES.get(id("emerald_mesh"))).offerTo(exporter, id("mesh/emerald"));

        createNetheriteRecipe(MESHES.get(id("diamond_mesh")), MESHES.get(id("netherite_mesh")), RecipeCategory.MISC)
                .criterion("has_previous", conditionsFromItem(MESHES.get(id("diamond_mesh"))))
                .offerTo(exporter, id("mesh/netherite"));
    }

    private static ShapedRecipeJsonBuilder createMeshRecipe(Item previous, Item ingredient, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, result)
                .input('#', ingredient)
                .input('O', previous)
                .pattern("# #")
                .pattern("#O#")
                .pattern("# #")
                .criterion("has_previous", conditionsFromItem(previous));
    }

    private static void offerPebbleToRockRecipes(Consumer<RecipeJsonProvider> exporter) {
        createPebbleToRockRecipe(PEBBLES.get(id("andesite_pebble")), Items.ANDESITE).offerTo(exporter, id("pebble_to_rock/andesite"));
        createPebbleToRockRecipe(PEBBLES.get(id("basalt_pebble")), Items.BASALT).offerTo(exporter, id("pebble_to_rock/basalt"));
        createPebbleToRockRecipe(PEBBLES.get(id("blackstone_pebble")), Items.BLACKSTONE).offerTo(exporter, id("pebble_to_rock/blackstone"));
        createPebbleToRockRecipe(PEBBLES.get(id("calcite_pebble")), Items.CALCITE).offerTo(exporter, id("pebble_to_rock/calcite"));
        createPebbleToRockRecipe(PEBBLES.get(id("deepslate_pebble")), Items.DEEPSLATE).offerTo(exporter, id("pebble_to_rock/deepslate"));
        createPebbleToRockRecipe(PEBBLES.get(id("diorite_pebble")), Items.DIORITE).offerTo(exporter, id("pebble_to_rock/diorite"));
        createPebbleToRockRecipe(PEBBLES.get(id("dripstone_pebble")), Items.POINTED_DRIPSTONE).offerTo(exporter, id("pebble_to_rock/dripstone"));
        createPebbleToRockRecipe(PEBBLES.get(id("granite_pebble")), Items.GRANITE).offerTo(exporter, id("pebble_to_rock/granite"));
        createPebbleToRockRecipe(PEBBLES.get(id("stone_pebble")), Items.STONE).offerTo(exporter, id("pebble_to_rock/stone"));
        createPebbleToRockRecipe(PEBBLES.get(id("tuff_pebble")), Items.TUFF).offerTo(exporter, id("pebble_to_rock/tuff"));
    }

    private static ShapedRecipeJsonBuilder createPebbleToRockRecipe(Item pebble, Item rock) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, rock)
                .input('#', pebble)
                .pattern("##")
                .pattern("##")
                .criterion("has_pebble", conditionsFromItem(pebble));
    }

    private static void offerOrePieceRecipes(Consumer<RecipeJsonProvider> exporter,
                                             Consumer<RecipeJsonProvider> mythicMetalsExporter,
                                             Consumer<RecipeJsonProvider> indrevExporter,
                                             Consumer<RecipeJsonProvider> modernIndustrializationExporter,
                                             Consumer<RecipeJsonProvider> techRebornExporter) {
        createOrePieceRecipe(ORE_PIECES.get(id("raw_copper_piece")), Items.RAW_COPPER).offerTo(exporter, id("ore_piece/copper"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_gold_piece")), Items.RAW_GOLD).offerTo(exporter, id("ore_piece/gold"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_iron_piece")), Items.RAW_IRON).offerTo(exporter, id("ore_piece/iron"));

        createOrePieceRecipe(ORE_PIECES.get(id("raw_adamantite_piece")), getDummyItem(MM_RAW_ADAMANTITE_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/adamantite"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_aquarium_piece")), getDummyItem(MM_RAW_AQUARIUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/aquarium"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_banglum_piece")), getDummyItem(MM_RAW_BANGLUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/banglum"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_carmot_piece")), getDummyItem(MM_RAW_CARMOT_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/carmot"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_kyber_piece")), getDummyItem(MM_RAW_KYBER_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/kyber"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_manganese_piece")), getDummyItem(MM_RAW_MANGANESE_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/manganese"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_midas_gold_piece")), getDummyItem(MM_RAW_MIDAS_GOLD_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/midas_gold"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_mythril_piece")), getDummyItem(MM_RAW_MYTHRIL_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/mythril"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_orichalcum_piece")), getDummyItem(MM_RAW_ORICHALCUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/orichalcum"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_osmium_piece")), getDummyItem(MM_RAW_OSMIUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/osmium"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_palladium_piece")), getDummyItem(MM_RAW_PALLADIUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/palladium"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_platinum_piece")), getDummyItem(MM_RAW_PLATINUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/platinum"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_prometheum_piece")), getDummyItem(MM_RAW_PROMETHEUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/prometheum"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_quadrillum_piece")), getDummyItem(MM_RAW_QUADRILLUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/quadrillum"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_runite_piece")), getDummyItem(MM_RAW_RUNITE_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/runite"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_silver_piece")), getDummyItem(MM_RAW_SILVER_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/silver"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_stormyx_piece")), getDummyItem(MM_RAW_STORMYX_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/stormyx"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_tin_piece")), getDummyItem(MM_RAW_TIN_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/tin"));

        createOrePieceRecipe(ORE_PIECES.get(id("raw_iridium_piece")), getDummyItem(TR_RAW_IRIDIUM_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/iridium"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_lead_piece")), getDummyItem(TR_RAW_LEAD_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/lead"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_silver_piece")), getDummyItem(TR_RAW_SILVER_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/silver"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_tin_piece")), getDummyItem(TR_RAW_TIN_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/tin"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_tungsten_piece")), getDummyItem(TR_RAW_TUNGSTEN_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/tungsten"));

        createOrePieceRecipe(ORE_PIECES.get(id("raw_antimony_piece")), getDummyItem(MI_RAW_ANTIMONY_ID)).offerTo(modernIndustrializationExporter, id("ore_piece/modern_industrialization/antimony"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_lead_piece")), getDummyItem(MI_RAW_LEAD_ID)).offerTo(modernIndustrializationExporter, id("ore_piece/modern_industrialization/lead"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_nickel_piece")), getDummyItem(MI_RAW_NICKEL_ID)).offerTo(modernIndustrializationExporter, id("ore_piece/modern_industrialization/nickel"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_tin_piece")), getDummyItem(MI_RAW_TIN_ID)).offerTo(modernIndustrializationExporter, id("ore_piece/modern_industrialization/tin"));

        createOrePieceRecipe(ORE_PIECES.get(id("raw_lead_piece")), getDummyItem(IR_RAW_LEAD_ID)).offerTo(indrevExporter, id("ore_piece/indrev/lead"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_silver_piece")), getDummyItem(IR_RAW_SILVER_ID)).offerTo(indrevExporter, id("ore_piece/indrev/silver"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_tin_piece")), getDummyItem(IR_RAW_TIN_ID)).offerTo(indrevExporter, id("ore_piece/indrev/tin"));
        createOrePieceRecipe(ORE_PIECES.get(id("raw_tungsten_piece")), getDummyItem(IR_RAW_TUNGSTEN_ID)).offerTo(indrevExporter, id("ore_piece/indrev/tungsten"));
    }

    private static ShapelessRecipeJsonBuilder createOrePieceRecipe(Item piece, Item rawOre) {
        return ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, rawOre)
                .input(piece)
                .input(piece)
                .input(piece)
                .input(piece)
                .criterion("has_piece", conditionsFromItem(piece));
    }

    private static SmithingTransformRecipeJsonBuilder createNetheriteRecipe(Item input, Item result, RecipeCategory recipeCategory) {
        return SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.ofItems(input),
                Ingredient.ofItems(Items.NETHERITE_INGOT),
                recipeCategory,
                result);
    }
}
