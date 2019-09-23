package exnihilofabrico.modules

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.ExNihiloFabricoAPI.BLOCK_GENERATOR
import exnihilofabrico.id
import exnihilofabrico.modules.barrels.BarrelBlock
import exnihilofabrico.modules.barrels.BarrelBlockEntity
import exnihilofabrico.modules.base.BaseFallingBlock
import exnihilofabrico.modules.crucibles.CrucibleBlock
import exnihilofabrico.modules.crucibles.CrucibleBlockEntity
import exnihilofabrico.modules.infested.InfestedLeavesBlock
import exnihilofabrico.modules.infested.InfestedLeavesItem
import exnihilofabrico.modules.infested.InfestingLeavesBlock
import exnihilofabrico.modules.infested.InfestingLeavesBlockEntity
import exnihilofabrico.modules.sieves.SieveBlock
import exnihilofabrico.modules.sieves.SieveBlockEntity
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.VanillaWoodDefinitions
import exnihilofabrico.util.strength
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.LeavesBlock
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModBlocks {
    private val itemSettings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(64)

    val woodSettings = FabricBlockSettings.of(Material.WOOD).strength(2.0f).sounds(BlockSoundGroup.WOOD).breakByHand(true)
    val stoneSettings = FabricBlockSettings.of(Material.STONE).strength(2.0f, 6.0f).sounds(BlockSoundGroup.STONE)
    val crushedSettings = FabricBlockSettings.of(Material.SAND).strength(0.6f).sounds(BlockSoundGroup.GRAVEL).breakByHand(true)
    val infestedLeavesSettings = FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS)

    val SIEVES: MutableMap<Identifier, SieveBlock> = mutableMapOf()
    val CRUCIBLES: MutableMap<Identifier, CrucibleBlock> = mutableMapOf()
    val BARRELS: MutableMap<Identifier, BarrelBlock> = mutableMapOf()

    val CRUSHED = mutableMapOf<Identifier, Block>(
        id("dust") to BaseFallingBlock(FabricBlockSettings.of(Material.SAND).strength(0.4f).breakByHand(true)),
        id("silt") to BaseFallingBlock(FabricBlockSettings.of(Material.SAND).strength(0.4f).breakByHand(true)),
        id("crushed_andesite") to BaseFallingBlock(crushedSettings),
        id("crushed_diorite") to BaseFallingBlock(crushedSettings),
        id("crushed_granite") to BaseFallingBlock(crushedSettings),
        id("crushed_prismarine") to BaseFallingBlock(crushedSettings),
        id("crushed_endstone") to BaseFallingBlock(crushedSettings),
        id("crushed_netherrack") to BaseFallingBlock(crushedSettings)
        //id("crushed_skystone") to BaseFallingBlock(crushedSettings)
    )

    val INFESTING_LEAVES = InfestingLeavesBlock(infestedLeavesSettings)
    val INFESTED_LEAVES: MutableMap<Identifier, InfestedLeavesBlock> = mutableMapOf()

    init {
        CRUCIBLES[id("stone_crucible")] = CrucibleBlock(id("block/stone_crucible"), id("porcelain"), stoneSettings)
        BARRELS[id("stone_barrel")] = BarrelBlock(Identifier("block/stone"), Identifier("stone"), Identifier("stone_slab"), stoneSettings)
    }

    fun registerBlocks(registry: Registry<Block>) {
        SIEVES.forEach { k, v -> Registry.register(registry, k, v) }
        CRUCIBLES.forEach { k, v -> Registry.register(registry, k, v) }
        BARRELS.forEach { k, v -> Registry.register(registry, k, v) }
        CRUSHED.forEach { k, v -> Registry.register(registry, k, v) }
        INFESTED_LEAVES.forEach { k, v -> Registry.register(registry, k, v) }

        Registry.register(registry, id("infesting_leaves"), INFESTING_LEAVES)

        // Fluid Blocks
        Registry.register(registry, id("witchwater"), WitchWaterFluid.Block)
    }

    fun registerBlockItems(registry: Registry<Item>) {
        SIEVES.forEach { (k, v) -> Registry.register(registry, k, BlockItem(v, itemSettings)) }
        CRUCIBLES.forEach { (k, v) -> Registry.register(registry, k, BlockItem(v, itemSettings)) }
        BARRELS.forEach { (k, v) -> Registry.register(registry, k, BlockItem(v, itemSettings)) }
        CRUSHED.forEach { (k, v) -> Registry.register(registry, k, BlockItem(v, itemSettings)) }
        INFESTED_LEAVES.forEach { (k, v) -> Registry.register(registry, k,
            InfestedLeavesItem(v, itemSettings)
        ) }
    }

    fun registerBlockEntities(registry: Registry<BlockEntityType<out BlockEntity>>) {
        Registry.register(registry, SieveBlockEntity.BLOCK_ENTITY_ID, SieveBlockEntity.TYPE)
        Registry.register(registry, CrucibleBlockEntity.BLOCK_ENTITY_ID, CrucibleBlockEntity.TYPE)
        Registry.register(registry, BarrelBlockEntity.BLOCK_ENTITY_ID, BarrelBlockEntity.TYPE)
        Registry.register(registry, InfestingLeavesBlockEntity.BLOCK_ENTITY_ID, InfestingLeavesBlockEntity.TYPE)
    }

    fun generateDerivedBlocks() {
        VanillaWoodDefinitions.values().forEach { wood ->
            val planksID = wood.getPlanksID()
            val slabID = wood.getSlabID()
            val logID = wood.getLogID()
            val leafBlock = wood.getLeafBlock() as LeavesBlock

            BLOCK_GENERATOR.createInfestedLeavesBlock(leafBlock)
            BLOCK_GENERATOR.createSieveBlock(planksID, slabID)
            BLOCK_GENERATOR.createWoodBarrelBlock(planksID, slabID)
            BLOCK_GENERATOR.createWoodCrucibleBlock(logID)
        }
    }
}