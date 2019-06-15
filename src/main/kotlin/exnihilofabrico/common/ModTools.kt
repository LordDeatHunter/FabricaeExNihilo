package exnihilofabrico.common

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.common.materials.ModToolMaterials
import exnihilofabrico.common.tools.CrookTool
import exnihilofabrico.common.tools.HammerTool
import exnihilofabrico.id
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModTools {
    val tool_settings: Item.Settings = Item.Settings().itemGroup(ExNihiloFabrico.ITEM_GROUP).stackSize(1)

    val CROOKS = ModToolMaterials.values().map {
        id("crook_${it.name.toLowerCase()}") to CrookTool(it, tool_settings)
    }.toMap()

    val HAMMERS = mapOf<Identifier, Item>(
        id("hammer_wood") to HammerTool(ToolMaterials.WOOD, tool_settings),
        id("hammer_stone") to HammerTool(ToolMaterials.STONE, tool_settings),
        id("hammer_iron") to HammerTool(ToolMaterials.IRON, tool_settings),
        id("hammer_gold") to HammerTool(ToolMaterials.GOLD, tool_settings),
        id("hammer_diamond") to HammerTool(ToolMaterials.DIAMOND, tool_settings)
    )

    fun registerItems(registry: Registry<Item>) {
        CROOKS.forEach { (k, v) -> Registry.register(registry, k, v) }
        HAMMERS.forEach { (k, v) -> Registry.register(registry, k, v) }
    }
}