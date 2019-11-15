package exnihilofabrico.api.crafting

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityCategory
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnType
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class EntityStack(val type: EntityType<*>, var size: Int = 1, var data: CompoundTag = CompoundTag()) {
    constructor(identifier: Identifier, size: Int, data: CompoundTag): this(Registry.ENTITY_TYPE[identifier], size, data)
    constructor(identifier: String, size: Int, data: CompoundTag): this(Identifier(identifier), size, data)
    constructor(tag: CompoundTag): this(tag.getString("type"), tag.getInt("size"), tag.getCompound("data"))

    fun isEmpty() = this == EMPTY || size == 0

    fun getCategory(): EntityCategory = type.category

    fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.putString("type",  Registry.ENTITY_TYPE.getId(type).toString())
        tag.putInt("size", size)
        tag.put("data", data)
        return tag
    }

    fun getEntity(world: World, pos: BlockPos, spawnType: SpawnType = SpawnType.SPAWNER): Entity? {
        return type.create(world, data, null, null, pos, spawnType, true, true)
    }

    companion object {
        val EMPTY = EntityStack(EntityType.PIG,0)
    }
}