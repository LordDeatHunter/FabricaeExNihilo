package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import exnihilofabrico.util.asREIEntry
import exnihilofabrico.util.getExNihiloItemStack
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.gui.widget.EntryWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.Widget
import java.util.function.Supplier

class AlchemyCategory: RecipeCategory<AlchemyDisplay> {

    override fun getIdentifier() = PluginEntry.ALCHEMY
    override fun getLogo() = getExNihiloItemStack("oak_barrel").asREIEntry()
    override fun getCategoryName() = "Alchemy"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: AlchemyDisplay) =
        WIDTH

    override fun setupDisplay(displaySupplier: Supplier<AlchemyDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val plusGlyph = GlyphWidget(bounds, bounds.minX + PLUS_X, bounds.minY + PLUS_Y, GLYPH_WIDTH, GLYPH_HEIGHT, GLYPHS, PLUS_U, PLUS_V)
        val arrowGlyph = GlyphWidget(bounds, bounds.minX + ARROW_X, bounds.minY + ARROW_Y, GLYPH_WIDTH, GLYPH_HEIGHT, GLYPHS, ARROW_U, ARROW_V)
        widgets.add(plusGlyph)
        widgets.add(arrowGlyph)

        val reactant = display.inputEntries[0]
        val catalyst = display.inputEntries[1]
        val barrels = display.inputEntries[2]
        val product = display.outputEntries[0]
        val byproduct = display.outputEntries[1]
        val toSpawn = display.outputEntries[2]

        widgets.add(EntryWidget.create(bounds.minX + REACTANT_X, bounds.minY + REACTANT_Y).entries(reactant))
        widgets.add(EntryWidget.create(bounds.minX + CATALYST_X, bounds.minY + CATALYST_Y).entries(catalyst))
        widgets.add(EntryWidget.create(bounds.minX + PRODUCT_X, bounds.minY + PRODUCT_Y).entry(product))
        widgets.add(EntryWidget.create(bounds.minX + BYPRODUCT_X, bounds.minY + BYPRODUCT_Y).entry(byproduct))
        widgets.add(EntryWidget.create(bounds.minX + SPAWN_X, bounds.minY + SPAWN_Y).entry(toSpawn))
        widgets.add(EntryWidget.create(bounds.minX + BARRELS_X, bounds.minY + BARRELS_Y).entries(barrels))

        return widgets
    }

    companion object {
        val GLYPHS = id("textures/gui/rei/glyphs.png")

        val MARGIN = 6

        val WIDTH = 8*18 + MARGIN*2
        val HEIGHT = 3*18 + MARGIN*2

        val BARRELS_X = WIDTH/2 - 9
        val BARRELS_Y = HEIGHT/2 + 9

        val ARROW_X = BARRELS_X
        val ARROW_Y = BARRELS_Y - 18

        val REACTANT_X = ARROW_X - 18
        val REACTANT_Y = ARROW_Y

        val PLUS_X = REACTANT_X - 18
        val PLUS_Y = REACTANT_Y

        val CATALYST_X = PLUS_X - 18
        val CATALYST_Y = PLUS_Y
        val PRODUCT_X = ARROW_X + 18
        val PRODUCT_Y = ARROW_Y
        val BYPRODUCT_X = PRODUCT_X + 18
        val BYPRODUCT_Y = PRODUCT_Y
        val SPAWN_X = BYPRODUCT_X + 18
        val SPAWN_Y = BYPRODUCT_Y

        val GLYPH_WIDTH = 16
        val GLYPH_HEIGHT = 16
        val ARROW_U = 0
        val ARROW_V= 0

        val PLUS_U = 3*16
        val PLUS_V= 0
    }

}