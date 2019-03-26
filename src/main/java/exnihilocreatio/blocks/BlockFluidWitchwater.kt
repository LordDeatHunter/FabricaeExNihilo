package exnihilocreatio.blocks

import exnihilocreatio.ModFluids
import exnihilocreatio.config.ModConfig
import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.Data
import net.minecraft.block.Block
import net.minecraft.block.BlockLiquid
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.monster.*
import net.minecraft.entity.passive.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.potion.PotionEffect
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.BlockFluidBase
import net.minecraftforge.fluids.BlockFluidClassic
import net.minecraftforge.fluids.FluidRegistry

class BlockFluidWitchwater : BlockFluidClassic(ModFluids.fluidWitchwater, Material.WATER) {
    private val coldMixes: List<IBlockState>
    private val hotMixes: List<IBlockState>
    init {

        this.setRegistryName("witchwater")
        this.translationKey = "witchwater"

        coldMixes = ModConfig.witchwater.coldMixing.map { BlockInfo(it).blockState }
        hotMixes = ModConfig.witchwater.hotMixing.map { BlockInfo(it).blockState }

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

            is EntitySlime ->
                if (entity !is EntityMagmaCube)
                    replaceMob(world, entity, EntityMagmaCube(world))

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

            is EntityCow ->
                if (entity !is EntityMooshroom)
                    replaceMob(world, entity, EntityMooshroom(world))

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

    private fun replaceMob(world: World, toKill: EntityLivingBase, toSpawn: EntityLivingBase) {
        toSpawn.setLocationAndAngles(toKill.posX, toKill.posY, toKill.posZ, toKill.rotationYaw, toKill.rotationPitch)
        toSpawn.renderYawOffset = toKill.renderYawOffset
        toSpawn.health = toSpawn.maxHealth * toKill.health / toKill.maxHealth

        toKill.setDead()
        world.spawnEntity(toSpawn)
    }

    companion object {
        private val PRIEST = ResourceLocation("minecraft:priest")
        private val BUTCHER = ResourceLocation("minecraft:butcher")
        private val LIBRARIAN = ResourceLocation("minecraft:librarian")
    }

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun neighborChanged(state: IBlockState, world: World, pos: BlockPos, neighborBlock: Block, neighbourPos: BlockPos) {
        super.neighborChanged(state, world, pos, neighborBlock, neighbourPos)

        interactWithAdjacent(world, pos)
    }

    override fun onBlockAdded(world: World, pos: BlockPos, state: IBlockState) {
        super.onBlockAdded(world, pos, state)

        interactWithAdjacent(world, pos)
    }

    private fun interactWithAdjacent(world: World, pos: BlockPos) {
        if(!ModConfig.witchwater.enableWitchWaterBlockForming)
            return
        var shouldCreateBlock = false
        var isCold = true
        for(side in EnumFacing.VALUES) {
            if(side == EnumFacing.DOWN)
                continue
            val offset = world.getBlockState(pos.offset(side))
            if(offset.material.isLiquid && offset.block !is BlockFluidWitchwater &&
                    (offset.block is BlockFluidBase || offset.block is BlockLiquid)) {
                shouldCreateBlock = true
                isCold = when(offset.block) {
                    is BlockFluidBase -> (offset.block as BlockFluidBase).temperature
                    else -> when(offset.material) {
                        Material.LAVA -> FluidRegistry.LAVA.temperature
                        else -> FluidRegistry.WATER.temperature
                    }
                } <= ModConfig.witchwater.coldCutoff
                break
            }
        }
        if(shouldCreateBlock) {
            val newState =
                    if(isCold)
                        coldMixes[world.rand.nextInt(coldMixes.size)]
                    else
                        hotMixes[world.rand.nextInt(hotMixes.size)]
            world.setBlockState(pos, newState)

            val sound = if(isCold) SoundEvents.BLOCK_GRAVEL_BREAK else SoundEvents.BLOCK_STONE_BREAK
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f)

            for(i in 0 until 10) {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.x + world.rand.nextDouble(), pos.y + world.rand.nextDouble(), pos.z + world.rand.nextDouble(), 0.0, 0.0, 0.0)
            }
        }
    }
}
