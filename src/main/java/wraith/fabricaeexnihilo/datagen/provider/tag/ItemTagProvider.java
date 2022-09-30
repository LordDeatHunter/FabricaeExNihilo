package wraith.fabricaeexnihilo.datagen.provider.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.ModTools;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ItemTagProvider(FabricDataGenerator generator, @Nullable BlockTagProvider blockTagProvider) {
        super(generator, blockTagProvider);
    }

    @Override
    protected void generateTags() {
        // Optional to avoid issues with optional modules.
        ModTools.CROOKS.keySet().forEach(getOrCreateTagBuilder(ModTags.CROOKS)::addOptional);
        ModTools.HAMMERS.keySet().forEach(getOrCreateTagBuilder(ModTags.HAMMERS)::addOptional);

        ModBlocks.BARRELS.keySet().forEach(getOrCreateTagBuilder(ModTags.BARRELS)::addOptional);

        getOrCreateTagBuilder(ModTags.Common.VEGETABLES)
                .add(Items.POTATO, Items.CARROT, Items.BEETROOT);
        getOrCreateTagBuilder(ModTags.Common.SEEDS)
                .add(Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS)
                .add(id("sea_pickle_seeds"), id("grass_seeds"), id("mycelium_seeds"), id("chorus_seeds"), id("cactus_seeds"), id("sugarcane_seeds"), id("kelp_seeds"));
        getOrCreateTagBuilder(ModTags.Common.RAW_MEAT)
                .add(Items.COD, Items.SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH,
                        Items.BEEF, Items.MUTTON, Items.PORKCHOP, Items.CHICKEN,
                        Items.RABBIT, ModItems.RAW_SILKWORM);
        getOrCreateTagBuilder(ModTags.Common.COOKED_MEAT)
                .add(Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKED_BEEF,
                        Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_CHICKEN,
                        Items.COOKED_RABBIT, ModItems.COOKED_SILKWORM);
    }
}
