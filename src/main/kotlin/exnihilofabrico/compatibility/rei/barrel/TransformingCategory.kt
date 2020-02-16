package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.gui.widget.EntryWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.block.Blocks
import java.util.function.Supplier

class TransformingCategory: RecipeCategory<TransformingDisplay> {

    override fun getIdentifier() = PluginEntry.TRANSFORMING
    override fun getLogo() = EntryStack.create(Blocks.MYCELIUM)
    override fun getCategoryName() = "Transformation"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: TransformingDisplay) =
        WIDTH

    override fun setupDisplay(displaySupplier: Supplier<TransformingDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val arrow1 = GlyphWidget(bounds, bounds.minX + ARROW1_X, bounds.minY + ARROW1_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V)
        val arrow2 = GlyphWidget(bounds, bounds.minX + ARROW2_X, bounds.minY + ARROW2_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V)
        widgets.add(arrow1)
        widgets.add(arrow2)

        val inBarrel = display.inputEntries[0]
        val below = display.inputEntries[1]
        val barrels = display.inputEntries[2]
        val outputs = display.outputEntries

        widgets.add(EntryWidget.create(bounds.minX + INPUT_X, bounds.minY + INPUT_Y).entries(inBarrel))
        widgets.add(EntryWidget.create(bounds.minX + BELOW_X, bounds.minY + BELOW_Y).entries(below))
        widgets.add(EntryWidget.create(bounds.minX + BARRELS_X, bounds.minY + BARRELS_Y).entries(barrels))
        widgets.add(EntryWidget.create(bounds.minX + OUTPUT_X, bounds.minY + OUTPUT_Y).entries(outputs))

        return widgets
    }

    companion object {
        val ARROW = id("textures/gui/rei/glyphs.png")

        val MARGIN = 6

        val WIDTH = 8*18 + MARGIN*2
        val HEIGHT = 2*18 + MARGIN*2

        val BARRELS_X = WIDTH/2 - 9
        val BARRELS_Y = MARGIN

        val ARROW1_X = BARRELS_X -18
        val ARROW1_Y = BARRELS_Y
        val ARROW2_X = BARRELS_X + 18
        val ARROW2_Y = BARRELS_Y

        val BELOW_X = BARRELS_X
        val BELOW_Y = BARRELS_Y + 18

        val INPUT_X = BARRELS_X - 2*18
        val INPUT_Y = BARRELS_Y

        val OUTPUT_X = BARRELS_X + 2*18
        val OUTPUT_Y = BARRELS_Y

        val ARROW_WIDTH = 16
        val ARROW_HEIGHT= 16
        val ARROW_U = 0
        val ARROW_V= 0
    }

}