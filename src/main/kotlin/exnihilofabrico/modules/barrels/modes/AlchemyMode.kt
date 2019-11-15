package exnihilofabrico.modules.barrels.modes

import exnihilofabrico.api.crafting.EntityStack
import net.minecraft.nbt.CompoundTag

data class AlchemyMode(val before: BarrelMode = EmptyMode(),
                       val after: BarrelMode = EmptyMode(),
                       val toSpawn: EntityStack = EntityStack.EMPTY,
                       var countdown: Int = 0): BarrelMode {

    override fun tagKey() = "alchemy_mode"

    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put("before", before.toTag())
        tag.put("after", after.toTag())
        tag.put("toSpawn", toSpawn.toTag())
        tag.putInt("countdown", countdown)
        return tag
    }

    companion object {
        fun fromTag(tag: CompoundTag) =
            AlchemyMode(
                BarrelModeFactory(tag.getCompound("before")),
                BarrelModeFactory(tag.getCompound("after")),
                EntityStack(tag.getCompound("toSpawn")),
                tag.getInt("countdown"))
    }
}