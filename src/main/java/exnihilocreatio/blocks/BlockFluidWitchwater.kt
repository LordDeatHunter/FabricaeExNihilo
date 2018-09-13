package exnihilocreatio.blocks

import exnihilocreatio.ModFluids
import exnihilocreatio.util.Data
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.monster.*
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.BlockFluidClassic

class BlockFluidWitchwater : BlockFluidClassic(ModFluids.fluidWitchwater, Material.WATER) {
    init {

        this.setRegistryName("witchwater")
        this.translationKey = "witchwater"

        Data.BLOCKS.add(this)
    }

    override fun onEntityCollision(world: World?, pos: BlockPos?, state: IBlockState?, entity: Entity?) {
        world ?: return
        entity ?: return

        if (world.isRemote || entity.isDead)
            return

        when (entity) {
            is EntitySkeleton -> replaceMob(world, entity, EntityWitherSkeleton(world))

            is EntityCreeper -> {
                if (!entity.powered) {
                    entity.onStruckByLightning(EntityLightningBolt(world, entity.posX, entity.posY, entity.posZ, true))
                    entity.health = entity.maxHealth
                }
            }

            is EntitySpider ->
                if (entity !is EntityCaveSpider)
                    replaceMob(world, entity, EntityCaveSpider(world))


            is EntitySquid -> replaceMob(world, entity, EntityGhast(world))

            is EntityVillager -> {
                val prof = entity.professionForge

                val spawnEntity = when (prof.registryName) {
                    PRIEST -> EntityWitch(world)
                    BUTCHER -> EntityVindicator(world)
                    LIBRARIAN -> EntityEvoker(world)
                    else -> EntityZombieVillager(world).apply { this.forgeProfession = prof }
                }

                replaceMob(world, entity, spawnEntity)
            }

            is EntityAnimal ->
                entity.onStruckByLightning(EntityLightningBolt(world, entity.posX, entity.posY, entity.posZ, true))


            is EntityPlayer -> {
                entity.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 210, 0))
                entity.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 210, 2))
                entity.addPotionEffect(PotionEffect(MobEffects.WITHER, 210, 0))
                entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 210, 0))
            }

        }
    }

    fun replaceMob(world: World, toKill: EntityLivingBase, toSpawn: EntityLivingBase) {
        toSpawn.setLocationAndAngles(toKill.posX, toKill.posY, toKill.posZ, toKill.rotationYaw, toKill.rotationPitch)
        toSpawn.renderYawOffset = toKill.renderYawOffset
        toSpawn.health = toSpawn.maxHealth

        toKill.setDead()
        world.spawnEntity(toSpawn)

    }

    companion object {
        private val PRIEST = ResourceLocation("minecraft:priest")
        private val BUTCHER = ResourceLocation("minecraft:butcher")
        private val LIBRARIAN = ResourceLocation("minecraft:librarian")
    }
}
