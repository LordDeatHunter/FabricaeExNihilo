package exnihilofabrico.compatibility.rei.barrel

import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem
import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import exnihilofabrico.util.asREIEntry
import me.shedaniel.math.api.Point
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.gui.widget.EntryWidget
import me.shedaniel.rei.gui.widget.LabelWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.block.Blocks
import java.util.function.Supplier

class LeakingCategory: RecipeCategory<LeakingDisplay> {

    override fun getIdentifier() = PluginEntry.LEAKING
    override fun getLogo() = Blocks.MOSSY_COBBLESTONE.asREIEntry()
    override fun getCategoryName() = "Barrel Leaking"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: LeakingDisplay) =
        WIDTH

    override fun setupDisplay(displaySupplier: Supplier<LeakingDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val arrowWidget = GlyphWidget(bounds, bounds.minX + ARROW_OFFSET_X, bounds.minY + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V)
        widgets.add(arrowWidget)

        val targets = display.inputEntries[0]
        val fluids = display.inputEntries[1]
        val loss = display.recipe.loss
        val result = display.outputEntries

        widgets.add(EntryWidget.create(bounds.minX + OUTPUT_X, bounds.minY + OUTPUT_Y).entries(result))
        widgets.add(EntryWidget.create(bounds.minX + BUCKET_X, bounds.minY + BUCKET_Y).entries(fluids))
        widgets.add(EntryWidget.create(bounds.minX + TARGET_X, bounds.minY + TARGET_Y).entries(targets))

        val label = (fluids.firstOrNull())?.let { bucketStack ->
            val key = (bucketStack.item as? IBucketItem)?.libblockattributes__getFluid(bucketStack.itemStack)
            key?.unitSet?.localizeAmount(loss) } ?: "?"

        val text =  LabelWidget(0, 0, "-${label}")
        text.position = Point(bounds.maxX - MARGIN - text.bounds.maxX, bounds.minY + MARGIN + 9)
        widgets.add(text)

        return widgets
    }

    companion object {
        val ARROW = id("textures/gui/rei/glyphs.png")
        val ARROW_WIDTH = 16
        val ARROW_HEIGHT= 16
        val ARROW_U = 0
        val ARROW_V= 0

        val MARGIN = 6

        val WIDTH = 8*18 + MARGIN*2
        val HEIGHT = 3*18 + MARGIN*2

        // Center the arrow
        val ARROW_OFFSET_X = WIDTH/2 -9
        val ARROW_OFFSET_Y = HEIGHT/2 -9

        val TARGET_X = ARROW_OFFSET_X - 18
        val TARGET_Y = ARROW_OFFSET_Y + 9

        val BUCKET_X = ARROW_OFFSET_X - 18
        val BUCKET_Y = ARROW_OFFSET_Y - 9

        val OUTPUT_X = ARROW_OFFSET_X + 18
        val OUTPUT_Y = ARROW_OFFSET_Y + 9
    }

}