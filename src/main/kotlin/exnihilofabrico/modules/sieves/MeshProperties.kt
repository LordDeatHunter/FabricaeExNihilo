package exnihilofabrico.modules.sieves

import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder
import exnihilofabrico.util.Color
import net.minecraft.util.Identifier

data class MeshProperties(val identifier: Identifier,
                          val displayName: String,
                          val color: Color,
                          val keyIngredient: Identifier,
                          val item: MeshItem = MeshItem(color, displayName)) {
    fun generateRecipe(builder: ShapedRecipeBuilder) {
        builder.pattern("xox", "oxo","xox")
            .ingredientItem('x', Identifier("string"))
            .ingredientItem('o', keyIngredient)
            .result(identifier, 1)
    }

}