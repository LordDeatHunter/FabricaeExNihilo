package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.util.Color
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import java.util.function.Predicate

data class CompostRecipe(val ingredient: ItemIngredient, val result: ItemStack, val amount: Double, val color: Color): Predicate<ItemStack> {
    override fun test(t: ItemStack) = ingredient.test(t)
    fun test(t: ItemConvertible) = ingredient.test(t)
}