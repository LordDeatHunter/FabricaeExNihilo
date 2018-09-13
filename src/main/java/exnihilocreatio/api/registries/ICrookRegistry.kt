package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.CrookReward
import exnihilocreatio.util.BlockInfo
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient

interface ICrookRegistry : IRegistryMappedList<Ingredient, CrookReward> {
    fun register(block: Block, meta: Int, reward: ItemStack, chance: Float, fortuneChance: Float)
    fun register(state: IBlockState, reward: ItemStack, chance: Float, fortuneChance: Float)
    fun register(info: BlockInfo, reward: ItemStack, chance: Float, fortuneChance: Float)
    fun register(name: String, reward: ItemStack, chance: Float, fortuneChance: Float)
    fun register(ingredient: Ingredient, reward: CrookReward)

    fun isRegistered(block: Block): Boolean
    fun getRewards(state: IBlockState): List<CrookReward>
}