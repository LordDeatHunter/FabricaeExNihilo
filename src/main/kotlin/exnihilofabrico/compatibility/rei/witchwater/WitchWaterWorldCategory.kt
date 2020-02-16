package exnihilofabrico.compatibility.rei.witchwater

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.asREIEntry
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.gui.widget.EntryWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.Widget
import java.util.function.Supplier

class WitchWaterWorldCategory: RecipeCategory<WitchWaterWorldDisplay> {

    override fun getIdentifier() = PluginEntry.WITCH_WATER_WORLD
    override fun getLogo() = WitchWaterFluid.bucket.asREIEntry()
    override fun getCategoryName() = "Witch Water Fluid Interactions"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: WitchWaterWorldDisplay) = WIDTH

    override fun setupDisplay(displaySupplier: Supplier<WitchWaterWorldDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val glyphPlus = GlyphWidget(bounds, bounds.minX + GLYPH_PLUS_X, bounds.minY + GLYPH_PLUS_Y, GLYPH_WIDTH, GLYPH_HEIGHT, GLYPHS, GLYPH_PLUS_U, GLYPH_PLUS_V)
        val glypgArrow = GlyphWidget(bounds, bounds.minX + GLYPH_ARROW_X, bounds.minY + GLYPH_ARROW_Y, GLYPH_WIDTH, GLYPH_HEIGHT, GLYPHS, GLYPH_ARROW_U, GLYPH_ARROW_V)
        widgets.add(glyphPlus)
        widgets.add(glypgArrow)

        val witchWaterwitchWater = display.inputEntries[0]
        val fluids = display.inputEntries[1]
        val results = display.outputEntries

        widgets.add(EntryWidget.create(bounds.minX + WITCH_X, bounds.minY + WITCH_Y).entries(witchWaterwitchWater))
        widgets.add(EntryWidget.create(bounds.minX + FLUID_X, bounds.minY + FLUID_Y).entries(fluids))


        results.forEachIndexed { index, result ->
            widgets.add(
                EntryWidget.create(
                    bounds.minX + OUTPUT_X + (index % OUTPUT_SLOTS_X)*18,
                    bounds.minY + OUTPUT_Y + (index / OUTPUT_SLOTS_X)*18).entry(result))
        }
        // Fill in the empty spots
        for(index in results.size until MAX_OUTPUTS) {
            widgets.add(
                EntryWidget.create(
                    bounds.minX + OUTPUT_X + (index % OUTPUT_SLOTS_X)*18,
                    bounds.minY + OUTPUT_Y + (index / OUTPUT_SLOTS_X)*18))
        }

        return widgets
    }

    companion object {
        val GLYPHS = id("textures/gui/rei/glyphs.png")

        val OUTPUT_SLOTS_X = maxOf(ExNihiloFabrico.config.modules.REI.witchwaterworldCols, 1)
        val OUTPUT_SLOTS_Y = maxOf(ExNihiloFabrico.config.modules.REI.witchwaterworldRows, 3)

        val MAX_OUTPUTS = OUTPUT_SLOTS_X*OUTPUT_SLOTS_Y

        val MARGIN = 6

        val WIDTH = (OUTPUT_SLOTS_X+2)*18 + MARGIN*2
        val HEIGHT = OUTPUT_SLOTS_Y*18 + MARGIN*2

        val GLYPH_PLUS_X = MARGIN
        val GLYPH_PLUS_Y = HEIGHT/2 - 9
        val GLYPH_ARROW_X = MARGIN + 18
        val GLYPH_ARROW_Y= GLYPH_PLUS_Y

        val OUTPUT_X = GLYPH_ARROW_X + 18
        val OUTPUT_Y = MARGIN

        val FLUID_X = MARGIN
        val FLUID_Y = GLYPH_PLUS_Y + 18
        val WITCH_X = MARGIN
        val WITCH_Y = GLYPH_PLUS_Y - 18

        val GLYPH_WIDTH = 16
        val GLYPH_HEIGHT = 16
        val GLYPH_PLUS_U = 3*16
        val GLYPH_PLUS_V = 0
        val GLYPH_ARROW_U = 0
        val GLYPH_ARROW_V= 0
    }

}