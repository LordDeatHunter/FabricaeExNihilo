package exnihilofabrico.modules

import exnihilofabrico.id
import net.fabricmc.fabric.api.tag.TagRegistry

object ModTags {
    val CROOK_TAG = TagRegistry.item(id("crook"))
    val HAMMER_TAG = TagRegistry.item(id("hammer"))
    val INFESTED_LEAVES = TagRegistry.item(id("infested_leaves"))
    val INFESTED_LEAVES_BLOCK = TagRegistry.block(id("infested_leaves"))
}