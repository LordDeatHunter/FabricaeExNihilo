package exnihilofabrico.compatibility.rei.tools

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.api.Renderer
import me.shedaniel.rei.gui.renderers.ItemStackRenderer
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.SlotWidget
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import java.util.function.Supplier

class ToolCategory(val tool: Identifier, val icon: ItemStack, val name: String): RecipeCategory<ToolDisplay> {
    val GLYPH_U = if(tool == PluginEntry.HAMMER) 16 else 32

    override fun getIdentifier() = tool
    override fun getIcon(): ItemStackRenderer = Renderer.fromItemStack(icon)
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

        val inputs = display.input ?: mutableListOf(mutableListOf(),mutableListOf())
        val outputs = display.output

        // Tools
        if(!inputs[1].isEmpty())
            widgets.add(SlotWidget(bounds.minX + TOOL_X, bounds.minY + TOOL_Y, Renderer.fromItemStacks(inputs[1]), false, true, true))
        // Target
        widgets.add(SlotWidget(bounds.minX + BLOCK_X, bounds.minY + BLOCK_Y, Renderer.fromItemStacks(inputs[0]), true, true, true))

        outputs.forEachIndexed { index, output ->
            widgets.add(
                SlotWidget(
                    bounds.minX + OUTPUT_X + (index % OUTPUT_SLOTS_X)*18,
                    bounds.minY + OUTPUT_Y + (index / OUTPUT_SLOTS_X)*18,
                    Renderer.fromItemStack(output),
                    true, true, true
                )
            )
        }
        // Fill in the empty spots
        for(index in outputs.size until MAX_OUTPUTS) {
            widgets.add(
                SlotWidget(
                    bounds.minX + OUTPUT_X + (index % OUTPUT_SLOTS_X)*18,
                    bounds.minY + OUTPUT_Y + (index / OUTPUT_SLOTS_X)*18,
                    Renderer.fromItemStack(ItemStack.EMPTY),
                    true, false, false
                )
            )
        }

        return widgets
    }

    companion object {
        val GLYPH = id("textures/gui/rei/glyphs.png")

        val OUTPUT_SLOTS_X = maxOf(ExNihiloFabrico.config.modules.REI.toolNumRows, 1)
        val OUTPUT_SLOTS_Y = maxOf(ExNihiloFabrico.config.modules.REI.toolNumCols, 2)

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