package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.Meltable
import exnihilocreatio.util.StackInfo
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.fluids.Fluid

interface ICrucibleRegistry : IRegistryMap<Ingredient, Meltable> {
    fun register(item: StackInfo, fluid: Fluid, amount: Int)
    fun register(item: StackInfo, meltable: Meltable)
    fun register(stack: ItemStack, fluid: Fluid, amount: Int)
    fun register(stack: ItemStack, meltable: Meltable)
    fun register(name: String, fluid: Fluid, amount: Int)
    fun register(name: String, meltable: Meltable)

    fun canBeMelted(stack: ItemStack): Boolean
    fun canBeMelted(info: StackInfo): Boolean

    fun getMeltable(stack: ItemStack): Meltable
    fun getMeltable(info: StackInfo): Meltable
    fun getMeltable(item: Item, meta: Int): Meltable
}