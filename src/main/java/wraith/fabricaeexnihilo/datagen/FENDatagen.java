package wraith.fabricaeexnihilo.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import wraith.fabricaeexnihilo.datagen.provider.AdvancementProvider;
import wraith.fabricaeexnihilo.datagen.provider.ModelProvider;
import wraith.fabricaeexnihilo.datagen.provider.loot_tables.BlockLootTableProvider;
import wraith.fabricaeexnihilo.datagen.provider.loot_tables.StrainerLootTableProvider;
import wraith.fabricaeexnihilo.datagen.provider.tag.BlockTagProvider;
import wraith.fabricaeexnihilo.datagen.provider.tag.FluidTagProvider;
import wraith.fabricaeexnihilo.datagen.provider.tag.ItemTagProvider;

public class FENDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(ModelProvider::new);
        generator.addProvider(AdvancementProvider::new);
        generator.addProvider(StrainerLootTableProvider::new);
        generator.addProvider(BlockLootTableProvider::new);
        generator.addProvider(FluidTagProvider::new);
        var blockTags = generator.addProvider(BlockTagProvider::new);
        generator.addProvider(new ItemTagProvider(generator, blockTags));
    }
}
