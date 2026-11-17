package wraith.fabricaeexnihilo.datagen.provider.tag;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.ModTools;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ItemTagProvider(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> registries, @Nullable BlockTagProvider blockTagProvider) {
        super(generator, registries, blockTagProvider);
    }

    /** 1.21.11 tag builders take RegistryKeys, so map the objects we pass in. */
    private ProvidedTagBuilder<Item, Item> tag(TagKey<Item> key) {
        return builder(key).mapped(v -> Registries.ITEM.getKey(v).orElseThrow());
    }

    /**
     * Raw builder that keeps RegistryKey values, used for entries from optional
     * mods: the mapped adapter used by {@link #tag(TagKey)} forwards addOptional()
     * to the underlying add() in 1.21.11, dropping the "required": false flag.
     */
    private ProvidedTagBuilder<RegistryKey<Item>, Item> keyedTag(TagKey<Item> key) {
        return builder(key);
    }

    private static RegistryKey<Item> itemKey(Identifier id) {
        return RegistryKey.of(RegistryKeys.ITEM, id);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Optional to avoid issues with optional modules.
        ModTools.CROOKS.keySet().forEach(id -> keyedTag(ModTags.CROOKS).addOptional(itemKey(id)));
        ModTools.HAMMERS.keySet().forEach(id -> keyedTag(ModTags.HAMMERS).addOptional(itemKey(id)));
        ModBlocks.BARRELS.keySet().forEach(id -> keyedTag(ModTags.BARRELS).addOptional(itemKey(id)));
        ModBlocks.CRUCIBLES.keySet().forEach(id -> keyedTag(ModTags.CRUCIBLES).addOptional(itemKey(id)));
        ModBlocks.SIEVES.keySet().forEach(id -> keyedTag(ModTags.SIEVES).addOptional(itemKey(id)));

        tag(ModTags.Common.VEGETABLES)
                .add(Items.POTATO, Items.CARROT, Items.BEETROOT);
        var seedIds = List.of(id("sea_pickle_seeds"), id("grass_seeds"), id("mycelium_seeds"), id("chorus_seeds"), id("cactus_seeds"), id("sugarcane_seeds"), id("kelp_seeds"));
        var seedsTag = tag(ModTags.Common.SEEDS)
                .add(Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS);
        seedIds.forEach(id -> keyedTag(ModTags.Common.SEEDS).addOptional(itemKey(id)));
        tag(ModTags.Common.RAW_MEAT)
                .add(Items.COD, Items.SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH,
                        Items.BEEF, Items.MUTTON, Items.PORKCHOP, Items.CHICKEN,
                        Items.RABBIT, ModItems.RAW_SILKWORM);
        tag(ModTags.Common.COOKED_MEAT)
                .add(Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKED_BEEF,
                        Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_CHICKEN,
                        Items.COOKED_RABBIT, ModItems.COOKED_SILKWORM);
        tag(ModTags.Common.PORCELAIN)
                .add(ModItems.PORCELAIN);
    }

}
