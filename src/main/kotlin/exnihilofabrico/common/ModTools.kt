package exnihilofabrico.common

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.common.tools.CrookTool
import exnihilofabrico.common.tools.HammerTool
import exnihilofabrico.id
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModTools {
    val tool_settings: Item.Settings = Item.Settings().itemGroup(ExNihiloFabrico.ITEM_GROUP).stackSize(1)

    val CROOKS = mapOf<Identifier, Item>(
        id("crook_wood") to CrookTool(ToolMaterials.WOOD, tool_settings),
        id("crook_andesite") to CrookTool(ToolMaterials.STONE, tool_settings),
        id("crook_granite") to CrookTool(ToolMaterials.STONE, tool_settings),
        id("crook_diorite") to CrookTool(ToolMaterials.STONE, tool_settings),
        id("crook_bone") to CrookTool(ToolMaterials.STONE, tool_settings),
        id("crook_stone") to CrookTool(ToolMaterials.STONE, tool_settings)
    )

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