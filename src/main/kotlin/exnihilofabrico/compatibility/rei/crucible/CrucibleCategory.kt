package exnihilofabrico.compatibility.rei.crucible

import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.id
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.api.Renderer
import me.shedaniel.rei.gui.renderers.ItemStackRenderer
import me.shedaniel.rei.gui.widget.LabelWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.SlotWidget
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import java.util.function.Supplier

class CrucibleCategory(val crucible: Identifier, val icon: ItemStack, val name: String): RecipeCategory<CrucibleDisplay> {

    override fun getIdentifier() = crucible
    override fun getIcon(): ItemStackRenderer = Renderer.fromItemStack(icon)
    override fun getCategoryName() = name


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: CrucibleDisplay) = WIDTH

    override fun setupDisplay(displaySupplier: Supplier<CrucibleDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val inputs = display.input[0]
        val outputs = display.output
        val output = display.recipe.output

        // Input
        widgets.add(
            SlotWidget(bounds.minX + INPUT_X, bounds.minY + INPUT_Y, Renderer.fromItemStacks(inputs), true, true, true)
        )

        val arrowWidget = GlyphWidget(bounds, bounds.minX + GLYPH_X, bounds.minY + GLYPH_Y, GLYPH_WIDTH, GLYPH_HEIGHT, GLYPH, GLYPH_U, GLYPH_V)
        widgets.add(arrowWidget)

        // Output
        widgets.add(
            SlotWidget(bounds.minX + OUTPUT_X, bounds.minY + OUTPUT_Y, Renderer.fromItemStacks(outputs), true, true, true)
        )



        // Amount Text Value
        val text =  LabelWidget(0, 0, output.localizeAmount())
        text.x = bounds.maxX - MARGIN - text.bounds.maxX
        text.y = bounds.minY + MARGIN + 18 - text.bounds.maxY - text.bounds.maxY / 2

        widgets.add(text)

        return widgets
    }

    companion object {
        val GLYPH = id("textures/gui/rei/glyphs.png")

        val MARGIN = 6
        val WIDTH = MARGIN*2 + 18*9
        val HEIGHT = MARGIN*2 + 16

        val INPUT_X = MARGIN
        val INPUT_Y = MARGIN

        val GLYPH_X = INPUT_X + 18
        val GLYPH_Y = MARGIN

        val GLYPH_WIDTH = 16
        val GLYPH_HEIGHT= 16
        val GLYPH_U = 0
        val GLYPH_V= 0

        val OUTPUT_X = GLYPH_X + 18
        val OUTPUT_Y = MARGIN
    }

}