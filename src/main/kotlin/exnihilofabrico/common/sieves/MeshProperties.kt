package exnihilofabrico.common.sieves

import exnihilofabrico.util.Color
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier

data class MeshProperties(val identifier: Identifier,
                          val displayName: String,
                          val color: Color,
                          val keyIngredient: Ingredient,
                          val item: MeshItem = MeshItem(color, displayName))