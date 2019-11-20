package exnihilofabrico.modules.sieves

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries.SIEVE
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.base.BaseBlockEntity
import exnihilofabrico.util.ofSize
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.BucketItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import virtuoel.towelette.api.Fluidloggable
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs

class SieveBlockEntity: BaseBlockEntity(TYPE), BlockEntityClientSerializable {

    var mesh: ItemStack = ItemStack.EMPTY
    var contents: ItemStack = ItemStack.EMPTY
    var progress: Double = 0.0

    fun activate(state: BlockState?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {

        if(world?.isClient != false || player == null)
            return true

        val held = player.getStackInHand(hand ?: player.activeHand) ?: ItemStack.EMPTY

        if(held.item is BucketItem)
            return false // Done for fluid logging


        // Remove/Swap a mesh
        if(SIEVE.isValidMesh(held)) {
            // Removing  mesh
            if(!mesh.isEmpty) {
                player.giveItemStack(mesh.copy())
                mesh = ItemStack.EMPTY
            }
            // Add mesh
            if(SIEVE.isValidMesh(held)) {
                mesh = held.ofSize(1)
                if(!player.isCreative)
                    held.decrement(1)
            }
            markDirtyClient()
            return true
        }
        val sieves = getConnectedSieves()
        // Make Progress
        if(!contents.isEmpty) {
            sieves.forEach { it.doProgress(player) }
            return true
        }

        // Add a block
        if(!held.isEmpty && SIEVE.isValidRecipe(mesh, getFluid(), held)) {
            sieves.forEach { it.setContents(held, !player.isCreative) }
            return true
        }
        return false
    }

    fun setContents(stack: ItemStack, doDrain: Boolean) {
        if(stack.isEmpty || !contents.isEmpty) return
        contents = stack.ofSize(1)
        if(doDrain)
            stack.decrement(1)
        progress = 0.0
        markDirtyClient()
    }

    fun getFluid(): Fluid? {
        val state = world?.getBlockState(pos) ?: return null
        if(state.block !is Fluidloggable) return Fluids.EMPTY
        return state.fluidState.fluid
    }

    fun doProgress(player: PlayerEntity) {

        val efficiency = if(ExNihiloFabrico.config.modules.sieves.efficiency)
            EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, mesh)
        else
            0
        val haste = if(ExNihiloFabrico.config.modules.sieves.haste)
            (player.activeStatusEffects[StatusEffects.HASTE]?.amplifier ?: -1) + 1
        else
            0

        progress += ExNihiloFabrico.config.modules.sieves.baseProgress
                + ExNihiloFabrico.config.modules.sieves.efficiencyScaleFactor * efficiency
                + ExNihiloFabrico.config.modules.sieves.hasteScaleFactor * haste

        // TODO spawn some particles

        if(progress > 1.0) {
            SIEVE.getResult(mesh, getFluid(), contents, player, world?.random ?: return)
                .forEach {player.giveItemStack(it)}
            progress = 0.0
            contents = ItemStack.EMPTY
        }
        markDirtyClient()
    }

    fun dropInventory() {
        ItemScatterer.spawn(world, pos.up(), DefaultedList.copyOf(mesh, contents))
        mesh = ItemStack.EMPTY
        contents = ItemStack.EMPTY
    }

    fun dropMesh() {
        ItemScatterer.spawn(world, pos.up(), DefaultedList.copyOf(mesh))
        mesh = ItemStack.EMPTY
    }


    fun dropContents() {
        ItemScatterer.spawn(world, pos.up(), DefaultedList.copyOf(contents))
        contents = ItemStack.EMPTY
    }

    fun getConnectedSieves(): Collection<SieveBlockEntity> {
        val sieves = mutableListOf<SieveBlockEntity>()

        val tested = HashSet<BlockPos>()
        val stack = Stack<BlockPos>()
        stack.add(this.pos)
        while(!stack.empty()) {
            val popped = stack.pop()
            // Record that this one has been tested
            tested.add(popped)
            if(matchingSieveAt(world?: continue, popped)) {
                sieves.add(this.world?.getBlockEntity(popped) as? SieveBlockEntity ?: continue)
                // Add adjacent locations to test to the stack
                Direction.values()
                    // Horizontals
                    .filter{
                        it.offsetY == 0 }
                    // to BlockPos
                    .map { popped.offset(it) }
                    // Remove already tested positions
                    .filter { !tested.contains(it) && !stack.contains(it) }
                    // Remove positions too far away
                    .filter {
                        abs(this.pos.x - it.x) <= ExNihiloFabrico.config.modules.sieves.sieveRadius &&
                            abs(this.pos.z - it.z) <= ExNihiloFabrico.config.modules.sieves.sieveRadius }
                    // Add to the stack to be processed
                    .forEach { stack.add(it) }
            }
        }

        return sieves
    }

    private fun matchingSieveAt(world: World, pos: BlockPos): Boolean {
        val other = world.getBlockEntity(pos) as? SieveBlockEntity ?: return false
        return ItemStack.areItemsEqual(mesh, other.mesh)
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