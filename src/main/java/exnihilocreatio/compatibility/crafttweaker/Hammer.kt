package exnihilocreatio.compatibility.crafttweaker

import crafttweaker.IAction
import crafttweaker.api.item.IItemStack
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCBaseAdd
import exnihilocreatio.registries.manager.ExNihiloRegistryManager
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenClass("mods.exnihilocreatio.Hammer")
object Hammer {

    @ZenMethod
    fun removeAll() {
        CrTIntegration.removeActions.add(RemoveAll())
    }

    @ZenMethod
    fun addRecipe(block: IItemStack, drop: IItemStack, miningLevel: Int, chance: Float, fortuneChance: Float) {
        CrTIntegration.addActions.add(AddRecipe(block, drop, miningLevel, chance, fortuneChance))
    }

    private class RemoveAll : IAction {
        override fun apply() = ExNihiloRegistryManager.HAMMER_REGISTRY.clearRegistry()
        override fun describe() = "Removing all recipes for the Ex Nihilo Hammer."
    }

    private class AddRecipe internal constructor(private val blockIn: IItemStack, private val drop: IItemStack, private val miningLevel: Int, private val chance: Float, private val fortuneChance: Float) : ENCBaseAdd() {
        private val state: IBlockState?

        init {
            val hammerBlock = Block.getBlockFromItem((blockIn.internal as ItemStack).item)

            if (hammerBlock !== Blocks.AIR) {
                state = hammerBlock.getStateFromMeta(blockIn.metadata)
            } else {
                state = null
            }
        }

        override fun apply() {
            if (state == null) return

            ExNihiloRegistryManager.HAMMER_REGISTRY.register(state, drop.internal as ItemStack, miningLevel, chance, fortuneChance)
        }

        override fun describe(): String {
            return if (state == null) {
                "Can't add Hammer recipe for $blockIn as it has no Block"
            } else "Adding Hammer recipe for $state with drop $drop at a chance of $chance with mining level $miningLevel"
        }
    }
}
