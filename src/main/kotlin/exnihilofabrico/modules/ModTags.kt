package exnihilofabrico.modules

import exnihilofabrico.id
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.Tag

object ModTags {
    val CROOK_TAG: Tag<Item>? = TagRegistry.item(id("crook"))
    val HAMMER_TAG: Tag<Item>? = TagRegistry.item(id("hammer"))
    val INFESTED_LEAVES: Tag<Item>? = TagRegistry.item(id("infested_leaves"))
    val INFESTED_LEAVES_BLOCK: Tag<Block>? = TagRegistry.block(id("infested_leaves"))
}