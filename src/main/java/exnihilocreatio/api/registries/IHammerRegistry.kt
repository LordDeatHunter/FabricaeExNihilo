package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.HammerReward
import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.StackInfo
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import java.util.*

interface IHammerRegistry : IRegistryMappedList<Ingredient, HammerReward> {

    /**
     * Adds a new Hammer recipe for use with Ex Nihilo hammers.
     *
     * @param state         The blocks state to add
     * @param reward        Reward
     * @param miningLevel   Mining level of hammer. 0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond. Can be higher, but will need corresponding tool material.
     * @param chance        Chance of drop
     * @param fortuneChance Chance of drop per level of fortune
     */
    fun register(state: IBlockState, reward: ItemStack, miningLevel: Int, chance: Float, fortuneChance: Float)

    fun register(block: Block, meta: Int, reward: ItemStack, miningLevel: Int, chance: Float, fortuneChance: Float)
    fun register(stackInfo: StackInfo, reward: ItemStack, miningLevel: Int, chance: Float, fortuneChance: Float)
    fun register(stack: ItemStack, reward: HammerReward)
    fun register(name: String, reward: ItemStack, miningLevel: Int, chance: Float, fortuneChance: Float)
    fun register(ingredient: Ingredient, reward: HammerReward)

    fun getRewardDrops(random: Random, block: IBlockState, miningLevel: Int, fortuneLevel: Int): List<ItemStack>

    fun getRewards(block: IBlockState): List<HammerReward>
    fun getRewards(block: Block, meta: Int): List<HammerReward>
    fun getRewards(stackInfo: BlockInfo): List<HammerReward>
    fun getRewards(ingredient: Ingredient): List<HammerReward>

    fun isRegistered(block: IBlockState): Boolean
    fun isRegistered(block: Block): Boolean
    fun isRegistered(stackInfo: BlockInfo): Boolean
}
