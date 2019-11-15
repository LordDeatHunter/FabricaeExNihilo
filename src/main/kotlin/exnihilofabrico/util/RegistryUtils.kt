package exnihilofabrico.util

import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.village.VillagerProfession

fun Item.getId() = Registry.ITEM.getId(this)
fun Fluid.getId() = Registry.FLUID.getId(this)
fun Block.getId() = Registry.BLOCK.getId(this)


fun StatusEffect.getId() = Registry.STATUS_EFFECT.getId(this)
fun EntityType<out Entity>.getId(): Identifier? = EntityType.getId(this)
fun VillagerProfession.getId() = Registry.VILLAGER_PROFESSION.getId(this)