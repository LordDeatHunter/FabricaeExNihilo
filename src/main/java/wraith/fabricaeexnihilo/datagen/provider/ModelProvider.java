package wraith.fabricaeexnihilo.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.BasicItemModel;
import net.minecraft.client.render.item.tint.ConstantTintSource;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.compatibility.TechRebornApiModule;
import wraith.fabricaeexnihilo.mixins.ItemModelGeneratorAccess;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTools;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

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
        register(Models.PARTICLE, ModBlocks.INFESTING_LEAVES, Identifier.of("block/oak_leaves"), generator);

        // As we are using the vanilla textures we can't autogenerate these
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.porcelainCrucible, id("block/porcelain_crucible"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.oakBlocks.crucible(), Identifier.of("block/oak_log"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.birchBlocks.crucible(), Identifier.of("block/birch_log"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.spruceBlocks.crucible(), Identifier.of("block/spruce_log"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.darkOakBlocks.crucible(), Identifier.of("block/dark_oak_log"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.jungleBlocks.crucible(), Identifier.of("block/jungle_log"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.acaciaBlocks.crucible(), Identifier.of("block/acacia_log"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.crimsonBlocks.crucible(), Identifier.of("block/crimson_stem"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.warpedBlocks.crucible(), Identifier.of("block/warped_stem"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.mangroveBlocks.crucible(), Identifier.of("block/mangrove_log"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.cherryBlocks.crucible(), Identifier.of("block/cherry_log"), generator);
        register(CRUCIBLE_MODEL, DefaultApiModule.INSTANCE.bambooBlocks.crucible(), Identifier.of("block/bamboo_block"), generator);

        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.oakBlocks.sieve(), Identifier.of("block/oak_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.birchBlocks.sieve(), Identifier.of("block/birch_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.spruceBlocks.sieve(), Identifier.of("block/spruce_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.darkOakBlocks.sieve(), Identifier.of("block/dark_oak_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.jungleBlocks.sieve(), Identifier.of("block/jungle_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.acaciaBlocks.sieve(), Identifier.of("block/acacia_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.crimsonBlocks.sieve(), Identifier.of("block/crimson_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.warpedBlocks.sieve(), Identifier.of("block/warped_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.mangroveBlocks.sieve(), Identifier.of("block/mangrove_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.cherryBlocks.sieve(), Identifier.of("block/cherry_planks"), generator);
        register(SIEVE_MODEL, DefaultApiModule.INSTANCE.bambooBlocks.sieve(), Identifier.of("block/bamboo_planks"), generator);

        register(BARREL_MODEL, DefaultApiModule.INSTANCE.stoneBarrel, Identifier.of("block/stone"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.oakBlocks.barrel(), Identifier.of("block/oak_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.birchBlocks.barrel(), Identifier.of("block/birch_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.spruceBlocks.barrel(), Identifier.of("block/spruce_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.darkOakBlocks.barrel(), Identifier.of("block/dark_oak_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.jungleBlocks.barrel(), Identifier.of("block/jungle_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.acaciaBlocks.barrel(), Identifier.of("block/acacia_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.crimsonBlocks.barrel(), Identifier.of("block/crimson_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.warpedBlocks.barrel(), Identifier.of("block/warped_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.mangroveBlocks.barrel(), Identifier.of("block/mangrove_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.cherryBlocks.barrel(), Identifier.of("block/cherry_planks"), generator);
        register(BARREL_MODEL, DefaultApiModule.INSTANCE.bambooBlocks.barrel(), Identifier.of("block/bamboo_planks"), generator);

        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.oakBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/oak_log")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.birchBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/birch_log")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.spruceBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/spruce_log")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.darkOakBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/dark_oak_log")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.jungleBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/jungle_log")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.acaciaBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/acacia_log")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.crimsonBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/crimson_stem")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.warpedBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/warped_stem")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.mangroveBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/mangrove_log")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.cherryBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/cherry_log")), generator);
        register(STRAINER_MODEL, DefaultApiModule.INSTANCE.bambooBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("block/bamboo_block")), generator);

        // Some have the wrong leaves as base model, because their texture has color. We depend on leaves looking white when they aren't tinted
        register(new Model(Optional.of(Identifier.of("block/oak_leaves")), Optional.empty()), DefaultApiModule.INSTANCE.infestedOakLeaves, new TextureMap(), generator);
        register(new Model(Optional.of(Identifier.of("block/birch_leaves")), Optional.empty()), DefaultApiModule.INSTANCE.infestedBirchLeaves, new TextureMap(), generator);
        register(new Model(Optional.of(Identifier.of("block/spruce_leaves")), Optional.empty()), DefaultApiModule.INSTANCE.infestedSpruceLeaves, new TextureMap(), generator);
        register(new Model(Optional.of(Identifier.of("block/dark_oak_leaves")), Optional.empty()), DefaultApiModule.INSTANCE.infestedDarkOakLeaves, new TextureMap(), generator);
        register(new Model(Optional.of(Identifier.of("block/acacia_leaves")), Optional.empty()), DefaultApiModule.INSTANCE.infestedAcaciaLeaves, new TextureMap(), generator);
        register(new Model(Optional.of(Identifier.of("block/jungle_leaves")), Optional.empty()), DefaultApiModule.INSTANCE.infestedJungleLeaves, new TextureMap(), generator);
        register(new Model(Optional.of(Identifier.of("block/mangrove_leaves")), Optional.empty()), DefaultApiModule.INSTANCE.infestedMangroveLeaves, new TextureMap(), generator);
        register(new Model(Optional.of(Identifier.of("block/oak_leaves")), Optional.empty()), DefaultApiModule.INSTANCE.infestedCherryLeaves, new TextureMap(), generator);

        generator.blockStateCollector
                .accept(
                        VariantsBlockModelDefinitionCreator.of(ModBlocks.END_CAKE)
                                .with(
                                        BlockStateVariantMap.models(Properties.BITES)
                                                .register(0, BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockModelId(ModBlocks.END_CAKE)))
                                                .register(1, BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice1")))
                                                .register(2, BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice2")))
                                                .register(3, BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice3")))
                                                .register(4, BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice4")))
                                                .register(5, BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice5")))
                                                .register(6, BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockSubModelId(ModBlocks.END_CAKE, "_slice6")))
                                )
                );

        // TechReborn is optional, so its blocks only get models when it is
        // actually present. The datagen run must survive without it.
        if (TechRebornApiModule.INSTANCE.rubberBlocks != null) {
            register(CRUCIBLE_MODEL, TechRebornApiModule.INSTANCE.rubberBlocks.crucible(), Identifier.of("techreborn:block/rubber_log_side"), generator);
            register(SIEVE_MODEL, TechRebornApiModule.INSTANCE.rubberBlocks.sieve(), Identifier.of("techreborn:block/rubber_planks"), generator);
            register(BARREL_MODEL, TechRebornApiModule.INSTANCE.rubberBlocks.barrel(), Identifier.of("techreborn:block/rubber_planks"), generator);
            register(STRAINER_MODEL, TechRebornApiModule.INSTANCE.rubberBlocks.strainer(), new TextureMap().put(PILAR_KEY, Identifier.of("techreborn:block/rubber_log_side")), generator);
        }
        if (TechRebornApiModule.INSTANCE.infestedRubberLeaves != null) {
            register(new Model(Optional.of(Identifier.of("block/acacia_leaves")), Optional.empty()), TechRebornApiModule.INSTANCE.infestedRubberLeaves, new TextureMap(), generator);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        ModItems.DOLLS.keySet().forEach(id -> {
            var item = Registries.ITEM.get(id);
            uploadModel(Models.GENERATED, item, Identifier.of(id.getNamespace(), "item/dolls/" + id.getPath()), generator);
        });
        ModItems.MESHES.forEach((id, item) -> uploadModel(MESH_MODEL, item, Identifier.of(id.getNamespace(), "item/ore/" + id.getPath()),
                List.of(new ConstantTintSource(((MeshItem) item).getColor(0))), generator));
        ModItems.ORE_PIECES.forEach((id, item) -> uploadModel(Models.GENERATED, item, Identifier.of(id.getNamespace(), "item/ore/" + id.getPath()), generator));
        ModItems.PEBBLES.forEach((id, item) -> uploadModel(Models.GENERATED, item, Identifier.of(id.getNamespace(), "item/pebbles/" + id.getPath()), generator));
        ModItems.SEEDS.forEach((id, item) -> uploadModel(Models.GENERATED, item, Identifier.of(id.getNamespace(), "item/seeds/" + id.getPath()), generator));
        ModTools.CROOKS.forEach((id, item) -> uploadModel(Models.HANDHELD, item, Identifier.of(id.getNamespace(), "item/tools/crook/" + id.getPath()), generator));
        ModTools.HAMMERS.forEach((id, item) -> uploadModel(Models.HANDHELD, item, Identifier.of(id.getNamespace(), "item/tools/hammer/" + id.getPath()), generator));

        ((ItemModelGeneratorAccess) generator).invokeRegister(BloodFluid.BUCKET, Models.GENERATED);
        ((ItemModelGeneratorAccess) generator).invokeRegister(BrineFluid.BUCKET, Models.GENERATED);
        ((ItemModelGeneratorAccess) generator).invokeRegister(WitchWaterFluid.BUCKET, Models.GENERATED);
        ((ItemModelGeneratorAccess) generator).invokeRegister(ModItems.COOKED_SILKWORM, Models.GENERATED);
        ((ItemModelGeneratorAccess) generator).invokeRegister(ModItems.RAW_SILKWORM, Models.GENERATED);
        ((ItemModelGeneratorAccess) generator).invokeRegister(ModItems.SALT_BOTTLE, Models.GENERATED);
        ((ItemModelGeneratorAccess) generator).invokeRegister(ModItems.PORCELAIN, Models.GENERATED);
        ((ItemModelGeneratorAccess) generator).invokeRegister(ModBlocks.END_CAKE.asItem(), Models.GENERATED);

        // Special case: looks like block, but isn't
        uploadModel(CRUCIBLE_MODEL, ModItems.UNFIRED_PORCELAIN_CRUCIBLE,
                ModelIds.getItemModelId(ModItems.UNFIRED_PORCELAIN_CRUCIBLE),
                TextureMap.all(id("block/unfired_porcelain_crucible")), List.of(), generator);
    }

    private void uploadModel(Model model, Item item, Identifier texture, ItemModelGenerator generator) {
        uploadModel(model, item, ModelIds.getItemModelId(item), TextureMap.layer0(texture), List.of(), generator);
    }

    private void uploadModel(Model model, Item item, Identifier texture, List<TintSource> tints, ItemModelGenerator generator) {
        uploadModel(model, item, ModelIds.getItemModelId(item), TextureMap.layer0(texture), tints, generator);
    }

    /**
     * Writes both halves of an item's model: the model json itself, and the item
     * definition under {@code assets/<ns>/items/} that points at it. Since 1.21.4
     * an item without that definition has no model in game.
     */
    private void uploadModel(Model model, Item item, Identifier modelId, TextureMap texture, List<TintSource> tints, ItemModelGenerator generator) {
        var access = (ItemModelGeneratorAccess) generator;
        model.upload(modelId, texture, access.getModelCollector());
        access.getOutput().accept(item, new BasicItemModel.Unbaked(modelId, tints));
    }

    private void register(Model model, Block block, Identifier texture, BlockStateModelGenerator generator) {
        register(model, block, TextureMap.all(texture), generator);
    }

    private void register(Model model, Block block, TextureMap textures, BlockStateModelGenerator generator) {
        var modelId = ModelIds.getBlockModelId(block);
        uploadModel(model, modelId, textures, generator.modelCollector);
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, BlockStateModelGenerator.createWeightedVariant(modelId)));
    }

    private void uploadModel(Model model, Identifier modelId, TextureMap texture, BiConsumer<Identifier, ModelSupplier> exporter) {
        model.upload(modelId, texture, exporter);
    }
}
