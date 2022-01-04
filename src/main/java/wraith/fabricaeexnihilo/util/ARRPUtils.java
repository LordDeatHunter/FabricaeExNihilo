package wraith.fabricaeexnihilo.util;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.loot.JLootTable;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.ModTools;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ARRPUtils {
    
    private ARRPUtils() {
    }
    
    public static void generateLootTables(RuntimeResourcePack resourcePack) {
        // Block Breaking
//        ModBlocks.SIEVES.keys.forEach { k ->
//            generateBlockEntityLootTable(k, builder)
//        }
//        ModBlocks.CRUCIBLES.filter{ it.key.path != "unfired_porcelain_crucible" }.keys.forEach { k ->
//            generateBlockEntityLootTable(k, builder)
//        }
//        ModBlocks.BARRELS.keys.forEach { k ->
//            generateBlockEntityLootTable(k, builder)
//        }
        ModBlocks.INFESTED_LEAVES.forEach((identifier, block) -> generateInfestedLeavesBlockLootTable(identifier, resourcePack));
    }
    
    public static void generateTags(RuntimeResourcePack resourcePack) {
        // fabricaeexnihilo:infested_leaves tag
        ModTags.registerBlockAndItem(resourcePack, ModTags.INFESTED_LEAVES_BLOCK, ModBlocks.INFESTED_LEAVES);
        ModTags.register(ModTags.Category.ITEMS, resourcePack, ModTags.HAMMER_TAG, ModTools.HAMMERS);
        ModTags.register(ModTags.Category.ITEMS, resourcePack, ModTags.CROOK_TAG, ModTools.CROOKS);
        
        EnchantmentTagManager.generateDefaultTags(resourcePack);
    }
    
    public static void generateBlockEntityLootTable(Identifier identifier, RuntimeResourcePack resourcePack) {
        resourcePack.addLootTable(id("blocks/" + identifier.getPath()),
                JLootTable.loot("minecraft:block")
                        .pool(JLootTable.pool()
                                .entry(JLootTable.entry()
                                        .type("minecraft:item")
                                        .name(identifier.toString())
                                        .function(JLootTable.function("minecraft:copy_name")
                                                .parameter("source", "block_entity")
                                        )
                                )
                                .rolls(1)
                                .condition(JLootTable.predicate("minecraft:survives_explosion"))
                        )
        );
    }
    
    public static void generateInfestedLeavesBlockLootTable(Identifier identifier, RuntimeResourcePack resourcePack) {
        resourcePack.addLootTable(id("blocks/" + identifier.getPath()),
                JLootTable.loot("minecraft:block")
                        .pool(JLootTable.pool()
                                .entry(JLootTable.entry()
                                        .type("minecraft:item")
                                        .name("minecraft:string"))
                                .rolls(1)
                        )
        );
    }
    
}
