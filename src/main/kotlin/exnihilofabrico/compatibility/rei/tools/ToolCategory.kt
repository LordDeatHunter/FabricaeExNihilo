package exnihilofabrico.compatibility.rei.tools

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import exnihilofabrico.util.asREIEntry
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.gui.widget.EntryWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import java.util.function.Supplier

class ToolCategory(val tool: Identifier, val icon: ItemStack, val name: String): RecipeCategory<ToolDisplay> {
    val GLYPH_U = if(tool == PluginEntry.HAMMER) 16 else 32

    override fun getIdentifier() = tool
    override fun getLogo() = icon.asREIEntry()
    override fun getCategoryName() = name


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: ToolDisplay) = WIDTH

    override fun setupDisplay(displaySupplier: Supplier<ToolDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val arrowWidget = GlyphWidget(
            bounds,
            bounds.minX + ARROW_OFFSET_X,
            bounds.minY + ARROW_OFFSET_Y,
            GLYPH_WIDTH,
            GLYPH_HEIGHT,
            GLYPH,
            GLYPH_U,
            GLYPH_V
        )
        widgets.add(arrowWidget)

        val inputs = display.inputEntries ?: mutableListOf(mutableListOf(),mutableListOf())
        val outputs = display.outputEntries

        // Tools
        if(!inputs[1].isEmpty())
            widgets.add(EntryWidget.create(bounds.minX + TOOL_X, bounds.minY + TOOL_Y).entries(inputs[1]))
        // Target
        widgets.add(EntryWidget.create(bounds.minX + BLOCK_X, bounds.minY + BLOCK_Y).entries(inputs[0]))

        outputs.forEachIndexed { index, output ->
            widgets.add(
                EntryWidget.create(
                    bounds.minX + OUTPUT_X + (index % OUTPUT_SLOTS_X)*18,
                    bounds.minY + OUTPUT_Y + (index / OUTPUT_SLOTS_X)*18).entry(output))
        }
        // Fill in the empty spots
        for(index in outputs.size until MAX_OUTPUTS) {
            widgets.add(
                EntryWidget.create(
                    bounds.minX + OUTPUT_X + (index % OUTPUT_SLOTS_X)*18,
                    bounds.minY + OUTPUT_Y + (index / OUTPUT_SLOTS_X)*18))
        }

        return widgets
    }

    companion object {
        val GLYPH = id("textures/gui/rei/glyphs.png")

        val OUTPUT_SLOTS_X = maxOf(ExNihiloFabrico.config.modules.REI.toolNumCols, 1)
        val OUTPUT_SLOTS_Y = maxOf(ExNihiloFabrico.config.modules.REI.toolNumRows, 2)

        val MARGIN = 6

        val TOOL_X = MARGIN
        val BLOCK_X = TOOL_X
        val ARROW_OFFSET_X = TOOL_X + 18
        val OUTPUT_X = ARROW_OFFSET_X + 18
        val WIDTH = OUTPUT_X + OUTPUT_SLOTS_X *18 + TOOL_X

        val OUTPUT_Y = MARGIN
        val HEIGHT = OUTPUT_Y + OUTPUT_SLOTS_Y *18 + OUTPUT_Y

        val TOOL_Y = MARGIN + (HEIGHT - 2*MARGIN)/2 - 18
        val BLOCK_Y = TOOL_Y + 18
        val ARROW_OFFSET_Y = MARGIN + (HEIGHT - 2*MARGIN)/2 - 9

        val GLYPH_WIDTH = 16
        val GLYPH_HEIGHT = 16
        val GLYPH_V = 0

        val MAX_OUTPUTS = OUTPUT_SLOTS_X * OUTPUT_SLOTS_Y
    }

}