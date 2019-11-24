package exnihilofabrico.compatibility.rei

import me.shedaniel.math.api.Rectangle
import me.shedaniel.math.compat.RenderHelper
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GuiLighting
import net.minecraft.util.Identifier

data class GlyphWidget(val rectangle: Rectangle, val x: Int, val y: Int, val width: Int, val height: Int, val texture: Identifier, val u: Int, val v: Int): RecipeBaseWidget(rectangle) {
    override fun render(mouseX: Int, mouseY: Int, delta: Float) {
        GuiLighting.disable()
        MinecraftClient.getInstance().textureManager.bindTexture(texture)
        val sprite = MinecraftClient.getInstance().spriteAtlas.getSprite(texture)
        RenderHelper.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        blit(x, y, u, v, width, height)
    }
}
