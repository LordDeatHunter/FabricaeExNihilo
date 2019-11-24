package exnihilofabrico.compatibility.rei

import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.gui.widget.WidgetWithBounds
import net.minecraft.block.Block
import net.minecraft.client.gui.Element

class BlockWidget(val block: Block): WidgetWithBounds() {
    override fun children(): MutableList<out Element> = mutableListOf()

    override fun getBounds(): Rectangle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(mouseX: Int, mouseY: Int, var3: Float) {

        val renderer =

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}