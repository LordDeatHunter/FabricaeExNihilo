package wraith.fabricaeexnihilo.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import wraith.fabricaeexnihilo.datagen.provider.AdvancementProvider;
import wraith.fabricaeexnihilo.datagen.provider.ModelProvider;
import wraith.fabricaeexnihilo.datagen.provider.loot_tables.BlockLootTableProvider;
import wraith.fabricaeexnihilo.datagen.provider.loot_tables.StrainerLootTableProvider;
import wraith.fabricaeexnihilo.datagen.provider.recipe.BaseRecipeProvider;
import wraith.fabricaeexnihilo.datagen.provider.tag.BlockTagProvider;
import wraith.fabricaeexnihilo.datagen.provider.tag.FluidTagProvider;
import wraith.fabricaeexnihilo.datagen.provider.tag.ItemTagProvider;

public class FENDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        final FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(ModelProvider::new);
        pack.addProvider(AdvancementProvider::new);
        pack.addProvider(StrainerLootTableProvider::new);
        pack.addProvider(BlockLootTableProvider::new);
        pack.addProvider(FluidTagProvider::new);
        var blockTags = pack.addProvider(BlockTagProvider::new);
        pack.addProvider((output, registries) -> new ItemTagProvider(output, registries, blockTags));
        pack.addProvider(BaseRecipeProvider::new);
    }
}
