package exnihilofabrico.common

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.common.barrels.BarrelBlock
import exnihilofabrico.common.barrels.BarrelBlockEntity
import exnihilofabrico.common.base.BaseFallingBlock
import exnihilofabrico.common.crucibles.CrucibleBlock
import exnihilofabrico.common.crucibles.CrucibleBlockEntity
import exnihilofabrico.common.sieves.SieveBlock
import exnihilofabrico.common.sieves.SieveBlockEntity
import exnihilofabrico.id
import exnihilofabrico.util.EnumVanillaWoodTypes
import exnihilofabrico.util.strength
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
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

    val SIEVES: MutableMap<Identifier, Block> = EnumVanillaWoodTypes.values().map { id("sieve_${it.text}") to SieveBlock(it.getTexturePlanks(), woodSettings) }.toMap().toMutableMap()
    val CRUCIBLES: MutableMap<Identifier, Block> = EnumVanillaWoodTypes.values().map { id("crucible_${it.text}") to CrucibleBlock(it.getTextureLog(), woodSettings) }.toMap().toMutableMap()
    val BARRELS: MutableMap<Identifier, Block> = EnumVanillaWoodTypes.values().map { id("barrel_${it.text}") to BarrelBlock(it.getTexturePlanks(), woodSettings) }.toMap().toMutableMap()


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

    init {
        CRUCIBLES[id("crucible_stone")] = CrucibleBlock(id("textures/block/crucible_stone"), stoneSettings)
        BARRELS[id("barrel_stone")] = BarrelBlock(Identifier("textures/block/stone"), stoneSettings)
    }

    fun registerBlocks(registry: Registry<Block>) {
        SIEVES.forEach { (k, v) -> Registry.register(registry, k, v) }
        CRUCIBLES.forEach { (k, v) -> Registry.register(registry, k, v) }
        BARRELS.forEach { (k, v) -> Registry.register(registry, k, v) }
        CRUSHED.forEach { (k, v) -> Registry.register(registry, k, v) }
    }

    fun registerBlockItems(registry: Registry<Item>) {
        SIEVES.forEach { (k, v) -> Registry.register(registry, k, BlockItem(v, itemSettings)) }
        CRUCIBLES.forEach { (k, v) -> Registry.register(registry, k, BlockItem(v, itemSettings)) }
        BARRELS.forEach { (k, v) -> Registry.register(registry, k, BlockItem(v, itemSettings)) }
        CRUSHED.forEach { (k, v) -> Registry.register(registry, k, BlockItem(v, itemSettings)) }
    }

    fun registerBlockEntities(registry: Registry<BlockEntityType<out BlockEntity>>) {
        Registry.register(registry, SieveBlockEntity.BLOCK_ENTITY_ID, SieveBlockEntity.TYPE)
        Registry.register(registry, CrucibleBlockEntity.BLOCK_ENTITY_ID, CrucibleBlockEntity.TYPE)
        Registry.register(registry, BarrelBlockEntity.BLOCK_ENTITY_ID, BarrelBlockEntity.TYPE)
    }
}