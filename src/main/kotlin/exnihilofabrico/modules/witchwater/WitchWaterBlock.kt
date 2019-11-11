package exnihilofabrico.modules.witchwater

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.api.registry.ExNihiloRegistries.WITCHWATER_ENTITY
import exnihilofabrico.modules.ModEffects
import exnihilofabrico.modules.base.BaseFluidBlock
import exnihilofabrico.util.getId
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LightningEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.mob.MagmaCubeEntity
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.fluid.BaseFluid
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class WitchWaterBlock(fluid: BaseFluid, settings: Settings): BaseFluidBlock(fluid, settings) {

    override fun onEntityCollision(state: BlockState?, world: World?, pos: BlockPos?, entity: Entity?) {
        if(world == null || entity == null || !entity.isAlive || entity.removed)
            return
        if(entity is LivingEntity && !isMarked(entity)) {
            when(entity.type) {
                EntityType.CREEPER -> {
                    markEntity(entity)
                    if(!(entity as CreeperEntity).isCharged)
                        entity.onStruckByLightning(LightningEntity(world, entity.pos.x, entity.pos.y, entity.pos.z, true))
                    entity.health = entity.healthMaximum
                    return
                }
                EntityType.RABBIT -> {
                    markEntity(entity)
                    // Killer Rabbit.
                    if((entity as RabbitEntity).rabbitType != 99)
                        entity.rabbitType = 99
                    return
                }
                EntityType.PLAYER -> {
                    ExNihiloFabrico.config.modules.witchwater.effects.forEach { effect, durationLevel ->
                        applyStatusEffect(entity,
                            StatusEffectInstance(Registry.STATUS_EFFECT[effect],
                                durationLevel.first,
                                durationLevel.second
                            )
                        )
                    }
                    return
                }
            }
            val toSpawn = WITCHWATER_ENTITY.getSpawn(entity)
            if(toSpawn != null) {
                replaceMob(world, entity, toSpawn)
                return
            }
            markEntity(entity)
            return
        }
        else
            when(entity.type) {
                EntityType.ARROW -> {
                    // Replace arrows with shulker bullets
                    val bullet = EntityType.SHULKER_BULLET.create(world)
                    bullet?.velocity = entity.velocity
                    bullet?.setPositionAndAngles(entity.blockPos, entity.yaw, entity.pitch)
                    replaceMob(world, entity, bullet)
                }
                // TODO item changes
            }
    }

    override fun receiveNeighborFluids(world: World?, pos: BlockPos?, state: BlockState?): Boolean {
        if(world == null || pos == null || state == null)
            return true
        for(direction in Direction.values()) {
            val fluidState = world.getFluidState(pos.offset(direction))
            if(fluidState.isEmpty) continue
            if(fluidInteraction(world, pos, pos.offset(direction)) && direction != Direction.DOWN)
                return true
        }
        return true
    }

    companion object {
        /**
         * A status effect is used to mark entities that have been processed so that they are no longer processed.
         */
        fun markEntity(entity: LivingEntity) = applyStatusEffect(entity, WitchWaterStatusEffect.getInstance())
        fun isMarked(entity: LivingEntity) =  entity.hasStatusEffect(ModEffects.WITCHWATERED)


        fun fluidInteraction(world: World, witchPos: BlockPos, otherPos: BlockPos): Boolean {
            val fluidState = world.getFluidState(otherPos)
            if(fluidState.isEmpty) return false
            val block = ExNihiloRegistries.WITCHWATER_WORLD.getResult(fluidState.fluid, world.random) ?: return false
            val changePos = if(witchPos.offset(Direction.DOWN) == otherPos) otherPos else witchPos
            world.setBlockState(changePos, block.defaultState)
            world.playSound(null, changePos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.7f,0.8f + world.random.nextFloat() * 0.2f)
            return true
        }
        fun replaceMob(world: World, toKill: LivingEntity, spawnType: EntityType<*>) {
            val toSpawn = spawnType.create(world)
            if(toSpawn is LivingEntity) {
                // Set postion and angles
                toSpawn.setPositionAndAngles(toKill.blockPos, toKill.yaw, toKill.pitch)
                toSpawn.velocity = toKill.velocity
                toSpawn.headYaw = toKill.headYaw

                // Slime -> Magma Slime
                if(toKill is SlimeEntity && toSpawn is MagmaCubeEntity) {
                    //TODO mixin for setting slime size
                }

                // Set Health
                toSpawn.health = toSpawn.healthMaximum * toKill.health / toKill.healthMaximum
            }
            replaceMob(world, toKill, toSpawn)
        }
        fun replaceMob(world: World, toKill: Entity, toSpawn: Entity?) {
            toKill.remove()
            if(toSpawn != null) {
                if(toSpawn is LivingEntity)
                    markEntity(toSpawn)
                world.spawnEntity(toSpawn)
            }
        }
        fun applyStatusEffect(entity: LivingEntity, statusEffect: StatusEffectInstance) {
            // Grab the potion effect on the entity (null if not active) compare its duration (defaulting to 0) to the new duration
            if(entity.activeStatusEffects[statusEffect.effectType]?.duration ?: Int.MIN_VALUE <= statusEffect.duration-20)
                entity.addPotionEffect(statusEffect)
        }
    }
}