package exnihilofabrico.util

import net.minecraft.entity.EntityType
import net.minecraft.util.registry.Registry

fun EntityType<*>.getID() = Registry.ENTITY_TYPE.getId(this)