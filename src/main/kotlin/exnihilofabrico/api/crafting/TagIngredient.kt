package exnihilofabrico.api.crafting

import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.tag.Tag
import java.util.function.Predicate

class TagIngredient<T: ItemConvertible>(val tag: Tag<T>): Predicate<ItemStack> {
    override fun test(input: ItemStack): Boolean {
        return tag.values().any { it.asItem() == input.item}
    }

    override fun toString(): String {
        return tag.id.toString()
    }
}