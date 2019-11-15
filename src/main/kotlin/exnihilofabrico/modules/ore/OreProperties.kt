package exnihilofabrico.modules.ore

import com.swordglowsblue.artifice.api.builder.data.recipe.CookingRecipeBuilder
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder
import exnihilofabrico.id
import exnihilofabrico.util.Color
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

data class OreProperties(
    val material: String,
    val color: Color = Color.WHITE,
    val piece: EnumPieceShape = EnumPieceShape.NORMAL,
    val chunk: EnumChunkShape = EnumChunkShape.CHUNK,
    val matrix: EnumChunkMaterial = EnumChunkMaterial.STONE,
    val displayName: String = "item.exnihilofabrico.$material"
) {
    fun getChunkID() = id("${material}_chunk".toLowerCase())
    fun getPieceID() = id("${material}_piece".toLowerCase())

    fun getIngotID() =
        when(material) {
            "iron", "gold" -> Identifier("${material}_ingot")
            else -> Identifier("c", "${material}_ingot")
        }
    fun getNuggetID() =
        when(material) {
            "iron", "gold" -> Identifier("${material}_nugget")
            else -> Identifier("c", "${material}_nugget")
        }
    fun getOreID() =
        when(material) {
            "iron", "gold" -> Identifier("${material}_ore")
            else -> Identifier("c", "${material}_ore")
        }

    fun getChunkItem() = Registry.ITEM.get(getChunkID())
    fun getPieceItem() = Registry.ITEM.get(getPieceID())

    fun generateRecipe(builder: ShapedRecipeBuilder) {
        builder.pattern("xx", "xx")
            .ingredientItem('x', getPieceID())
            .result(getChunkID(), 1)
    }
    fun generateNuggetCookingRecipe(builder: CookingRecipeBuilder) {
        builder.ingredientItem(getPieceID()).result(getNuggetID())
    }
    fun generateIngotCookingRecipe(builder: CookingRecipeBuilder) {
        builder.ingredientItem(getChunkID()).result(getIngotID())
    }
}

enum class EnumPieceShape {
    COARSE, NORMAL, FINE
}

enum class EnumChunkShape {
    CHUNK, FLINT, LUMP
}

enum class EnumChunkMaterial {
    ANDESITE, DIORITE, ENDSTONE, GRANITE, NETHERRACK, PRISMARINE, REDSAND, SAND, SOULSAND, STONE
}