package exnihilofabrico.util

import net.minecraft.block.Block
import net.minecraft.recipe.Ingredient
import net.minecraft.tag.Tag

fun IngredientfromBlockTag(tag: Tag<Block>) = Ingredient.ofStacks(*tag.values().map { it.asItem().defaultStack }.toTypedArray())