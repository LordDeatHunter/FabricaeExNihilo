package wraith.fabricaeexnihilo.datagen.provider;

import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTools;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ModelProvider extends FabricModelProvider {

    public static final Model BARREL_MODEL = new Model(Optional.of(id("block/barrel")), Optional.empty(), TextureKey.ALL);
    public static final Model CRUCIBLE_MODEL = new Model(Optional.of(id("block/crucible")), Optional.empty(), TextureKey.ALL);
    public static final Model MESH_MODEL = new Model(Optional.of(id("item/mesh")), Optional.empty());
    public static final Model SIEVE_MODEL = new Model(Optional.of(id("block/sieve")), Optional.empty(), TextureKey.ALL);
    private static final TextureKey PILAR_KEY = TextureKey.of("pilar");
    public static final Model STRAINER_MODEL = new Model(Optional.of(id("block/strainer")), Optional.empty(), PILAR_KEY);

    public ModelProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        ModBlocks.CRUSHED.forEach((id, block) -> generator.registerSimpleCubeAll(block));

        register(Models.PARTICLE, BrineFluid.BLOCK, id("block/fluid/brine_still"), generator);
        register(Models.PARTICLE, BloodFluid.BLOCK, id("block/fluid/blood_still"), generator);
        register(Models.PARTICLE, MilkFluid.BLOCK, id("block/fluid/milk_still"), generator);
        register(Models.PARTICLE, WitchWaterFluid.BLOCK, id("block/fluid/witchwater_still"), generator);
        register(Models.PARTICLE, ModBlocks.INFESTING_LEAVES, new Identifier("block/oak_leaves"), generator);

        // As we are using the vanilla textures we can't autogenerate these
        var crucibles = ModBlocks.CRUCIBLES;
        register(CRUCIBLE_MODEL, crucibles.get(id("porcelain_crucible")), id("block/porcelain_crucible"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("oak_crucible")), new Identifier("block/oak_log"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("birch_crucible")), new Identifier("block/birch_log"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("spruce_crucible")), new Identifier("block/spruce_log"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("dark_oak_crucible")), new Identifier("block/dark_oak_log"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("jungle_crucible")), new Identifier("block/jungle_log"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("acacia_crucible")), new Identifier("block/acacia_log"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("crimson_crucible")), new Identifier("block/crimson_stem"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("warped_crucible")), new Identifier("block/warped_stem"), generator);
        register(CRUCIBLE_MODEL, crucibles.get(id("rubber_crucible")), new Identifier("techreborn:block/rubber_log_side"), generator);

        var sieves = ModBlocks.SIEVES;
        register(SIEVE_MODEL, sieves.get(id("oak_sieve")), new Identifier("block/oak_planks"), generator);
        register(SIEVE_MODEL, sieves.get(id("birch_sieve")), new Identifier("block/birch_planks"), generator);
        register(SIEVE_MODEL, sieves.get(id("spruce_sieve")), new Identifier("block/spruce_planks"), generator);
        register(SIEVE_MODEL, sieves.get(id("dark_oak_sieve")), new Identifier("block/dark_oak_planks"), generator);
        register(SIEVE_MODEL, sieves.get(id("jungle_sieve")), new Identifier("block/jungle_planks"), generator);
        register(SIEVE_MODEL, sieves.get(id("acacia_sieve")), new Identifier("block/acacia_planks"), generator);
        register(SIEVE_MODEL, sieves.get(id("crimson_sieve")), new Identifier("block/crimson_planks"), generator);
        register(SIEVE_MODEL, sieves.get(id("warped_sieve")), new Identifier("block/warped_planks"), generator);
        register(SIEVE_MODEL, sieves.get(id("rubber_sieve")), new Identifier("techreborn:block/rubber_planks"), generator);

        var barrels = ModBlocks.BARRELS;
        register(BARREL_MODEL, barrels.get(id("stone_barrel")), new Identifier("block/stone"), generator);
        register(BARREL_MODEL, barrels.get(id("oak_barrel")), new Identifier("block/oak_planks"), generator);
        register(BARREL_MODEL, barrels.get(id("birch_barrel")), new Identifier("block/birch_planks"), generator);
        register(BARREL_MODEL, barrels.get(id("spruce_barrel")), new Identifier("block/spruce_planks"), generator);
        register(BARREL_MODEL, barrels.get(id("dark_oak_barrel")), new Identifier("block/dark_oak_planks"), generator);
        register(BARREL_MODEL, barrels.get(id("jungle_barrel")), new Identifier("block/jungle_planks"), generator);
        register(BARREL_MODEL, barrels.get(id("acacia_barrel")), new Identifier("block/acacia_planks"), generator);
        register(BARREL_MODEL, barrels.get(id("crimson_barrel")), new Identifier("block/crimson_planks"), generator);
        register(BARREL_MODEL, barrels.get(id("warped_barrel")), new Identifier("block/warped_planks"), generator);
        register(BARREL_MODEL, barrels.get(id("rubber_barrel")), new Identifier("techreborn:block/rubber_planks"), generator);

        var strainers = ModBlocks.STRAINERS;
        register(STRAINER_MODEL, strainers.get(id("oak_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("block/oak_log")), generator);
        register(STRAINER_MODEL, strainers.get(id("birch_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("block/birch_log")), generator);
        register(STRAINER_MODEL, strainers.get(id("spruce_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("block/spruce_log")), generator);
        register(STRAINER_MODEL, strainers.get(id("dark_oak_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("block/dark_oak_log")), generator);
        register(STRAINER_MODEL, strainers.get(id("jungle_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("block/jungle_log")), generator);
        register(STRAINER_MODEL, strainers.get(id("acacia_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("block/acacia_log")), generator);
        register(STRAINER_MODEL, strainers.get(id("crimson_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("block/crimson_stem")), generator);
        register(STRAINER_MODEL, strainers.get(id("warped_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("block/warped_stem")), generator);
        register(STRAINER_MODEL, strainers.get(id("rubber_strainer")), new TextureMap().put(PILAR_KEY, new Identifier("techreborn:block/rubber_log_side")), generator);

        var infestedLeaves = ModBlocks.INFESTED_LEAVES;
        register(new Model(Optional.of(new Identifier("block/oak_leaves")), Optional.empty()), infestedLeaves.get(id("infested_oak_leaves")), new TextureMap(), generator);
        register(new Model(Optional.of(new Identifier("block/birch_leaves")), Optional.empty()), infestedLeaves.get(id("infested_birch_leaves")), new TextureMap(), generator);
        register(new Model(Optional.of(new Identifier("block/spruce_leaves")), Optional.empty()), infestedLeaves.get(id("infested_spruce_leaves")), new TextureMap(), generator);
        register(new Model(Optional.of(new Identifier("block/dark_oak_leaves")), Optional.empty()), infestedLeaves.get(id("infested_dark_oak_leaves")), new TextureMap(), generator);
        register(new Model(Optional.of(new Identifier("block/acacia_leaves")), Optional.empty()), infestedLeaves.get(id("infested_acacia_leaves")), new TextureMap(), generator);
        register(new Model(Optional.of(new Identifier("block/jungle_leaves")), Optional.empty()), infestedLeaves.get(id("infested_jungle_leaves")), new TextureMap(), generator);
        register(new Model(Optional.of(new Identifier("block/acacia_leaves" /* We use acacia because the rubber texture has color */)), Optional.empty()), infestedLeaves.get(id("infested_rubber_leaves")), new TextureMap(), generator);

        generator.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(ModBlocks.END_CAKE)
                                .coordinate(
                                        BlockStateVariantMap.create(Properties.BITES)
                                                .register(0, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(ModBlocks.END_CAKE)))
                                                .register(1, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice1")))
                                                .register(2, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice2")))
                                                .register(3, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice3")))
                                                .register(4, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice4")))
                                                .register(5, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice5")))
                                                .register(6, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice6")))
                                )
                );
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        ModItems.DOLLS.keySet().forEach(id -> {
            var item = Registries.ITEM.get(id);
            uploadModel(Models.GENERATED, item, new Identifier(id.getNamespace(), "item/dolls/" + id.getPath()), generator);
        });
        ModItems.MESHES.forEach((id, item) -> uploadModel(MESH_MODEL, item, new Identifier(id.getNamespace(), "item/ore/" + id.getPath()), generator));
        ModItems.ORE_PIECES.forEach((id, item) -> uploadModel(Models.GENERATED, item, new Identifier(id.getNamespace(), "item/ore/" + id.getPath()), generator));
        ModItems.PEBBLES.forEach((id, item) -> uploadModel(Models.GENERATED, item, new Identifier(id.getNamespace(), "item/pebbles/" + id.getPath()), generator));
        ModItems.SEEDS.forEach((id, item) -> uploadModel(Models.GENERATED, item, new Identifier(id.getNamespace(), "item/seeds/" + id.getPath()), generator));
        ModTools.CROOKS.forEach((id, item) -> uploadModel(Models.HANDHELD, item, new Identifier(id.getNamespace(), "item/tools/crook/" + id.getPath()), generator));
        ModTools.HAMMERS.forEach((id, item) -> uploadModel(Models.HANDHELD, item, new Identifier(id.getNamespace(), "item/tools/hammer/" + id.getPath()), generator));

        generator.register(BloodFluid.BUCKET, Models.GENERATED);
        generator.register(BrineFluid.BUCKET, Models.GENERATED);
        generator.register(WitchWaterFluid.BUCKET, Models.GENERATED);
        generator.register(ModItems.COOKED_SILKWORM, Models.GENERATED);
        generator.register(ModItems.RAW_SILKWORM, Models.GENERATED);
        generator.register(ModItems.SALT_BOTTLE, Models.GENERATED);
        generator.register(ModItems.PORCELAIN, Models.GENERATED);
        generator.register(ModBlocks.END_CAKE.asItem(), Models.GENERATED);

        var writer = generator.getWriter();
        // Special case: looks like block, but isn't
        CRUCIBLE_MODEL.upload(ModelIds.getItemModelId(ModItems.UNFIRED_PORCELAIN_CRUCIBLE), TextureMap.all(id("block/unfired_porcelain_crucible")), writer);
    }

    private void uploadModel(Model model, Item item, Identifier texture, ItemModelGenerator generator) {
        uploadModel(model, ModelIds.getItemModelId(item), TextureMap.layer0(texture), generator.getWriter());
    }

    private void register(Model model, Block block, Identifier texture, BlockStateModelGenerator generator) {
        register(model, block, TextureMap.all(texture), generator);
    }

    private void register(Model model, Block block, TextureMap textures, BlockStateModelGenerator generator) {
        var modelId = ModelIds.getBlockModelId(block);
        uploadModel(model, modelId, textures, generator.modelCollector);
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));
    }

    private void uploadModel(Model model, Identifier modelId, TextureMap texture, BiConsumer<Identifier, Supplier<JsonElement>> exporter) {
        model.upload(modelId, texture, exporter);
    }
}
