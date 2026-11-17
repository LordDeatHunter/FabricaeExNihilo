package wraith.fabricaeexnihilo.datagen.provider.tag;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public BlockTagProvider(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> blockTags) {
        super(generator, blockTags);
    }

    /** 1.21.11 tag builders take RegistryKeys, so map the objects we pass in. */
    private ProvidedTagBuilder<Block, Block> tag(TagKey<Block> key) {
        return builder(key).mapped(v -> Registries.BLOCK.getKey(v).orElseThrow());
    }

    /**
     * Builder that keeps RegistryKey values instead of mapped Block objects.
     *
     * <p>1.21.11's mapped adapter forwards addOptional() to the underlying add()
     * (bytecode: ProvidedTagBuilder$2.method_71560 calls field_60484.method_71554),
     * which silently turns optional entries into hard entries. Tags that contain
     * blocks from optional mods must therefore be built through this raw builder so
     * the generated JSON keeps "required": false.
     */
    private ProvidedTagBuilder<RegistryKey<Block>, Block> keyedTag(TagKey<Block> key) {
        return builder(key);
    }

    private static RegistryKey<Block> blockKey(Identifier id) {
        return RegistryKey.of(RegistryKeys.BLOCK, id);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        tag(ModTags.Common.CONCRETE_POWDERS)
                .add(Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER,
                        Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER,
                        Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER,
                        Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER,
                        Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER);
        tag(ModTags.Common.CONCRETES)
                .add(Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE,
                        Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE,
                        Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE,
                        Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE,
                        Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE);
        tag(ModTags.Common.TORCHES)
                .add(Blocks.TORCH, Blocks.WALL_TORCH, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);

        // Optional to avoid issues with optional modules
        ModBlocks.INFESTED_LEAVES.keySet().forEach(id -> keyedTag(ModTags.INFESTED_LEAVES).addOptional(blockKey(id)));

        tag(ModTags.CROOKABLES)
                .addOptionalTag(BlockTags.LEAVES)
                .addTag(ModTags.INFESTED_LEAVES);
        tag(ModTags.HAMMERABLES)
                .addOptionalTag(ConventionalBlockTags.GLASS_BLOCKS)
                .addTag(ModTags.Common.CONCRETES)
                .addTag(ModTags.Common.CONCRETE_POWDERS)
                .addOptionalTag(BlockTags.WOOL)
                .addOptionalTag(BlockTags.CORAL_PLANTS)
                .addOptionalTag(BlockTags.CORAL_BLOCKS)
                .add(Blocks.STONE, Blocks.COBBLESTONE, Blocks.GRAVEL, Blocks.SAND, ItemUtils.getExNihiloBlock("silt"))
                .add(Blocks.ANDESITE, Blocks.GRANITE, Blocks.DIORITE, Blocks.CALCITE, Blocks.PRISMARINE)
                .add(Blocks.END_STONE, Blocks.END_STONE_BRICKS, Blocks.NETHER_BRICKS, Blocks.NETHERRACK)
                .add(Blocks.BLACKSTONE, Blocks.COBBLED_DEEPSLATE)
                .add(ItemUtils.getExNihiloBlock("crushed_granite"));

        var pickaxeMineableTag = tag(BlockTags.PICKAXE_MINEABLE);
        var axeMineableTag = keyedTag(BlockTags.AXE_MINEABLE);
        var shovelMineableTag = keyedTag(BlockTags.SHOVEL_MINEABLE);
        var hoeMineableTag = tag(BlockTags.SHOVEL_MINEABLE);
        ModBlocks.BARRELS.forEach((id, barrel) -> {
            if (barrel == DefaultApiModule.INSTANCE.stoneBarrel) {
                pickaxeMineableTag.add(barrel);
            } else {
                axeMineableTag.addOptional(blockKey(id));
            }
        });
        ModBlocks.CRUCIBLES.forEach((id, crucible) -> {
            if (crucible == DefaultApiModule.INSTANCE.porcelainCrucible) {
                pickaxeMineableTag.add(crucible);
            } else {
                axeMineableTag.addOptional(blockKey(id));
            }
        });
        ModBlocks.SIEVES.forEach((id, crucible) -> axeMineableTag.addOptional(blockKey(id)));
        ModBlocks.STRAINERS.forEach((id, crucible) -> axeMineableTag.addOptional(blockKey(id)));
        ModBlocks.CRUSHED.forEach((id, crucible) -> shovelMineableTag.addOptional(blockKey(id)));
        hoeMineableTag.addTag(ModTags.INFESTED_LEAVES);
    }
}
