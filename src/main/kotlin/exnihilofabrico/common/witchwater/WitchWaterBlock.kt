package exnihilofabrico.common.witchwater

import exnihilofabrico.ExNihiloConfig
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.common.base.BaseFluidBlock
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LightningEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.mob.MagmaCubeEntity
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.fluid.BaseFluid
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.village.VillagerProfession
import net.minecraft.world.World

class WitchWaterBlock(fluid: BaseFluid, settings: Settings): BaseFluidBlock(fluid, settings) {

    override fun onEntityCollision(state: BlockState?, world: World?, pos: BlockPos?, entity: Entity?) {
        if(world == null || entity == null || !entity.isAlive || entity.removed)
            return
        if(entity is LivingEntity){
            when(entity.type) {
                EntityType.SKELETON -> replaceMob(world, entity, EntityType.WITHER_SKELETON)
                EntityType.CREEPER -> {
                    if(!(entity as CreeperEntity).isCharged)
                        entity.onStruckByLightning(LightningEntity(world, entity.pos.x, entity.pos.y, entity.pos.z, true))
                    entity.health = entity.healthMaximum
                }
                EntityType.SLIME -> replaceMob(world, entity, EntityType.MAGMA_CUBE)
                EntityType.SPIDER -> replaceMob(world, entity, EntityType.CAVE_SPIDER)

                EntityType.COW -> replaceMob(world, entity, EntityType.MOOSHROOM)
                EntityType.PIG -> replaceMob(world, entity, EntityType.ZOMBIE_PIGMAN)
                EntityType.CHICKEN -> replaceMob(world, entity, EntityType.VEX)
                EntityType.SQUID -> replaceMob(world, entity, EntityType.GHAST)
                EntityType.PANDA, EntityType.POLAR_BEAR -> replaceMob(world, entity, EntityType.RAVAGER)
                EntityType.HORSE -> replaceMob(world, entity, EntityType.SKELETON_HORSE)
                EntityType.DONKEY, EntityType.MULE -> replaceMob(world, entity, EntityType.ZOMBIE_HORSE)
                EntityType.BAT, EntityType.PARROT -> replaceMob(world, entity, EntityType.PHANTOM)
                EntityType.RABBIT -> {
                    // Killer Rabbit.
                    if((entity as RabbitEntity).rabbitType != 99)
                        entity.rabbitType = 99
                }
                EntityType.TURTLE -> replaceMob(world, entity, EntityType.SHULKER)

                EntityType.PUFFERFISH -> replaceMob(world, entity, EntityType.GUARDIAN)
                EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.COD -> replaceMob(world, entity, EntityType.SILVERFISH)

                EntityType.VILLAGER -> {
                    when((entity as VillagerEntity).villagerData.profession) {
                        VillagerProfession.ARMORER -> replaceMob(world, entity,EntityType.PILLAGER)
                        VillagerProfession.BUTCHER -> replaceMob(world, entity,EntityType.VINDICATOR)
                        VillagerProfession.CARTOGRAPHER -> replaceMob(world, entity,EntityType.EVOKER)
                        VillagerProfession.CLERIC -> replaceMob(world, entity,EntityType.WITCH)
                        VillagerProfession.FARMER -> replaceMob(world, entity,EntityType.HUSK)
                        VillagerProfession.FISHERMAN -> replaceMob(world, entity,EntityType.DROWNED)
                        VillagerProfession.FLETCHER -> replaceMob(world, entity,EntityType.STRAY)
                        VillagerProfession.LEATHERWORKER -> replaceMob(world, entity,EntityType.PILLAGER)
                        VillagerProfession.LIBRARIAN -> replaceMob(world, entity,EntityType.ILLUSIONER)
                        VillagerProfession.MASON -> replaceMob(world, entity,EntityType.PILLAGER)
                        //VillagerProfession.NITWIT -> replaceMob(world, entity,EntityType.)
                        VillagerProfession.SHEPHERD -> replaceMob(world, entity,EntityType.PILLAGER)
                        VillagerProfession.TOOLSMITH -> replaceMob(world, entity,EntityType.PILLAGER)
                        VillagerProfession.WEAPONSMITH -> replaceMob(world, entity,EntityType.PILLAGER)
                        else -> replaceMob(world, entity, EntityType.ZOMBIE_VILLAGER)
                    }
                }
                EntityType.PLAYER -> {
                    if(ExNihiloConfig.Modules.WitchWater.Debuffs.blindness)
                        applyPotion(entity, StatusEffectInstance(StatusEffects.BLINDNESS, 210, 0))
                    if(ExNihiloConfig.Modules.WitchWater.Debuffs.hunger)
                        applyPotion(entity, StatusEffectInstance(StatusEffects.HUNGER, 210, 2))
                    if(ExNihiloConfig.Modules.WitchWater.Debuffs.slowness)
                    applyPotion(entity, StatusEffectInstance(StatusEffects.SLOWNESS, 210, 0))
                    if(ExNihiloConfig.Modules.WitchWater.Debuffs.weakness)
                        applyPotion(entity, StatusEffectInstance(StatusEffects.WEAKNESS, 210, 2))
                    if(ExNihiloConfig.Modules.WitchWater.Debuffs.wither)
                        applyPotion(entity, StatusEffectInstance(StatusEffects.WITHER, 210, 0))
                }
            }
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
        fun fluidInteraction(world: World, witchPos: BlockPos, otherPos: BlockPos): Boolean {
            val fluidState = world.getFluidState(otherPos)
            if(fluidState.isEmpty) return false
            val block = ExNihiloRegistries.WITCHWATER_WORLD.getResult(fluidState.fluid, world.random) ?: return false
            val changePos = if(witchPos.offset(Direction.DOWN) == otherPos) otherPos else witchPos
            world.setBlockState(changePos, block.defaultState)
            world.playSound(null, changePos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.7f,0.8f + world.random.nextFloat() * 0.2f)
            return true
        }
        fun <T: LivingEntity> replaceMob(world: World, toKill: LivingEntity, spawnType: EntityType<T>) {
            val toSpawn = spawnType.create(world)
            if(toSpawn != null) {
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
            if(toSpawn != null)
                world.spawnEntity(toSpawn)
        }
        fun applyPotion(entity: LivingEntity, potionEffect: StatusEffectInstance) {
            // Grab the potion effect on the entity (null if not active) compare its duration (defaulting to 0) to the new duration
            if(entity.activeStatusEffects[potionEffect.effectType]?.duration ?: 0 <= potionEffect.duration-20)
                entity.addPotionEffect(potionEffect)
        }
    }
}