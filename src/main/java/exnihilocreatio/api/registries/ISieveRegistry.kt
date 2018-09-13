package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.Siftable
import exnihilocreatio.util.StackInfo
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import java.util.*

interface ISieveRegistry : IRegistryMappedList<Ingredient, Siftable> {

    fun register(itemStack: ItemStack, drop: StackInfo, chance: Float, meshLevel: Int)
    fun register(item: Item, meta: Int, drop: StackInfo, chance: Float, meshLevel: Int)
    fun register(item: StackInfo, drop: StackInfo, chance: Float, meshLevel: Int)
    fun register(block: Block, meta: Int, drop: StackInfo, chance: Float, meshLevel: Int)
    fun register(state: IBlockState, drop: StackInfo, chance: Float, meshLevel: Int)
    fun register(location: ResourceLocation, meta: Int, drop: StackInfo, chance: Float, meshLevel: Int)
    fun register(name: String, drop: StackInfo, chance: Float, meshLevel: Int)
    fun register(ingredient: Ingredient, drop: Siftable)

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param stack The block to get the sieve drops for
     * @return ArrayList of [Siftable]
     * that could *potentially* be dropped.
     */
    fun getDrops(stack: StackInfo): List<Siftable>

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param stack The ItemStack to get the sieve drops for
     * @return ArrayList of [Siftable]
     * that could *potentially* be dropped.
     */
    fun getDrops(stack: ItemStack): List<Siftable>
    fun getDrops(ingredient: Ingredient): List<Siftable>

    fun getRewardDrops(random: Random, block: IBlockState, meshLevel: Int, fortuneLevel: Int): List<ItemStack>
    fun canBeSifted(stack: ItemStack): Boolean
}
