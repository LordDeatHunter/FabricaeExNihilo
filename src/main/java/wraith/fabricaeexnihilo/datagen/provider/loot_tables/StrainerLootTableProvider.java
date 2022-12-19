package wraith.fabricaeexnihilo.datagen.provider.loot_tables;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModLootContextTypes;

import java.util.function.BiConsumer;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class StrainerLootTableProvider extends SimpleFabricLootTableProvider {

    public StrainerLootTableProvider(FabricDataOutput generator) {
        super(generator, ModLootContextTypes.STRAINER);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> consumer) {
        consumer.accept(id("gameplay/strainer"), LootTable.builder()
            .pool(LootPool.builder()
                .with(LootTableEntry.builder(new Identifier("gameplay/fishing/junk"))
                    .weight(10))
                .with(LootTableEntry.builder(new Identifier("gameplay/fishing/fish"))
                    .weight(85))
            )
        );
    }
}
