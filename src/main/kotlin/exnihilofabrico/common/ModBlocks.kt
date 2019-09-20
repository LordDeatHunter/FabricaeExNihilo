package exnihilofabrico.common

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.common.barrels.BarrelBlock
import exnihilofabrico.common.barrels.BarrelBlockEntity
import exnihilofabrico.common.base.BaseFallingBlock
import exnihilofabrico.common.crucibles.CrucibleBlock
import exnihilofabrico.common.crucibles.CrucibleBlockEntity
import exnihilofabrico.common.farming.InfestedLeavesBlock
import exnihilofabrico.common.farming.InfestedLeavesItem
import exnihilofabrico.common.farming.InfestingLeavesBlock
import exnihilofabrico.common.farming.InfestingLeavesBlockEntity
import exnihilofabrico.common.sieves.SieveBlock
import exnihilofabrico.common.sieves.SieveBlockEntity
import exnihilofabrico.common.witchwater.WitchWaterFluid
import exnihilofabrico.id
import exnihilofabrico.util.EnumVanillaWoodTypes
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

    private val woodSettings = FabricBlockSettings.of(Material.WOOD).strength(2.0f).sounds(BlockSoundGroup.WOOD).breakByHand(true)
    private val stoneSettings = FabricBlockSettings.of(Material.STONE).strength(2.0f, 6.0f).sounds(BlockSoundGroup.STONE)
    private val crushedSettings = FabricBlockSettings.of(Material.SAND).strength(0.6f).sounds(BlockSoundGroup.GRAVEL).breakByHand(true)
    private val infestedLeavesSettings = FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS)

    val SIEVES: MutableMap<Identifier, SieveBlock> = EnumVanillaWoodTypes.values().map {
        id("sieve_${it.text}") to SieveBlock(it.getTexturePlanks(), Registry.ITEM.getId(it.getPlanksBlock().asItem()), Registry.ITEM.getId(it.getSlabBlock().asItem()), woodSettings)
    }.toMap().toMutableMap()
    val CRUCIBLES: MutableMap<Identifier, CrucibleBlock> = EnumVanillaWoodTypes.values().map {
        id("crucible_${it.text}") to CrucibleBlock(it.getTextureLog(), Registry.ITEM.getId(it.getLogBlock().asItem()), woodSettings)
    }.toMap().toMutableMap()
    val BARRELS: MutableMap<Identifier, BarrelBlock> = EnumVanillaWoodTypes.values().map {
        id("barrel_${it.text}") to BarrelBlock(it.getTexturePlanks(), Registry.ITEM.getId(it.getPlanksBlock().asItem()), Registry.ITEM.getId(it.getSlabBlock().asItem()), woodSettings)
    }.toMap().toMutableMap()


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
    val INFESTED_LEAVES = EnumVanillaWoodTypes.values().map { w ->
        id("infested_${w.text}_leaves") to InfestedLeavesBlock(w.getLeafBlock() as LeavesBlock, infestedLeavesSettings)
    }.toMap().toMutableMap()

    init {
        CRUCIBLES[id("crucible_stone")] = CrucibleBlock(id("block/crucible_stone"), Identifier("stone"), stoneSettings)
        BARRELS[id("barrel_stone")] = BarrelBlock(Identifier("block/stone"), Identifier("stone"), Identifier("stone_slab"), stoneSettings)
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
        INFESTED_LEAVES.forEach { (k, v) -> Registry.register(registry, k, InfestedLeavesItem(v, itemSettings)) }
    }

    fun registerBlockEntities(registry: Registry<BlockEntityType<out BlockEntity>>) {
        Registry.register(registry, SieveBlockEntity.BLOCK_ENTITY_ID, SieveBlockEntity.TYPE)
        Registry.register(registry, CrucibleBlockEntity.BLOCK_ENTITY_ID, CrucibleBlockEntity.TYPE)
        Registry.register(registry, BarrelBlockEntity.BLOCK_ENTITY_ID, BarrelBlockEntity.TYPE)
        Registry.register(registry, InfestingLeavesBlockEntity.BLOCK_ENTITY_ID, InfestingLeavesBlockEntity.TYPE)
    }
}