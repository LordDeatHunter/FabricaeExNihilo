package wraith.fabricaeexnihilo.util;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.loot.JLootTable;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.impl.EnchantmentTagManager;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.ModTools;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ARRPUtils {

    private ARRPUtils() {
    }

    public static void generateRecipes(RuntimeResourcePack resourcePack) {
        // Ore Chunk Crafting
        FabricaeExNihiloRegistries.ORES.getAll().forEach(ore -> {
            resourcePack.addRecipe(id(ore.getChunkID().getPath() + "_crafting"), ore.generateRecipe());

            if (Registry.ITEM.containsId(ore.getNuggetID())) {
                resourcePack.addRecipe(id(ore.getPieceID().getPath() + "_smelting"), ore.generateNuggetCookingRecipe());
                resourcePack.addRecipe(id(ore.getPieceID().getPath() + "_blasting"), ore.generateNuggetCookingRecipe());
            }
            if (Registry.ITEM.containsId(ore.getIngotID())) {
                resourcePack.addRecipe(id(ore.getChunkID().getPath() + "_smelting"), ore.generateIngotCookingRecipe());
                resourcePack.addRecipe(id(ore.getChunkID().getPath() + "_blasting"), ore.generateIngotCookingRecipe());
            }
        });
        // Mesh Crafting
        FabricaeExNihiloRegistries.MESH.getAll().forEach(mesh -> resourcePack.addRecipe(mesh.getIdentifier(), mesh.generateRecipe()));
        // Sieve Crafting
        ModBlocks.SIEVES.forEach((identifier, sieve) -> resourcePack.addRecipe(identifier, sieve.generateRecipe()));
        // Crucible Crafting
        ModBlocks.CRUCIBLES.entrySet().stream().filter(entry -> !entry.getKey().getPath().equals("unfired_crucible"))
                .forEach(entry -> resourcePack.addRecipe(entry.getKey(), entry.getValue().generateRecipe()));
        // Barrel Crafting
        ModBlocks.BARRELS.forEach((identifier, barrel) -> resourcePack.addRecipe(identifier, barrel.generateRecipe()));
    }

    public static void generateLootTables(RuntimeResourcePack resourcePack) {
        // Block Breaking
//        ModBlocks.SIEVES.keys.forEach { k ->
//            generateBlockEntityLootTable(k, builder)
//        }
//        ModBlocks.CRUCIBLES.filter{ it.key.path != "unfired_crucible" }.keys.forEach { k ->
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

        FabricaeExNihiloRegistries.ORES.getAll().forEach(property -> resourcePack.addTag(new Identifier(property.getOreID().getNamespace(), "items/" + property.getOreID().getPath()), ModTags.generateResourcePackTag(property.getChunkID()))); // common raw ore tag. 1 chunk is treated as 1 raw ore.
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
