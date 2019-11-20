package exnihilofabrico.util

import com.swordglowsblue.artifice.api.ArtificeResourcePack
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.ModTags
import exnihilofabrico.modules.ModTools
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ArtificeUtils {
    fun generateRecipes(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        // Ore Chunk Crafting
        ExNihiloRegistries.ORES.getAll().forEach { ore ->
            builder.addShapedRecipe(id("${ore.getChunkID().path}_crafting")) { ore.generateRecipe(it) }
            if(Registry.ITEM.containsId(ore.getNuggetID())) {
                builder.addSmeltingRecipe(id("${ore.getPieceID().path}_smelting")) { ore.generateNuggetCookingRecipe(it) }
                builder.addBlastingRecipe(id("${ore.getPieceID().path}_blasting")) { ore.generateNuggetCookingRecipe(it) }
            }
            if(Registry.ITEM.containsId(ore.getIngotID())) {
                builder.addSmeltingRecipe(id("${ore.getChunkID().path}_smelting")) { ore.generateIngotCookingRecipe(it) }
                builder.addBlastingRecipe(id("${ore.getChunkID().path}_blasting")) { ore.generateIngotCookingRecipe(it) }
            }
        }
        ExNihiloRegistries.MESH.getAll().forEach { mesh ->
            // Mesh Crafting
            builder.addShapedRecipe(mesh.identifier) { mesh.generateRecipe(it) }
        }
        // Sieve Crafting
        ModBlocks.SIEVES.forEach { (k, sieve) ->
            builder.addShapedRecipe(k) { sieve.generateRecipe(it) }
        }
        // Crucible Crafting
        ModBlocks.CRUCIBLES.filter{ it.key.path != "unfired_crucible" }.forEach { (k, crucible) ->
            builder.addShapedRecipe(k) { crucible.generateRecipe(it) }
        }
        // Barrel Crafting
        ModBlocks.BARRELS.forEach { (k, barrel) ->
            builder.addShapedRecipe(k) { barrel.generateRecipe(it) }
        }
    }

    fun generateLootTables(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        // Block Breaking
        ModBlocks.SIEVES.keys.forEach { k ->
            generateBlockEntityLootTable(k, builder)
        }
        ModBlocks.CRUCIBLES.filter{ it.key.path != "unfired_crucible" }.keys.forEach { k ->
            generateBlockEntityLootTable(k, builder)
        }
        ModBlocks.BARRELS.keys.forEach { k ->
            generateBlockEntityLootTable(k, builder)
        }
        (ModBlocks.INFESTED_LEAVES).forEach { k, block ->
            generateInfestedLeavesBlockLootTable(k, builder)
        }
    }

    fun generateTags(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        // exnihilofabrico:infested_leaves tag
        (ModTags.INFESTED_LEAVES_BLOCK)?.let {
            builder.addBlockTag(it.id) {tag ->
                tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
            }
            builder.addItemTag(it.id) {tag ->
                tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
            }
        }
        (ModTags.HAMMER_TAG)?.let {
            builder.addItemTag(it.id) {tag ->
                tag.values(*ModTools.HAMMERS.keys.toTypedArray())
            }
        }
        (ModTags.CROOK_TAG)?.let {
            builder.addItemTag(it.id) {tag ->
                tag.values(*ModTools.CROOKS.keys.toTypedArray())
            }
        }
        ExNihiloRegistries.ORES.getAll().forEach { property ->
            builder.addItemTag(property.getOreID()) {tag ->
                tag.value(property.getChunkID())
            }
        }
    }

    fun generateBlockEntityLootTable(identifier: Identifier, builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        builder.addLootTable(id("blocks/${identifier.path}")) { lootTable ->
            lootTable.type(Identifier("block"))
                .pool {pool ->
                    pool.rolls(1)
                        .entry { entry ->
                            entry.type(Identifier("item"))
                                .function(Identifier("copy_name")) {
                                    it.add("source", "block_entity")
                                }
                                .name(identifier)

                        }
                        .condition(Identifier("survives_explosion")) {}
                }

        }
    }

    fun generateInfestedLeavesBlockLootTable(identifier: Identifier, builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        builder.addLootTable(id("blocks/${identifier.path}")) { lootTable ->
            lootTable
                .type(Identifier("block"))
                .pool {pool ->
                    pool.rolls(1)
                        .entry { entry ->
                            entry.type(Identifier("item"))
                                .name(Identifier("string"))
                        }
                }

        }
    }
}
