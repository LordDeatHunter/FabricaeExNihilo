package exnihilofabrico.common

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.id
import exnihilofabrico.content.barrels.BarrelBlock
import exnihilofabrico.content.barrels.BarrelBlockEntity
import exnihilofabrico.content.crucibles.CrucibleBlock
import exnihilofabrico.content.crucibles.CrucibleBlockEntity
import exnihilofabrico.content.sieves.SieveBlock
import exnihilofabrico.content.sieves.SieveBlockEntity
import exnihilofabrico.util.EnumVanillaWoodTypes
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object ModBlocks {
    private val itemSettings = Item.Settings().itemGroup(ExNihiloFabrico.ITEM_GROUP).stackSize(64)

    val SIEVES: MutableMap<String, Block> = EnumVanillaWoodTypes.values().map {"sieve_${it.text}" to SieveBlock(it.getTexturePlanks()) }.toMap().toMutableMap()
    val CRUCIBLES: MutableMap<String, Block> = EnumVanillaWoodTypes.values().map {"crucible_${it.text}" to CrucibleBlock(it.getTextureLog()) }.toMap().toMutableMap()
    val BARRELS: MutableMap<String, Block> = EnumVanillaWoodTypes.values().map {"barrel_${it.text}" to BarrelBlock(it.getTexturePlanks()) }.toMap().toMutableMap()

    init {
        CRUCIBLES["crucible_stone"] = CrucibleBlock(id("textures/block/crucible_stone"), FabricBlockSettings.of(Material.STONE))
    }

    fun registerBlocks(registry: Registry<Block>) {
        for((name, block) in SIEVES.entries) {
            Registry.register(registry, id(name), block)
        }
        for((name, block) in CRUCIBLES.entries) {
            Registry.register(registry, id(name), block)
        }
        for((name, block) in BARRELS.entries) {
            Registry.register(registry, id(name), block)
        }
    }

    fun registerBlockItems(registry: Registry<Item>) {
        for((name, block) in SIEVES.entries) {
            Registry.register(registry, id(name), BlockItem(block, itemSettings))
        }
        for((name, block) in CRUCIBLES.entries) {
            Registry.register(registry, id(name), BlockItem(block, itemSettings))
        }
        for((name, block) in BARRELS.entries) {
            Registry.register(registry, id(name), BlockItem(block, itemSettings))
        }
    }

    fun registerBlockEntities(registry: Registry<BlockEntityType<out BlockEntity>>) {
        Registry.register(registry, SieveBlockEntity.BLOCK_ENTITY_ID, SieveBlockEntity.TYPE)
        Registry.register(registry, CrucibleBlockEntity.BLOCK_ENTITY_ID, CrucibleBlockEntity.TYPE)
        Registry.register(registry, BarrelBlockEntity.BLOCK_ENTITY_ID, BarrelBlockEntity.TYPE)
    }
}