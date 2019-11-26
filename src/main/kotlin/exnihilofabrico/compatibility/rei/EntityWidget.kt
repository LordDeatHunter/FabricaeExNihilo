package exnihilofabrico.compatibility.rei

import exnihilofabrico.client.renderEntity
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.gui.widget.WidgetWithBounds
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Element
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity

class EntityWidget(val xmin: Int, val ymin: Int, val width: Int, val height: Int, val type: EntityType<*>): WidgetWithBounds() {
    override fun getBounds(): Rectangle {
        return Rectangle(xmin, ymin, xmin+width, ymin+height)
    }

    override fun children(): MutableList<out Element> {
        return mutableListOf()
    }

    override fun render(mouseX: Int, mouseY: Int, delta: Float) {

        val world = MinecraftClient.getInstance().entityRenderManager.world

        (type.create(world) as? LivingEntity)?.let { living ->
            renderEntity(mouseX, mouseY, 0, 1.0, 0.0, 0.0, living)
        }
    }

}