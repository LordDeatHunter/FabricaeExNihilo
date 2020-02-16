package exnihilofabrico.util

import exnihilofabrico.id
import me.shedaniel.rei.api.EntryStack
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

fun ItemStack.ofSize(num: Int = 1):ItemStack {
    if(num <= 0) return ItemStack.EMPTY
    val stack = this.copy()
    stack.count = num
    return stack
}

fun ItemConvertible.asStack(num: Int = 1) = ItemStack(this.asItem(), num)
fun ItemConvertible.asREIEntry() = EntryStack.create(this.asItem())
fun ItemStack.asREIEntry() = EntryStack.create(this)

fun getItemStack(identifier: Identifier) = Registry.ITEM[identifier].asStack()
fun getExNihiloItemStack(str: String) = getItemStack(id(str))
fun getExNihiloBlock(str: String) = Registry.BLOCK[id(str)]
fun getExNihiloItem(str: String) = Registry.ITEM[id(str)]

fun ItemStack.asEntity(world: World, x: Double, y: Double, z: Double) = ItemEntity(world, x, y, z, this)

fun ItemStack.asEntity(world: World, pos: BlockPos) = this.asEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

fun ItemStack.spawnAsEntity(world: World, pos: BlockPos) {
    world.spawnEntity(this.asEntity(world, pos))
}

fun World.spawnStack(pos: BlockPos, stack: ItemStack) {
    this.spawnEntity(stack.asEntity(this, pos))
}