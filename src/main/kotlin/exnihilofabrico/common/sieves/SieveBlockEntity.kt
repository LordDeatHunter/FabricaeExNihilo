package exnihilofabrico.common.sieves

import exnihilofabrico.ExNihiloConfig
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries.SIEVE
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.id
import exnihilofabrico.util.ofSize
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.BucketItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import virtuoel.towelette.api.Fluidloggable

class SieveBlockEntity: BlockEntity(TYPE), BlockEntityClientSerializable {

    var mesh: ItemStack = ItemStack.EMPTY
    var contents: ItemStack = ItemStack.EMPTY
    var progress: Double = 0.0

    override fun markDirty() {
        super.markDirty()

        world?.apply {
            updateListeners(pos, getBlockState(pos), getBlockState(pos), 3)
        }
    }

    fun activate(state: BlockState?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {

        if(world?.isClient != false || player == null)
            return true

        val held = player.getStackInHand(hand ?: player.activeHand) ?: ItemStack.EMPTY

        if(held.item is BucketItem)
            return false // Done for fluid logging

        // Remove/Swap a mesh
        if(player.isSneaking || SIEVE.isValidMesh(held)) {
            // Removing  mesh
            if(!mesh.isEmpty) {
                player.giveItemStack(mesh.copy())
                mesh = ItemStack.EMPTY
            }
            // Add mesh
            if(SIEVE.isValidMesh(held)) {
                mesh = held.ofSize(1)
                if(!player.isCreative)
                    held.subtractAmount(1)
            }
            markDirty()
            return true
        }
        // Make Progress
        if(!contents.isEmpty) {
            doProgress(player)
            markDirty()
            return true
        }
        // Add a block
        if(!held.isEmpty && SIEVE.isValidRecipe(mesh, getFluid(), held)) { // TODO use sieve registry to determine if something is sievable
            contents = held.ofSize(1)
            if(!player.isCreative)
                held.subtractAmount(1)
            markDirty()
            return true
        }
        return true
    }

    fun getFluid(): Fluid? {
        val state = world?.getBlockState(pos) ?: return null
        if(state.block !is Fluidloggable) return null
        return state.fluidState.fluid
    }

    fun doProgress(player: PlayerEntity) {
        val efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, mesh)
        val haste = (player.activeStatusEffects[StatusEffects.HASTE]?.amplifier ?: -1) + 1

        progress += ExNihiloConfig.Modules.Sieve.Progress.baseProgress
                + ExNihiloConfig.Modules.Sieve.Progress.efficiencyScaleFactor * efficiency
                + ExNihiloConfig.Modules.Sieve.Progress.hasteScaleFactor * haste

        // TODO spawn some particles

        if(progress > 1.0) {
            SIEVE.getResult(mesh, getFluid(), contents, player, world?.random ?: return)
                .forEach {player.giveItemStack(it)}
            progress = 0.0
            contents = ItemStack.EMPTY
        }
    }

    fun dropInventory() {
        ItemScatterer.spawn(world, pos.up(), DefaultedList.create(mesh, contents))
        mesh = ItemStack.EMPTY
        contents = ItemStack.EMPTY
    }

    fun dropMesh() {
        ItemScatterer.spawn(world, pos.up(), DefaultedList.create(mesh))
        mesh = ItemStack.EMPTY
    }


    fun dropContents() {
        ItemScatterer.spawn(world, pos.up(), DefaultedList.create(contents))
        contents = ItemStack.EMPTY
    }

    /**
     * NBT Serialization section
     */

    override fun toTag(tag: CompoundTag) = toTagWithoutWorldInfo(super.toTag(tag))

    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        if(tag==null){
            ExNihiloFabrico.LOGGER.warn("A sieve at $pos is missing data.")
            return
        }
        fromTagWithoutWorldInfo(tag)
    }

    override fun toClientTag(tag: CompoundTag?) = toTag(tag ?: CompoundTag())
    override fun fromClientTag(tag: CompoundTag?) = fromTag(tag ?: CompoundTag())

    fun toTagWithoutWorldInfo(tag: CompoundTag): CompoundTag {
        tag.put("mesh", mesh.toTag(CompoundTag()))
        tag.put("contents", contents.toTag(CompoundTag()))
        tag.putDouble("progress", progress)
        return tag
    }

    fun fromTagWithoutWorldInfo(tag: CompoundTag) {
        mesh = ItemStack.fromTag(tag.getCompound("mesh"))
        contents = ItemStack.fromTag(tag.getCompound("contents"))
        progress = tag.getDouble("progress")
    }

    companion object {
        @JvmStatic
        val TYPE: BlockEntityType<SieveBlockEntity> =
                BlockEntityType.Builder.create({SieveBlockEntity()}, ModBlocks.SIEVES.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("sieve")
    }
}