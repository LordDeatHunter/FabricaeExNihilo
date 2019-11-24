package exnihilofabrico.compatibility.rei.sieve

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


class SieveCategory: RecipeCategory<SieveDisplay> {

    override fun getIdentifier() = PluginEntry.SIEVE
    override fun getIcon(): ItemStackRenderer = Renderer.fromItemStack(getExNihiloItemStack("oak_sieve"))
    override fun getCategoryName() = "Sieve"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: SieveDisplay) =
        WIDTH

    override fun setupDisplay(displaySupplier: Supplier<SieveDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val arrowWidget = GlyphWidget(
            bounds,
            bounds.minX + ARROW_OFFSET_X,
            bounds.minY + ARROW_OFFSET_Y,
            ARROW_WIDTH,
            ARROW_HEIGHT,
            ARROW,
            ARROW_U,
            ARROW_V
        )
        widgets.add(arrowWidget)

        val inputs = display.input ?: mutableListOf(mutableListOf(),mutableListOf(),mutableListOf())
        val outputs = display.output

        // Sieves
        widgets.add(SlotWidget(bounds.minX + MESH_X, bounds.minY + MESH_Y, Renderer.fromItemStacks(inputs[3]), false, false, false))
        // Meshes
        widgets.add(SlotWidget(bounds.minX + MESH_X, bounds.minY + MESH_Y, Renderer.fromItemStacks(inputs[1]), false, true, true))
        // Fluids
        if(!inputs[2].isEmpty())
            widgets.add(SlotWidget(bounds.minX + BUCKET_X, bounds.minY + BUCKET_Y, Renderer.fromItemStacks(inputs[2]), false, true, true))
        // Sievables
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
        val ARROW = id("textures/gui/rei/glyphs.png")

        val OUTPUT_SLOTS_X = maxOf(ExNihiloFabrico.config.modules.REI.sieveNumCols, 1)
        val OUTPUT_SLOTS_Y = maxOf(ExNihiloFabrico.config.modules.REI.sieveNumRows, 3)

        val MARGIN = 6

        val WIDTH = 2*18 + OUTPUT_SLOTS_X*18 + MARGIN*2
        val HEIGHT = OUTPUT_SLOTS_Y*18 + MARGIN*2

        val BLOCK_X = MARGIN
        val MESH_X = MARGIN
        val BUCKET_X = MARGIN
        val ARROW_OFFSET_X = MESH_X + 18
        val OUTPUT_X = ARROW_OFFSET_X + 18


        val BLOCK_Y = MARGIN + (HEIGHT - 2*MARGIN)/2 - 27
        val MESH_Y = BLOCK_Y + 18
        val BUCKET_Y = MESH_Y + 18
        val ARROW_OFFSET_Y = MESH_Y
        val OUTPUT_Y = MARGIN

        val ARROW_WIDTH = 16
        val ARROW_HEIGHT= 16
        val ARROW_U = 0
        val ARROW_V= 0

        val MAX_OUTPUTS = OUTPUT_SLOTS_X * OUTPUT_SLOTS_Y
    }

}