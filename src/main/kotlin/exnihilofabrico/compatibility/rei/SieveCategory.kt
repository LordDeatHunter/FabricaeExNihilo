package exnihilofabrico.compatibility.rei

import exnihilofabrico.id
import exnihilofabrico.util.ExNihiloItemStack
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.api.Renderer
import me.shedaniel.rei.gui.widget.Widget
import java.util.function.Supplier

class SieveCategory: RecipeCategory<SieveDisplay> {

    override fun getIdentifier() = CATEGORY_ID
    override fun getIcon() = Renderer.fromItemStack(ExNihiloItemStack("oak_sieve"))
    override fun getCategoryName() = "sieve"

    override fun setupDisplay(displaySupplier: Supplier<SieveDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>()
        return widgets
    }

    companion object {
        val DISPLAY_TEXTURE = id("textures/gui/rei/sieve.png")
        val CATEGORY_ID = id("sieve_category")
    }

}