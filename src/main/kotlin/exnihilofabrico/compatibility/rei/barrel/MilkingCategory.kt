package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import exnihilofabrico.util.asStack
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.api.Renderer
import me.shedaniel.rei.gui.renderers.ItemStackRenderer
import me.shedaniel.rei.gui.widget.LabelWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.SlotWidget
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.item.Items
import java.util.function.Supplier

class MilkingCategory: RecipeCategory<MilkingDisplay> {

    override fun getIdentifier() = PluginEntry.MILKING
    override fun getIcon(): ItemStackRenderer = Renderer.fromItemStack(Items.MILK_BUCKET.asStack())
    override fun getCategoryName() = "Barrel Milking"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: MilkingDisplay) = WIDTH

    override fun setupDisplay(displaySupplier: Supplier<MilkingDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val arrow = GlyphWidget(bounds, bounds.minX + ARROW_X, bounds.minY + ARROW_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V)
        widgets.add(arrow)

        val eggs = display.input[0]
        val barrels = display.input[1]
        val outputs = display.output

        widgets.add(SlotWidget(bounds.minX + ABOVE_X, bounds.minY + ABOVE_Y, Renderer.fromItemStacks(eggs), false, true, true))
        widgets.add(SlotWidget(bounds.minX + BARRELS_X, bounds.minY + BARRELS_Y, Renderer.fromItemStacks(barrels), false, false, true))
        widgets.add(SlotWidget(bounds.minX + OUTPUT_X, bounds.minY + OUTPUT_Y, Renderer.fromItemStacks(outputs), true, true, true))

        // TODO some day figure out how to render a little entity over the barrel instead of an egg :/
//        (display.recipe.entity.flatten().firstOrNull())?.let{
//            widgets.add(EntityWidget(bounds.minX, bounds.minY, 36, 36, it))
//        }


        val text =  LabelWidget(0, 0, display.recipe.result.localizeAmount())
        text.x = bounds.maxX - MARGIN - text.bounds.maxX
        text.y = bounds.minY + MARGIN
        widgets.add(text)


        return widgets
    }

    companion object {
        val ARROW = id("textures/gui/rei/glyphs.png")

        val MARGIN = 6

        val WIDTH = 8*18 + MARGIN*2
        val HEIGHT = 2*18 + MARGIN*2

        val ARROW_X = WIDTH/2 - 9
        val ARROW_Y = HEIGHT/2

        val BARRELS_X = ARROW_X - 18
        val BARRELS_Y = ARROW_Y

        val ABOVE_X = BARRELS_X
        val ABOVE_Y = BARRELS_Y - 18

        val OUTPUT_X = ARROW_X + 18
        val OUTPUT_Y = ARROW_Y

        val ARROW_WIDTH = 16
        val ARROW_HEIGHT= 16
        val ARROW_U = 0
        val ARROW_V= 0
    }

}