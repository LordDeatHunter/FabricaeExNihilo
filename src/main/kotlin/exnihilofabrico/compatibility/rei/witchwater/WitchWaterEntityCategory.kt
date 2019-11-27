package exnihilofabrico.compatibility.rei.witchwater

import exnihilofabrico.compatibility.rei.GlyphWidget
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.id
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.asStack
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.api.Renderer
import me.shedaniel.rei.gui.renderers.ItemStackRenderer
import me.shedaniel.rei.gui.widget.LabelWidget
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import me.shedaniel.rei.gui.widget.SlotWidget
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.client.resource.language.I18n
import net.minecraft.entity.EntityType
import net.minecraft.util.registry.Registry
import net.minecraft.village.VillagerProfession
import java.util.function.Supplier

class WitchWaterEntityCategory: RecipeCategory<WitchWaterEntityDisplay> {

    override fun getIdentifier() = PluginEntry.WITCH_WATER_ENTITY
    override fun getIcon(): ItemStackRenderer = Renderer.fromItemStack(WitchWaterFluid.bucket.asStack())
    override fun getCategoryName() = "Witch Water Bathing"


    override fun getDisplayHeight() = HEIGHT
    override fun getDisplayWidth(display: WitchWaterEntityDisplay) = WIDTH

    override fun setupDisplay(displaySupplier: Supplier<WitchWaterEntityDisplay>, bounds: Rectangle): MutableList<Widget> {
        val display = displaySupplier.get()
        val widgets = mutableListOf<Widget>(RecipeBaseWidget(bounds))

        val arrowIn = GlyphWidget(bounds, bounds.minX + ARROW_IN_X, bounds.minY + ARROW_IN_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_IN_U, ARROW_IN_V)
        val arrowOut = GlyphWidget(bounds, bounds.minX + ARROW_OUT_X, bounds.minY + ARROW_OUT_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_OUT_U, ARROW_OUT_V)
        widgets.add(arrowIn)
        widgets.add(arrowOut)

        val eggsIn = display.input[0]
        val eggOut = display.output
        val fluids = display.input[1]

        widgets.add(SlotWidget(bounds.minX + IN_X, bounds.minY + IN_Y, Renderer.fromItemStacks(eggsIn), false, true, true))
        widgets.add(SlotWidget(bounds.minX + OUT_X, bounds.minY + OUT_Y, Renderer.fromItemStacks(eggOut), false, true, true))
        widgets.add(SlotWidget(bounds.minX + FLUID_X, bounds.minY + FLUID_Y, Renderer.fromItemStacks(fluids), false, true, true))

        if(display.recipe.target.test(EntityType.VILLAGER)) {
            val profession = Registry.VILLAGER_PROFESSION.getId(display.recipe.profession ?: VillagerProfession.NONE)
            val text = LabelWidget(0, 0, I18n.translate("entity.${profession.namespace}.villager.${profession.path}"))
            text.x = bounds.minX + MARGIN + text.bounds.maxX
            text.y = bounds.minY - MARGIN + text.bounds.maxY
            widgets.add(text)
        }

        return widgets
    }

    companion object {
        val ARROW = id("textures/gui/rei/glyphs.png")

        val MARGIN = 6

        val WIDTH = 8*18 + MARGIN*2
        val HEIGHT = 3*18 + MARGIN*2 - 9

        val FLUID_X = WIDTH/2 - 9
        val FLUID_Y = HEIGHT/2 + 9

        val ARROW_IN_X = FLUID_X - 5
        val ARROW_IN_Y = FLUID_Y - 18

        val ARROW_OUT_X = FLUID_X + 9
        val ARROW_OUT_Y = FLUID_Y - 18

        val IN_X = ARROW_IN_X - 18
        val IN_Y = ARROW_IN_Y - 7

        val OUT_X = ARROW_OUT_X + 18
        val OUT_Y = ARROW_OUT_Y

        val ARROW_WIDTH = 16
        val ARROW_HEIGHT = 16
        val ARROW_IN_U = 0
        val ARROW_IN_V = 16*5
        val ARROW_OUT_U = 0
        val ARROW_OUT_V= 16*4
    }

}