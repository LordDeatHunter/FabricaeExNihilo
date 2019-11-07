package exnihilofabrico.modules.barrels

import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.recipes.BarrelAlchemyRecipe
import exnihilofabrico.modules.base.NBTSerializable
import exnihilofabrico.modules.fluid.FluidInstance
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

interface BarrelMode {
    fun toTag(): CompoundTag
}

class EmptyMode: BarrelMode {
    override fun toTag() = CompoundTag()
}

data class ItemMode(val stack: ItemStack): BarrelMode {
    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put("item", stack.toTag(CompoundTag()))
        return tag
    }
    companion object {
        fun fromTag(tag: CompoundTag) = ItemMode(ItemStack.fromTag(tag.getCompound("item")))
    }
}

data class FluidMode(val fluid: FluidInstance): BarrelMode{
    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put("fluid", fluid.toTag())
        return tag
    }
    companion object {
        fun fromTag(tag: CompoundTag) = FluidMode(FluidInstance.create(tag.getCompound("fluid")))
    }
}

data class AlchemyMode(val before: BarrelMode, val after: BarrelMode, val toSpawn: EntityStack, var progress: Double): BarrelMode {
    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put("before", before.toTag())
        tag.put("after", after.toTag())
        tag.put("toSpawn", toSpawn.toTag())
        tag.putDouble("progress", progress)
        return tag
    }
    companion object {
        fun fromTag(tag: CompoundTag) =
            AlchemyMode(
                BarrelModeFactory(tag.getCompound("before")),
                BarrelModeFactory(tag.getCompound("after")),
                EntityStack(tag.getCompound("toSpawn")),
                tag.getDouble("progress")
            )
    }
}

fun BarrelModeFactory(tag: CompoundTag): BarrelMode {
    return if(tag.containsKey("item"))
        ItemMode.fromTag(tag.getCompound("item"))
    else if(tag.containsKey("fluid"))
        FluidMode.fromTag(tag.getCompound("fluid"))
    else if(tag.containsKey("alchemy"))
        AlchemyMode.fromTag(tag.getCompound("alchemy"))
    else
        EmptyMode()
}