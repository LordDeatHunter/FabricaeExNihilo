package exnihilofabrico.modules

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.id
import exnihilofabrico.modules.materials.ModToolMaterials
import exnihilofabrico.modules.tools.CrookTool
import exnihilofabrico.modules.tools.HammerTool
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModTools {
    val tool_settings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(1)

    val CROOKS = ModToolMaterials.values().map {
        id("crook_${it.name.toLowerCase()}") to
                CrookTool(it, Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(1).maxDamage(it.durability))
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