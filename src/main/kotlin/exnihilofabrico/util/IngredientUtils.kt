package exnihilofabrico.util

import net.minecraft.block.Block
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.tag.Tag
import java.util.function.Predicate

fun IngredientfromBlockTag(tag: Tag<Block>) = Ingredient.ofStacks(*tag.values().map { it.asItem().asStack() }.toTypedArray())
fun Predicate<ItemStack>.test(item: ItemConvertible) = this.test(item.asStack())