package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import exnihilofabrico.util.getExNihiloItemStack
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.api.Renderer
import me.shedaniel.rei.gui.renderers.ItemStackRenderer
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.SlotWidget
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.item.ItemStack
import java.util.function.Supplier

class CompostCategory: RecipeCategory<CompostDisplay> {

    override fun getIdentifier() = PluginEntry.COMPOSTING
    override fun getIcon(): ItemStackRenderer = Renderer.fromItemStack(getExNihiloItemStack("oak_barrel"))
    override fun getCategoryName() = "Barrel Composting"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: CompostDisplay) =
        WIDTH

    override fun setupDisplay(displaySupplier: Supplier<CompostDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val arrowWidget = GlyphWidget(bounds, bounds.minX + ARROW_OFFSET_X, bounds.minY + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V)
        widgets.add(arrowWidget)

        val inputs = display.input[0]
        val outputs = display.output

        // Output
        widgets.add(SlotWidget(bounds.minX + BLOCK_X, bounds.minY + BLOCK_Y, Renderer.fromItemStacks(outputs), true, true, true))

        // Input
        inputs.forEachIndexed { index, output ->
            widgets.add(
                SlotWidget(
                    bounds.minX + INPUT_X + (index % INPUT_SLOTS_X)*18,
                    bounds.minY + INPUT_Y + (index / INPUT_SLOTS_X)*18,
                    Renderer.fromItemStack(output),
                    true, true, true
                )
            )
        }
        // Fill in the empty spots
        for(index in inputs.size until MAX_INPUT) {
            widgets.add(
                SlotWidget(
                    bounds.minX + INPUT_X + (index % INPUT_SLOTS_X)*18,
                    bounds.minY + INPUT_Y + (index / INPUT_SLOTS_X)*18,
                    Renderer.fromItemStack(ItemStack.EMPTY),
                    true, false, false
                )
            )
        }

        return widgets
    }

    companion object {
        val ARROW = id("textures/gui/rei/glyphs.png")

        val INPUT_SLOTS_X = maxOf(ExNihiloFabrico.config.modules.REI.compostNumCols, 1)
        val INPUT_SLOTS_Y = maxOf(ExNihiloFabrico.config.modules.REI.compostNumRows, 1)

        val MARGIN = 6

        val WIDTH = 2*18 + INPUT_SLOTS_X*18 + MARGIN*2
        val HEIGHT = INPUT_SLOTS_Y*18 + MARGIN*2

        val BLOCK_X = MARGIN
        val ARROW_OFFSET_X = BLOCK_X + 18
        val INPUT_X = ARROW_OFFSET_X + 18


        val BLOCK_Y = MARGIN + (HEIGHT - 2*MARGIN)/2 - 9
        val ARROW_OFFSET_Y = BLOCK_Y
        val INPUT_Y = MARGIN

        val ARROW_WIDTH = 16
        val ARROW_HEIGHT= 16
        val ARROW_U = 0
        val ARROW_V= 16

        val MAX_INPUT = INPUT_SLOTS_X * INPUT_SLOTS_Y
    }

}