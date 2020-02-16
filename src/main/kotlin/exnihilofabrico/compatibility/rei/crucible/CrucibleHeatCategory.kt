package exnihilofabrico.compatibility.rei.crucible

import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.util.asREIEntry
import exnihilofabrico.util.getExNihiloItemStack
import me.shedaniel.math.api.Point
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.gui.widget.EntryWidget
import me.shedaniel.rei.gui.widget.LabelWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.Widget
import java.util.function.Supplier

class CrucibleHeatCategory(): RecipeCategory<CrucibleHeatDisplay> {

    override fun getIdentifier() = PluginEntry.CRUCIBLE_HEAT
    override fun getLogo() = getExNihiloItemStack("stone_crucible").asREIEntry()
    override fun getCategoryName() = "Heat"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: CrucibleHeatDisplay) =
        WIDTH

    override fun setupDisplay(displaySupplier: Supplier<CrucibleHeatDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val inputs = display.inputEntries[0]
        val heat = display.getHeat()

        // Block
        widgets.add(EntryWidget.create(bounds.minX + BLOCK_X, bounds.minY + BLOCK_Y).entries(inputs))
        // Heat Value
        val text =  LabelWidget(0, 0, "${heat}")
        text.position = Point(bounds.minX + WIDTH - MARGIN - text.bounds.maxX, bounds.minY + MARGIN + 18 - text.bounds.maxY - text.bounds.maxY / 2)

        widgets.add(text)

        return widgets
    }

    companion object {

        val MARGIN = 6
        val WIDTH = MARGIN*2 + 18*4
        val HEIGHT = MARGIN*2 + 16

        val BLOCK_X = MARGIN
        val BLOCK_Y = MARGIN
    }

}