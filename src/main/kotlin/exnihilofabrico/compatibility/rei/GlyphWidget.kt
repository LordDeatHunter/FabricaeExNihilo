package exnihilofabrico.compatibility.rei

import com.mojang.blaze3d.systems.RenderSystem
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.gui.widget.RecipeBaseWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

data class GlyphWidget(val rectangle: Rectangle, val x: Int, val y: Int, val width: Int, val height: Int, val texture: Identifier, val u: Int, val v: Int): RecipeBaseWidget(rectangle) {
    override fun render(mouseX: Int, mouseY: Int, delta: Float) {
        RenderSystem.disableLighting()
        MinecraftClient.getInstance().textureManager.bindTexture(texture)
        val sprite = MinecraftClient.getInstance().getSpriteAtlas(texture).apply(texture)
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        blit(x, y, u, v, width, height)
    }
}
