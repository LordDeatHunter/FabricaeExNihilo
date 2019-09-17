package exnihilofabrico.common.ore

import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder
import exnihilofabrico.id
import exnihilofabrico.util.Color
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

data class OreProperties(
    val material: String,
    val color: Color,
    val piece: EnumPieceShape = EnumPieceShape.NORMAL,
    val chunk: EnumChunkShape = EnumChunkShape.CHUNK,
    val matrix: EnumChunkMaterial = EnumChunkMaterial.STONE
) {
    fun getChunkID() = id("${material}_chunk".toLowerCase())
    fun getPieceID() = id("${material}_piece".toLowerCase())

    fun getChunkItem() = Registry.ITEM.get(getChunkID())
    fun getPieceItem() = Registry.ITEM.get(getPieceID())

    fun getPieceTags(): List<Identifier> {
        val tags: MutableList<Identifier> = mutableListOf()

        // Cotton Common Resources
        tags.add(Identifier("cotton", "${material}_piece"))
        // TODO add compatible tags here

        return tags
    }

    fun getChunkTags(): List<Identifier> {
        val tags: MutableList<Identifier> = mutableListOf()

        // Cotton Common Resources
        tags.add(Identifier("cotton", "${material}_ore"))
        // TODO add compatible tags here

        return tags
    }

    fun generateRecipe(builder: ShapedRecipeBuilder) {
        builder.pattern("xx", "xx")
            .ingredientItem('x', getPieceID())
            .result(getChunkID(), 1)
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