package exnihilocreatio.blocks

import exnihilocreatio.ModFluids
import exnihilocreatio.util.Data
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
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
            is EntitySkeleton -> {
                entity.setDead()

                val witherSkeleton = EntityWitherSkeleton(world)
                witherSkeleton.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch)
                witherSkeleton.renderYawOffset = entity.renderYawOffset
                witherSkeleton.health = witherSkeleton.maxHealth

                world.spawnEntity(witherSkeleton)
            }

            is EntityCreeper -> {
                if (!entity.powered) {
                    entity.onStruckByLightning(EntityLightningBolt(world, entity.posX, entity.posY, entity.posZ, true))
                    entity.health = entity.maxHealth
                }
            }

            is EntitySpider -> {
                if (entity !is EntityCaveSpider) {
                    entity.setDead()

                    val caveSpider = EntityCaveSpider(world)
                    caveSpider.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch)
                    caveSpider.renderYawOffset = entity.renderYawOffset
                    caveSpider.health = caveSpider.maxHealth

                    world.spawnEntity(caveSpider)
                }
            }

            is EntitySquid -> {
                entity.setDead()

                val ghast = EntityGhast(world)
                ghast.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch)
                ghast.renderYawOffset = entity.renderYawOffset
                ghast.health = ghast.maxHealth

                world.spawnEntity(ghast)
            }

            is EntityVillager -> {
                val prof = entity.professionForge

                val spawnEntity = when (prof.registryName) {
                    PRIEST -> EntityWitch(world)
                    BUTCHER -> EntityVindicator(world)
                    LIBRARIAN -> EntityEvoker(world)
                    else -> EntityZombieVillager(world).apply { this.forgeProfession = prof }
                }

                entity.setDead()
                spawnEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch)
                spawnEntity.renderYawOffset = entity.renderYawOffset
                spawnEntity.health = spawnEntity.maxHealth

                world.spawnEntity(spawnEntity)
            }

            is EntityAnimal -> {
                entity.onStruckByLightning(EntityLightningBolt(world, entity.posX, entity.posY, entity.posZ, true))
            }

            is EntityPlayer -> {
                entity.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 210, 0))
                entity.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 210, 2))
                entity.addPotionEffect(PotionEffect(MobEffects.WITHER, 210, 0))
                entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 210, 0))
            }

        }
    }

    companion object {
        private val PRIEST = ResourceLocation("minecraft:priest")
        private val BUTCHER = ResourceLocation("minecraft:butcher")
        private val LIBRARIAN = ResourceLocation("minecraft:librarian")
    }
}
