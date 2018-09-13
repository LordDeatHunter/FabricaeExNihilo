package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.Compostable
import exnihilocreatio.texturing.Color
import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.StackInfo
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation

interface ICompostRegistry : IRegistryMap<Ingredient, Compostable> {
    fun register(itemStack: ItemStack, value: Float, state: BlockInfo, color: Color)
    fun register(item: Item?, meta: Int, value: Float, state: BlockInfo, color: Color)
    fun register(block: Block, meta: Int, value: Float, state: BlockInfo, color: Color)
    fun register(item: StackInfo, value: Float, state: BlockInfo, color: Color)
    fun register(location: ResourceLocation, meta: Int, value: Float, state: BlockInfo, color: Color)
    fun register(name: String, value: Float, state: BlockInfo, color: Color)

    /**
     * Registers a oredict for sifting with a dynamic color based on the itemColor
     */
    fun register(name: String, value: Float, state: BlockInfo)

    fun getItem(item: Item, meta: Int): Compostable
    fun getItem(stack: ItemStack): Compostable
    fun getItem(info: StackInfo): Compostable

    fun containsItem(item: Item, meta: Int): Boolean
    fun containsItem(stack: ItemStack): Boolean
    fun containsItem(info: StackInfo): Boolean
}